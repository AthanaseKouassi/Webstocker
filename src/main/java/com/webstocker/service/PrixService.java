package com.webstocker.service;

import com.webstocker.domain.Client;
import com.webstocker.domain.Prix;
import com.webstocker.domain.Produit;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service Interface for managing Prix.
 */
public interface PrixService {

    /**
     * Save a prix.
     *
     * @param prix the entity to save
     * @return the persisted entity
     */
    Prix save(Prix prix);

    /**
     *  Get all the prixes.
     *
     *  @return the list of entities
     */
    List<Prix> findAll();

    /**
     *  Get the "id" prix.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Prix findOne(Long id);

    /**
     *  Delete the "id" prix.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the prix corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @return the list of entities
     */
    List<Prix> search(String query);

    /**
     *
     * @param client
     * @param produit
     * @return
     */
    BigDecimal findPrixClientParProduit(Client client, Produit produit);
    
    BigDecimal findPrixClientParProduitValide(Client client, Produit produit);
    
    
}
