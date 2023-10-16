package com.webstocker.service;

import com.webstocker.domain.Localite;

import java.util.List;

/**
 * Service Interface for managing Localite.
 */
public interface LocaliteService {

    /**
     * Save a localite.
     * 
     * @param localite the entity to save
     * @return the persisted entity
     */
    Localite save(Localite localite);

    /**
     *  Get all the localites.
     *  
     *  @return the list of entities
     */
    List<Localite> findAll();

    /**
     *  Get the "id" localite.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Localite findOne(Long id);

    /**
     *  Delete the "id" localite.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the localite corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Localite> search(String query);
    
    /**
     * 
     * @param nom
     * @return 
     */
    Localite findByNom(String nom);
}
