package com.webstocker.service.impl;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.StatusTransfert;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.EtatDeTousLesProduitsDunMagasinService;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
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
public class EtatDeTousLesProduitsDunMagasinServiceImpl implements EtatDeTousLesProduitsDunMagasinService {

    @Inject
    ProduitRepository produitRepository;

    @Inject
    MagasinRepository magasinRepository;

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    LignelivraisonRepository lignelivraisonRepository;
    
    @Inject
    InventaireRepository inventaireRepository;

    @Override
    public List<EtatDeTousLesProduitsDunMagasinWrapper> etatdeTousLesProduitDunMagasin(Magasin magasin, String dateDebut, String dateFin) {

        String produit1 = null;
        String produit2 = null;
        String dateCommenecement = "2016-12-01";

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
        
        Long qteArrivage = 0L;
        Long qteStockInitial = 0L;
        Long qteFinTransfertRecu = 0L;
        Long qteFinVente = 0L;
        Long qteFinTransfert = 0L;
        Long qteFinPromotion = 0L;
        Long qteFinPerte = 0L;
        
        
        Long TESTINITIAL =0L;
        Long quantiteDebutTransfert = 0L;
        Long quantiteDebutTransfertRecu = 0L;
        Long quantiteDebutVente = 0L;
        Long quantiteDebutPromotion = 0L;
        Long quantiteDebutPerte = 0L;
        Long quantiteArrivageDateFin = 0L;
        Long qteArrivageAvant = 0L;


        //Format de la date en LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);

        List<EtatDeTousLesProduitsDunMagasinWrapper> listeStockParMagasin = new LinkedList<>();

        // Toute livraison total d'un produit au magasin central
        List<Lignelivraison> toutesLivraison = null;
        // Toute les sortie d'un produit 
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

        // liste de tous les produits
        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> itProduits = listProduits.iterator();

        while (itProduits.hasNext()) {
            Produit produit = itProduits.next();

            EtatDeTousLesProduitsDunMagasinWrapper etatDeTousLesProduits = new EtatDeTousLesProduitsDunMagasinWrapper();

            produit1 = produit.getNomProduit();
            if (produit1.equals(produit2)) {
                System.out.println("Oh ooooh ! " + produit1 + " est le que " + produit2);
            } else {
                etatDeTousLesProduits.setProduit(produit);
                etatDeTousLesProduits.setMagasin(magasin);

                // Toutes les livraison d'un produit au magasin Central
                toutesLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);
                for (Lignelivraison tl : toutesLivraison) {
                    quantiteGlobaleLivre += tl.getQuantiteLotLivre();
                }

                // Toutes les sorties de produits de AIMAS
                toutesSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
                for (LigneBonDeSortie lbs : toutesSortie) {
                    quantiteGlobaleSortie += lbs.getQuantite();
                }
                
                //Tous les arrivages au magasin central du produit dans la periode de date dateDebutStock à date debut 
                List <Lignelivraison> listLivraisonProduitPeriodedateDebutStock = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, dateDebutStock, debut);
       
                //Recuperer les arrivages d'un produit de la periode dateDebutStock et date debut
                for(Lignelivraison arrivagePdateDebut: listLivraisonProduitPeriodedateDebutStock){
                    qteArrivageAvant += arrivagePdateDebut.getQuantiteLotLivre();
                }
                
                //Toutes les livraisons au magasin central du produit dans la periode de DateDebutStock a date fin
                List <Lignelivraison> listLivraisonProduitDateDebutStockADateFin = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, dateDebutStock, fin);
                        
                //Recuperer les arrivages d'un produit de la periode dateDebutStock et date fin
                for(Lignelivraison qteArrivageDateFin: listLivraisonProduitDateDebutStockADateFin){
                    quantiteArrivageDateFin += qteArrivageDateFin.getQuantiteLotLivre();
                }
                
