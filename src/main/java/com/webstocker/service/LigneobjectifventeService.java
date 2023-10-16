package com.webstocker.service;

import com.webstocker.domain.Ligneobjectifvente;

import java.util.List;

/**
 * Service Interface for managing Ligneobjectifvente.
 */
public interface LigneobjectifventeService {

    /**
     * Save a ligneobjectifvente.
     * 
     * @param ligneobjectifvente the entity to save
     * @return the persisted entity
     */
    Ligneobjectifvente save(Ligneobjectifvente ligneobjectifvente);

    /**
     *  Get all the ligneobjectifventes.
     *  
     *  @return the list of entities
     */
    List<Ligneobjectifvente> findAll();

    /**
     *  Get the "id" ligneobjectifvente.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Ligneobjectifvente findOne(Long id);

    /**
     *  Delete the "id" ligneobjectifvente.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ligneobjectifvente corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Ligneobjectifvente> search(String query);
}
