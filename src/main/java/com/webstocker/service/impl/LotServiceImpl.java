package com.webstocker.service.impl;

import com.webstocker.service.LotService;
import com.webstocker.domain.Lot;
import com.webstocker.repository.LotRepository;
import com.webstocker.repository.search.LotSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Implementation for managing Lot.
 */
@Service
@Transactional
public class LotServiceImpl implements LotService{

    private final Logger log = LoggerFactory.getLogger(LotServiceImpl.class);
    
    @Inject
    private LotRepository lotRepository;
    
    @Inject
    private LotSearchRepository lotSearchRepository;
    
    /**
     * Save a lot.
     * 
     * @param lot the entity to save
     * @return the persisted entity
     */
    public Lot save(Lot lot) {
        log.debug("Request to save Lot : {}", lot);
        Lot result = lotRepository.save(lot);
        lotSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the lots.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Lot> findAll() {
        log.debug("Request to get all Lots");
        List<Lot> result = lotRepository.findAll();
        return result;
    }

    /**
     *  Get one lot by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Lot findOne(Long id) {
        log.debug("Request to get Lot : {}", id);
        Lot lot = lotRepository.findOne(id);
        return lot;
    }

    /**
     *  Delete the  lot by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Lot : {}", id);
        lotRepository.delete(id);
        lotSearchRepository.delete(id);
    }

    /**
     * Search for the lot corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Lot> search(String query) {
        log.debug("Request to search Lots for query {}", query);
        return StreamSupport
            .stream(lotSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Lot> findAll(Pageable pageable) {
        log.debug("Request to get all Lots");
        Page<Lot> result = lotRepository.findAll(pageable); 
        return result;
    }

    @Override
    public Page<Lot> findByNumeroLotContaining(Long numeroLot, Pageable pageable) {
        log.debug("Request to search for a page of Lots  {}", numeroLot);
        return lotRepository.findByNumeroLotContaining(numeroLot, pageable); 
    }
}
