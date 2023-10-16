package com.webstocker.service.impl;

import com.webstocker.domain.Client;
import com.webstocker.domain.Produit;
import com.webstocker.service.PrixService;
import com.webstocker.domain.Prix;
import com.webstocker.repository.PrixRepository;
import com.webstocker.repository.search.PrixSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Prix.
 */
@Service
@Transactional
public class PrixServiceImpl implements PrixService{

    private final Logger log = LoggerFactory.getLogger(PrixServiceImpl.class);

    private final PrixRepository prixRepository;

    private final PrixSearchRepository prixSearchRepository;

    @Autowired
    public PrixServiceImpl(PrixRepository prixRepository, PrixSearchRepository prixSearchRepository) {
        this.prixRepository = prixRepository;
        this.prixSearchRepository = prixSearchRepository;
    }

    /**
     * Save a prix.
     *
     * @param prix the entity to save
     * @return the persisted entity
     */
    @Override
    public Prix save(Prix prix) {
        log.debug("Request to save Prix : {}", prix);
        Prix result = prixRepository.save(prix);
//        prixSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the prixes.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Prix> findAll() {
        log.debug("Request to get all Prixes");
        List<Prix> result = prixRepository.findAll();

        return result;
    }

    /**
     *  Get one prix by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Prix findOne(Long id) {
        log.debug("Request to get Prix : {}", id);
        Prix prix = prixRepository.findOne(id);
        return prix;
    }

    /**
     *  Delete the  prix by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prix : {}", id);
        prixRepository.delete(id);
//        prixSearchRepository.delete(id);
    }

    /**
     * Search for the prix corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Prix> search(String query) {
        log.debug("Request to search Prixes for query {}", query);
        return StreamSupport
            .stream(prixSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal findPrixClientParProduit(Client client, Produit produit) {
        log.debug("Request to search Prixes for client {}", client);
        Prix prix = prixRepository.findByProduitAndCategorieclient(produit, client.getCategorieclient());
        if (prix.getPrixUnitaire()== null){
            System.out.println("Le prix ne peut être null");
        }else{
            prix.getPrixUnitaire();
        }
        return prix.getPrixUnitaire();
    }
    
    @Override
    public BigDecimal findPrixClientParProduitValide(Client client, Produit produit) {
        log.debug("Request to search Prixes for client {}", client);
//        Prix prix = prixRepository.findByProduitAndCategorieclient(produit, client.getCategorieclient());
        Prix prix = prixRepository.findByProduitAndCategorieclientAndActif(produit, client.getCategorieclient(),true);
      
        try{
        if (prix.getPrixUnitaire() == null){
            System.out.println("Le prix ne peut être null"); 
            /**
             * Cette ligne permet de fixer le prix unitaire à en cas ou la valeur est null
             */
            prix.setPrixUnitaire(BigDecimal.ZERO);
        }else{
            prix.getPrixUnitaire();
        }     
        }catch(NullPointerException ex){
            ex.printStackTrace();
//            ex.getMessage();
        }
        return prix.getPrixUnitaire();
    }
}
