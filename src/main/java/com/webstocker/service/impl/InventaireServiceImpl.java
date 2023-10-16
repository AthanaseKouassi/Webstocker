package com.webstocker.service.impl;

import com.webstocker.service.InventaireService;
import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.repository.search.InventaireSearchRepository;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Inventaire.
 */
@Service
@Transactional
public class InventaireServiceImpl implements InventaireService {
    
    private final Logger log = LoggerFactory.getLogger(InventaireServiceImpl.class);

    @Inject
    private InventaireRepository inventaireRepository;

    @Inject
    private InventaireSearchRepository inventaireSearchRepository; 

    @Inject
    private MagasinRepository magasinRepository;
    
    @Inject
    private ProduitRepository produitRepository;

    /**
     * Save a inventaire.
     *
     * @param inventaire the entity to save
     * @return the persisted entity
     */
    @Override
    public Inventaire save(Inventaire inventaire) {
        log.debug("Request to save Inventaire : {}", inventaire);
        Inventaire result = inventaireRepository.save(inventaire);
//        inventaireSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the inventaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Inventaire> findAll(Pageable pageable) {
        log.debug("Request to get all Inventaires");
        Page<Inventaire> result = inventaireRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one inventaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Inventaire findOne(Long id) {
        log.debug("Request to get Inventaire : {}", id);
        Inventaire inventaire = inventaireRepository.findOne(id);
        return inventaire;
    }

    /**
     * Delete the inventaire by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inventaire : {}", id);
        inventaireRepository.delete(id);
//        inventaireSearchRepository.delete(id);
    }

    /**
     * Search for the inventaire corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Inventaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Inventaires for query {}", query);
        Page<Inventaire> result = inventaireSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

//    @Override
//    public Page<Inventaire> findAllOrderByIdDesc(Pageable pageable) {
//        log.debug("Request for a page of Inventaires  {}");
//        Page<Inventaire> result = inventaireRepository.findAllDesc(pageable);
//        return result;
//    }

    /**
     * rechercher l'inventaire d'un mois d'un magasin
     *
     * @param nomMagasin
     * @param dateDebut
     * @param dateFin
     * @return
     */
    @Override
    public Page<Inventaire> findByMagasinAndDateInventaireBetween(String nomMagasin, String dateDuMois, Pageable pageable) {

        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);
        String dateDebutPeriode = null;
        String dateFinPeriode = null;

        //Classe retournant la première et la dernière date du mois de la date données en paramètre : dateInventaire
        PremierEtDernierJourDuMois madateInventaire = new PremierEtDernierJourDuMois();
        dateDebutPeriode = madateInventaire.getDateDebutDuMois(dateDuMois);
        dateFinPeriode = madateInventaire.getDateFinDuMois(dateDuMois);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebutPeriode, formatter);
        LocalDate fin = LocalDate.parse(dateFinPeriode, formatter);
        

        Page<Inventaire> result = inventaireRepository.findByMagasinAndDateInventaireBetween(magasin, debut, fin, pageable);

        return result;
    }

//    @Override
//    public Inventaire retrouverUnInventaireParProduitEtMagasin(String nomProduit, String nomMagasin, String dateDuMois) {

//       String dateRecherche = null;
//       
//       //Classe retournant la première et la dernière date du mois de la date données en paramètre : dateInventaire
//        PremierEtDernierJourDuMois madateInventaire = new PremierEtDernierJourDuMois();
//        dateRecherche = madateInventaire.getDateDebutDuMois(dateDuMois);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate laDate = LocalDate.parse(dateRecherche, formatter);        
//        
//        laDate.getMonthValue();
//        
//       
//       
//       return null;
//    }

    @Override
    public Inventaire retrouverUnInventaireParProduitEtMagasin(String nomProduit, String nomMagasin) {
        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);
        Produit produit = produitRepository.findByNomProduit(nomProduit);
        
        Inventaire inventaire = inventaireRepository.findByProduitAndMagasin(produit, magasin);
        return inventaire;
    }

}
