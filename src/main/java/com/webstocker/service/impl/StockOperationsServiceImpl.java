package com.webstocker.service.impl;

import com.webstocker.domain.*;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.EtatProduitParMagasinWrapper;
import com.webstocker.domain.wrapper.StockSortieWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.LotRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.service.StockOperationsService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by komi on 09/09/16.
 */
@Service
public class StockOperationsServiceImpl implements StockOperationsService {

    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    private LignelivraisonRepository lignelivraisonRepository;

    @Inject
    private LotRepository lotRepository;

    @Inject
    private MagasinRepository magasinRepository;



    @Override
    public Long getAvailableProducts(Produit produit) {
        List<Lignelivraison> lignelivraisons = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);
        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieRepository.findAllByProduit(produit);

        int qteSortie=0;
        int qteLivree=0;

        for (Lignelivraison lignelivraison :
            lignelivraisons) {
            qteLivree+=lignelivraison.getQuantiteLotLivre();
        }

        for (LigneBonDeSortie ligneBonDeSortie :
            ligneBonDeSorties) {
            qteSortie+=ligneBonDeSortie.getQuantite().intValue();
        }

        Integer qteRestante=qteLivree-qteSortie;


        return qteRestante.longValue();
    }

    @Override
    public Long getAvailableProductsByMagasin(Produit produit, Magasin magasin) {

        int qteSortie=0;
        int qteLivree=0;

        if(magasin.getId() == 1L || magasin.getNomMagasin().contains("MAGASIN CENTRAL")) {

            List<EtatProduitParMagasinWrapper> etatProduitParMagasinWrappers = etatProduitParMagasin(produit);

            for(EtatProduitParMagasinWrapper produitParMagasinWrapper:etatProduitParMagasinWrappers){
                if (produitParMagasinWrapper.getMagasin().getId()==1L){
                    qteLivree= produitParMagasinWrapper.getQuantiteTotalEnStock().intValue();
                }
            }

//            List<Lignelivraison> lignelivraisons = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);
//
//            List<LigneBonDeSortie> ligneTransfertsRecus = ligneBonDeSortieRepository.findAllByProduitAndBonDeSortieDestination(produit,magasin);
//
//            List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieRepository.findAllByProduit(produit);
//
//
//            for (Lignelivraison lignelivraison :
//                lignelivraisons) {
//                qteLivree += lignelivraison.getQuantiteLotLivre();
//            }
//
//            for (LigneBonDeSortie ligneBonDeSortie:ligneTransfertsRecus){
//                qteLivree+=ligneBonDeSortie.getQuantite().intValue();
//            }
//
//            for (LigneBonDeSortie ligneBonDeSortie :
//                ligneBonDeSorties) {
//                qteSortie += ligneBonDeSortie.getQuantite().intValue();
//            }
        }else {

            List<LigneBonDeSortie> lignelivraisons = ligneBonDeSortieRepository.findAllByProduitAndBonDeSortieDestination(produit,magasin);
            List<LigneBonDeSortie> ligneBonDeSortiesVente = ligneBonDeSortieRepository.findAllByProduitAndBonDeSortieMagasinAndBonDeSortieTypeSortie(produit, magasin, TypeSortie.VENTE);
            List<LigneBonDeSortie> ligneBonDeSortiesPromotion = ligneBonDeSortieRepository.findAllByProduitAndBonDeSortieMagasinAndBonDeSortieTypeSortie(produit, magasin, TypeSortie.PROMOTION);
            List<LigneBonDeSortie> ligneBonDeSortiesPerte = ligneBonDeSortieRepository.findAllByProduitAndBonDeSortieMagasinAndBonDeSortieTypeSortie(produit,magasin, TypeSortie.PERTE);
            List<LigneBonDeSortie> ligneBonDeSortiesTransfert = ligneBonDeSortieRepository.findAllByProduitAndBonDeSortieMagasinAndBonDeSortieTypeSortie(produit,magasin, TypeSortie.TRANSFERT);

            List<LigneBonDeSortie> ligneBonDeSorties =new LinkedList<>();
            ligneBonDeSorties.addAll(ligneBonDeSortiesVente);
            ligneBonDeSorties.addAll(ligneBonDeSortiesPromotion);
            ligneBonDeSorties.addAll(ligneBonDeSortiesPerte);
            ligneBonDeSorties.addAll(ligneBonDeSortiesTransfert);

            for (LigneBonDeSortie lignelivraison :
                lignelivraisons) {
                qteLivree += lignelivraison.getQuantite();
            }

            for (LigneBonDeSortie ligneBonDeSortie :
                ligneBonDeSorties) {
                qteSortie += ligneBonDeSortie.getQuantite().intValue();
            }
        }

        Integer qteRestante=qteLivree-qteSortie;


        return qteRestante.longValue();
    }

    @Override
    public List<StockSortieWrapper> faireUneSortie(Produit produit, Long quantite) {

        List<StockSortieWrapper> sortieWrapperList=new LinkedList<>();
        Long qteRestante = quantite;
        List<Lot> lots = lotRepository.findAllByProduitOrderByDatePeremptionAsc(produit);
        Iterator<Lot> iterator = lots.iterator();
        while (iterator.hasNext()){
            Lot lot = iterator.next();
            if(lot.isComplete()){
                iterator.remove();
            }
        }

        for (Lot lot : lots) {
            if(qteRestante==0L){
                break;
            }

            if (lot.quantiteRestante()==0L) {
                continue;
            }

            Long qteRestLot=lot.quantiteRestante();
            StockSortieWrapper sortieWrapper=new StockSortieWrapper();
            sortieWrapper.setLot(lot);
            sortieWrapper.setProduit(produit);
            if(qteRestante>=qteRestLot){
                sortieWrapper.setQuantite(qteRestLot);
                qteRestante=qteRestante-qteRestLot;
            }else{
                sortieWrapper.setQuantite(qteRestante);
                qteRestante=0L;
            }
            sortieWrapperList.add(sortieWrapper);

        }

        return sortieWrapperList;
    }


    public List<EtatProduitParMagasinWrapper> etatProduitParMagasin(Produit produit) {



        String magasin1 = null;
        String magasin2 = null;

        String dateCommenecement = "2016-12-01";
        String dateDebut=dateCommenecement;
        String dateFin;

        Long QuantiteProduitTranfereRecuMagasin = 0L;
        Long quantiteTransfere = 0L;
        Long quantiteVendue = 0L, quantitePromo = 0L, quantitePerte = 0L;
        Long quantiteTotalEnStock = 0L, quantiteGlobaleLivre = 0L, quantiteGlobaleSortie = 0L;
        Long reste = 0L;

        Long qteTransfertRecuDebut = 0L;
        Long qteVenteDebut = 0L;
        Long qteTransfertDebut = 0L;
        Long qtePromoDebut = 0L;
        Long qtePerteDebut = 0L;

        Long qteFinTransfertRecu = 0L;
        Long qteFinVente = 0L;
        Long qteFinTransfert = 0L;
        Long qteFinPromotion = 0L;
        Long qteFinPerte = 0L;

        //Format de la date en LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.now();
        LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);

        List<EtatProduitParMagasinWrapper> listeStockParMagasin = new LinkedList<>();

        // Toute livraison total d'un produit au magasin central
        List<Lignelivraison> toutesLivraison = null;
        // Toute d'un produit
        List<LigneBonDeSortie> toutesSortie = null;

        //Liste des produits sortie  de date dateDebutStock à date debut
        List<LigneBonDeSortie> listeSortieDebut = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(dateDebutStock, debut);

        //Liste des produits sortie  de date dateDebutStock à date fin
        List<LigneBonDeSortie> sorteDebutaFin = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(dateDebutStock, fin);

        // Liste des produits livré au magasin central de date dateDebutStock à date debut
        List<Lignelivraison> listeProduitLivreDebut = lignelivraisonRepository.findByLivraisonDateLivraisonBetween(dateDebutStock, debut);

        //Liste des produits sortie  de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        Iterator<LigneBonDeSortie> itSortieMagasin = listeSortieParPeriode.iterator();

        // Liste des produits livré au magasin central de date debut à date fin
        List<Lignelivraison> listeProduitLivre = lignelivraisonRepository.findByLivraisonDateLivraisonBetween(debut, fin);

        //Liste de tous les magasins
        List<Magasin> tousLesMagasin = magasinRepository.findAll();
        Iterator<Magasin> itMagasin = tousLesMagasin.iterator();

        while (itMagasin.hasNext()) {
            Magasin magasin = itMagasin.next();

            EtatProduitParMagasinWrapper etatProduitParMagasin = new EtatProduitParMagasinWrapper();

            magasin1 = magasin.getNomMagasin();
            if (magasin1.equals(magasin2)) {

            } else {
                etatProduitParMagasin.setMagasin(magasin);
                etatProduitParMagasin.setProduit(produit);

                toutesLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);
                for (Lignelivraison tl : toutesLivraison) {
                    quantiteGlobaleLivre += tl.getQuantiteLotLivre();
                }

                toutesSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
                for (LigneBonDeSortie lbs : toutesSortie) {
                    quantiteGlobaleSortie += lbs.getQuantite();
                }

                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' à date fin dans les differents magasin autre que le magasin central
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if (sortieQteStock.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                                && sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && sortieQteStock.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            qteFinTransfertRecu += sortieQteStock.getQuantite();
                        }
                    }
                }

                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' dans les differents magasin autre que le magasin central
