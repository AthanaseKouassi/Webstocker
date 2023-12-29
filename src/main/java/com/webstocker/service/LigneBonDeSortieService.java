package com.webstocker.service;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.web.rest.dto.newfeature.DetailFactureDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing LigneBonDeSortie.
 */
public interface LigneBonDeSortieService {

    /**
     * Save a ligneBonDeSortie.
     *
     * @param ligneBonDeSortie the entity to save
     * @return the persisted entity
     */
    LigneBonDeSortie save(LigneBonDeSortie ligneBonDeSortie);

    /**
     * Get all the ligneBonDeSorties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LigneBonDeSortie> findAll(Pageable pageable);

    /**
     * Get the "id" ligneBonDeSortie.
     *
     * @param id the id of the entity
     * @return the entity
     */
    LigneBonDeSortie findOne(Long id);

    /**
     * Delete the "id" ligneBonDeSortie.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ligneBonDeSortie corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LigneBonDeSortie> search(String query, Pageable pageable);

    List<LigneBonDeSortie> recupererLignesPerte(BonDeSortie bonDeSortie);

    List<LigneBonDeSortie> recupererLignesFacture(BonDeSortie bonDeSortie);

    List<LigneBonDeSortie> recupererLignesBonDeSortieTransfert(BonDeSortie bonDeSortie);

    List<LigneBonDeSortie> recupererLignesBonDeSortiePromotion(BonDeSortie bonDeSortie);

    List<LigneBonDeSortie> venteRealiseParMagasin(String typeSortie, String nomMagasin, String dateDebutPeriode, String dateFinPeriode);

    List<LigneBonDeSortie> RecupererLigneBonDesortiePourBordereauLivraison(BonDeSortie bonDeSortie);

    List<LigneBonDeSortie> chiffreAffaireParProduit(String dateDebutPeriode, String dateFinPeriode);

    List<LigneBonDeSortie> getChiffreAffaireParMagasin(String dateDebutPeriode, String dateFinPeriode);

    List<LigneBonDeSortie> chiffreAffaireParClient(String dateDebutPeriode, String dateFinPeriode);

    List<LigneBonDeSortie> sortieRealiseParMagasin(String typeSortie, String nomMagasin, String dateDebutPeriode, String dateFinPeriode, String produit);

    Long quantiteTotalProduitVenduParMois(String dateObjectif, String nomProduit);

    List<LigneBonDeSortie> getQuantiteProduitVendueMois(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getQuantiteProduitPromotionMois(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getQuantiteProduitVendueACreditMois(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getQuantiteProduitVendueCashMois(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getValeurVenteQuantiteProduitACreditMois(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getValeurVenteQuantiteProduitCashMois(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getValeurRecouvrementsProduitMois(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getValeurCreanceMoinsDeTrenteJours(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getValeurCreanceEntreTrenteEtQuatreVingtDixJours(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getValeurCreancePlusQuatreVingtDixJours(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getQuantiteVendueparProduitEtParDistrict(String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getQuantiteProduitVendueDansUneVille(String ville, String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getChiffreAffaireUnclient(String nomClient, String dateDebutMois, String dateFinMois);

    List<LigneBonDeSortie> getListeDesFactureParPeriode(String dateDebut, String dateFin);

    List<LigneBonDeSortie> findByBonDeSortieDestination(Magasin magasin);

    List<LigneBonDeSortie> getQuantiteDeProduitVendueParMois(String dateDuMois);

    List<LigneBonDeSortie> getFrequenceAchatByClientPeriode(String dateDebut, String dateFin);

    List<LigneBonDeSortie> getChiffreAffaireCategorieClient(String dateDebut, String dateFin);

    // List<LigneBonDeSortie> getCalculVenteEtInventaire(String dateDebut, String dateFin);
    // List<LigneBonDeSortie> getCTIL(String maDate);
//     Long getQuantiteProduitVenduParMois(String madate);
    List<LigneBonDeSortie> getReglementProduitParDate(String maDate);

    List<LigneBonDeSortie> getRecouvrementParProduitDuMois(Produit produit, String maDate);

    List<LigneBonDeSortie> getTousLesTranfertAvantDate(String madate);

    List<LigneBonDeSortie> getAllFactureParPeriode(String dateDebut, String dateFin);

    Page<LigneBonDeSortie> getAllFactureParPeriode(String dateDebut, String dateFin, Pageable pageable);

    List<LigneBonDeSortie> findAllByBonDeSortie(BonDeSortie bonDeSortie);

    List<LigneBonDeSortie> transfert(String magasin, String dateDebut, String dateFin);

    List<LigneBonDeSortie> listeDesTransfertParMagasin(String dateDebut, String dateFin);

    Page<LigneBonDeSortie> getListVenteParDate(String dateDebut, String dateFin, Pageable pageable);

    Page<LigneBonDeSortie> getListVenteParNumeroFacture(String numeroFacturenormalise, Pageable pageable);

    List<LigneBonDeSortie> findFactureParPeriode(String dateDebut, String dateFin);

    List<LigneBonDeSortie> getDetailFactureNonReglee(Long idFacture);

    List<DetailFactureDto> getDetailFacture(Long idFacture);


}
