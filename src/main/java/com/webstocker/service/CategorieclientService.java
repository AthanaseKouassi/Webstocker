package com.webstocker.service;

import com.webstocker.domain.Categorieclient;

import java.util.List;

/**
 * Service Interface for managing Categorieclient.
 */
public interface CategorieclientService {

    /**
     * Save a categorieclient.
     * 
     * @param categorieclient the entity to save
     * @return the persisted entity
     */
    Categorieclient save(Categorieclient categorieclient);

    /**
     *  Get all the categorieclients.
     *  
     *  @return the list of entities
     */
    List<Categorieclient> findAll();

    /**
     *  Get the "id" categorieclient.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Categorieclient findOne(Long id);

    /**
     *  Delete the "id" categorieclient.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the categorieclient corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Categorieclient> search(String query);
    
    /**
     * @param nomLibelle
     * @return 
     */
    Categorieclient findByLibelleCategorieClient(String nomLibelle);
}
