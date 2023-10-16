package com.webstocker.service;

import com.webstocker.domain.LigneBudget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing LigneBudget.
 */
public interface LigneBudgetService {

    /**
     * Save a ligneBudget.
     * 
     * @param ligneBudget the entity to save
     * @return the persisted entity
     */
    LigneBudget save(LigneBudget ligneBudget);

    /**
     *  Get all the ligneBudgets.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LigneBudget> findAll(Pageable pageable);

    /**
     *  Get the "id" ligneBudget.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    LigneBudget findOne(Long id);

    /**
     *  Delete the "id" ligneBudget.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ligneBudget corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<LigneBudget> search(String query, Pageable pageable);
}
