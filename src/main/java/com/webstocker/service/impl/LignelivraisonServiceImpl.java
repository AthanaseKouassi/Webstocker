package com.webstocker.service.impl;

import com.webstocker.domain.Bailleur;
import com.webstocker.service.LignelivraisonService;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;
import com.webstocker.domain.Produit;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.search.LignelivraisonSearchRepository;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Lignelivraison.
 */
@Service
@Transactional
public class LignelivraisonServiceImpl implements LignelivraisonService{

    private final Logger log = LoggerFactory.getLogger(LignelivraisonServiceImpl.class);
    
    @Inject
    private LignelivraisonRepository lignelivraisonRepository;
    
    @Inject
    private LignelivraisonSearchRepository lignelivraisonSearchRepository;
    
    /**
     * Save a lignelivraison.
     * 
     * @param lignelivraison the entity to save
     * @return the persisted entity
     */
    public Lignelivraison save(Lignelivraison lignelivraison) {
        log.debug("Request to save Lignelivraison : {}", lignelivraison);
        Lignelivraison result = lignelivraisonRepository.save(lignelivraison);
        lignelivraisonSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the lignelivraisons.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Lignelivraison> findAll() {
        log.debug("Request to get all Lignelivraisons");
        List<Lignelivraison> result = lignelivraisonRepository.findAll();
        return result;
    }

    /**
     *  Get one lignelivraison by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Lignelivraison findOne(Long id) {
        log.debug("Request to get Lignelivraison : {}", id);
        Lignelivraison lignelivraison = lignelivraisonRepository.findOne(id);
        return lignelivraison;
    }

    /**
     *  Delete the  lignelivraison by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Lignelivraison : {}", id);
        lignelivraisonRepository.delete(id);
        lignelivraisonSearchRepository.delete(id);
    }

    /**
     * Search for the lignelivraison corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Lignelivraison> search(String query) {
        log.debug("Request to search Lignelivraisons for query {}", query);
        return StreamSupport
            .stream(lignelivraisonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Lignelivraison> recupererLignesLivraison(Livraison livraison) {
        // if(bonDeSortie.getTypeSortie()== TypeSortie.VENTE){
            return  lignelivraisonRepository.findAllByLivraison(livraison);
       // }
        //return null;
    }

    @Override
    public List<Lignelivraison> getQuantiteProduitLivreMois(String dateDebutMois, String dateFinMois) {
       return lignelivraisonRepository.quantiteProduitLivreParMois(dateDebutMois, dateFinMois);
    }

    @Override
    public List<Lignelivraison> getLivraisonParMois(String maDate) {
        String dateDebut;
        String dateFin;
        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        return lignelivraisonRepository.findByLivraisonDateLivraisonBetween(debut, fin);
    }

    @Override
    public List<Lignelivraison> getLivraisonDuBailleurParPeriode(String nomBailleur, String dateDebut, String dateFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        
        return lignelivraisonRepository.findByLivraisonCommandeBailleurNomBailleurAndLivraisonDateLivraisonBetween(nomBailleur, debut, fin);
    }

//    @Override
//    public List<Lignelivraison> getLivraisonDuBailleurParPeriode(Bailleur bailleur, Produit produit, String dateDebut, String dateFin) {
//        
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate debut = LocalDate.parse(dateDebut, formatter);
//        LocalDate fin = LocalDate.parse(dateFin, formatter);
//        
//        return lignelivraisonRepository.findByLivraisonCommandeBailleurAndLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(bailleur, produit, debut, fin);
//    }

    @Override
    public List<Lignelivraison> getLivraisonParBailleur(Bailleur bailleur, String dateDebut, String dateFin) {
        
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        
        return lignelivraisonRepository.findByLivraisonCommandeBailleurAndLivraisonDateLivraisonBetween(bailleur, debut, fin);
    }

    @Override
    public List<Lignelivraison> getTouteLesQuantititeLivreBailleur(Bailleur bailleur) {
        return lignelivraisonRepository.findAllByLivraisonCommandeBailleur(bailleur);
    }
    
}
