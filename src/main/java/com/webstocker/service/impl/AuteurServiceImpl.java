package com.webstocker.service.impl;

import com.webstocker.service.AuteurService;
import com.webstocker.domain.Auteur;
import com.webstocker.repository.AuteurRepository;
import com.webstocker.repository.search.AuteurSearchRepository;
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
 * Service Implementation for managing Auteur.
 */
@Service
@Transactional
public class AuteurServiceImpl implements AuteurService{

    private final Logger log = LoggerFactory.getLogger(AuteurServiceImpl.class);
    
    @Inject
    private AuteurRepository auteurRepository;
    
    @Inject
    private AuteurSearchRepository auteurSearchRepository;
    
    /**
     * Save a auteur.
     * 
     * @param auteur the entity to save
     * @return the persisted entity
     */
    public Auteur save(Auteur auteur) {
        log.debug("Request to save Auteur : {}", auteur);
        Auteur result = auteurRepository.save(auteur);
        auteurSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the auteurs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Auteur> findAll() {
        log.debug("Request to get all Auteurs");
        List<Auteur> result = auteurRepository.findAll();
        return result;
    }

    /**
     *  Get one auteur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Auteur findOne(Long id) {
        log.debug("Request to get Auteur : {}", id);
        Auteur auteur = auteurRepository.findOne(id);
        return auteur;
    }

    /**
     *  Delete the  auteur by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Auteur : {}", id);
        auteurRepository.delete(id);
        auteurSearchRepository.delete(id);
    }

    /**
     * Search for the auteur corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Auteur> search(String query) {
        log.debug("Request to search Auteurs for query {}", query);
        return StreamSupport
            .stream(auteurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
