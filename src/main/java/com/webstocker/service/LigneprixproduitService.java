package com.webstocker.service;

import com.webstocker.domain.Ligneprixproduit;

import java.util.List;

/**
 * Service Interface for managing Ligneprixproduit.
 */
public interface LigneprixproduitService {

    /**
     * Save a ligneprixproduit.
     * 
     * @param ligneprixproduit the entity to save
     * @return the persisted entity
     */
    Ligneprixproduit save(Ligneprixproduit ligneprixproduit);

    /**
     *  Get all the ligneprixproduits.
     *  
     *  @return the list of entities
     */
    List<Ligneprixproduit> findAll();

    /**
     *  Get the "id" ligneprixproduit.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Ligneprixproduit findOne(Long id);

    /**
     *  Delete the "id" ligneprixproduit.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ligneprixproduit corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Ligneprixproduit> search(String query);
}
