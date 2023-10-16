package com.webstocker.service;

import com.webstocker.domain.TypeActivite;

import java.util.List;

/**
 * Service Interface for managing TypeActivite.
 */
public interface TypeActiviteService {

    /**
     * Save a typeActivite.
     * 
     * @param typeActivite the entity to save
     * @return the persisted entity
     */
    TypeActivite save(TypeActivite typeActivite);

    /**
     *  Get all the typeActivites.
     *  
     *  @return the list of entities
     */
    List<TypeActivite> findAll();

    /**
     *  Get the "id" typeActivite.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    TypeActivite findOne(Long id);

    /**
     *  Delete the "id" typeActivite.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the typeActivite corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<TypeActivite> search(String query);
}
