package com.webstocker.service.impl;

import com.webstocker.service.BudgetService;
import com.webstocker.domain.Budget;
import com.webstocker.repository.BudgetRepository;
import com.webstocker.repository.search.BudgetSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Budget.
 */
@Service
@Transactional
public class BudgetServiceImpl implements BudgetService{

    private final Logger log = LoggerFactory.getLogger(BudgetServiceImpl.class);
    
    @Inject
    private BudgetRepository budgetRepository;
    
    @Inject
    private BudgetSearchRepository budgetSearchRepository;
    
    /**
     * Save a budget.
     * 
     * @param budget the entity to save
     * @return the persisted entity
     */
    public Budget save(Budget budget) {
        log.debug("Request to save Budget : {}", budget);
        Budget result = budgetRepository.save(budget);
        budgetSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the budgets.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Budget> findAll() {
        log.debug("Request to get all Budgets");
        List<Budget> result = budgetRepository.findAll();
        return result;
    }

    /**
     *  Get one budget by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Budget findOne(Long id) {
        log.debug("Request to get Budget : {}", id);
        Budget budget = budgetRepository.findOne(id);
        return budget;
    }

    /**
     *  Delete the  budget by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Budget : {}", id);
        budgetRepository.delete(id);
        budgetSearchRepository.delete(id);
    }

    /**
     * Search for the budget corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Budget> search(String query) {
        log.debug("Request to search Budgets for query {}", query);
        return StreamSupport
            .stream(budgetSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
