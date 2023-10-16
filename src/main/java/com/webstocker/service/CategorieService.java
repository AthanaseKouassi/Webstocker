package com.webstocker.service;

import com.webstocker.domain.Categorie;

import java.util.List;

/**
 * Service Interface for managing Categorie.
 */
public interface CategorieService {

    /**
     * Save a categorie.
     * 
     * @param categorie the entity to save
     * @return the persisted entity
     */
    Categorie save(Categorie categorie);

    /**
     *  Get all the categories.
     *  
     *  @return the list of entities
     */
    List<Categorie> findAll();

    /**
     *  Get the "id" categorie.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Categorie findOne(Long id);

    /**
     *  Delete the "id" categorie.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the categorie corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Categorie> search(String query);
    
    Categorie findByNomCategorie(String nomCategorie);
}
