package com.webstocker.service;

import com.webstocker.domain.Produit;

import java.util.List;

/**
 * Service Interface for managing Produit.
 */
public interface ProduitService {

    /**
     * Save a produit.
     * 
     * @param produit the entity to save
     * @return the persisted entity
     */
    Produit save(Produit produit);

    /**
     *  Get all the produits.
     *  
     *  @return the list of entities
     */
    List<Produit> findAll();

    /**
     *  Get the "id" produit.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Produit findOne(Long id);

    /**
     *  Delete the "id" produit.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the produit corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Produit> search(String query);
    
    Produit findByNomProduit(String nomproduit);
}
