package com.webstocker.service;

import com.webstocker.domain.Bailleur;

import java.util.List;

/**
 * Service Interface for managing Bailleur.
 */
public interface BailleurService {

    /**
     * Save a bailleur.
     * 
     * @param bailleur the entity to save
     * @return the persisted entity
     */
    Bailleur save(Bailleur bailleur);

    /**
     *  Get all the bailleurs.
     *  
     *  @return the list of entities
     */
    List<Bailleur> findAll();

    /**
     *  Get the "id" bailleur.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Bailleur findOne(Long id);

    /**
     *  Delete the "id" bailleur.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the bailleur corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Bailleur> search(String query);
   
   /**
    * avoir un bailleur à partir de son nom
    * @param nomBailleur
    * @return entity
    */   
    Bailleur findByNomBailleur(String nomBailleur);
}
