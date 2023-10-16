package com.webstocker.service;

import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignecommande;

import java.util.List;

/**
 * Service Interface for managing Lignecommande.
 */
public interface LignecommandeService {

    /**
     * Save a lignecommande.
     *
     * @param lignecommande the entity to save
     * @return the persisted entity
     */
    Lignecommande save(Lignecommande lignecommande);

    /**
     *  Get all the lignecommandes.
     *
     *  @return the list of entities
     */
    List<Lignecommande> findAll();

    /**
     *  Get the "id" lignecommande.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Lignecommande findOne(Long id);

    /**
     *  Delete the "id" lignecommande.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lignecommande corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Lignecommande> search(String query);

    List<Lignecommande> findByCommande(Commande commande);
}
