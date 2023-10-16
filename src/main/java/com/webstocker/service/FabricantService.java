package com.webstocker.service;

import com.webstocker.domain.Fabricant;
import com.webstocker.domain.Produit;

import java.util.List;

/**
 * Service Interface for managing Fabricant.
 */
public interface FabricantService {

    /**
     * Save a fabricant.
     * 
     * @param fabricant the entity to save
     * @return the persisted entity
     */
    Fabricant save(Fabricant fabricant);

    /**
     *  Get all the fabricants.
     *  
     *  @return the list of entities
     */
    List<Fabricant> findAll();

    /**
     *  Get the "id" fabricant.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Fabricant findOne(Long id);

    /**
     *  Delete the "id" fabricant.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the fabricant corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Fabricant> search(String query);

    List<Fabricant> findAllByProduit(Long  idProduit);
}
