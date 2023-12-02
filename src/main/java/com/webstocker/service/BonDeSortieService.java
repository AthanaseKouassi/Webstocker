package com.webstocker.service;

import com.webstocker.domain.BonDeSortie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing BonDeSortie.
 */
public interface BonDeSortieService {

    /**
     * Save a bonDeSortie.
     *
     * @param bonDeSortie the entity to save
     * @return the persisted entity
     */
    BonDeSortie save(BonDeSortie bonDeSortie);

    /**
     * Save a bonDeSortie and create a {@link com.webstocker.domain.Facture}
     *
     * @param bonDeSortie the entity to save
     * @param remise
     * @return the persisted entity
     */
    BonDeSortie saveVente(BonDeSortie bonDeSortie, Integer delaiPaiement, Long remise
        , LocalDate dateFacture, LocalDate dateReglement);

    /**
     * save a bonDeSortie and create a factureZero
     *
     * @param bonDeSortie
     * @param dateFacture
     * @return
     */
    BonDeSortie savePromotion(BonDeSortie bonDeSortie, LocalDate dateFacture);

    /**
     * Get all the bonDeSorties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BonDeSortie> findAll(Pageable pageable);

    List<BonDeSortie> findAll();

    Page<BonDeSortie> findBonByNumero(String numero, Pageable pageable);

    /**
     * Get the "id" bonDeSortie.
     *
     * @param id the id of the entity
     * @return the entity
     */
    BonDeSortie findOne(Long id);

    /**
     * Delete the "id" bonDeSortie.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the bonDeSortie corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    Page<BonDeSortie> search(String query, Pageable pageable);

    List<BonDeSortie> bonEtBordereauParMagasin(String nomMagasin, String dateDebut, String dateFin);

    List<BonDeSortie> listeTransfertParMagasin(String nomMagasin, String dateDebut, String dateFin);

    Page<BonDeSortie> listeBonDeSortieVente(Pageable pageable);

    Page<BonDeSortie> listeBonDeSortiePromotion(Pageable pageable);

    List<BonDeSortie> listeBonDeSortiePromotion();

    Page<BonDeSortie> listeBonDeSortieTransfert(Pageable pageable);

    Page<BonDeSortie> listeBonDeSortiePerte(Pageable pageable);

    Page<BonDeSortie> retrouverBonDeSortieVenteParNumero(String numero, Pageable pageable);

    Page<BonDeSortie> retrouverBonDeSortiePromotionParNumero(String numero, Pageable pageable);

    Page<BonDeSortie> retrouverBonDeSortieTRansfertParNumero(String numero, Pageable pageable);

    Page<BonDeSortie> retrouverBonDeSortiePerteParNumero(String numero, Pageable pageable);

    Page<BonDeSortie> transfertEncours(Pageable pageable);

}
