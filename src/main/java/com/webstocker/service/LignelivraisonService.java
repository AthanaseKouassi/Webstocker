package com.webstocker.service;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;
import com.webstocker.domain.Produit;

import java.util.List;

/**
 * Service Interface for managing Lignelivraison.
 */
public interface LignelivraisonService {

    /**
     * Save a lignelivraison.
     * 
     * @param lignelivraison the entity to save
     * @return the persisted entity
     */
    Lignelivraison save(Lignelivraison lignelivraison);

    /**
     *  Get all the lignelivraisons.
     *  
     *  @return the list of entities
     */
    List<Lignelivraison> findAll();

    /**
     *  Get the "id" lignelivraison.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Lignelivraison findOne(Long id);

    /**
     *  Delete the "id" lignelivraison.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lignelivraison corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    List<Lignelivraison> search(String query);
    
    List<Lignelivraison> recupererLignesLivraison(Livraison livraison);
    
    List<Lignelivraison> getQuantiteProduitLivreMois(String dateDebutMois, String dateFinMois);
    
    List<Lignelivraison> getLivraisonParMois(String madate);
    
    List<Lignelivraison> getLivraisonDuBailleurParPeriode(String nomBailleur, String dateDebut, String dateFin);
   
  // List<Lignelivraison> getLivraisonDuBailleurParPeriode(Bailleur bailleur,Produit produit, String dateDebut, String dateFin);
    
    List<Lignelivraison> getLivraisonParBailleur(Bailleur bailleur, String dateDebut, String dateFin);
    
    List<Lignelivraison> getTouteLesQuantititeLivreBailleur(Bailleur bailleur);
}
