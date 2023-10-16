package com.webstocker.service.impl;

import com.webstocker.service.ProduitService;
import com.webstocker.domain.Produit;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.repository.search.ProduitSearchRepository;
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
 * Service Implementation for managing Produit.
 */
@Service
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final Logger log = LoggerFactory.getLogger(ProduitServiceImpl.class);

    @Inject
    private ProduitRepository produitRepository;

    @Inject
    private ProduitSearchRepository produitSearchRepository;

    /**
     * Save a produit.
     *
     * @param produit the entity to save
     * @return the persisted entity
     */
    public Produit save(Produit produit) {
        log.debug("Request to save Produit : {}", produit);
        Produit result = produitRepository.save(produit);
        produitSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the produits.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Produit> findAll() {
        log.debug("Request to get all Produits");
        List<Produit> result = produitRepository.findAll();
        return result;
    }

    /**
     * Get one produit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Produit findOne(Long id) {
        log.debug("Request to get Produit : {}", id);
        Produit produit = produitRepository.findOne(id);
        return produit;
    }

    /**
     * Delete the produit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Produit : {}", id);
        produitRepository.delete(id);
        produitSearchRepository.delete(id);
    }

    /**
     * Search for the produit corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Produit> search(String query) {
        log.debug("Request to search Produits for query {}", query);
        return StreamSupport
                .stream(produitSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Produit findByNomProduit(String nomproduit) {
        Produit result = null;
        String nomProduit = null;
        nomProduit = nomproduit;
        return result = produitRepository.findByNomProduit(nomProduit);
    }
}
