package com.webstocker.service.impl;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.VenteParDistrictWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.VenteParDistrictService;
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
public class VenteParDistrictServiceImpl implements VenteParDistrictService {

    private final Logger log = LoggerFactory.getLogger(VenteParDistrictServiceImpl.class);

    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    private ProduitRepository produitRepository;

    @Override
    public List<VenteParDistrictWrapper> quantiteVendueParDistrict(String ville, String dateDebut, String dateFin) {
        Long quantiteVendue = 0L;
        String nomProduit = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<VenteParDistrictWrapper> lesVentesParDistrict = new LinkedList<>();

        //Liste des produits sortie  de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);

        // liste de tous les produits
        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> itProduits = listProduits.iterator();

        while (itProduits.hasNext()) {
            Produit produit = itProduits.next();
            nomProduit = produit.getNomProduit();

            VenteParDistrictWrapper venteParDistrictWrapper = new VenteParDistrictWrapper();

            venteParDistrictWrapper.setProduit(nomProduit);
            venteParDistrictWrapper.setVille(ville);

            for (LigneBonDeSortie lbs : listeSortieParPeriode) {
                if (lbs.getBonDeSortie().getMagasin() != null) {
                    if (lbs.getBonDeSortie().getMagasin().getLocalite() != null) {
                        if (lbs.getBonDeSortie().getMagasin().getLocalite().getCommunes().getVille().getLibelle().equals(ville)
                                && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)
                                && lbs.getProduit().getNomProduit().equals(nomProduit)) {
                            quantiteVendue += lbs.getQuantite();
                        }
                    }
                }
            }
            venteParDistrictWrapper.setQuantiteVendue(quantiteVendue);

            quantiteVendue = 0L;

            lesVentesParDistrict.add(venteParDistrictWrapper);

        }

        return lesVentesParDistrict;
    }

}
