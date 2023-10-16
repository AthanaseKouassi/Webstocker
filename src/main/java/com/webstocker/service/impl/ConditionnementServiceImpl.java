package com.webstocker.service.impl;

import com.webstocker.service.ConditionnementService;
import com.webstocker.domain.Conditionnement;
import com.webstocker.repository.ConditionnementRepository;
import com.webstocker.repository.search.ConditionnementSearchRepository;
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
 * Service Implementation for managing Conditionnement.
 */
@Service
@Transactional
public class ConditionnementServiceImpl implements ConditionnementService{

    private final Logger log = LoggerFactory.getLogger(ConditionnementServiceImpl.class);
    
    @Inject
    private ConditionnementRepository conditionnementRepository;
    
    @Inject
    private ConditionnementSearchRepository conditionnementSearchRepository;
    
    /**
     * Save a conditionnement.
     * 
     * @param conditionnement the entity to save
     * @return the persisted entity
     */
    public Conditionnement save(Conditionnement conditionnement) {
        log.debug("Request to save Conditionnement : {}", conditionnement);
        Conditionnement result = conditionnementRepository.save(conditionnement);
        conditionnementSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the conditionnements.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Conditionnement> findAll() {
        log.debug("Request to get all Conditionnements");
        List<Conditionnement> result = conditionnementRepository.findAll();
        return result;
    }

    /**
     *  Get one conditionnement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Conditionnement findOne(Long id) {
        log.debug("Request to get Conditionnement : {}", id);
        Conditionnement conditionnement = conditionnementRepository.findOne(id);
        return conditionnement;
    }

    /**
     *  Delete the  conditionnement by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Conditionnement : {}", id);
        conditionnementRepository.delete(id);
        conditionnementSearchRepository.delete(id);
    }

    /**
     * Search for the conditionnement corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Conditionnement> search(String query) {
        log.debug("Request to search Conditionnements for query {}", query);
        return StreamSupport
            .stream(conditionnementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Conditionnement findByLibelle(String libelle) {
        Conditionnement result = null;
        String libelleConditionement = null;        
        libelleConditionement = libelle;
        return result = conditionnementRepository.findByLibelle(libelleConditionement);
    }
}
