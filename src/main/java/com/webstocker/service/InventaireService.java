package com.webstocker.service;

import com.webstocker.domain.Inventaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Inventaire.
 */
public interface InventaireService {

    /**
     * Save a inventaire.
     *
     * @param inventaire the entity to save
     * @return the persisted entity
     */
    Inventaire save(Inventaire inventaire);

    /**
     * Get all the inventaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Inventaire> findAll(Pageable pageable);

    /**
     * Get the "id" inventaire.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Inventaire findOne(Long id);

    /**
     * Delete the "id" inventaire.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the inventaire corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Inventaire> search(String query, Pageable pageable);

    Page<Inventaire> findByMagasinAndDateInventaireBetween(String nomMagasin, String dateDuMois, Pageable pageable);

    Inventaire retrouverUnInventaireParProduitEtMagasin(String nomProduit, String nomMagasin);

    Page<Inventaire> getDateInventaireBetween(String dateDuMois, Pageable pageable);

    Inventaire create(Inventaire inventaire);

}
