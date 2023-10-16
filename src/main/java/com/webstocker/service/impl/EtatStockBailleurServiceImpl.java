package com.webstocker.service.impl;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import com.webstocker.repository.BailleurRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.EtatStockBailleurService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Athanase
 */
@Service
@Transactional
public class EtatStockBailleurServiceImpl implements EtatStockBailleurService {

    @Inject
    ProduitRepository produitRepository;

    @Inject
    LignelivraisonRepository lignelivraisonRepository;

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    BailleurRepository bailleurRepository;

    @Override
    public List<EtatStockGlobalAimasWrapper> etatStockParBailleur(String nomBailleur, String dateDebut, String dateFin) {

        String monProduit1 = null;
        String monProduit2 = null;

        int qteVente = 0, qtePromo = 0, valVente = 0, qteCash = 0, somCret = 0, somCash = 0, qteLivre = 0;
        int qtePerte = 0, qteTrans = 0, qteTotalLivreBailleur = 0, qteSortieBailleur = 0;
        int qteAllSortie = 0, qteAllLivre = 0, qteInitiale = 0, somRecouvre = 0, qteFinale = 0;

        Long qv = 0L, qc = 0L, vlv = 0L, qI = 0L, qtlbail = 0L, ql = 0L, qp = 0L, qpt = 0L, qt = 0L, qT = 0L, sr = 0L;

        List<EtatStockGlobalAimasWrapper> etatStockBailleurList = new LinkedList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<Lignelivraison> allLivraison;
        List<LigneBonDeSortie> allSortie;

        Bailleur bailleur = bailleurRepository.findByNomBailleur(nomBailleur);

        List<Lignelivraison> lesLivraisonDunBailleur = lignelivraisonRepository.findAllByLivraisonCommandeBailleur(bailleur);
        Iterator<Lignelivraison> itLivraisonParBailleur = lesLivraisonDunBailleur.iterator();

        List<LigneBonDeSortie> ligneVente = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);

        List<Produit> allProduits = produitRepository.findAll();
        List<Lignelivraison> livraisonBailleur = lignelivraisonRepository.findByLivraisonCommandeBailleurNomBailleurAndLivraisonDateLivraisonBetween(nomBailleur, debut, fin);

        //List<Lignelivraison> livraisonBailleur = lignelivraisonRepository.findByLivraisonCommandeBailleurAndLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(bailleur,produit, debut, fin);
        Iterator<Lignelivraison> livreIt = livraisonBailleur.iterator();
//        Iterator<Produit> prodIt = allProduits.iterator(); 

//        while (livreIt.hasNext()) {
//        while (itTouteLivraison.hasNext()) {
        while (itLivraisonParBailleur.hasNext()) {

            Lignelivraison livraison = itLivraisonParBailleur.next();

            EtatStockGlobalAimasWrapper etatStockBailleur = new EtatStockGlobalAimasWrapper();
            lesLivraisonDunBailleur = lignelivraisonRepository.findAllByLivraisonCommandeBailleur(livraison.getLivraison().getCommande().getBailleur());

            allSortie = ligneBonDeSortieRepository.findAllByProduit(livraison.getLot().getProduit());
            monProduit1 = livraison.getLot().getProduit().getNomProduit();

            if (monProduit1.equals(monProduit2)) {
                System.out.println("LE MEME PRODUIT OOOHHH");
            } else {
                etatStockBailleur.setProduit(livraison.getLot().getProduit());
                etatStockBailleur.setBailleur(livraison.getLivraison().getCommande().getBailleur());

                for (Lignelivraison livbail : lesLivraisonDunBailleur) {
                    if (livbail.getLot().getProduit().getNomProduit().equals(livraison.getLot().getProduit().getNomProduit())) {
                        qteTotalLivreBailleur += livbail.getQuantiteLotLivre();
                    }
                }

                for (LigneBonDeSortie allSortieParBailleur : allSortie) {
                    if (allSortieParBailleur.getLot().getProduit().getNomProduit().equals(livraison.getLot().getProduit().getNomProduit())) {
                        qteSortieBailleur += allSortieParBailleur.getQuantite().intValue();
                    }
                }

                for (Lignelivraison alllivraison : livraisonBailleur) {
                    if (alllivraison.getLivraison().getCommande().getBailleur().getNomBailleur().equals(nomBailleur)) {
                        qteAllLivre += alllivraison.getQuantiteLotLivre();
                    }
                }

                //etatStockBailleur.setQuantiteLivre(ql);
                for (LigneBonDeSortie lbs : ligneVente) {
                    if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE) && lbs.getLot().getProduit().getNomProduit().equals(livraison.getLot().getProduit().getNomProduit())) {
                        qteVente += lbs.getQuantite().intValue();
//                        valVente += lbs.getMontantVente().intValue();
                        valVente += lbs.getPrixDeVente().intValue();
                        qv = Long.valueOf(String.valueOf(qteVente));
                        vlv = Long.valueOf(String.valueOf(valVente));
                    }
                }
                etatStockBailleur.setQuantiteVendue(qv);
                etatStockBailleur.setValeurVente(vlv);

