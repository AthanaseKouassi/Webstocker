package com.webstocker.service.impl;

import com.webstocker.service.BailleurService;
import com.webstocker.domain.Bailleur;
import com.webstocker.repository.BailleurRepository;
import com.webstocker.repository.search.BailleurSearchRepository;
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
 * Service Implementation for managing Bailleur.
 */
@Service
@Transactional
public class BailleurServiceImpl implements BailleurService{

    private final Logger log = LoggerFactory.getLogger(BailleurServiceImpl.class);
    
    @Inject
    private BailleurRepository bailleurRepository;
    
    @Inject
    private BailleurSearchRepository bailleurSearchRepository;
    
    /**
     * Save a bailleur.
     * 
     * @param bailleur the entity to save
     * @return the persisted entity
     */
    public Bailleur save(Bailleur bailleur) {
        log.debug("Request to save Bailleur : {}", bailleur);
        Bailleur result = bailleurRepository.save(bailleur);
        bailleurSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the bailleurs.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Bailleur> findAll() {
        log.debug("Request to get all Bailleurs");
        List<Bailleur> result = bailleurRepository.findAll();
        return result;
    }

    /**
     *  Get one bailleur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Bailleur findOne(Long id) {
        log.debug("Request to get Bailleur : {}", id);
        Bailleur bailleur = bailleurRepository.findOne(id);
        return bailleur;
    }

    /**
     *  Delete the  bailleur by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bailleur : {}", id);
        bailleurRepository.delete(id);
        bailleurSearchRepository.delete(id);
    }

    /**
     * Search for the bailleur corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Bailleur> search(String query) {
        log.debug("Request to search Bailleurs for query {}", query);
        return StreamSupport
            .stream(bailleurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Bailleur findByNomBailleur(String nomBailleur) {
        Bailleur bailleur = bailleurRepository.findByNomBailleur(nomBailleur);
        return bailleur;
    }
}
