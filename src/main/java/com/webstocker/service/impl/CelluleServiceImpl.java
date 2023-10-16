package com.webstocker.service.impl;

import com.webstocker.service.CelluleService;
import com.webstocker.domain.Cellule;
import com.webstocker.repository.CelluleRepository;
import com.webstocker.repository.search.CelluleSearchRepository;
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
 * Service Implementation for managing Cellule.
 */
@Service
@Transactional
public class CelluleServiceImpl implements CelluleService{

    private final Logger log = LoggerFactory.getLogger(CelluleServiceImpl.class);
    
    @Inject
    private CelluleRepository celluleRepository;
    
    @Inject
    private CelluleSearchRepository celluleSearchRepository;
    
    /**
     * Save a cellule.
     * 
     * @param cellule the entity to save
     * @return the persisted entity
     */
    public Cellule save(Cellule cellule) {
        log.debug("Request to save Cellule : {}", cellule);
        Cellule result = celluleRepository.save(cellule);
        celluleSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the cellules.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Cellule> findAll(Pageable pageable) {
        log.debug("Request to get all Cellules");
        Page<Cellule> result = celluleRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one cellule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Cellule findOne(Long id) {
        log.debug("Request to get Cellule : {}", id);
        Cellule cellule = celluleRepository.findOne(id);
        return cellule;
    }

    /**
     *  Delete the  cellule by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cellule : {}", id);
        celluleRepository.delete(id);
        celluleSearchRepository.delete(id);
    }

    /**
     * Search for the cellule corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Cellule> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cellules for query {}", query);
        return celluleSearchRepository.search(queryStringQuery(query), pageable);
    }
}
