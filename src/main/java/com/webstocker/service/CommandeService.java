package com.webstocker.service;

import com.webstocker.domain.Commande;
import com.webstocker.domain.enumeration.StatutCommande;

import java.util.List;

/**
 * Service Interface for managing Commande.
 */
public interface CommandeService {

    /**
     * Save a commande.
     *
     * @param commande the entity to save
     * @return the persisted entity
     */
    Commande save(Commande commande);

    /**
     *  Get all the commandes.
     *
     *  @return the list of entities
     */
    List<Commande> findAll();

    /**
     *  Get the "id" commande.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Commande findOne(Long id);

    /**
     *  Delete the "id" commande.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the commande corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Commande> search(String query);

    List<Commande> findActiveCommandes(StatutCommande statutCommande);

    Long quantiteRestanteAlivrer(Long commandeID);
}
