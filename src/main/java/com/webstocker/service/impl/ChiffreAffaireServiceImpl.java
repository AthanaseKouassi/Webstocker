package com.webstocker.service.impl;

import com.webstocker.domain.Client;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;
import com.webstocker.repository.CategorieclientRepository;
import com.webstocker.repository.ClientRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.ChiffreAffaireService;
import java.math.BigDecimal;
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
public class ChiffreAffaireServiceImpl implements ChiffreAffaireService {

    private final Logger log = LoggerFactory.getLogger(ChiffreAffaireServiceImpl.class);

    @Inject
    private ProduitRepository produitRepository;

    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    private MagasinRepository magasinRepository;

    @Inject
    private CategorieclientRepository categorieclientRepository;
    
    @Inject
    private ClientRepository clientRepository;

    @Override
    public List<ChiffreAffaireWrapper> chiffreAffaireParProduit(String dateDebut, String dateFin) {
//        BigDecimal chiffreAffaire = BigDecimal.ZERO;
        Long chiffreAffaire = 0L;
        Long quantiteVendu = 0L;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<ChiffreAffaireWrapper> lesChiffreAffaire = new LinkedList<>();

        //Liste des produits sortie  de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);

        // liste de tous les produits
        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> itProduits = listProduits.iterator();

        while (itProduits.hasNext()) {
            Produit p = itProduits.next();

            ChiffreAffaireWrapper chiffreAffaireWrapper = new ChiffreAffaireWrapper();
            chiffreAffaireWrapper.setNomProduit(p.getNomProduit());

            for (LigneBonDeSortie lbs : listeSortieParPeriode) {
                if (lbs.getLot().getProduit().getNomProduit().equals(p.getNomProduit())
                        && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)) {
//                    chiffreAffaire = chiffreAffaire.add(lbs.getMontantVente());
                    chiffreAffaire += lbs.getPrixDeVente();
                    quantiteVendu += lbs.getQuantite();
                }
            }
            chiffreAffaireWrapper.setChiffreAffaire(chiffreAffaire);
            chiffreAffaireWrapper.setQuantiteVendue(quantiteVendu);

//            chiffreAffaire = BigDecimal.ZERO;
            chiffreAffaire = 0L;
            quantiteVendu = 0L;

            lesChiffreAffaire.add(chiffreAffaireWrapper);
        }