//                for (LigneBonDeSortie sortiePromoDebut : listeSortieDebut) {
//                    if (sortiePromoDebut.getBonDeSortie().getMagasin() != null) {
//                        if (sortiePromoDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
//                                && sortiePromoDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
//                                && sortiePromoDebut.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
//                            qteTransfertRecuDebut += sortiePromoDebut.getQuantite();
//                        }
//                    }
//                }

                //Les quantités reçue par transfert dans les differents magasin autre que le magasin central
                for (LigneBonDeSortie totalTransfert : listeSortieParPeriode) {
                    if (totalTransfert.getBonDeSortie().getMagasin() != null) {
                        if (totalTransfert.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                                && totalTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && totalTransfert.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            QuantiteProduitTranfereRecuMagasin += totalTransfert.getQuantite();
                        }
                    }
                }
                etatProduitParMagasin.setQuantiteTransfertRecuMagasin(QuantiteProduitTranfereRecuMagasin);

                //Quantité vendue par magasin depuis le debut '01-12-2016' à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            qteFinVente += sortieQteStock.getQuantite();
                        }
                    }
                }

                //Quantité vendue par magasin depuis le debut '01-12-2016'
//                for (LigneBonDeSortie sortieVenteDebut : listeSortieDebut) {
//                    if (sortieVenteDebut.getBonDeSortie().getMagasin() != null) {
//                        if ((sortieVenteDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
//                                && (sortieVenteDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
//                                && (sortieVenteDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
//                            qteVenteDebut += sortieVenteDebut.getQuantite();
//                        }
//                    }
//                }

                //Quantité vendue par magasin
                for (LigneBonDeSortie ligneDesVentes : listeSortieParPeriode) {
                    if (ligneDesVentes.getBonDeSortie().getMagasin() != null) {
                        if ((ligneDesVentes.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneDesVentes.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneDesVentes.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            quantiteVendue += ligneDesVentes.getQuantite();
                        }
                    }
                }
                etatProduitParMagasin.setQuantiteVendue(quantiteVendue);

                //Quantité sortie en promo par magasin depuis le 01-12-2016 à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            qteFinPromotion += sortieQteStock.getQuantite();
                        }
                    }
                }

                //Quantité sortie en promo par magasin depuis le 01-12-2016
