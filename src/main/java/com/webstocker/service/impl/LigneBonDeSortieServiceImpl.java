package com.webstocker.service.impl;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.search.LigneBonDeSortieSearchRepository;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LigneBonDeSortie.
 */
@Service
@Transactional
public class LigneBonDeSortieServiceImpl implements LigneBonDeSortieService{

    private final Logger log = LoggerFactory.getLogger(LigneBonDeSortieServiceImpl.class);

    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    private LigneBonDeSortieSearchRepository ligneBonDeSortieSearchRepository;
    
    @Inject
    private ProduitRepository produitRepository;
    
    @Inject
    private MagasinRepository magasinRepository;

//    public LigneBonDeSortieServiceImpl(LigneBonDeSortieRepository ligneBonDeSortieRepository, LigneBonDeSortieSearchRepository ligneBonDeSortieSearchRepository,
//            ProduitRepository produitRepository, MagasinRepository magasinRepository) {
//        this.ligneBonDeSortieRepository = ligneBonDeSortieRepository;
//        this.ligneBonDeSortieSearchRepository = ligneBonDeSortieSearchRepository;
//        this.produitRepository =  produitRepository;
//        this.magasinRepository = magasinRepository;
//    }
 
    /**
     * Save a ligneBonDeSortie.
     *
     * @param ligneBonDeSortie the entity to save
     * @return the persisted entity
     */
    @Override
    public LigneBonDeSortie save(LigneBonDeSortie ligneBonDeSortie) {
        log.debug("Request to save LigneBonDeSortie : {}", ligneBonDeSortie);
        LigneBonDeSortie result = ligneBonDeSortieRepository.save(ligneBonDeSortie);
//        ligneBonDeSortieSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ligneBonDeSorties.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LigneBonDeSortie> findAll(Pageable pageable) {
        log.debug("Request to get all LigneBonDeSorties");
        return ligneBonDeSortieRepository.findAll(pageable);
    }

