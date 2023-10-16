package com.webstocker.service.impl;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.SortieParProduitMagasinWrapper;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.SortieParProduitMagasinService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
public class SortieParProduitMagasinServiceImpl implements SortieParProduitMagasinService {

    private final Logger log = LoggerFactory.getLogger(SortieParProduitMagasinServiceImpl.class);

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    ProduitRepository produitRepository;

    @Inject
    MagasinRepository magasinRepository;

    @Inject
    BonDeSortieRepository bonDeSortieRepository;

    @Override
    public List<SortieParProduitMagasinWrapper> venteParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin) {
        String p;
        String m;
        Long idBonDeSorties = 0L;
        Long idLigneBonDeSortie = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        Produit produit = produitRepository.findByNomProduit(nomProduit);
        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        List<SortieParProduitMagasinWrapper> sortieParproduitMagasin = new LinkedList<>();

        List<LigneBonDeSortie> lignesortie = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        Iterator<LigneBonDeSortie> Itlbs = lignesortie.iterator();

        List<BonDeSortie> bonDeSortie = bonDeSortieRepository.findByDaateCreationBetween(debut, fin);
        Iterator<BonDeSortie> ItBondeSortie = bonDeSortie.iterator();

//        while (ItBondeSortie.hasNext()) {
//            BonDeSortie bs = ItBondeSortie.next();
        
        while (Itlbs.hasNext()) {
//            BonDeSortie bs = ItBondeSortie.next();
            LigneBonDeSortie lbs = Itlbs.next();
            idBonDeSorties = lbs.getBonDeSortie().getId();
            SortieParProduitMagasinWrapper sortie = new SortieParProduitMagasinWrapper();
           
            if (!Objects.equals(idBonDeSorties, idLigneBonDeSortie)) {           
                if (lbs.getBonDeSortie().getMagasin() != null) {
                    if (lbs.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin())
                            && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                            && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)) {
                        sortie.setBonDeSortie(lbs.getBonDeSortie());
                         sortieParproduitMagasin.add(sortie);
                    }                    
                }
            }
           
                idLigneBonDeSortie = lbs.getBonDeSortie().getId();
                idBonDeSorties = 0L; 
        }

        return sortieParproduitMagasin;
    }

    @Override
    public List<SortieParProduitMagasinWrapper> promotionParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin) {

        String p;
        String m;
        Long idbs = 0L;
        Long idbs1 = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        Produit produit = produitRepository.findByNomProduit(nomProduit);
        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        List<SortieParProduitMagasinWrapper> sortieParproduitMagasin = new LinkedList<>();

        List<LigneBonDeSortie> lignesortie = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        Iterator<LigneBonDeSortie> Itlbs = lignesortie.iterator();

        List<BonDeSortie> bonDeSortie = bonDeSortieRepository.findByDaateCreationBetween(debut, fin);
        Iterator<BonDeSortie> ItBondeSortie = bonDeSortie.iterator();

//        while (ItBondeSortie.hasNext()) {
        while (Itlbs.hasNext()) {

//            BonDeSortie bs = ItBondeSortie.next();
            LigneBonDeSortie lbs = Itlbs.next();
            idbs = lbs.getBonDeSortie().getId();
            if (idbs != idbs1) {
                if (lbs.getBonDeSortie().getMagasin() != null) {
                    if (lbs.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin())
                            && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                            && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PROMOTION)) {

                        SortieParProduitMagasinWrapper sortie = new SortieParProduitMagasinWrapper();

                        sortie.setBonDeSortie(lbs.getBonDeSortie());

                        sortieParproduitMagasin.add(sortie);

                    }
                }
                idbs1=lbs.getBonDeSortie().getId();
                idbs = 0L;

            }

        }

        return sortieParproduitMagasin;
    }

    @Override
    public List<SortieParProduitMagasinWrapper> transfertParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin) {

        String p;
        String m;
        Long idbs = 0L;
        Long idbs1 = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        Produit produit = produitRepository.findByNomProduit(nomProduit);
        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        List<SortieParProduitMagasinWrapper> sortieParproduitMagasin = new LinkedList<>();

        List<LigneBonDeSortie> lignesortie = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        Iterator<LigneBonDeSortie> Itlbs = lignesortie.iterator();

        List<BonDeSortie> bonDeSortie = bonDeSortieRepository.findByDaateCreationBetween(debut, fin);
        Iterator<BonDeSortie> ItBondeSortie = bonDeSortie.iterator();

//        while (ItBondeSortie.hasNext()) {
        while (Itlbs.hasNext()) {

//            BonDeSortie bs = ItBondeSortie.next();
            LigneBonDeSortie lbs = Itlbs.next();
            idbs = lbs.getBonDeSortie().getId();
            if (idbs != idbs1) {
                if (lbs.getBonDeSortie().getMagasin() != null) {
                    if (lbs.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin())
                            && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                            && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.TRANSFERT)) {

                        SortieParProduitMagasinWrapper sortie = new SortieParProduitMagasinWrapper();

                        sortie.setBonDeSortie(lbs.getBonDeSortie());

                        sortieParproduitMagasin.add(sortie);

                    }
                }
                idbs1=lbs.getBonDeSortie().getId();
                idbs = 0L;

            }

        }

        return sortieParproduitMagasin;

    }

    @Override
    public List<SortieParProduitMagasinWrapper> perteParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin) {

        String p;
        String m;
        Long idbs = 0L;
        Long idbs1 = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        Produit produit = produitRepository.findByNomProduit(nomProduit);
        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        List<SortieParProduitMagasinWrapper> sortieParproduitMagasin = new LinkedList<>();

        List<LigneBonDeSortie> lignesortie = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        Iterator<LigneBonDeSortie> Itlbs = lignesortie.iterator();

        List<BonDeSortie> bonDeSortie = bonDeSortieRepository.findByDaateCreationBetween(debut, fin);
        Iterator<BonDeSortie> ItBondeSortie = bonDeSortie.iterator();

//        while (ItBondeSortie.hasNext()) {
        while (Itlbs.hasNext()) {

//            BonDeSortie bs = ItBondeSortie.next();
            LigneBonDeSortie lbs = Itlbs.next();
            idbs = lbs.getBonDeSortie().getId();
            if (idbs != idbs1) {
                if (lbs.getBonDeSortie().getMagasin() != null) {
                    if (lbs.getBonDeSortie().getMagasin().getNomMagasin().equals(magasin.getNomMagasin())
                            && lbs.getLot().getProduit().getNomProduit().equals(produit.getNomProduit())
                            && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.PERTE)) {

                        SortieParProduitMagasinWrapper sortie = new SortieParProduitMagasinWrapper();

                        sortie.setBonDeSortie(lbs.getBonDeSortie());

                        sortieParproduitMagasin.add(sortie);

                    }
                }
                idbs1=lbs.getBonDeSortie().getId();
                idbs = 0L;

            }

        }

        return sortieParproduitMagasin;

    }

}