//                for (LigneBonDeSortie sortiePromoDebut : listeSortieDebut) {
//                    if (sortiePromoDebut.getBonDeSortie().getMagasin() != null) {
//                        if ((sortiePromoDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
//                                && (sortiePromoDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
//                                && (sortiePromoDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
//                            qtePromoDebut += sortiePromoDebut.getQuantite();
//                        }
//                    }
//                }

                //Quantité sortie en promo par magasin
                for (LigneBonDeSortie sortiePromoDebut : listeSortieParPeriode) {
                    if (sortiePromoDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortiePromoDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortiePromoDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortiePromoDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            quantitePromo += sortiePromoDebut.getQuantite();
                        }
                    }
                }
                etatProduitParMagasin.setQuantitePromo(quantitePromo);

                //Quantité perdue par magasin depuis 01-12-2016 à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            qteFinPerte += sortieQteStock.getQuantite();
                        }
                    }
                }

                //Quantité perdue par magasin depuis 01-12-2016
//                for (LigneBonDeSortie sortiePerteDebut : listeSortieDebut) {
//                    if (sortiePerteDebut.getBonDeSortie().getMagasin() != null) {
//                        if ((sortiePerteDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
//                                && (sortiePerteDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
//                                && (sortiePerteDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
//                            qtePerteDebut += sortiePerteDebut.getQuantite();
//                        }
//                    }
//                }

                //Quantité perdue par magasin
                for (LigneBonDeSortie sortiePerteDebut : listeSortieParPeriode) {
                    if (sortiePerteDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortiePerteDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortiePerteDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortiePerteDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            quantitePerte += sortiePerteDebut.getQuantite();
                        }
                    }
                }
                etatProduitParMagasin.setQuantitePerte(quantitePerte);

                // Quantité de produit transferée du magasin d'origine depuis le 01-12-2016 à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            qteFinTransfert += sortieQteStock.getQuantite();
                        }
                    }
                }

                // Quantité de transferée du magasin d'origine depuis le 01-12-2016