    /**
     *  Get one ligneBonDeSortie by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LigneBonDeSortie findOne(Long id) {
        log.debug("Request to get LigneBonDeSortie : {}", id);
        return ligneBonDeSortieRepository.findOne(id);
    }

    /**
     *  Delete the  ligneBonDeSortie by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LigneBonDeSortie : {}", id);
        ligneBonDeSortieRepository.delete(id);
//        ligneBonDeSortieSearchRepository.delete(id);
    }

    /**
     * Search for the ligneBonDeSortie corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LigneBonDeSortie> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneBonDeSorties for query {}", query);
        Page<LigneBonDeSortie> result = ligneBonDeSortieSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Override
    public List<LigneBonDeSortie> recupererLignesFacture(BonDeSortie bonDeSortie) {
        if (bonDeSortie.getTypeSortie() == TypeSortie.VENTE) {
            return ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
            }
        return null;
    }

    @Override
    public List<LigneBonDeSortie> recupererLignesBonDeSortieTransfert(BonDeSortie bonDeSortie) {
        if (bonDeSortie.getTypeSortie() == TypeSortie.TRANSFERT) {
            return ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
        }
        return null;
    }

    @Override
    public List<LigneBonDeSortie> recupererLignesBonDeSortiePromotion(BonDeSortie bonDeSortie) {
        if (bonDeSortie.getTypeSortie() == TypeSortie.PROMOTION) {
            return ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
        }
        return null;
    }
//    @Override
//    public List<LigneBonDeSortie> recupererLignesBonDeSortiePromotion(BonDeSortie bonDeSortie) {
//        if (bonDeSortie.getTypeSortie() == TypeSortie.PROMOTION || bonDeSortie.getTypeSortie() == TypeSortie.PERTE) {
//            return ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
//        }
//        return null;
//    }

    @Override
    public List<LigneBonDeSortie> recupererLignesPerte(BonDeSortie bonDeSortie) {
        if (bonDeSortie.getTypeSortie() == TypeSortie.PERTE) {
            return ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
        }
        return null;
    }

    @Override
    public List<LigneBonDeSortie> venteRealiseParMagasin(String typeSortie, String nomMagasin, String dateDebutPeriode, String dateFinPeriode) {

        TypeSortie typeSortie1 = TypeSortie.valueOf(typeSortie);
        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebutPeriode, formatter);
        LocalDate fin = LocalDate.parse(dateFinPeriode, formatter);
        Produit produit = produitRepository.findOne(1L);

        return ligneBonDeSortieRepository.findAllByProduitAndBonDeSortieMagasinAndBonDeSortieTypeSortieAndBonDeSortieDaateCreationBetween(produit, magasin, typeSortie1, debut, fin);

//        return ligneBonDeSortieRepository.findallByProduit(typeSortie, nomMagasin, dateDebutPeriode, dateFinPeriode);
    }

    @Override
    public List<LigneBonDeSortie> RecupererLigneBonDesortiePourBordereauLivraison(BonDeSortie bonDeSortie) {
        return ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
    }

    @Override
    public List<LigneBonDeSortie> chiffreAffaireParProduit(String dateDebutPeriode, String dateFinPeriode) {
        return ligneBonDeSortieRepository.findChiffreAffaireByProduit(dateDebutPeriode, dateFinPeriode);
    }

    @Override
    public List<LigneBonDeSortie> chiffreAffaireParClient(String dateDebutPeriode, String dateFinPeriode) {
        return ligneBonDeSortieRepository.findChiffreAffaireByClient(dateDebutPeriode, dateFinPeriode);
    }

    @Override
    public List<LigneBonDeSortie> sortieRealiseParMagasin(String typeSortie, String nomMagasin, String dateDebutPeriode, String dateFinPeriode, String produit) {
        return ligneBonDeSortieRepository.findallLotProduitSortie(typeSortie, nomMagasin, dateDebutPeriode, dateFinPeriode, produit);
    }

    @Override
    public Long quantiteTotalProduitVenduParMois(String dateObjectif, String nomProduit) {

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(dateObjectif);
        premierEtDernier.getDateDebutDuMois(dateObjectif);
        premierEtDernier.getDateFinDuMois(dateObjectif);
        return ligneBonDeSortieRepository.quantiteVenduByProduit(premierEtDernier.getDateDebutDuMois(dateObjectif), premierEtDernier.getDateFinDuMois(dateObjectif), nomProduit);
    }

    @Override
    public List<LigneBonDeSortie> getQuantiteProduitVendueMois(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.quantiteProduitVendueDuMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getQuantiteProduitPromotionMois(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.quantiteProduitPromotionDuMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getQuantiteProduitVendueACreditMois(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.quantiteProduitVendueCREDITDuMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getQuantiteProduitVendueCashMois(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.quantiteProduitVendueCASHDuMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getValeurVenteQuantiteProduitACreditMois(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.valeurVenteCREDITDuMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getValeurVenteQuantiteProduitCashMois(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.valeurVenteCASHDuMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getValeurRecouvrementsProduitMois(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.valeurRecouvrementParProduitParMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getValeurCreanceMoinsDeTrenteJours(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.creanceMoinsdeTrenteJourParProduit(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getValeurCreanceEntreTrenteEtQuatreVingtDixJours(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.creanceEntreTrenteEtQuatreVingtDixJour(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getValeurCreancePlusQuatreVingtDixJours(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.creancePlusDeQuatreVingtDixJour(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getQuantiteVendueparProduitEtParDistrict(String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.quantiteVendueParProduitEtParVille(dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getQuantiteProduitVendueDansUneVille(String ville, String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.quantiteVenduePourUneVille(ville, dateDebutMois, dateFinMois);
    }

    @Override
    public List<LigneBonDeSortie> getChiffreAffaireParMagasin(String dateDebutPeriode, String dateFinPeriode) {
        return ligneBonDeSortieRepository.findChiffreAffaireByMagasin(dateDebutPeriode, dateFinPeriode);
    }

    @Override
    public List<LigneBonDeSortie> getChiffreAffaireUnclient(String nomClient, String dateDebutMois, String dateFinMois) {
        return ligneBonDeSortieRepository.chiffreAffaireUnClient(nomClient, dateDebutMois, dateFinMois);
    }

    @Transactional(readOnly = true)
    public List<LigneBonDeSortie> getListeDesFactureParPeriode(String dateDebut, String dateFin) {
        return ligneBonDeSortieRepository.listeFactureParPeriode(dateDebut, dateFin);
    }

    /**
     * Quantité de produit vendue par mois 
     * @param dateDuMois
     * @return 
     */    
    @Override
    public List<LigneBonDeSortie> getQuantiteDeProduitVendueParMois(String dateDuMois) {
        String dateDebut;
        String dateFin;
        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(dateDuMois);
        dateDebut = premierEtDernier.getDateDebutDuMois(dateDuMois);
        dateFin = premierEtDernier.getDateFinDuMois(dateDuMois);
        return ligneBonDeSortieRepository.quantiteDeProduitVendusParMois(dateDebut, dateFin);
    }

