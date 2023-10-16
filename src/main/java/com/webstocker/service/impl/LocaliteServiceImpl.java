package com.webstocker.service.impl;

import com.webstocker.service.LocaliteService;
import com.webstocker.domain.Localite;
import com.webstocker.repository.LocaliteRepository;
import com.webstocker.repository.search.LocaliteSearchRepository;
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
 * Service Implementation for managing Localite.
 */
@Service
@Transactional
public class LocaliteServiceImpl implements LocaliteService{

    private final Logger log = LoggerFactory.getLogger(LocaliteServiceImpl.class);
    
    @Inject
    private LocaliteRepository localiteRepository;
    
    @Inject
    private LocaliteSearchRepository localiteSearchRepository;
    
    /**
     * Save a localite.
     * 
     * @param localite the entity to save
     * @return the persisted entity
     */
    public Localite save(Localite localite) {
        log.debug("Request to save Localite : {}", localite);
        Localite result = localiteRepository.save(localite);
        localiteSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the localites.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Localite> findAll() {
        log.debug("Request to get all Localites");
        List<Localite> result = localiteRepository.findAll();
        return result;
    }

    /**
     *  Get one localite by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Localite findOne(Long id) {
        log.debug("Request to get Localite : {}", id);
        Localite localite = localiteRepository.findOne(id);
        return localite;
    }

    /**
     *  Delete the  localite by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Localite : {}", id);
        localiteRepository.delete(id);
        localiteSearchRepository.delete(id);
    }

    /**
     * Search for the localite corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Localite> search(String query) {
        log.debug("Request to search Localites for query {}", query);
        return StreamSupport
            .stream(localiteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Localite findByNom(String nom) {
        Localite result = null;
        String nomLocalite = null;
        nomLocalite = nom;
        return result = localiteRepository.findByNom(nomLocalite);
    }
}
