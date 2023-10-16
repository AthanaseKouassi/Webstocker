package com.webstocker.service.impl;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Produit;
import com.webstocker.domain.Reglement;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.enumeration.TypeVente;
import com.webstocker.domain.wrapper.EtatDeReconciliationWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.repository.ReglementRepository;
import com.webstocker.service.EtatReconciliationService;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Athanase
 */
@Service
@Transactional
public class EtatReconciliationServiceImpl implements EtatReconciliationService {

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    ProduitRepository produitRepository;

    @Inject
    LignelivraisonRepository lignelivraisonRepository;

    @Inject
    ReglementRepository reglementRepository;

    @Override
    @SuppressWarnings("UnusedAssignment")
    public List<EtatDeReconciliationWrapper> etatGlobalReconciliation(String maDate) {

        final long JOUR_30 = 30;
        final long JOUR_90 = 90;
        String dateDebut;
        String dateFin;
        int qteVente = 0, qtePromo = 0, qteCredit = 0, qteCash = 0, somCret = 0, somCash = 0, qteLivre = 0;
        int qtePerte = 0, qteTrans = 0;
        int qteAllSortie = 0, qteAllLivre = 0, qteTotale = 0, somRecouvre = 0, balanceOuvertureCreance = 0, balanceClotureCreance = 0;
        int sRecouv = 0;
        int creanceRecouvree = 0, creance_30_jours = 0, creance_30_90_jours = 0, creance_90_joursPlus = 0;
        Long nombreJours = 0L, bocrea = 0L, bccrea = 0L;
        BigDecimal   sca = BigDecimal.ZERO,  scr = BigDecimal.ZERO,   creaRecouv = BigDecimal.ZERO, crea30 = BigDecimal.ZERO, crea30_90 = BigDecimal.ZERO, crea_90p = BigDecimal.ZERO; 
        Long qv = 0L, qc = 0L, qca = 0L,   ql = 0L, qp = 0L, qpt = 0L, qt = 0L, qT = 0L, sr = 0L;

        LocalDate today = LocalDate.now();
        //convertir une date Localdate en chaîne de caractère
        String dateDujour = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        LocalDate aujourdhui = LocalDate.parse(dateDujour, formatter);

        List<EtatDeReconciliationWrapper> etatReconWrapperList = new LinkedList<>();
        List<Lignelivraison> allLivraison;
        List<LigneBonDeSortie> allSortie;
        List<LigneBonDeSortie> allRecouvrementProduit;

        List<LigneBonDeSortie> allLignefactures = ligneBonDeSortieRepository.findByBonDeSortieFactureDateLimitePaiementBetween(debut, fin);

        List<Reglement> allReglementsTotal = reglementRepository.findAll();
        List<Reglement> lesReglements = reglementRepository.findByDateReglementBetween(debut, fin);
        Iterator<Reglement> allRegleIt = allReglementsTotal.iterator();
        Iterator<Reglement> reglementIt = lesReglements.iterator();

        List<LigneBonDeSortie> lbsdate = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        List<Lignelivraison> ligneLivre = lignelivraisonRepository.findByLivraisonDateLivraisonBetween(debut, fin);

        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> it = listProduits.iterator();

        while (it.hasNext()) {
            Produit produit = it.next();
            EtatDeReconciliationWrapper etatReconWrapper = new EtatDeReconciliationWrapper();
            etatReconWrapper.setProduit(produit);

            allSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
            allLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);

            allRecouvrementProduit = ligneBonDeSortieRepository.findByProduitAndBonDeSortieFactureDateLimitePaiementBetween(produit, debut, fin);

            for (LigneBonDeSortie allsortie : allSortie) {
                qteAllSortie += allsortie.getQuantite().intValue();
            }

            for (Lignelivraison alllivraison : allLivraison) {
                qteAllLivre += alllivraison.getQuantiteLotLivre();
            }

            //Balance d'ouverture des créances à une date donnée
            while (allRegleIt.hasNext()) {
                Reglement allRegleTotal = allRegleIt.next();

                Long idFacture = 0L;
                Long idReglementFacture = 0L;

                if (allRegleTotal.getDateReglement().isBefore(debut)) {
                    idReglementFacture = allRegleTotal.getFacture().getId();
                }

                for (LigneBonDeSortie allsortie : allSortie) {
                    try {

                        if (allsortie.getBonDeSortie().getFacture() != null) {
                            if (allsortie.getBonDeSortie().getFacture().getDateLimitePaiement().isBefore(debut)) {
                                System.out.println("LA NOUVELLE DATE BOOLEEN " + allsortie.getBonDeSortie().getFacture().getDateLimitePaiement().isBefore(debut));
                                System.out.println("LA NOUVELLE DATE " + allsortie.getBonDeSortie().getFacture().getDateLimitePaiement());
                                idFacture = allsortie.getBonDeSortie().getFacture().getId();
                                if (Objects.equals(idFacture, idReglementFacture)) {
//                                    balanceOuvertureCreance += allsortie.getMontantVente().intValue();
                                    balanceOuvertureCreance += allsortie.getPrixDeVente().intValue();
                                }
                            }
                        }
                    } catch (NullPointerException er) {
                        er.printStackTrace();
                    }
                }
            }

            bocrea = Long.valueOf(String.valueOf(balanceOuvertureCreance));
            etatReconWrapper.setBalanceOuvertureCreance(bocrea);

            while (reglementIt.hasNext()) {
                Reglement regle = reglementIt.next();

                for (LigneBonDeSortie lbs : allLignefactures) {
                    if (lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                        if (lbs.getBonDeSortie().getFacture().getDateLimitePaiement().equals(regle.getDateReglement())) {
//                            creanceRecouvree += lbs.getMontantVente().intValue();
                            creanceRecouvree += lbs.getPrixDeVente().intValue();
                        } else {
                            nombreJours = (-1) * ChronoUnit.DAYS.between(aujourdhui, lbs.getBonDeSortie().getFacture().getDateFacture());
//                            nombreJours = (-1) * ChronoUnit.DAYS.between(lbs.getBonDeSortie().getFacture().getDateLimitePaiement(), lbs.getBonDeSortie().getFacture().getDateFacture());
                            System.out.println("LE NOMBRE DE JOUR : " + nombreJours);
                            if (nombreJours < JOUR_30) {
//                                creance_30_jours += lbs.getMontantVente().intValue();
                                creance_30_jours += lbs.getPrixDeVente().intValue();
                            } else if (nombreJours >= JOUR_30 && nombreJours <= JOUR_90) {
//                                creance_30_90_jours += lbs.getMontantVente().intValue();
                                creance_30_90_jours += lbs.getPrixDeVente().intValue();
                            } else if (nombreJours > JOUR_90) {
//                                creance_90_joursPlus += lbs.getMontantVente().intValue();
                                creance_90_joursPlus += lbs.getPrixDeVente().intValue();
                            }
                        }
                    }
                }
                nombreJours = 0L;
            }
            creaRecouv = BigDecimal.valueOf( Long.valueOf(String.valueOf(creanceRecouvree)));
//            System.out.println("RECOUVEMENTS : " + creaRecouv);
            crea30 = BigDecimal.valueOf(Long.valueOf(String.valueOf(creance_30_jours)));
//            System.out.println("CREANCE DE 30 JOURS : " + crea30);
            crea30_90 = BigDecimal.valueOf(Long.valueOf(String.valueOf(creance_30_90_jours)));
//            System.out.println("CREANCE DE 30 A 90 JOURS : " + crea30_90);
            crea_90p = BigDecimal.valueOf(Long.valueOf(String.valueOf(creance_90_joursPlus)));
//            System.out.println("CREANCE DE 90 JOURS PLUS : " + crea_90p);

            etatReconWrapper.setSommeRecouvree(creaRecouv);
            etatReconWrapper.setSommeCreance30(crea30);
            etatReconWrapper.setSommeCreance30_90(crea30_90);
            etatReconWrapper.setSommeCreance90(crea_90p);
            
            //calcul de la balance de cloture des creance
            if (balanceOuvertureCreance >= creanceRecouvree) {
                balanceClotureCreance = balanceOuvertureCreance - creanceRecouvree;
            } else {
                balanceClotureCreance = 0;
            }

            bccrea = Long.valueOf(String.valueOf(balanceClotureCreance));
            etatReconWrapper.setBalanceClotureCreance(bccrea);

            balanceOuvertureCreance = 0;
            balanceClotureCreance = 0;
            creanceRecouvree = 0;
            creance_30_jours = 0;
            creance_30_90_jours = 0;
            creance_90_joursPlus = 0;
            creaRecouv = BigDecimal.ZERO;
            bocrea = 0L;
            crea30 = BigDecimal.ZERO;
            crea30_90 = BigDecimal.ZERO;
            crea_90p = BigDecimal.ZERO;
            bccrea = 0L;

            for (LigneBonDeSortie lbs
                    : lbsdate) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {

                    qteVente += lbs.getQuantite().intValue();
                    qv = Long.valueOf(String.valueOf(qteVente));
                }
            }
            etatReconWrapper.setQuantiteVendue(qv);

