package com.webstocker.service.impl;

import com.webstocker.service.LigneprixproduitService;
import com.webstocker.domain.Ligneprixproduit;
import com.webstocker.repository.LigneprixproduitRepository;
import com.webstocker.repository.search.LigneprixproduitSearchRepository;
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
 * Service Implementation for managing Ligneprixproduit.
 */
@Service
@Transactional
public class LigneprixproduitServiceImpl implements LigneprixproduitService{

    private final Logger log = LoggerFactory.getLogger(LigneprixproduitServiceImpl.class);
    
    @Inject
    private LigneprixproduitRepository ligneprixproduitRepository;
    
    @Inject
    private LigneprixproduitSearchRepository ligneprixproduitSearchRepository;
    
    /**
     * Save a ligneprixproduit.
     * 
     * @param ligneprixproduit the entity to save
     * @return the persisted entity
     */
    public Ligneprixproduit save(Ligneprixproduit ligneprixproduit) {
        log.debug("Request to save Ligneprixproduit : {}", ligneprixproduit);
        Ligneprixproduit result = ligneprixproduitRepository.save(ligneprixproduit);
        ligneprixproduitSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ligneprixproduits.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Ligneprixproduit> findAll() {
        log.debug("Request to get all Ligneprixproduits");
        List<Ligneprixproduit> result = ligneprixproduitRepository.findAll();
        return result;
    }

    /**
     *  Get one ligneprixproduit by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Ligneprixproduit findOne(Long id) {
        log.debug("Request to get Ligneprixproduit : {}", id);
        Ligneprixproduit ligneprixproduit = ligneprixproduitRepository.findOne(id);
        return ligneprixproduit;
    }

    /**
     *  Delete the  ligneprixproduit by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ligneprixproduit : {}", id);
        ligneprixproduitRepository.delete(id);
        ligneprixproduitSearchRepository.delete(id);
    }

    /**
     * Search for the ligneprixproduit corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Ligneprixproduit> search(String query) {
        log.debug("Request to search Ligneprixproduits for query {}", query);
        return StreamSupport
            .stream(ligneprixproduitSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
