package com.webstocker.service;

import com.webstocker.domain.Livraison;
import java.time.LocalDate;

import java.util.List;

/**
 * Service Interface for managing Livraison.
 */
public interface LivraisonService {

    /**
     * Save a livraison.
     * 
     * @param livraison the entity to save
     * @return the persisted entity
     */
    Livraison save(Livraison livraison);

    /**
     *  Get all the livraisons.
     *  
     *  @return the list of entities
     */
    List<Livraison> findAll();

    /**
     *  Get the "id" livraison.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Livraison findOne(Long id);

    /**
     *  Delete the "id" livraison.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the livraison corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Livraison> search(String query);
    
    List<Livraison> listeDesLivraisonParDate(String dateDebut, String dateFin);
}