//                for (LigneBonDeSortie sortieTransfertDebut : listeSortieDebut) {
//                    if (sortieTransfertDebut.getBonDeSortie().getMagasin() != null) {
//                        if ((sortieTransfertDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
//                                && (sortieTransfertDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
//                                && (sortieTransfertDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
//                            qteTransfertDebut += sortieTransfertDebut.getQuantite();
//                        }
//                    }
//                }

                // Quantité de transferée du magasin d'origine
                for (LigneBonDeSortie ligneSortieTransfert : listeSortieParPeriode) {
                    if (ligneSortieTransfert.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieTransfert.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieTransfert.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            quantiteTransfere += ligneSortieTransfert.getQuantite();
                        }
                    }
                }
                etatProduitParMagasin.setQuantiteTransfert(quantiteTransfere);

                if (magasin.getNomMagasin().equals("MAGASIN CENTRAL")) {
                    quantiteTotalEnStock = (quantiteGlobaleLivre - qteFinTransfert) + qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte);
                } else {
                    quantiteTotalEnStock = qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte) - qteFinTransfert;
                }

//                if (magasin.getNomMagasin().equals("MAGASIN CENTRAL")) {
//                    if (dateDebutStock.isEqual(debut)) {
//                        quantiteTotalEnStock = (quantiteGlobaleLivre - quantiteTransfere) + QuantiteProduitTranfereRecuMagasin - (quantiteVendue + quantitePromo + quantitePerte);
//                    } else {
//                        quantiteTotalEnStock = (quantiteGlobaleLivre - quantiteTransfere) - (qteVenteDebut + qtePromoDebut + qtePerteDebut + qteTransfertDebut + qteTransfertRecuDebut) + QuantiteProduitTranfereRecuMagasin - (quantiteVendue + quantitePromo + quantitePerte);
//                    }
//                } else if (dateDebutStock.isEqual(debut)) {
////                     quantiteTotalEnStock = 0L;
//                    quantiteTotalEnStock = QuantiteProduitTranfereRecuMagasin - (quantiteVendue + quantitePromo + quantitePerte) - quantiteTransfere;
////                    quantiteTotalEnStock = quantiteTransfere + QuantiteProduitTranfereRecuMagasin - (quantiteVendue + quantitePromo + quantitePerte);
//                } else {
////                    quantiteTotalEnStock = quantiteTransfere + QuantiteProduitTranfereRecuMagasin - (qteTransfertDebut + qteTransfertRecuDebut) - (qteVenteDebut + qtePromoDebut + qtePerteDebut) - (quantiteVendue + quantitePromo + quantitePerte);
//                    quantiteTotalEnStock = QuantiteProduitTranfereRecuMagasin - (qteTransfertDebut + qteTransfertRecuDebut) - (qteVenteDebut + qtePromoDebut + qtePerteDebut) - (quantiteVendue + quantitePromo + quantitePerte) - quantiteTransfere;
//                }
                if (quantiteTotalEnStock < 0) {
                    quantiteTotalEnStock = 0 - quantiteTotalEnStock;
                }

                etatProduitParMagasin.setQuantiteTotalEnStock(quantiteTotalEnStock);

                QuantiteProduitTranfereRecuMagasin = 0L;
                quantiteGlobaleLivre = 0L;
                quantiteGlobaleSortie = 0L;
                quantitePerte = 0L;
                quantitePromo = 0L;
                quantiteTotalEnStock = 0L;
                quantiteTransfere = 0L;
                quantiteVendue = 0L;
                qteTransfertDebut = 0L;
                qteTransfertRecuDebut = 0L;
                qteVenteDebut = 0L;
                qtePromoDebut = 0L;
                qtePerteDebut = 0L;

                qteFinTransfertRecu = 0L;
                qteFinVente = 0L;
                qteFinTransfert = 0L;
                qteFinPromotion = 0L;
                qteFinPerte = 0L;

                listeStockParMagasin.add(etatProduitParMagasin);
            }
            magasin2 = magasin.getNomMagasin();
        }
        return listeStockParMagasin;
    }


}
