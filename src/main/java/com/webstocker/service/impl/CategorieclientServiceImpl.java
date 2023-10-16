package com.webstocker.service.impl;

import com.webstocker.service.CategorieclientService;
import com.webstocker.domain.Categorieclient;
import com.webstocker.repository.CategorieclientRepository;
import com.webstocker.repository.search.CategorieclientSearchRepository;
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
 * Service Implementation for managing Categorieclient.
 */
@Service
@Transactional
public class CategorieclientServiceImpl implements CategorieclientService{

    private final Logger log = LoggerFactory.getLogger(CategorieclientServiceImpl.class);
    
    @Inject
    private CategorieclientRepository categorieclientRepository;
    
    @Inject
    private CategorieclientSearchRepository categorieclientSearchRepository;
    
    /**
     * Save a categorieclient.
     * 
     * @param categorieclient the entity to save
     * @return the persisted entity
     */
    public Categorieclient save(Categorieclient categorieclient) {
        log.debug("Request to save Categorieclient : {}", categorieclient);
        Categorieclient result = categorieclientRepository.save(categorieclient);
        categorieclientSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the categorieclients.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Categorieclient> findAll() {
        log.debug("Request to get all Categorieclients");
        List<Categorieclient> result = categorieclientRepository.findAll();
        return result;
    }

    /**
     *  Get one categorieclient by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Categorieclient findOne(Long id) {
        log.debug("Request to get Categorieclient : {}", id);
        Categorieclient categorieclient = categorieclientRepository.findOne(id);
        return categorieclient;
    }

    /**
     *  Delete the  categorieclient by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Categorieclient : {}", id);
        categorieclientRepository.delete(id);
        categorieclientSearchRepository.delete(id);
    }

    /**
     * Search for the categorieclient corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Categorieclient> search(String query) {
        log.debug("Request to search Categorieclients for query {}", query);
        return StreamSupport
            .stream(categorieclientSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Categorieclient findByLibelleCategorieClient(String nomLibelle) {
        Categorieclient result = null;
        String nomcategorie = null;
        nomcategorie = nomLibelle;
        return result = categorieclientRepository.findByLibelleCategorieClient(nomLibelle);
    }
}
