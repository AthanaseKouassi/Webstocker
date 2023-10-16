package com.webstocker.service;

import com.webstocker.domain.Commune;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Commune.
 */
public interface CommuneService {

    /**
     * Save a commune.
     * 
     * @param commune the entity to save
     * @return the persisted entity
     */
    Commune save(Commune commune);

    /**
     *  Get all the communes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Commune> findAll(Pageable pageable);

    /**
     *  Get the "id" commune.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Commune findOne(Long id);

    /**
     *  Delete the "id" commune.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the commune corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Commune> search(String query, Pageable pageable);
    
    /**
     * 
     * @param libelle
     * @return the entity
     */
    Commune findByLibelle(String libelle);
}
