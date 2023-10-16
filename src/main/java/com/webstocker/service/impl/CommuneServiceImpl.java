package com.webstocker.service.impl;

import com.webstocker.service.CommuneService;
import com.webstocker.domain.Commune;
import com.webstocker.repository.CommuneRepository;
import com.webstocker.repository.search.CommuneSearchRepository;
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
 * Service Implementation for managing Commune.
 */
@Service
@Transactional
public class CommuneServiceImpl implements CommuneService{

    private final Logger log = LoggerFactory.getLogger(CommuneServiceImpl.class);
    
    @Inject
    private CommuneRepository communeRepository;
    
    @Inject
    private CommuneSearchRepository communeSearchRepository;
    
    /**
     * Save a commune.
     * 
     * @param commune the entity to save
     * @return the persisted entity
     */
    public Commune save(Commune commune) {
        log.debug("Request to save Commune : {}", commune);
        Commune result = communeRepository.save(commune);
        communeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the communes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Commune> findAll(Pageable pageable) {
        log.debug("Request to get all Communes");
        Page<Commune> result = communeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one commune by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Commune findOne(Long id) {
        log.debug("Request to get Commune : {}", id);
        Commune commune = communeRepository.findOne(id);
        return commune;
    }

    /**
     *  Delete the  commune by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Commune : {}", id);
        communeRepository.delete(id);
        communeSearchRepository.delete(id);
    }

    /**
     * Search for the commune corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Commune> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Communes for query {}", query);
        return communeSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public Commune findByLibelle(String libelle) {
       Commune result = null;
        String nomCommune = null;        
        nomCommune = libelle;
        return result = communeRepository.findByLibelle(nomCommune);
    }
}