                //Tous les arrivages au magasin central du produit dans la periode de date debut et fin
                List <Lignelivraison> listLivraisonProduitPeriode = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, debut, fin);
                       
                //Recuperer les arrivages d'un produit d'une periode de date debut et date fin
                for(Lignelivraison arrivageProduit:listLivraisonProduitPeriode){                  
                    qteArrivage += arrivageProduit.getQuantiteLotLivre();                        
                }

                //=============================================================================/
                //Pour le calcul du stock initial
                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' à date debut dans les differents magasin autre que le magasin central 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if (ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                                && ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (ligneSortieMoisDernier.getBonDeSortie().getStatusTranfert()== null || ligneSortieMoisDernier.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && ligneSortieMoisDernier.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            quantiteDebutTransfertRecu += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' à date fin dans les differents magasin autre que le magasin central 
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if (sortieQteStock.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                                && sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (sortieQteStock.getBonDeSortie().getStatusTranfert()== null || sortieQteStock.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && sortieQteStock.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            qteFinTransfertRecu += sortieQteStock.getQuantite();
                        }
                    }
                }
                
                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' dans les differents magasin autre que le magasin central 
                for (LigneBonDeSortie sortiePromoDebut : listeSortieDebut) {
                    if (sortiePromoDebut.getBonDeSortie().getMagasin() != null) {
                        if (sortiePromoDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                                && sortiePromoDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (sortiePromoDebut.getBonDeSortie().getStatusTranfert()== null || sortiePromoDebut.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && sortiePromoDebut.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            qteTransfertRecuDebut += sortiePromoDebut.getQuantite();
                        }
                    }
                }

                //Les quantités reçue par transfert dans les differents magasin autre que le magasin central
                for (LigneBonDeSortie ligneTransfert : listeSortieParPeriode) {
                    if (ligneTransfert.getBonDeSortie().getMagasin() != null) {
                        if (ligneTransfert.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                                && ligneTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (ligneTransfert.getBonDeSortie().getStatusTranfert()== null || ligneTransfert.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && ligneTransfert.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            QuantiteProduitTranfereRecuMagasin += ligneTransfert.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantiteTransfertRecuMagasin(QuantiteProduitTranfereRecuMagasin);

                //=============================================================================/
                //Pour le calcul du stock initial
                //Quantité vendue par magasin depuis le debut '01-12-2016' à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            quantiteDebutVente += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
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
                for (LigneBonDeSortie sortieVenteDebut : listeSortieDebut) {
                    if (sortieVenteDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortieVenteDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortieVenteDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieVenteDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            qteVenteDebut += sortieVenteDebut.getQuantite();
                        }
                    }
                }

                //Quantité de produit vendue du magasin
                for (LigneBonDeSortie ligneSortieVente : listeSortieParPeriode) {
                    if (ligneSortieVente.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieVente.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieVente.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieVente.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            quantiteVendue += ligneSortieVente.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantiteVendue(quantiteVendue);
                

                //=============================================================================/
                //Pour le calcul du stock initial
                //Quantité sortie en promo par magasin depuis le debut '01-12-2016' à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                     if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            quantiteDebutPromotion += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
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
                for (LigneBonDeSortie sortiePromoDebut : listeSortieDebut) {
                    if (sortiePromoDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortiePromoDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortiePromoDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortiePromoDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            qtePromoDebut += sortiePromoDebut.getQuantite();
                        }
                    }
                }

                //Quantité de produit sortie en promo d'un magasin
                for (LigneBonDeSortie ligneSortieTransfert : listeSortieParPeriode) {
                    if (ligneSortieTransfert.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieTransfert.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieTransfert.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            quantitePromo += ligneSortieTransfert.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantitePromo(quantitePromo);

                 //=============================================================================/
                //Pour le calcul du stock initial
                //Quantité perdue par magasin depuis le debut '01-12-2016' à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                   if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            quantiteDebutPerte += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
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
                for (LigneBonDeSortie sortiePerteDebut : listeSortieDebut) {
                    if (sortiePerteDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortiePerteDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortiePerteDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortiePerteDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            qtePerteDebut += sortiePerteDebut.getQuantite();
                        }
                    }
                }

                //Quantité produit perdue du magasin
                for (LigneBonDeSortie lignePerte : listeSortieParPeriode) {
                    if (lignePerte.getBonDeSortie().getMagasin() != null) {
                        if ((lignePerte.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (lignePerte.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (lignePerte.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            quantitePerte += lignePerte.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantitePerte(quantitePerte);
                
                //=============================================================================/
                //Pour le calcul du stock initial
                //Quantité de produit transferée du magasin d'origine depuis le 01-12-2016 à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            quantiteDebutTransfert += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/

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
                for (LigneBonDeSortie sortieTransfertDebut : listeSortieDebut) {
                    if (sortieTransfertDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortieTransfertDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortieTransfertDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieTransfertDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            qteTransfertDebut += sortieTransfertDebut.getQuantite();
                        }
                    }
                }

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
                etatDeTousLesProduits.setQuantiteTransfert(quantiteTransfere);

                if (magasin.getNomMagasin().equals("MAGASIN CENTRAL")) {
                    
                    quantiteTotalEnStock = (quantiteArrivageDateFin - qteFinTransfert) + qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte);
        
                    qteStockInitial = quantiteTotalEnStock - qteArrivage + quantiteVendue + quantitePromo + quantiteTransfere + quantitePerte - QuantiteProduitTranfereRecuMagasin;             

//                    qteStockInitial = qteArrivageAvant + quantiteDebutTransfertRecu - (quantiteDebutVente + quantiteDebutPromotion + quantiteDebutTransfert + quantiteDebutPerte);
                    System.out.println("LE STOCK INITIAL DU MAGASIN CENTRAL "+qteStockInitial);
                    
                } else {
                    qteArrivage = 0L;
                    quantiteTotalEnStock = qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte) - qteFinTransfert;                   
//                    qteStockInitial = quantiteDebutTransfertRecu - (quantiteDebutVente + quantiteDebutPromotion + quantiteDebutPerte) - quantiteDebutTransfert;                        
                    qteStockInitial = quantiteTotalEnStock - QuantiteProduitTranfereRecuMagasin + (quantiteVendue + quantitePromo + quantitePerte + quantiteTransfere);

                    System.out.println("LE STOCK INITIAL POUR LES AUTRES MAGASINS "+ qteStockInitial);
                }
                
                System.out.println("ARRIVAGE AU MAGASIN " +qteArrivage);
                 etatDeTousLesProduits.setArrivage(qteArrivage); 
                etatDeTousLesProduits.setStockInitial(qteStockInitial);
                etatDeTousLesProduits.setQuantiteTotalEnStock(quantiteTotalEnStock);
                
  
                // reinitialisation des variable à 01
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
                
                qteArrivage = 0L;
                qteStockInitial = 0L;
                qteFinTransfertRecu = 0L;
                qteFinVente = 0L;
                qteFinTransfert = 0L;
                qteFinPromotion = 0L;
                qteFinPerte = 0L;
                
                TESTINITIAL = 0L;
                quantiteDebutTransfert = 0L;
                quantiteDebutTransfertRecu = 0L;
                quantiteDebutVente = 0L;
                quantiteDebutPromotion = 0L;
                quantiteDebutPerte = 0L;
                quantiteArrivageDateFin = 0L;
                qteArrivageAvant = 0L;

                // ajouter un objet de la classe EtatDeTousLesProduitsDunMAgasinWrapper à l'objet LinkedList
                listeStockParMagasin.add(etatDeTousLesProduits);

            }
            produit2 = produit.getNomProduit();
        }
        return listeStockParMagasin;
    }

    /**
     * Etat de la situation du magasin par mois
     * @param magasin
     * @param dateDumois
     * @return 
     */
    @Override
    public List<EtatDeTousLesProduitsDunMagasinWrapper> etatSituationStockMagasin(Magasin magasin, String dateDumois) {
        
        String produit1 = null;
        String produit2 = null;
        String dateCommenecement = "2016-12-01";

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
        
        Long qteStockInitial =0L;
        Long qteArrivage = 0L;
        Long qteFinTransfertRecu = 0L;
        Long qteFinVente = 0L;
        Long qteFinTransfert = 0L;
        Long qteFinPromotion = 0L;
        Long qteFinPerte = 0L;
        
        Long TESTINITIAL =0L;
        Long quantiteDebutTransfert = 0L;
        Long quantiteDebutTransfertRecu = 0L;
        Long quantiteDebutVente = 0L;
        Long quantiteDebutPromotion = 0L;
        Long quantiteDebutPerte = 0L;
        Long quantiteArrivageDateFin = 0L;
        Long qteArrivageAvant = 0L;

        String dateDebutPeriode = null;
        String dateFinPeriode = null;
        
        Long ecart = 0L;
        Long quantiteStock = 0L;

        //Classe retournant la première et la dernière date du mois de la date données en paramètre : dateInventaire
        PremierEtDernierJourDuMois madateInventaire = new PremierEtDernierJourDuMois();
        dateDebutPeriode = madateInventaire.getDateDebutDuMois(dateDumois);
        dateFinPeriode = madateInventaire.getDateFinDuMois(dateDumois);

        //Format de la date en LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebutPeriode, formatter);
        LocalDate fin = LocalDate.parse(dateFinPeriode, formatter);
        LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);

        List<EtatDeTousLesProduitsDunMagasinWrapper> listeStockParMagasin = new LinkedList<>();

        // Toute livraison total d'un produit au magasin central
        List<Lignelivraison> toutesLivraison = null;
        // Toute les sortie d'un produit 
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

        List<Inventaire> inventaireDuMois = inventaireRepository.findByMagasinAndDateInventaireBetween(magasin, debut, fin);
        Iterator<Inventaire>itInvent = inventaireDuMois.iterator();
        
        // liste de tous les produits
        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> itProduits = listProduits.iterator();

//        while (itProduits.hasNext()) {
//            Produit produit = itProduits.next();
        while (itInvent.hasNext()) {
            Inventaire inventaire = itInvent.next();

            EtatDeTousLesProduitsDunMagasinWrapper etatDeTousLesProduits = new EtatDeTousLesProduitsDunMagasinWrapper();

            produit1 = inventaire.getProduit().getNomProduit();
            if (produit1.equals(produit2)) {
                System.out.println("Oh ooooh ! " + produit1 + " est le que " + produit2);
            } else {
                etatDeTousLesProduits.setProduit(inventaire.getProduit());
                etatDeTousLesProduits.setMagasin(magasin);
                etatDeTousLesProduits.setStockReel(inventaire.getStockReel());

                // Toutes les livraison d'un produit au magasin Central
                toutesLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(inventaire.getProduit());
                for (Lignelivraison tl : toutesLivraison) {
                    quantiteGlobaleLivre += tl.getQuantiteLotLivre();
                }

                //Toutes les livraisons au magasin central du produit dans la periode de DateDebutStock a date fin
                List <Lignelivraison> listLivraisonProduitDateDebutStockADateFin = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(inventaire.getProduit(), dateDebutStock, fin);
        
                 //Recuperer les arrivages d'un produit de la periode dateDebutStock et date fin
                for(Lignelivraison qteArrivageDateFin: listLivraisonProduitDateDebutStockADateFin){
                    quantiteArrivageDateFin += qteArrivageDateFin.getQuantiteLotLivre();
                }
                
                //Tous les arrivages au magasin central du produit dans la periode de date dateDebutStock à date debut 
                List <Lignelivraison> listLivraisonProduitPeriodedateDebutStock = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(inventaire.getProduit(), dateDebutStock, debut);
       
                //Recuperer les arrivages d'un produit de la periode dateDebutStock et date debut
                for(Lignelivraison arrivagePdateDebut: listLivraisonProduitPeriodedateDebutStock){
                    qteArrivageAvant += arrivagePdateDebut.getQuantiteLotLivre();
                }
                
                // Toutes les sorties de produits de AIMAS
                toutesSortie = ligneBonDeSortieRepository.findAllByProduit(inventaire.getProduit());
                for (LigneBonDeSortie lbs : toutesSortie) {
                    quantiteGlobaleSortie += lbs.getQuantite();
                }
                
                //Tous les arrivages au magasin central du produit dans la periode de date debut et fin
                List <Lignelivraison> listLivraisonProduitPeriode = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(inventaire.getProduit(), debut, fin);
       
                  //Recuperer les arrivages d'un produit d'une periode de date debut et date fin
                for(Lignelivraison arrivageProduit:listLivraisonProduitPeriode){                  
                    qteArrivage += arrivageProduit.getQuantiteLotLivre();                        
                }

                 //=============================================================================/
                //Pour le calcul du stock initial
                //Quantité de produit transferée du magasin d'origine depuis le 01-12-2016 à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            quantiteDebutTransfert += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
                
                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' à date fin dans les differents magasin autre que le magasin central 
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if (sortieQteStock.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit())
                                && sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (sortieQteStock.getBonDeSortie().getStatusTranfert()== null || sortieQteStock.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && sortieQteStock.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            qteFinTransfertRecu += sortieQteStock.getQuantite();
                        }
                    }
                }
                
                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' dans les differents magasin autre que le magasin central 
                for (LigneBonDeSortie sortiePromoDebut : listeSortieDebut) {
                    if (sortiePromoDebut.getBonDeSortie().getMagasin() != null) {
                        if (sortiePromoDebut.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit())
                                && sortiePromoDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (sortiePromoDebut.getBonDeSortie().getStatusTranfert()== null || sortiePromoDebut.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && sortiePromoDebut.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            qteTransfertRecuDebut += sortiePromoDebut.getQuantite();
                        }
                    }
                }

                //=============================================================================/
                //Pour le calcul du stock initial
                //Les quantités reçue par transfert depuis le debut du stock :'2016-12-01' à date debut dans les differents magasin autre que le magasin central 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if (ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit())
                                && ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (ligneSortieMoisDernier.getBonDeSortie().getStatusTranfert()== null || ligneSortieMoisDernier.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && ligneSortieMoisDernier.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            quantiteDebutTransfertRecu += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
                //Les quantités reçue par transfert dans les differents magasin autre que le magasin central
                for (LigneBonDeSortie ligneTransfert : listeSortieParPeriode) {
                    if (ligneTransfert.getBonDeSortie().getMagasin() != null) {
                        if (ligneTransfert.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit())
                                && ligneTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (ligneTransfert.getBonDeSortie().getStatusTranfert()== null || ligneTransfert.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && ligneTransfert.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            QuantiteProduitTranfereRecuMagasin += ligneTransfert.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantiteTransfertRecuMagasin(QuantiteProduitTranfereRecuMagasin);

                //=============================================================================/
                //Pour le calcul du stock initial
                ///Quantité vendue par magasin depuis le debut '01-12-2016' à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            quantiteDebutVente += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
                 //Quantité vendue par magasin depuis le debut '01-12-2016' à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            qteFinVente += sortieQteStock.getQuantite();
                        }
                    }
                }
                
                //Quantité vendue par magasin depuis le debut '01-12-2016'
                for (LigneBonDeSortie sortieVenteDebut : listeSortieDebut) {
                    if (sortieVenteDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortieVenteDebut.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortieVenteDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieVenteDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            qteVenteDebut += sortieVenteDebut.getQuantite();
                        }
                    }
                }

                //Quantité de produit vendue du magasin
                for (LigneBonDeSortie ligneSortieVente : listeSortieParPeriode) {
                    if (ligneSortieVente.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieVente.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (ligneSortieVente.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieVente.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE))) {
                            quantiteVendue += ligneSortieVente.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantiteVendue(quantiteVendue);

                 //=============================================================================/
               //Pour le calcul du stock initial
               ///Quantité sortie en promo par magasin depuis le debut '01-12-2016' à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                     if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            quantiteDebutPromotion += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
                //Quantité sortie en promo par magasin depuis le 01-12-2016 à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            qteFinPromotion += sortieQteStock.getQuantite();
                        }
                    }
                }
                
                //Quantité sortie en promo par magasin depuis le 01-12-2016
                for (LigneBonDeSortie sortiePromoDebut : listeSortieDebut) {
                    if (sortiePromoDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortiePromoDebut.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortiePromoDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortiePromoDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            qtePromoDebut += sortiePromoDebut.getQuantite();
                        }
                    }
                }

                //Quantité de produit sortie en promo d'un magasin
                for (LigneBonDeSortie ligneSortieTransfert : listeSortieParPeriode) {
                    if (ligneSortieTransfert.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieTransfert.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (ligneSortieTransfert.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION))) {
                            quantitePromo += ligneSortieTransfert.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantitePromo(quantitePromo);

                //=============================================================================/
                //Pour le calcul du stock initial
                //Quantité perdue par magasin depuis le debut '01-12-2016' à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                   if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            quantiteDebutPerte += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                }
                //=============================================================================/
                
                //Quantité perdue par magasin depuis 01-12-2016 à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            qteFinPerte += sortieQteStock.getQuantite();
                        }
                    }
                }
                
                //Quantité perdue par magasin depuis 01-12-2016
                for (LigneBonDeSortie sortiePerteDebut : listeSortieDebut) {
                    if (sortiePerteDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortiePerteDebut.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortiePerteDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortiePerteDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            qtePerteDebut += sortiePerteDebut.getQuantite();
                        }
                    }
                }

                //Quantité produit perdue du magasin
                for (LigneBonDeSortie lignePerte : listeSortieParPeriode) {
                    if (lignePerte.getBonDeSortie().getMagasin() != null) {
                        if ((lignePerte.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (lignePerte.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (lignePerte.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE))) {
                            quantitePerte += lignePerte.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantitePerte(quantitePerte);

                // Quantité de produit transferée du magasin d'origine depuis le 01-12-2016 à la date fin
                for (LigneBonDeSortie sortieQteStock : sorteDebutaFin) {
                    if (sortieQteStock.getBonDeSortie().getMagasin() != null) {
                        if ((sortieQteStock.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortieQteStock.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieQteStock.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            qteFinTransfert += sortieQteStock.getQuantite();
                        }
                    }
                }
                
                // Quantité de transferée du magasin d'origine depuis le 01-12-2016
                for (LigneBonDeSortie sortieTransfertDebut : listeSortieDebut) {
                    if (sortieTransfertDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortieTransfertDebut.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (sortieTransfertDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieTransfertDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            qteTransfertDebut += sortieTransfertDebut.getQuantite();
                        }
                    }
                }

                // Quantité de transferée du magasin d'origine
                for (LigneBonDeSortie ligneSortieTransfert : listeSortieParPeriode) {
                    if (ligneSortieTransfert.getBonDeSortie().getMagasin() != null) {
                        if ((ligneSortieTransfert.getLot().getProduit().getNomProduit().equals(inventaire.getProduit().getNomProduit()))
                                && (ligneSortieTransfert.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            quantiteTransfere += ligneSortieTransfert.getQuantite();
                        }
                    }
                }
                etatDeTousLesProduits.setQuantiteTransfert(quantiteTransfere);

                if (magasin.getNomMagasin().equals("MAGASIN CENTRAL")) {

                    quantiteTotalEnStock = (quantiteArrivageDateFin - qteFinTransfert) + qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte);
          
//                    qteStockInitial = qteArrivageAvant + quantiteDebutTransfertRecu - (quantiteDebutVente + quantiteDebutPromotion + quantiteDebutTransfert + quantiteDebutPerte);
                    qteStockInitial = quantiteTotalEnStock - qteArrivage + quantiteVendue + quantitePromo + quantiteTransfere + quantitePerte - QuantiteProduitTranfereRecuMagasin;             

                System.out.println("LE STOCK INITIAL EST "+qteStockInitial);
                } else {
                    qteArrivage = 0L;                    
                    quantiteTotalEnStock = qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte) - qteFinTransfert;                    
                    qteStockInitial = quantiteDebutTransfertRecu - (quantiteDebutVente + quantiteDebutPromotion + quantiteDebutPerte) - quantiteDebutTransfert;
                }
                
                System.out.println("ESSSSAAIII !!!! DE L'ARRIVAGE " +qteArrivage);
                etatDeTousLesProduits.setStockInitial(qteStockInitial); 
                etatDeTousLesProduits.setQuantiteTotalEnStock(quantiteTotalEnStock);
                
                ecart = quantiteTotalEnStock - inventaire.getStockReel();
                etatDeTousLesProduits.setEcart(ecart);

                // reinitialisation des variable à 01
                TESTINITIAL = 0L;
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
                qteArrivage = 0L;
                qteStockInitial =0L;
                
                qteFinTransfertRecu = 0L;
                qteFinVente = 0L;
                qteFinTransfert = 0L;
                qteFinPromotion = 0L;
                qteFinPerte = 0L;
                
                quantiteDebutTransfert = 0L;
                quantiteDebutTransfertRecu = 0L;
                quantiteDebutVente = 0L;
                quantiteDebutPromotion = 0L;
                quantiteDebutPerte = 0L;
                quantiteArrivageDateFin = 0L;
                qteArrivageAvant = 0L;
                
                // ajouter un objet de la classe EtatDeTousLesProduitsDunMAgasinWrapper à l'objet LinkedList
                listeStockParMagasin.add(etatDeTousLesProduits);

            }
            produit2 = inventaire.getProduit().getNomProduit();
        }
        return listeStockParMagasin;
    }

}
