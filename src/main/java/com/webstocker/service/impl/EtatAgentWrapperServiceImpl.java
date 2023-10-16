package com.webstocker.service.impl;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.EtatAgentWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.EtatAgentWrapperService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Athanase
 */
@Service
@Transactional
public class EtatAgentWrapperServiceImpl implements EtatAgentWrapperService {

    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    LignelivraisonRepository lignelivraisonRepository;

    ProduitRepository produitRepository;

    @Override
    public List<EtatAgentWrapper> RapportAgentParPeriode(String nomUser, String dateDebut, String dateFin) {

        int qteDetenue = 0, qteSortie = 0, qteRecue = 0, qteVente = 0, qtePromo = 0, allQteSortie = 0, allQteLivre = 0;
        int qteInitiale = 0;
        Long qteD = 0L, qteS = 0L, qteR = 0L, qteV = 0L, qteP = 0L, alqS = 0L, alqL = 0L, qteI = 0L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        //Toutes les livraisons reçues
        List<Lignelivraison> toutesLesLivraisons;
        //Toutes les sorties de produit effectuées
        List<LigneBonDeSortie> toutesLesSortie;

        List<EtatAgentWrapper> listeEtatAgent = new LinkedList<>();
        List<LigneBonDeSortie> ligneSortieAgent = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        List<Lignelivraison> ligneReceptionAgent = lignelivraisonRepository.findByLivraisonDateLivraisonBetween(debut, fin);

        List<Produit> listProduits = produitRepository.findAll();

        Iterator<Produit> itProduit = listProduits.iterator();
        while (itProduit.hasNext()) {
            Produit produit = itProduit.next();

            EtatAgentWrapper etatAgent = new EtatAgentWrapper();
            etatAgent.setProduit(produit);

            //Toutes les sorties effectuées d'un produit
            toutesLesSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
            // Toutes les livraisons effectuées d'un produits
            toutesLesLivraisons = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);

            for (LigneBonDeSortie allsortie : toutesLesSortie) {
                if (allsortie.getBonDeSortie().getMagasin().getNomMagasin().equals(nomUser)) {
                    allQteSortie += allsortie.getQuantite().intValue();
                }
            }

            for (Lignelivraison alllivraison : toutesLesLivraisons) {
                if (alllivraison.getLivraison().getMagasin().getNomMagasin().equals(nomUser)) {
                    allQteLivre += alllivraison.getQuantiteLotLivre();
                }
            }

            //Quantité vendue par agent sur une période 
            for (LigneBonDeSortie lignebs : ligneSortieAgent) {
                //Il reste une condition à ajouter dans le if{} 
                // verifié si la vente est liée a l'agent ou au magasin 
                if (lignebs.getBonDeSortie().getMagasin().getNomMagasin().equals(nomUser)) {
                    if (lignebs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()) && lignebs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)) {
                        qteVente += lignebs.getQuantite().intValue();
                        // qteV = Long.valueOf(String.valueOf(qteVente));
                    }
                }
            }

            //Quantité sortie en promotion par agent sur une période 
            for (LigneBonDeSortie lignebs : ligneSortieAgent) {
                //Il reste une condition à ajouter dans le if{} 
                // verifié si la vente est liée a l'agent ou au magasin 
                if (lignebs.getBonDeSortie().getMagasin().getNomMagasin().equals(nomUser)) {
                    if (lignebs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()) && lignebs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)) {
                        qtePromo += lignebs.getQuantite().intValue();
                        // qteP = Long.valueOf(String.valueOf(qtePromo));
                    }
                }
            }

            //calcul de la quantité sortie par produit sur une période par Agent
            qteSortie = qteVente + qtePromo;
            qteS = Long.valueOf(String.valueOf(qteSortie));
            etatAgent.setQuantiteSortie(qteS);

            //quantité totale reçue par un agent sur une période
            for (Lignelivraison livreAgent : ligneReceptionAgent) {
                if (livreAgent.getLivraison().getMagasin().getNomMagasin().equals(nomUser) && livreAgent.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteRecue += livreAgent.getQuantiteLotLivre();
                    qteR = Long.valueOf(String.valueOf(qteRecue));
                }
            }

            etatAgent.setQuantiteRecue(qteR);

            //quantité initiale de produit detenue par un agent au debut de la periode
            qteInitiale = qteInitiale + (allQteLivre - qteRecue) - (allQteSortie - qteSortie);
            qteI = Long.valueOf(String.valueOf(qteInitiale));
            etatAgent.setQuantiteInitiale(qteI);

            //calcul de la quantité détenue par l'agent à la fin de la période
            qteDetenue = (qteInitiale + qteRecue) - qteSortie;
            qteD = Long.valueOf(String.valueOf(qteDetenue));
            etatAgent.setQuantiteDetenue(qteD);

            qteVente = 0;
            qtePromo = 0;
            qteSortie = 0;
            allQteLivre = 0;
            allQteSortie = 0;
            qteInitiale = 0;
            qteDetenue = 0;
            qteRecue = 0;
            qteR = 0L;
            qteS = 0L;
            qteI = 0L;
            qteD = 0L;

            listeEtatAgent.add(etatAgent);
        }

        return listeEtatAgent;
    }

}
