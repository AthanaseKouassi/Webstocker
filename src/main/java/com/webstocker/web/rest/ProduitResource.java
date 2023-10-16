package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Produit;
import com.webstocker.service.ProduitService;
import com.webstocker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.persistence.NonUniqueResultException;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * REST controller for managing Produit.
 */
@RestController
@RequestMapping("/api")
public class ProduitResource {

    private final Logger log = LoggerFactory.getLogger(ProduitResource.class);

    @Inject
    private ProduitService produitService;

    /**
     * POST /produits : Create a new produit.
     *
     * @param produit the produit to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new produit, or with status 400 (Bad Request) if the produit has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/produits",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produit> createProduit(@Valid @RequestBody Produit produit) throws URISyntaxException {
        log.debug("REST request to save Produit : {}", produit);
        if (produit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("produit", "idexists", "A new produit cannot already have an ID")).body(null);
        }
        Produit result = produitService.save(produit);
        return ResponseEntity.created(new URI("/api/produits/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("produit", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /produits : Updates an existing produit.
     *
     * @param produit the produit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * produit, or with status 400 (Bad Request) if the produit is not valid, or
     * with status 500 (Internal Server Error) if the produit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/produits",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produit> updateProduit(@Valid @RequestBody Produit produit) throws URISyntaxException {
        log.debug("REST request to update Produit : {}", produit);
        if (produit.getId() == null) {
            return createProduit(produit);
        }
        Produit result = produitService.save(produit);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("produit", produit.getId().toString()))
                .body(result);
    }

    /**
     * GET /produits : get all the produits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of produits
     * in body
     */
    @RequestMapping(value = "/produits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Produit> getAllProduits() {
        log.debug("REST request to get all Produits");
        return produitService.findAll();
    }

    /**
     * GET /produits/:id : get the "id" produit.
     *
     * @param id the id of the produit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * produit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/produits/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produit> getProduit(@PathVariable Long id) {
        log.debug("REST request to get Produit : {}", id);
        Produit produit = produitService.findOne(id);
        return Optional.ofNullable(produit)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /produits/:id : delete the "id" produit.
     *
     * @param id the id of the produit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/produits/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        log.debug("REST request to delete Produit : {}", id);
        produitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("produit", id.toString())).build();
    }

    /**
     * SEARCH /_search/produits?query=:query : search for the produit
     * corresponding to the query.
     *
     * @param query the query of the produit search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/produits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Produit> searchProduits(@RequestParam String query) {
        log.debug("REST request to search Produits for query {}", query);
        return produitService.search(query);
    }

    /**
     *
     * @param nomproduit
     * @return
     */
    @RequestMapping(value = "/produits/produitsparNomProduit/{nomproduit}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produit> trouverProduitParNom(@PathVariable String nomproduit) {
        log.debug("REST request to get Produit by nomProduit: {}", nomproduit);
        Produit produit = null;
        try {
            produit = produitService.findByNomProduit(nomproduit);
            return Optional.ofNullable(produit)
                    .map(result -> new ResponseEntity<>(
                            result,
                            HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IncorrectResultSizeDataAccessException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        }
    }

}
