package com.webstocker.service;

import com.webstocker.domain.Cellule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Cellule.
 */
public interface CelluleService {

    /**
     * Save a cellule.
     * 
     * @param cellule the entity to save
     * @return the persisted entity
     */
    Cellule save(Cellule cellule);

    /**
     *  Get all the cellules.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Cellule> findAll(Pageable pageable);

    /**
     *  Get the "id" cellule.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Cellule findOne(Long id);

    /**
     *  Delete the "id" cellule.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cellule corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Cellule> search(String query, Pageable pageable);
}
