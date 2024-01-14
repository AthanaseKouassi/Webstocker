package com.webstocker.service;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing Facture.
 */
public interface FactureService {

    /**
     * Save a facture.
     *
     * @param facture the entity to save
     * @return the persisted entity
     */
    Facture save(Facture facture);

    /**
     * Get all the factures.
     *
     * @return the list of entities
     */
    List<Facture> findAll();

    List<Facture> findAllCreancesThirtyDayAgo(LocalDate localDate, Integer critere);

    List<Facture> findFactureByDate(LocalDate dateDebut, LocalDate dateFin);

    List<Facture> findFactureNonReglees(Long id);

    List<Facture> findFisrtFacture(Long id);

    /**
     * Get the "id" facture.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Facture findOne(Long id);

    /**
     * Delete the "id" facture.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the facture corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    List<Facture> search(String query);

    List<Facture> getFatureAregleeParPeriode(String maDate);

    List<Facture> getFactureParPeriode(String dateDebut, String dateFin);

    Facture getFactureParBonDeSortie(BonDeSortie bonDesortie);

    //List<Facture> getFactureNonRegleeParNumero(String numero);

    List<Facture> getFactureNonSoldeParPeriode(String dateDebut, String dateFin);

    List<Facture> getFactureNonSoldeParNumero(String numero);

}
