package com.webstocker.service;

import com.webstocker.domain.LigneMissionActivite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing LigneMissionActivite.
 */
public interface LigneMissionActiviteService {

    /**
     * Save a ligneMissionActivite.
     * 
     * @param ligneMissionActivite the entity to save
     * @return the persisted entity
     */
    LigneMissionActivite save(LigneMissionActivite ligneMissionActivite);

    /**
     *  Get all the ligneMissionActivites.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LigneMissionActivite> findAll(Pageable pageable);

    /**
     *  Get the "id" ligneMissionActivite.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    LigneMissionActivite findOne(Long id);

    /**
     *  Delete the "id" ligneMissionActivite.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ligneMissionActivite corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<LigneMissionActivite> search(String query, Pageable pageable);
}
