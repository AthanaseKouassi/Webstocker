package com.webstocker.service;

import com.webstocker.domain.Lot;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Lot.
 */
public interface LotService {

    /**
     * Save a lot.
     * 
     * @param lot the entity to save
     * @return the persisted entity
     */
    Lot save(Lot lot);

    /**
     *  Get all the lots.
     *  
     *  @return the list of entities
     */
    List<Lot> findAll();
    
    /**
     *  Get all the lots by page.
     *  
     * @param pageable
     *  @return the list of entities
     */
    Page<Lot> findAll(Pageable pageable);

    /**
     *  Get the "id" lot.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Lot findOne(Long id);

    /**
     *  Delete the "id" lot.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lot corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Lot> search(String query);
    
    Page <Lot> findByNumeroLotContaining(Long numeroLot,Pageable pageable);
}
