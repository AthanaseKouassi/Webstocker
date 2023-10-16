package com.webstocker.service.impl;

import com.webstocker.service.LigneMissionActiviteService;
import com.webstocker.domain.LigneMissionActivite;
import com.webstocker.repository.LigneMissionActiviteRepository;
import com.webstocker.repository.search.LigneMissionActiviteSearchRepository;
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
 * Service Implementation for managing LigneMissionActivite.
 */
@Service
@Transactional
public class LigneMissionActiviteServiceImpl implements LigneMissionActiviteService{

    private final Logger log = LoggerFactory.getLogger(LigneMissionActiviteServiceImpl.class);
    
    @Inject
    private LigneMissionActiviteRepository ligneMissionActiviteRepository;
    
    @Inject
    private LigneMissionActiviteSearchRepository ligneMissionActiviteSearchRepository;
    
    /**
     * Save a ligneMissionActivite.
     * 
     * @param ligneMissionActivite the entity to save
     * @return the persisted entity
     */
    public LigneMissionActivite save(LigneMissionActivite ligneMissionActivite) {
        log.debug("Request to save LigneMissionActivite : {}", ligneMissionActivite);
        LigneMissionActivite result = ligneMissionActiviteRepository.save(ligneMissionActivite);
        ligneMissionActiviteSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ligneMissionActivites.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<LigneMissionActivite> findAll(Pageable pageable) {
        log.debug("Request to get all LigneMissionActivites");
        Page<LigneMissionActivite> result = ligneMissionActiviteRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one ligneMissionActivite by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LigneMissionActivite findOne(Long id) {
        log.debug("Request to get LigneMissionActivite : {}", id);
        LigneMissionActivite ligneMissionActivite = ligneMissionActiviteRepository.findOne(id);
        return ligneMissionActivite;
    }

    /**
     *  Delete the  ligneMissionActivite by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneMissionActivite : {}", id);
        ligneMissionActiviteRepository.delete(id);
        ligneMissionActiviteSearchRepository.delete(id);
    }

    /**
     * Search for the ligneMissionActivite corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneMissionActivite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneMissionActivites for query {}", query);
        return ligneMissionActiviteSearchRepository.search(queryStringQuery(query), pageable);
    }
}
