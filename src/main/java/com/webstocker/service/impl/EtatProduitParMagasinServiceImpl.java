package com.webstocker.service.impl;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.StatusTransfert;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.EtatProduitParMagasinWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.EtatProduitParMagasinService;
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
public class EtatProduitParMagasinServiceImpl implements EtatProduitParMagasinService {

    @Inject
    ProduitRepository produitRepository;

    @Inject
    MagasinRepository magasinRepository;

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    LignelivraisonRepository lignelivraisonRepository;

    @Override
    public List<EtatProduitParMagasinWrapper> etatProduitParMagasin(Produit produit, String dateDebut, String dateFin) {

        String magasin1 = null;
        String magasin2 = null;

        String dateCommenecement = "2016-12-01";

        Long QuantiteProduitTranfereRecuMagasin = 0L;
        Long quantiteTransfere = 0L;
        Long quantiteVendue = 0L, quantitePromo = 0L, quantitePerte = 0L;
        Long quantiteTotalEnStock = 0L, quantiteGlobaleLivre = 0L, quantiteGlobaleSortie = 0L;
        Long reste = 0L;

//        Long TESTREST = 0L;
        Long TESTINITIAL = 0L;
        Long NvTestInitial = 0L;
        Long NvTestStockDisponible = 0L;
        
//        Long StockInitNouveau = 0L;
        Long sortiePourStockInitial = 0L;
        Long qteTransfertRecuDebut = 0L;
        Long qteVenteDebut = 0L;
        Long qteTransfertDebut = 0L;
        Long qtePromoDebut = 0L;
        Long qtePerteDebut = 0L;
        
        Long transfertRetourAuMagasinCentral = 0L;
        Long qteFinTransfertRecu = 0L;
        Long qteFinVente = 0L;
        Long qteFinTransfert = 0L;
        Long qteFinPromotion = 0L;
        Long qteFinPerte = 0L;
        Long qteStockInitial =0L;
        Long  qteArrivage =0L;
        Long qteArrivageAvant = 0L;
        
        Long quantiteDebutTransfertRecu = 0L;
        Long quantiteDebutVente = 0L;
        Long quantiteDebutTransfert = 0L;
        Long quantiteDebutPromotion = 0L;
        Long quantiteDebutPerte = 0L;
        Long quantiteArrivageDateFin = 0L;
        
        //Format de la date en LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);
        
        LocalDate finDuMoisDernier = debut.minusDays(1);
        LocalDate debutDuMoisdernier = debut.minusMonths(1);
        System.out.println("LA FIN DU MOIS DERNIER "+finDuMoisDernier);
        System.out.println("LE DEBUT DU MOIS DERNIER "+debutDuMoisdernier);

        List<EtatProduitParMagasinWrapper> listeStockParMagasin = new LinkedList<>();

        // Toute livraison total d'un produit au magasin central
        List<Lignelivraison> toutesLivraison = null;
        
        // Toutes les sorties d'un produit au magasin central
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
        
        //Tous les arrivages au magasin central du produit dans la periode de date debut et fin
        List <Lignelivraison> listLivraisonProduitPeriode = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, debut, fin);
       
