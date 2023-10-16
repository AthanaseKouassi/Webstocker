package com.webstocker.service;

import com.webstocker.domain.Magasin;

import java.util.List;

/**
 * Service Interface for managing Magasin.
 */
public interface MagasinService {

    /**
     * Save a magasin.
     * 
     * @param magasin the entity to save
     * @return the persisted entity
     */
    Magasin save(Magasin magasin);

    /**
     *  Get all the magasins.
     *  
     *  @return the list of entities
     */
    List<Magasin> findAll();

    /**
     *  Get the "id" magasin.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Magasin findOne(Long id);

    /**
     *  Delete the "id" magasin.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the magasin corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Magasin> search(String query);
    
    Magasin findByNomMagasin(String nom);
}