                for (LigneBonDeSortie lbs : ligneVente) {
                    if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION) && lbs.getLot().getProduit().getNomProduit().equals(livraison.getLot().getProduit().getNomProduit())) {
                        qtePromo += lbs.getQuantite();
                        qp = Long.valueOf(String.valueOf(qtePromo));
                    }
                }
                etatStockBailleur.setQuantitePromotion(qp);

                for (LigneBonDeSortie lbs : ligneVente) {
                    if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE) && lbs.getLot().getProduit().getNomProduit().equals(livraison.getLot().getProduit().getNomProduit())) {
                        qtePerte += lbs.getQuantite().intValue();
                        qpt = Long.valueOf(String.valueOf(qtePerte));
                    }
                }
                etatStockBailleur.setQuantitePerte(qpt);

                for (LigneBonDeSortie lbs : ligneVente) {
                    if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT) && lbs.getLot().getProduit().getNomProduit().equals(livraison.getLot().getProduit().getNomProduit())) {
                        qteTrans += lbs.getQuantite().intValue();
                        qt = Long.valueOf(String.valueOf(qteTrans));
                    }
                }
                etatStockBailleur.setQuantiteTransfert(qt);

                for (Lignelivraison lignelv : livraisonBailleur) {
                    if (lignelv.getLot().getProduit().getNomProduit().equals(livraison.getLot().getProduit().getNomProduit())) {
                        qteLivre += lignelv.getQuantiteLotLivre().longValue();
                        ql = Long.valueOf(String.valueOf(qteLivre));
                    }
                }
                etatStockBailleur.setQuantiteLivre(ql);

                qteFinale = (qteTotalLivreBailleur ) -(qtePromo + qteVente + qtePerte);
//                qteFinale = (qteTotalLivreBailleur + qteSortieBailleur ) -(qtePromo + qteVente + qtePerte);
//                qteTotale = (qteAllLivre - qteLivre) - ((qteAllSortie) - (qtePromo + qteVente + qtePerte));
//                qteFinale = (qteTotalLivreBailleur - qteLivre) - ((qteSortieBailleur) - (qtePromo + qteVente + qtePerte));
                qT = Long.valueOf(String.valueOf(qteFinale));
                etatStockBailleur.setQuantiteFinale(qT);

                // qteInitiale = qteInitiale ;
                qI = Long.valueOf(String.valueOf(qteInitiale));
//                qteFinale = (qteTotalLivreBailleur - qteLivre) - ((qteAllSortie + qteTrans) - (qtePromo + qteVente + qtePerte));

                etatStockBailleur.setQuantiteInitial(qI);
                System.out.println("LA QUANTITE INITIALE " + qteFinale);

                qteAllLivre = 0;
                qteTotalLivreBailleur = 0;
                qteAllSortie = 0;
                qteFinale = 0;
                qteInitiale = 0;
                qteVente = 0;
                qtePerte = 0;
                qteTrans = 0;
                qtePromo = 0;
                qteVente = 0;
                valVente = 0;
                qteLivre = 0;
                qI = 0L;
                qpt = 0L;
                qT = 0L;
                qt = 0L;
                qp = 0L;
                vlv = 0L;
                qv = 0L;
                ql = 0L;

                etatStockBailleurList.add(etatStockBailleur);
            }
            monProduit2 = livraison.getLot().getProduit().getNomProduit();
        }

        return etatStockBailleurList;
    }

    @Override
    public List<EtatStockGlobalAimasWrapper> etatStockProduitLivreParBailleur(Bailleur bailleur, String dateDebut, String dateFin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
