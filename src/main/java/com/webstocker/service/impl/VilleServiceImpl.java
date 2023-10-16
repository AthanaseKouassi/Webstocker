package com.webstocker.service.impl;


import com.webstocker.service.VilleService;
import com.webstocker.domain.Ville;
import com.webstocker.repository.VilleRepository;
import com.webstocker.repository.search.VilleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ville.
 */
@Service
@Transactional
public class VilleServiceImpl implements VilleService{

    private final Logger log = LoggerFactory.getLogger(VilleServiceImpl.class);
    
    @Inject
    private VilleRepository villeRepository;
    
    @Inject
    private VilleSearchRepository villeSearchRepository;
    
    /**
     * Save a ville.
     * 
     * @param ville the entity to save
     * @return the persisted entity
     */
    public Ville save(Ville ville) {
        log.debug("Request to save Ville : {}", ville);
        Ville result = villeRepository.save(ville);
        villeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the villes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Ville> findAll(Pageable pageable) {
        log.debug("Request to get all Villes");
        Page<Ville> result = villeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one ville by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Ville findOne(Long id) {
        log.debug("Request to get Ville : {}", id);
        Ville ville = villeRepository.findOne(id);
        return ville;
    }

    /**
     *  Delete the  ville by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ville : {}", id);
        villeRepository.delete(id);
        villeSearchRepository.delete(id);
    }

    /**
     * Search for the ville corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ville> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Villes for query {}", query);
        return villeSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public Ville findByLibelle(String libelle) {
        Ville result = null;
        String nom = null;
        nom = libelle;
        return result = villeRepository.findByLibelle(nom);
    }
}
