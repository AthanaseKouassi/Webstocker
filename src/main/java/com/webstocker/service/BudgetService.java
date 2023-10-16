package com.webstocker.service;

import com.webstocker.domain.Budget;

import java.util.List;

/**
 * Service Interface for managing Budget.
 */
public interface BudgetService {

    /**
     * Save a budget.
     * 
     * @param budget the entity to save
     * @return the persisted entity
     */
    Budget save(Budget budget);

    /**
     *  Get all the budgets.
     *  
     *  @return the list of entities
     */
    List<Budget> findAll();

    /**
     *  Get the "id" budget.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Budget findOne(Long id);

    /**
     *  Delete the "id" budget.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the budget corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Budget> search(String query);
}
