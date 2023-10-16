package com.webstocker.service;

import com.webstocker.domain.Activite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Activite.
 */
public interface ActiviteService {

    /**
     * Save a activite.
     * 
     * @param activite the entity to save
     * @return the persisted entity
     */
    Activite save(Activite activite);

    /**
     *  Get all the activites.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Activite> findAll(Pageable pageable);

    /**
     *  Get the "id" activite.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Activite findOne(Long id);

    /**
     *  Delete the "id" activite.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the activite corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Activite> search(String query, Pageable pageable);
}
