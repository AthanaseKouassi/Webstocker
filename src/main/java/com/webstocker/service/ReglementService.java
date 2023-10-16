package com.webstocker.service;

import com.webstocker.domain.Facture;
import com.webstocker.domain.Reglement;

import java.util.List;

/**
 * Service Interface for managing Reglement.
 */
public interface ReglementService {

    /**
     * Save a reglement.
     *
     * @param reglement the entity to save
     * @return the persisted entity
     */
    Reglement save(Reglement reglement);

    /**
     *  Get all the reglements.
     *
     *  @return the list of entities
     */
    List<Reglement> findAll();

    /**
     *  Get the "id" reglement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Reglement findOne(Long id);

    /**
     *  Delete the "id" reglement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the reglement corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Reglement> search(String query);

    List<Reglement> getTousLesReglementDuMois(String madate);

    List<Reglement> findByFacture(Facture facture);
}