    @Override
    public List<LigneBonDeSortie> getFrequenceAchatByClientPeriode(String dateDebut, String dateFin) {
        return ligneBonDeSortieRepository.frequenceAchatByClientPeriode(dateDebut, dateFin);
    }

    @Override
    public List<LigneBonDeSortie> getChiffreAffaireCategorieClient(String dateDebut, String dateFin) {
        return ligneBonDeSortieRepository.chiffreAffaireCategorieClient(dateDebut, dateFin);
    }

    @Override
    public List<LigneBonDeSortie> getReglementProduitParDate(String maDate) {
        String dateDebut;
        String dateFin;

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return ligneBonDeSortieRepository.findByBonDeSortieFactureDateLimitePaiementBetween(debut, fin);
    }

    @Override
    public List<LigneBonDeSortie> getRecouvrementParProduitDuMois(Produit produit, String maDate) {
        String dateDebut;
        String dateFin;

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return ligneBonDeSortieRepository.findByProduitAndBonDeSortieFactureDateLimitePaiementBetween(produit, debut, fin);
    }

    @Override
    public List<LigneBonDeSortie> getTousLesTranfertAvantDate(String madate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDebut = LocalDate.parse(madate, formatter);

        return ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBefore(dateDebut);
    }

    @Override
    public List<LigneBonDeSortie> getAllFactureParPeriode(String dateDebut, String dateFin) {
        return ligneBonDeSortieRepository.findAllFactureParPeriode(dateDebut, dateFin);
    }

    @Override
    public List<LigneBonDeSortie> findAllByBonDeSortie(BonDeSortie bonDeSortie) {
        return ligneBonDeSortieRepository.findAllByBonDeSortie(bonDeSortie);
    }

    @Override
    public List<LigneBonDeSortie> findByBonDeSortieDestination(Magasin magasin) {
        return ligneBonDeSortieRepository.findByBonDeSortieDestination(magasin);
    }

    @Override
    public List<LigneBonDeSortie> listeDesTransfertParMagasin(String dateDebut, String dateFin) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        System.out.println("LISTE SANS LE MAGASIN " + ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin));
        System.out.println("TAILLE  " + ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin).size());
        return ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
    }

    @Override
    public List<LigneBonDeSortie> transfert(String nomMagasin, String dateDebut, String dateFin) {

        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);
        TypeSortie typesortie = TypeSortie.TRANSFERT;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return ligneBonDeSortieRepository.findByBonDeSortieMagasinAndBonDeSortieTypeSortieAndBonDeSortieDaateCreationBetween(magasin, typesortie, debut, fin);
    }

    @Override
    public Page<LigneBonDeSortie> getListVenteParDate(String dateDebut, String dateFin, Pageable pageable) {

        TypeSortie typesortie = TypeSortie.VENTE;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return ligneBonDeSortieRepository.findByBonDeSortieTypeSortieAndBonDeSortieFactureDateFactureBetween(typesortie, debut, fin, pageable);
    }

    @Override
    public Page<LigneBonDeSortie> getListVenteParNumeroFacture(String numeroFacturenormalise, Pageable pageable) {
        TypeSortie typesortie = TypeSortie.VENTE;
        Page<LigneBonDeSortie> page = ligneBonDeSortieRepository.findByBonDeSortieTypeSortieAndBonDeSortieNumeroFactureNormaliseContaining(typesortie, numeroFacturenormalise, pageable);

        return page;
    }

    @Override
    public Page<LigneBonDeSortie> getAllFactureParPeriode(String dateDebut, String dateFin, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LigneBonDeSortie> findFactureParPeriode(String dateDebut, String dateFin) {

        return ligneBonDeSortieRepository.findFactureParPeriode(dateDebut, dateFin);

    }



}
