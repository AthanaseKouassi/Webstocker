/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.service.impl;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Produit;
import com.webstocker.domain.User;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.QuantiteVendueParAgentWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.repository.UserRepository;
import com.webstocker.service.QuantiteVendueParAgentWrapperService;
import com.webstocker.web.rest.LigneBonDeSortieResource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Athanase
 */
@Service
@Transactional
public class QuantiteVendueParAgentWrapperServiceImpl implements QuantiteVendueParAgentWrapperService {

    private final Logger log = LoggerFactory.getLogger(QuantiteVendueParAgentWrapperServiceImpl.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    private MagasinRepository magasinRepository;

    @Inject
    private ProduitRepository produitRepository;

    @Override
    public List<QuantiteVendueParAgentWrapper> quantiteVendueParAgentCommercial(Long IDCommercial, String dateDebut, String dateFin) {
        String nomAgent = null;
        String magasin = null;
//        BigDecimal montantDesVente = BigDecimal.ZERO;
        Long montantDesVente = 0L;
        Long quantiteVendue = 0L;
        String nomProd = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<QuantiteVendueParAgentWrapper> venteDuCommercial = new LinkedList<>();

        List<LigneBonDeSortie> ligneBonSortie = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        
        User user = userRepository.findOne(IDCommercial);

        List<Produit> listProduit = produitRepository.findAll();
        Iterator<Produit> itProduit = listProduit.iterator();

        while (itProduit.hasNext()) {
            Produit p = itProduit.next();
            nomProd = p.getNomProduit();

            for (LigneBonDeSortie lbs : ligneBonSortie) {
                if (lbs.getBonDeSortie().getDemandeur() != null) {
                    if (nomProd.equals(lbs.getLot().getProduit().getNomProduit()) && lbs.getBonDeSortie().getDemandeur().getId().equals(IDCommercial)
                            && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)) {
                        quantiteVendue += lbs.getQuantite();
//                        montantDesVente = montantDesVente.add(lbs.getMontantVente());
                        montantDesVente += lbs.getPrixDeVente();
                    }
                }
            }
            if (quantiteVendue > 0) {
                QuantiteVendueParAgentWrapper qteVenduWrapper = new QuantiteVendueParAgentWrapper();
                qteVenduWrapper.setNomproduit(nomProd);
                qteVenduWrapper.setQuantiteVendueAgent(quantiteVendue);
                qteVenduWrapper.setMontantVente(montantDesVente);
                qteVenduWrapper.setNomAgent(user.getLastName());
                qteVenduWrapper.setPrenomAgent(user.getFirstName());

                venteDuCommercial.add(qteVenduWrapper);
                quantiteVendue = 0L;
                montantDesVente = 0L;
            }
        }

        return venteDuCommercial;
    }

    @Override
    public List<QuantiteVendueParAgentWrapper> quantiteVendueParAgentCommercialParProduit(String nomCommercial, String nomProduit, String dateDebut, String dateFin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
