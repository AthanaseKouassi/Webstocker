package com.webstocker.service;

import com.webstocker.domain.Conditionnement;

import java.util.List;

/**
 * Service Interface for managing Conditionnement.
 */
public interface ConditionnementService {

    /**
     * Save a conditionnement.
     * 
     * @param conditionnement the entity to save
     * @return the persisted entity
     */
    Conditionnement save(Conditionnement conditionnement);

    /**
     *  Get all the conditionnements.
     *  
     *  @return the list of entities
     */
    List<Conditionnement> findAll();

    /**
     *  Get the "id" conditionnement.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Conditionnement findOne(Long id);

    /**
     *  Delete the "id" conditionnement.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the conditionnement corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Conditionnement> search(String query);
    
    Conditionnement findByLibelle(String libelle);
}
