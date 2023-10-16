package com.webstocker.service;

import com.webstocker.domain.Objectifs;
import com.webstocker.domain.Produit;
import java.time.LocalDate;

import java.util.List;

/**
 * Service Interface for managing Objectifs.
 */
public interface ObjectifsService {

    /**
     * Save a objectifs.
     * 
     * @param objectifs the entity to save
     * @return the persisted entity
     */
    Objectifs save(Objectifs objectifs);

    /**
     *  Get all the objectifs.
     *  
     *  @return the list of entities
     */
    List<Objectifs> findAll();

    /**
     *  Get the "id" objectifs.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Objectifs findOne(Long id);

    /**
     *  Delete the "id" objectifs.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the objectifs corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Objectifs> search(String query);
    
    List<Objectifs> lesObjectifsAMettreAjour();
    
    List<Objectifs> lesTauxAtteinteObjectifs();
    
    List<Objectifs> tauxAtteinteObjectifsParMoisEtParProduit();
    
    List<Objectifs> tauxAtteinteObjectifsParProduitPourUnMois(String maDate);
    
    List<Objectifs> miseaJourTauxAtteinte(int quantiteVendue);
    
    List<Objectifs> getObjectifsDuMois(String madate);
    
    List<Objectifs>getMiseAJourDesTaux(String madate);
    
    
}