        return lesChiffreAffaire;
    }

    @Override
    public List<ChiffreAffaireWrapper> chiffreAffaireParClient(String nomClient, String dateDebut, String dateFin) {
//        BigDecimal chiffre = BigDecimal.ZERO;
        Long chiffre = 0L;
        Long quantiteVendu = 0L;
        String nomProduit = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<ChiffreAffaireWrapper> lesChiffreAffaire = new LinkedList<>();
        Client client = clientRepository.findByNomClient(nomClient);
        //Liste des produits sortie  de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);

        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> itProduits = listProduits.iterator();

        while (itProduits.hasNext()) {
            Produit p = itProduits.next();
            nomProduit = p.getNomProduit();

            for (LigneBonDeSortie lbs : listeSortieParPeriode) {
                if (lbs.getBonDeSortie().getClient() != null) {
                    if (lbs.getBonDeSortie().getClient().getNomClient().equals(nomClient) && lbs.getLot().getProduit().getNomProduit().equals(nomProduit)
                            && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)) {
//                        chiffre = chiffre.add(lbs.getMontantVente());
                        chiffre += lbs.getPrixDeVente();
                        quantiteVendu += lbs.getQuantite();
                    }
                }
            }

            if (quantiteVendu > 0) {
                ChiffreAffaireWrapper chiffreAffaireWrapper = new ChiffreAffaireWrapper();

                chiffreAffaireWrapper.setNomClient(nomClient);
                chiffreAffaireWrapper.setLibellecategorie(client.getCategorieclient().getLibelleCategorieclient());
                chiffreAffaireWrapper.setChiffreAffaire(chiffre);
                chiffreAffaireWrapper.setQuantiteVendue(quantiteVendu);
                chiffreAffaireWrapper.setNomProduit(nomProduit);
                chiffreAffaireWrapper.setTelephoneClient(client.getTelephoneClient());

                lesChiffreAffaire.add(chiffreAffaireWrapper);
                quantiteVendu = 0L;
                chiffre = 0L;
            }

        }

        return lesChiffreAffaire;
    }

    @Override
    public List<ChiffreAffaireWrapper> chiffreAffaireParMagasin(String magasin, String dateDebut, String DateFin) {

        return null;
    }

    @Override
    public List<ChiffreAffaireWrapper> chiffreAffaireParCategorieClient(String dateDebut, String DateFin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ChiffreAffaireWrapper> chiffreAffaireMagasin(String dateDebut, String dateFin) {

//        BigDecimal chiffre = BigDecimal.ZERO;
        Long chiffre = 0L;
        Long quantiteVendu = 0L;
        String nomProduit = null;
        String nomMagasin = null;
        String nomMagasin2 = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<ChiffreAffaireWrapper> lesChiffreAffaire = new LinkedList<>();

        //Liste des produits sortie  de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);

        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> itProduits = listProduits.iterator();

        List<Magasin> listMagasin = magasinRepository.findAll();
        Iterator<Magasin> itMagasin = listMagasin.iterator();

        while (itMagasin.hasNext()) {
            Magasin magasin = itMagasin.next();
            nomMagasin = magasin.getNomMagasin();

            if (nomMagasin.equals(nomMagasin2)) {

            } else {
                for (Produit produit : listProduits) {
                    nomProduit = produit.getNomProduit();

                    for (LigneBonDeSortie lbs : listeSortieParPeriode) {
                        if (lbs.getBonDeSortie().getMagasin() != null) {
                            if (lbs.getBonDeSortie().getMagasin().getNomMagasin().equals(nomMagasin)
                                    && lbs.getLot().getProduit().getNomProduit().equals(nomProduit)
                                    && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)) {
//                                chiffre = chiffre.add(lbs.getMontantVente());
                               chiffre += lbs.getPrixDeVente();
                                quantiteVendu += lbs.getQuantite();
                            }
                        }
                    }

                    if (quantiteVendu > 0) {
                        ChiffreAffaireWrapper chiffreAffaireWrapper = new ChiffreAffaireWrapper();
                        chiffreAffaireWrapper.setNomProduit(nomProduit);
                        chiffreAffaireWrapper.setNommagasin(nomMagasin);
                        chiffreAffaireWrapper.setQuantiteVendue(quantiteVendu);
                        chiffreAffaireWrapper.setChiffreAffaire(chiffre);

                        chiffre = 0L;
                        quantiteVendu = 0L;
                        nomProduit = null;

                        lesChiffreAffaire.add(chiffreAffaireWrapper);
                    }

                }

            }

            nomMagasin2 = magasin.getNomMagasin();
        }

        return lesChiffreAffaire;
    }

    @Override
    public List<ChiffreAffaireWrapper> chiffreAffaireUneCategorieClient(String libelleCategorie, String dateDebut, String dateFin) {
//        BigDecimal chiffre = BigDecimal.ZERO;
        Long chiffre = 0L;
        Long quantiteVendu = 0L;
        String nomProduit = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        List<ChiffreAffaireWrapper> lesChiffreAffaire = new LinkedList<>();

        //Liste des produits sortie  de date debut à date fin
        List<LigneBonDeSortie> listeSortieParPeriode = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);

        List<Produit> listProduits = produitRepository.findAll();
        Iterator<Produit> itProduits = listProduits.iterator();

        while (itProduits.hasNext()) {
            Produit p = itProduits.next();
            nomProduit = p.getNomProduit();

            for (LigneBonDeSortie lbs : listeSortieParPeriode) {
                if (lbs.getBonDeSortie().getClient() != null) {
                    if (lbs.getBonDeSortie().getClient().getCategorieclient() != null) {
                        if (lbs.getBonDeSortie().getClient().getCategorieclient().getLibelleCategorieclient().equals(libelleCategorie) && lbs.getLot().getProduit().getNomProduit().equals(nomProduit)
                                && lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE)) {
//                            chiffre = chiffre.add(lbs.getMontantVente());
                            chiffre += lbs.getPrixDeVente();
                            quantiteVendu += lbs.getQuantite();
                        }
                    }
                }
            }

            if (quantiteVendu > 0) {
                ChiffreAffaireWrapper chiffreAffaireWrapper = new ChiffreAffaireWrapper();
                chiffreAffaireWrapper.setLibellecategorie(libelleCategorie);
                chiffreAffaireWrapper.setChiffreAffaire(chiffre);
                chiffreAffaireWrapper.setQuantiteVendue(quantiteVendu);
                chiffreAffaireWrapper.setNomProduit(nomProduit);

                lesChiffreAffaire.add(chiffreAffaireWrapper);
                chiffre = 0L;
                quantiteVendu = 0L;
            }
        }
        return lesChiffreAffaire;
    }

}
