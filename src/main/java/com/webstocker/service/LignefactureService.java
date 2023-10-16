package com.webstocker.service;

import com.webstocker.domain.Lignefacture;

import java.util.List;

/**
 * Service Interface for managing Lignefacture.
 */
public interface LignefactureService {

    /**
     * Save a lignefacture.
     * 
     * @param lignefacture the entity to save
     * @return the persisted entity
     */
    Lignefacture save(Lignefacture lignefacture);

    /**
     *  Get all the lignefactures.
     *  
     *  @return the list of entities
     */
    List<Lignefacture> findAll();

    /**
     *  Get the "id" lignefacture.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Lignefacture findOne(Long id);

    /**
     *  Delete the "id" lignefacture.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lignefacture corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Lignefacture> search(String query);
}