            for (LigneBonDeSortie lbs : lbsdate) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qtePromo += lbs.getQuantite().intValue();
                    qp = Long.valueOf(String.valueOf(qtePromo));
                }
            }
            etatReconWrapper.setQuantitePromotion(qp);

            for (LigneBonDeSortie lbs : lbsdate) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteTrans += lbs.getQuantite().intValue();
                    qt = Long.valueOf(String.valueOf(qteTrans));
                }
            }
            etatReconWrapper.setQuantiteTransfert(qt);

            for (LigneBonDeSortie lbs : lbsdate) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qtePerte += lbs.getQuantite().intValue();
                    qpt = Long.valueOf(String.valueOf(qtePerte));
                }
            }
            etatReconWrapper.setQuantitePerte(qpt);

            for (LigneBonDeSortie lbs : lbsdate) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    if (lbs.getBonDeSortie().getTypeVente().equals(TypeVente.CASH) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {

                        qteCash += lbs.getQuantite().intValue();
//                        somCash += lbs.getMontantVente().intValue();
                        somCash += lbs.getPrixDeVente().intValue();
                        qca = Long.valueOf(String.valueOf(qteCash));
                        sca =BigDecimal.valueOf( Long.valueOf(String.valueOf(somCash)));
                    }
                }
            }
            etatReconWrapper.setSommeVenteCash(sca);
            etatReconWrapper.setQuantiteVenteCash(qca);
            qteCash = 0;
            somCash = 0;
            qca = 0L;
            sca = BigDecimal.ZERO;

            for (LigneBonDeSortie lbs : lbsdate) {
                if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    if (lbs.getBonDeSortie().getTypeVente().equals(TypeVente.CREDIT) && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {

                        qteCredit += lbs.getQuantite().intValue();
//                        somCret += lbs.getMontantVente().intValue();
                        somCret += lbs.getPrixDeVente().intValue();
                        qc = Long.valueOf(String.valueOf(qteCredit));
                        scr = BigDecimal.valueOf( Long.valueOf(String.valueOf(somCret)));
                    }
                }
            }
            etatReconWrapper.setSommeVenteCredit(scr);
            etatReconWrapper.setQuantiteVenteCredit(qc);
            qteCredit = 0;
            somCret = 0;
            qc = 0L;
            scr = BigDecimal.ZERO;

            for (Lignelivraison lignelv : ligneLivre) {
                if (lignelv.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())) {
                    qteLivre += lignelv.getQuantiteLotLivre().longValue();
                    ql = Long.valueOf(String.valueOf(qteLivre));
                }
            }
            etatReconWrapper.setQuantiteLivree(ql);

            //Calcul de la quantité de de stock au début du mois la balance de cloture du mois précedent ou encore balance d'ouverture
            //cette quantité est la différence entre toutes les quantités livrés et toutes les sorties 
            //moins les quantités livrés du mois et les quantité sortie du mois
            qteTotale = (qteAllLivre - qteLivre) - ((qteAllSortie - qteTrans) - (qtePromo + qteVente + qtePerte));
            qT = Long.valueOf(String.valueOf(qteTotale));
            etatReconWrapper.setQuantiteTotal(qT);

            System.out.println(" QUANTITE INITIALE AU DEBUT DE LA PERIODE" + qteTotale);
            etatReconWrapper.setQuantiteInitial(qT);

            qteAllLivre = 0;
            qteAllSortie = 0;
            qtePromo = 0;
            qteVente = 0;
            qteTrans = 0;
            qteLivre = 0;
            qteTotale = 0;
            qtePerte = 0;
            qp = 0L;
            qv = 0L;
            qt = 0L;
            ql = 0L;
            qT = 0L;
            qpt = 0L;

            etatReconWrapperList.add(etatReconWrapper);

        }

        return etatReconWrapperList;
    }

}
