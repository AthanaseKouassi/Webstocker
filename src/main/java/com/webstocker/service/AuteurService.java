package com.webstocker.service;

import com.webstocker.domain.Auteur;

import java.util.List;

/**
 * Service Interface for managing Auteur.
 */
public interface AuteurService {

    /**
     * Save a auteur.
     * 
     * @param auteur the entity to save
     * @return the persisted entity
     */
    Auteur save(Auteur auteur);

    /**
     *  Get all the auteurs.
     *  
     *  @return the list of entities
     */
    List<Auteur> findAll();

    /**
     *  Get the "id" auteur.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Auteur findOne(Long id);

    /**
     *  Delete the "id" auteur.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the auteur corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Auteur> search(String query);
}