        //Tous les arrivages au magasin central du produit dans la periode de date dateDebutStock à date debut 
        List <Lignelivraison> listLivraisonProduitPeriodedateDebutStock = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, dateDebutStock, debut);
       
        //Toutes les livraisons au magasin central du produit dans la periode de DateDebutStock a date fin
        List <Lignelivraison> listLivraisonProduitDateDebutStockADateFin = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, dateDebutStock, fin);
        
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
                
                //Recuperer les arrivages d'un produit d'une periode de date debut et date fin
                for(Lignelivraison arrivageProduit:listLivraisonProduitPeriode){                  
                    qteArrivage += arrivageProduit.getQuantiteLotLivre();                        
                }
                
                //Recuperer les arrivages d'un produit de la periode dateDebutStock et date debut
                for(Lignelivraison arrivagePdateDebut: listLivraisonProduitPeriodedateDebutStock){
                    qteArrivageAvant += arrivagePdateDebut.getQuantiteLotLivre();
                }
                
                //Recuperer les arrivages d'un produit de la periode dateDebutStock et date fin
                for(Lignelivraison qteArrivageDateFin: listLivraisonProduitDateDebutStockADateFin){
                    quantiteArrivageDateFin += qteArrivageDateFin.getQuantiteLotLivre();
                }

                 //Toutes les sorties de produit dans la periode dateDebutStock et date debut
                 for(LigneBonDeSortie touteLesSortieDateDebutStockDateDebut : listeSortieDebut){
                     if(touteLesSortieDateDebutStockDateDebut.getLot().getProduit().getNomProduit().equals(produit)){
                     sortiePourStockInitial += touteLesSortieDateDebutStockDateDebut.getQuantite();
                     }
                 }
                 System.out.println("LE PRODUIT : "+produit.getNomProduit());
                 System.out.println("TOUTES LES SORTIE : "+sortiePourStockInitial);
                 
                toutesLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);
                for (Lignelivraison tl : toutesLivraison) {
                    quantiteGlobaleLivre += tl.getQuantiteLotLivre();
                }

                toutesSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
                for (LigneBonDeSortie lbs : toutesSortie) {
                    quantiteGlobaleSortie += lbs.getQuantite();
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
                for (LigneBonDeSortie totalTransfert : listeSortieParPeriode) {
                    if (totalTransfert.getBonDeSortie().getMagasin() != null) {
                        if (totalTransfert.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                                && totalTransfert.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)
                                && (totalTransfert.getBonDeSortie().getStatusTranfert()== null || totalTransfert.getBonDeSortie().getStatusTranfert().equals(StatusTransfert.RECU))
                                && totalTransfert.getBonDeSortie().getDestination().getNomMagasin().equals(magasin.getNomMagasin())) {
                            QuantiteProduitTranfereRecuMagasin += totalTransfert.getQuantite();
                        }
                    }
                }
                etatProduitParMagasin.setQuantiteTransfertRecuMagasin(QuantiteProduitTranfereRecuMagasin);
               
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

                // Quantité de produit transferée du magasin d'origine depuis le 01-12-2016
                for (LigneBonDeSortie sortieTransfertDebut : listeSortieDebut) {
                    if (sortieTransfertDebut.getBonDeSortie().getMagasin() != null) {
                        if ((sortieTransfertDebut.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (sortieTransfertDebut.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (sortieTransfertDebut.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            qteTransfertDebut += sortieTransfertDebut.getQuantite();
                        }
                    }
                }

                // Quantité de produit transferée du magasin d'origine
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
               
                /*************************************************************************************************/
                //Pour le calcul du stock initial
                //Quantité de produit retournée par transfert au magasin central depuis le 01-12-2016 à la date debut 
                for (LigneBonDeSortie ligneSortieMoisDernier : listeSortieDebut) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin() != null) {
                    if (ligneSortieMoisDernier.getBonDeSortie().getMagasin().equals("MAGASIN CENTRAL")) {
                        if ((ligneSortieMoisDernier.getLot().getProduit().getNomProduit().equals(produit.getNomProduit()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin()))
                                && (ligneSortieMoisDernier.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT))) {
                            transfertRetourAuMagasinCentral += ligneSortieMoisDernier.getQuantite();
                        }
                    }
                    }
                }
                /***************************************************************************************************/

                if (magasin.getNomMagasin().equals("MAGASIN CENTRAL")) {
                    
//                    quantiteTotalEnStock = (quantiteGlobaleLivre - qteFinTransfert) + qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte);
                    quantiteTotalEnStock = (quantiteArrivageDateFin - qteFinTransfert) + qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte);
                   
                    NvTestStockDisponible = (quantiteArrivageDateFin - qteFinTransfert) + qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte);
                
                    qteStockInitial = quantiteTotalEnStock - qteArrivage + quantiteVendue + quantitePromo + quantiteTransfere + quantitePerte - QuantiteProduitTranfereRecuMagasin;             
