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
import java.util.Optional;
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

        return Optional.ofNullable(prixRepository.findByProduitAndCategorieclient(produit, client.getCategorieclient()))
            .orElseGet(() -> {
                log.warn("Le prix pour le produit {} et le client {} est null. \n" +
                    "Fixer le prix unitaire à zéro.", produit.getId(), client.getId());
                Prix prix1 = new Prix();
                prix1.setPrixUnitaire(BigDecimal.ZERO);
                return prix1;
            }).getPrixUnitaire();

    }

    @Override
    public BigDecimal findPrixClientParProduitValide(Client client, Produit produit) {

        log.debug("Request to search Prixes for client {}", client);

        // Utilisation d'Optional pour éviter les NullPointerException
        return Optional.ofNullable(prixRepository.findByProduitAndCategorieclientAndActif(produit, client.getCategorieclient(), true))
            .orElseGet(() -> {
                log.warn("Le prix ne peut être null. Fixer le prix unitaire à zéro.");
                Prix prix1 = new Prix();
                 prix1.setPrixUnitaire(BigDecimal.ZERO);
                 return prix1;
            }).getPrixUnitaire();


    }
}
