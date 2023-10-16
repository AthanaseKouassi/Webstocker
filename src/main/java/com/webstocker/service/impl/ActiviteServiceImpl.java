package com.webstocker.service.impl;

import com.webstocker.service.ActiviteService;
import com.webstocker.domain.Activite;
import com.webstocker.repository.ActiviteRepository;
import com.webstocker.repository.search.ActiviteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Activite.
 */
@Service
@Transactional
public class ActiviteServiceImpl implements ActiviteService{

    private final Logger log = LoggerFactory.getLogger(ActiviteServiceImpl.class);
    
    @Inject
    private ActiviteRepository activiteRepository;
    
    @Inject
    private ActiviteSearchRepository activiteSearchRepository;
    
    /**
     * Save a activite.
     * 
     * @param activite the entity to save
     * @return the persisted entity
     */
    public Activite save(Activite activite) {
        log.debug("Request to save Activite : {}", activite);
        Activite result = activiteRepository.save(activite);
        activiteSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the activites.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Activite> findAll(Pageable pageable) {
        log.debug("Request to get all Activites");
        Page<Activite> result = activiteRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one activite by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Activite findOne(Long id) {
        log.debug("Request to get Activite : {}", id);
        Activite activite = activiteRepository.findOne(id);
        return activite;
    }

    /**
     *  Delete the  activite by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Activite : {}", id);
        activiteRepository.delete(id);
        activiteSearchRepository.delete(id);
    }

    /**
     * Search for the activite corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Activite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Activites for query {}", query);
        return activiteSearchRepository.search(queryStringQuery(query), pageable);
    }
}