//                    qteStockInitial = qteArrivageAvant + quantiteDebutTransfertRecu - (quantiteDebutVente + quantiteDebutPromotion + quantiteDebutTransfert + quantiteDebutPerte);
                    
                    NvTestInitial = quantiteArrivageDateFin - qteArrivage + quantiteDebutTransfertRecu - (quantiteDebutVente + quantiteDebutPromotion + quantiteDebutTransfert + quantiteDebutPerte);             
                 
                    System.out.println("LA QUANTITE INITIALE DU MOIS M+1 "+qteStockInitial);
                    System.out.println("LA QUANTITE ARRIVAGE AVANT DU MOIS "+qteArrivageAvant);
                    System.out.println("LA QUANTITE DEBUT TRANFERT RECU "+quantiteDebutTransfertRecu);
                    
                System.out.println("MAGASIN CENTRAL QUANTITE ARRIVEE " + qteArrivage+" QUANTITE RECU "+quantiteTransfere + " QUANTITE  VENDU "+quantiteVendue+" quantité Promotion fin "+quantitePromo+" QUANTITE PERDUE FIN "+quantitePerte);
                System.out.println("----------------------------------");
                    
                       
                } else {
                    qteArrivage = 0L;
                    etatProduitParMagasin.setArrivage(qteArrivage);
                    
                    quantiteTotalEnStock = qteFinTransfertRecu - (qteFinVente + qteFinPromotion + qteFinPerte) - qteFinTransfert;
                    
                    System.out.println("LES AUTRES MAGASIN QUANTITE RECU "+qteFinTransfertRecu +" VENDU "+qteFinVente+" PROMOTION "+qteFinPromotion+" PERTE "+qteFinPerte+" LES TRANSFERT EFFECTUES "+qteFinTransfert);
                    
                    qteStockInitial = quantiteTotalEnStock - QuantiteProduitTranfereRecuMagasin + (quantiteVendue + quantitePromo + quantitePerte + quantiteTransfere);
                    
                }

                // Ajoute le signe (-) quand le nombre est negatif pour afficher le nombre sans le signe (-)
//                if (quantiteTotalEnStock < 0) {
//                    quantiteTotalEnStock = 0 - quantiteTotalEnStock;
//                }
//                
//                if (qteStockInitial < 0) {
//                    qteStockInitial = 0 - qteStockInitial;
//                }
                
                etatProduitParMagasin.setArrivage(qteArrivage);
                etatProduitParMagasin.setQuantiteTotalEnStock(quantiteTotalEnStock);
                etatProduitParMagasin.setStockInitial(qteStockInitial);
                
//                TESTINITIAL=0L;
//                TESTREST=0L;
                NvTestStockDisponible = 0L;
                NvTestInitial = 0L;
                
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

                transfertRetourAuMagasinCentral = 0L;
                qteFinTransfertRecu = 0L;
                qteFinVente = 0L;
                qteFinTransfert = 0L;
                qteFinPromotion = 0L;
                qteFinPerte = 0L;
                qteStockInitial = 0L;
                
                quantiteArrivageDateFin = 0L;
                
                quantiteDebutTransfertRecu = 0L;
                quantiteDebutVente = 0L;
                quantiteDebutTransfert = 0L;
                quantiteDebutPerte = 0L;
                quantiteDebutPromotion = 0L;
                qteArrivage = 0L;
                qteArrivageAvant = 0L;
                sortiePourStockInitial = 0L;

                listeStockParMagasin.add(etatProduitParMagasin);
            }
            magasin2 = magasin.getNomMagasin();
        }
        return listeStockParMagasin;
    }

}
