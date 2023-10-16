package com.webstocker.service.impl;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.NouvelleFactureWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.service.NouvelleFactureWrapperService;
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
public class NouvelleFactureWrapperServiceImpl implements NouvelleFactureWrapperService {

    private final Logger log = LoggerFactory.getLogger(NouvelleFactureWrapperServiceImpl.class);

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Override
    public List<NouvelleFactureWrapper> nouvelleFactureSansLot(BonDeSortie bonDeSortie) {

        long cumulQuantite = 0;
        String nomProduit1 = null;
        String nomProduit2 = null;

        List<NouvelleFactureWrapper> nouvelleFacture = new LinkedList<>();

        List<LigneBonDeSortie> ligneDeLaFacture = ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);

        Iterator<LigneBonDeSortie> iteratorNouvelleFacture = ligneDeLaFacture.iterator();

        while (iteratorNouvelleFacture.hasNext()) {
            LigneBonDeSortie ligneBS = iteratorNouvelleFacture.next();
            nomProduit1 = ligneBS.getLot().getProduit().getNomProduit();
            NouvelleFactureWrapper nFact = new NouvelleFactureWrapper();

            if (nomProduit1.equals(nomProduit2)) {

            } else {
                for (LigneBonDeSortie lbs : ligneDeLaFacture) {                 
                    if (lbs.getLot().getProduit().getNomProduit() != null) {
                        if (lbs.getLot().getProduit().getNomProduit().equals(nomProduit1)) {
                            cumulQuantite += lbs.getQuantite();
                        }
                    }                  
                }
                
                nFact.setNomProduit(nomProduit1);
                nFact.setPrixDeVente(ligneBS.getPrixDeVente());
                nFact.setBonDeSortie(bonDeSortie);
                nFact.setQuantiteProduit(cumulQuantite);
                System.out.println("LA QUANTITE TOTALE " + cumulQuantite);
            }
            cumulQuantite = 0;
            nomProduit2 = ligneBS.getLot().getProduit().getNomProduit();
            nouvelleFacture.add(nFact);
        }
System.out.println("EXECUTION TOTALE OUAIS " + cumulQuantite);
        return nouvelleFacture;
    }

}
