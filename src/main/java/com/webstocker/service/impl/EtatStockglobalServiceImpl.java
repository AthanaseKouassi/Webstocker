package com.webstocker.service.impl;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.EtatStockGlobalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Athanase
 */
@Slf4j
@Service
@Transactional
public class EtatStockglobalServiceImpl implements EtatStockGlobalService {

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    ProduitRepository produitRepository;

    @Inject
    LignelivraisonRepository lignelivraisonRepository;

    @Override
    public List<EtatStockGlobalAimasWrapper> etatStockGlobal(String dateDebut, String dateFin) {

        int qteVente = 0, qtePromo = 0, valVente = 0, qteLivre = 0;
        int qtePerte = 0, qteTrans = 0;
        int qteAllSortie = 0, qteAllLivre = 0, qteTotale = 0, somRecouvre = 0, qteTotaltransfert = 0;
        int sizeLesReglements = 0, sRecouv = 0;
        Long qv = 0L, qc = 0L, vlv = 0L, scr = 0L, sca = 0L, ql = 0L;
        Long qp = 0L, qpt = 0L, qt = 0L, qT = 0L, sr = 0L, qtTf = 0L;

        String dateCommenecement = "2016-12-01";
        Long qteFinTransfertRecu = 0L;
        Long qteFinVendue = 0L;
        Long qteFinTransfert = 0L;
        Long qteFinPromotion = 0L;
        Long qteFinPerte = 0L;
        Long montantVente = 0L;
        Long quantiteGlobalLivre = 0L;
        Long quantiteGlobaleProduitEnStock = 0L;
        Long quantiteArrivageDateFin = 0L;
        Long quantiteTotalEnStock = 0L;

        Long qteStockInitial = 0L;


        List<EtatStockGlobalAimasWrapper> etatStockGlobalList = new LinkedList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);

        List<Lignelivraison> allLivraison;
        List<LigneBonDeSortie> allSortie;

        List<LigneBonDeSortie> lignebons = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        List<Lignelivraison> ligneLivre = lignelivraisonRepository.findByLivraisonDateLivraisonBetween(debut, fin);

        //Tous les transferts de produit avant la date de la fin du mois
        List<LigneBonDeSortie> lignebsTransfertAvantdate = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBefore(fin);

        //Liste des produits sortie de date dateDebutStock à date fin
        List<LigneBonDeSortie> sorteDebutaFin = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(dateDebutStock, fin);


        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> it = listProduits.iterator();
        while (it.hasNext()) {
            Produit produit = it.next();

            EtatStockGlobalAimasWrapper etatStockGlobal = new EtatStockGlobalAimasWrapper();
            etatStockGlobal.setProduit(produit);

            //Toutes les livraisons au magasin central du produit dans la periode de DateDebutStock a date fin
            List<Lignelivraison> listLivraisonProduitDateDebutStockADateFin = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, dateDebutStock, fin);

            allSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
            allLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);

            for (Lignelivraison alllivraison : allLivraison) {
                quantiteGlobalLivre += alllivraison.getQuantiteLotLivre();
            }

            //Recuperer les arrivages d'un produit de la periode dateDebutStock et date fin
            for (Lignelivraison qteArrivageDateFin : listLivraisonProduitDateDebutStockADateFin) {
                quantiteArrivageDateFin += qteArrivageDateFin.getQuantiteLotLivre();
            }
            etatStockGlobal.setArrivage(quantiteArrivageDateFin);

            for (LigneBonDeSortie allsortie : allSortie) {
                qteAllSortie += allsortie.getQuantite().intValue();
            }

            //tous les transferts effectués pour un produit
            for (LigneBonDeSortie allsortie : allSortie) {
                if (allsortie.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)) {
                    qteTotaltransfert += allsortie.getQuantite();
                    qtTf = Long.valueOf(String.valueOf(qteTotaltransfert));
                }
            }

            etatStockGlobal.setQuantiteTotalTransfert(qtTf);

            //Les ventes realisées depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : sorteDebutaFin) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinVendue += ligneDebutaFin.getQuantite();
                    montantVente += ligneDebutaFin.getPrixDeVente();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteVente += lbs.getQuantite().intValue();
                    valVente += lbs.getPrixDeVente().intValue();
                    qv = Long.valueOf(String.valueOf(qteVente));
                    vlv = Long.valueOf(String.valueOf(valVente));
                }
            }
            etatStockGlobal.setQuantiteVendue(qv);
            etatStockGlobal.setValeurVente(vlv);

            //les promotions realisées depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : sorteDebutaFin) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinPromotion += ligneDebutaFin.getQuantite();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qtePromo += lbs.getQuantite().intValue();
                    qp = Long.valueOf(String.valueOf(qtePromo));
                }
            }
            etatStockGlobal.setQuantitePromotion(qp);

            //les transferts realisés depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : sorteDebutaFin) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinTransfert += ligneDebutaFin.getQuantite();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteTrans += lbs.getQuantite().intValue();
                    qt = Long.valueOf(String.valueOf(qteTrans));
                }
            }
            etatStockGlobal.setQuantiteTransfert(qt);

            //les pertes realisées depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : sorteDebutaFin) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinPerte += ligneDebutaFin.getQuantite();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qtePerte += lbs.getQuantite().intValue();
                    qpt = Long.valueOf(String.valueOf(qtePerte));
                }
            }
            etatStockGlobal.setQuantitePerte(qpt);

            for (Lignelivraison lignelv : ligneLivre) {
                if (lignelv.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteLivre += (int) lignelv.getQuantiteLotLivre().longValue();
                    ql = Long.valueOf(String.valueOf(qteLivre));
                }
            }
            etatStockGlobal.setQuantiteLivre(ql);
            quantiteGlobaleProduitEnStock = quantiteArrivageDateFin - (qteFinVendue + qteFinPromotion + qteFinPerte);

            qteStockInitial = quantiteGlobaleProduitEnStock - quantiteArrivageDateFin + (qteFinVendue + qteFinPromotion + qteFinPerte);


            etatStockGlobal.setQuantiteInitial(qteStockInitial);
            etatStockGlobal.setQuantiteTotalEnStock(quantiteGlobaleProduitEnStock);

            qteAllLivre = 0;
            qteAllSortie = 0;
            qteTotaltransfert = 0;
            qteVente = 0;
            qtePerte = 0;
            qteTrans = 0;
            qtePromo = 0;
            qteVente = 0;
            valVente = 0;
            qteLivre = 0;
            qteTotale = 0;
            qpt = 0L;
            qt = 0L;
            qp = 0L;
            vlv = 0L;
            qv = 0L;
            ql = 0L;
            qT = 0L;

            qteFinVendue = 0L;
            qteFinTransfert = 0L;
            qteFinPromotion = 0L;
            qteFinPerte = 0L;
            montantVente = 0L;
            quantiteGlobalLivre = 0L;
            quantiteGlobaleProduitEnStock = 0L;
            quantiteArrivageDateFin = 0L;
            quantiteTotalEnStock = 0L;

            etatStockGlobalList.add(etatStockGlobal);

        }

        return etatStockGlobalList;
    }

    @Override
    public List<EtatStockGlobalAimasWrapper> etatStockGlobalNew(LocalDate startDate, LocalDate endDate) {

        int qteVente = 0, qtePromo = 0, valVente = 0, qteLivre = 0;
        int qtePerte = 0, qteTrans = 0;
        int qteAllSortie = 0, qteAllLivre = 0, qteTotale = 0, somRecouvre = 0, qteTotaltransfert = 0;
        int sizeLesReglements = 0, sRecouv = 0;
        Long qv = 0L, qc = 0L, vlv = 0L, scr = 0L, sca = 0L, ql = 0L;
        Long qp = 0L, qpt = 0L, qt = 0L, qT = 0L, sr = 0L, qtTf = 0L;

        String dateCommenecement = "2016-12-01";
        Long qteFinTransfertRecu = 0L;
        Long qteFinVendue = 0L;
        Long qteFinTransfert = 0L;
        Long qteFinPromotion = 0L;
        Long qteFinPerte = 0L;
        Long montantVente = 0L;
        Long quantiteGlobalLivre = 0L;
        Long quantiteGlobaleProduitEnStock = 0L;
        Long quantiteArrivageDateFin = 0L;
        Long quantiteTotalEnStock = 0L;

        Long qteStockInitial = 0L;

        List<EtatStockGlobalAimasWrapper> etatStockGlobalList = new LinkedList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);

        List<Lignelivraison> allLivraison;
        List<LigneBonDeSortie> allSortie;

        List<LigneBonDeSortie> lignebons = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(startDate, endDate);
        List<Lignelivraison> ligneLivre = lignelivraisonRepository.findByLivraisonDateLivraisonBetween(startDate, endDate);

        //Tous les transferts de produit avant la date de la fin du mois
        List<LigneBonDeSortie> lignebsTransfertAvantdate = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBefore(endDate);

        //Liste des produits sortie de date dateDebutStock à date fin
        List<LigneBonDeSortie> lstSorteDebutaFins = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(dateDebutStock, endDate);


        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> it = listProduits.iterator();
        while (it.hasNext()) {
            Produit produit = it.next();

            EtatStockGlobalAimasWrapper etatStockGlobal = new EtatStockGlobalAimasWrapper();
            etatStockGlobal.setProduit(produit);

            //Toutes les livraisons au magasin central du produit dans la periode de DateDebutStock a date fin
            List<Lignelivraison> listLivraisonProduitDateDebutStockADateFin = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, dateDebutStock, endDate);

            allSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
            allLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);

            for (Lignelivraison alllivraison : allLivraison) {
                quantiteGlobalLivre += alllivraison.getQuantiteLotLivre();
            }

            //Recuperer les arrivages d'un produit de la periode dateDebutStock et date fin
            for (Lignelivraison qteArrivageDateFin : listLivraisonProduitDateDebutStockADateFin) {
                quantiteArrivageDateFin += qteArrivageDateFin.getQuantiteLotLivre();
            }
            etatStockGlobal.setArrivage(quantiteArrivageDateFin);

            for (LigneBonDeSortie allsortie : allSortie) {
                qteAllSortie += allsortie.getQuantite().intValue();
            }

            //tous les transferts effectués pour un produit
            for (LigneBonDeSortie allsortie : allSortie) {
                if (allsortie.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)) {
                    qteTotaltransfert += allsortie.getQuantite();
                    qtTf = Long.valueOf(String.valueOf(qteTotaltransfert));
                }
            }

            etatStockGlobal.setQuantiteTotalTransfert(qtTf);

            //Les ventes realisées depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : lstSorteDebutaFins) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinVendue += ligneDebutaFin.getQuantite();
                    montantVente += ligneDebutaFin.getPrixDeVente();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteVente += lbs.getQuantite().intValue();
                    valVente += lbs.getPrixDeVente().intValue();
                    qv = Long.valueOf(String.valueOf(qteVente));
                    vlv = Long.valueOf(String.valueOf(valVente));
                }
            }
            etatStockGlobal.setQuantiteVendue(qv);
            etatStockGlobal.setValeurVente(vlv);

            //les promotions realisées depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : lstSorteDebutaFins) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinPromotion += ligneDebutaFin.getQuantite();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qtePromo += lbs.getQuantite().intValue();
                    qp = Long.valueOf(String.valueOf(qtePromo));
                }
            }
            etatStockGlobal.setQuantitePromotion(qp);

            //les transferts realisés depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : lstSorteDebutaFins) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinTransfert += ligneDebutaFin.getQuantite();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteTrans += lbs.getQuantite().intValue();
                    qt = Long.valueOf(String.valueOf(qteTrans));
                }
            }
            etatStockGlobal.setQuantiteTransfert(qt);

            //les pertes realisées depuis la date dateDebutStock à la date fin
            for (LigneBonDeSortie ligneDebutaFin : lstSorteDebutaFins) {
                if (ligneDebutaFin.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE)
                    && ligneDebutaFin.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteFinPerte += ligneDebutaFin.getQuantite();
                }
            }

            for (LigneBonDeSortie lbs : lignebons) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE)
                    && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qtePerte += lbs.getQuantite().intValue();
                    qpt = Long.valueOf(String.valueOf(qtePerte));
                }
            }
            etatStockGlobal.setQuantitePerte(qpt);

            for (Lignelivraison lignelv : ligneLivre) {
                if (lignelv.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteLivre += (int) lignelv.getQuantiteLotLivre().longValue();
                    ql = Long.valueOf(String.valueOf(qteLivre));
                }
            }
            etatStockGlobal.setQuantiteLivre(ql);
            quantiteGlobaleProduitEnStock = quantiteArrivageDateFin - (qteFinVendue + qteFinPromotion + qteFinPerte);
            log.info("LA QUANTITE GLOBAL EN STOCK ::{}", quantiteGlobaleProduitEnStock);

            qteStockInitial = quantiteGlobaleProduitEnStock - quantiteArrivageDateFin + (qteFinVendue + qteFinPromotion + qteFinPerte);


            etatStockGlobal.setQuantiteInitial(qteStockInitial);
            etatStockGlobal.setQuantiteTotalEnStock(quantiteGlobaleProduitEnStock);

            qteAllLivre = 0;
            qteAllSortie = 0;
            qteTotaltransfert = 0;
            qteVente = 0;
            qtePerte = 0;
            qteTrans = 0;
            qtePromo = 0;
            qteVente = 0;
            valVente = 0;
            qteLivre = 0;
            qteTotale = 0;
            qpt = 0L;
            qt = 0L;
            qp = 0L;
            vlv = 0L;
            qv = 0L;
            ql = 0L;
            qT = 0L;

            qteFinVendue = 0L;
            qteFinTransfert = 0L;
            qteFinPromotion = 0L;
            qteFinPerte = 0L;
            montantVente = 0L;
            quantiteGlobalLivre = 0L;
            quantiteGlobaleProduitEnStock = 0L;
            quantiteArrivageDateFin = 0L;
            quantiteTotalEnStock = 0L;

            etatStockGlobalList.add(etatStockGlobal);

        }

        return etatStockGlobalList;
    }

}
