package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Ligneprixproduit;
import com.webstocker.service.LigneprixproduitService;
import com.webstocker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Ligneprixproduit.
 */
@RestController
@RequestMapping("/api")
public class LigneprixproduitResource {

    private final Logger log = LoggerFactory.getLogger(LigneprixproduitResource.class);
        
    @Inject
    private LigneprixproduitService ligneprixproduitService;
    
    /**
     * POST  /ligneprixproduits : Create a new ligneprixproduit.
     *
     * @param ligneprixproduit the ligneprixproduit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneprixproduit, or with status 400 (Bad Request) if the ligneprixproduit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligneprixproduits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ligneprixproduit> createLigneprixproduit(@RequestBody Ligneprixproduit ligneprixproduit) throws URISyntaxException {
        log.debug("REST request to save Ligneprixproduit : {}", ligneprixproduit);
        if (ligneprixproduit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ligneprixproduit", "idexists", "A new ligneprixproduit cannot already have an ID")).body(null);
        }
        Ligneprixproduit result = ligneprixproduitService.save(ligneprixproduit);
        return ResponseEntity.created(new URI("/api/ligneprixproduits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ligneprixproduit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligneprixproduits : Updates an existing ligneprixproduit.
     *
     * @param ligneprixproduit the ligneprixproduit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneprixproduit,
     * or with status 400 (Bad Request) if the ligneprixproduit is not valid,
     * or with status 500 (Internal Server Error) if the ligneprixproduit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligneprixproduits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ligneprixproduit> updateLigneprixproduit(@RequestBody Ligneprixproduit ligneprixproduit) throws URISyntaxException {
        log.debug("REST request to update Ligneprixproduit : {}", ligneprixproduit);
        if (ligneprixproduit.getId() == null) {
            return createLigneprixproduit(ligneprixproduit);
        }
        Ligneprixproduit result = ligneprixproduitService.save(ligneprixproduit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ligneprixproduit", ligneprixproduit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligneprixproduits : get all the ligneprixproduits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ligneprixproduits in body
     */
    @RequestMapping(value = "/ligneprixproduits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ligneprixproduit> getAllLigneprixproduits() {
        log.debug("REST request to get all Ligneprixproduits");
        return ligneprixproduitService.findAll();
    }

    /**
     * GET  /ligneprixproduits/:id : get the "id" ligneprixproduit.
     *
     * @param id the id of the ligneprixproduit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneprixproduit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ligneprixproduits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ligneprixproduit> getLigneprixproduit(@PathVariable Long id) {
        log.debug("REST request to get Ligneprixproduit : {}", id);
        Ligneprixproduit ligneprixproduit = ligneprixproduitService.findOne(id);
        return Optional.ofNullable(ligneprixproduit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ligneprixproduits/:id : delete the "id" ligneprixproduit.
     *
     * @param id the id of the ligneprixproduit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ligneprixproduits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLigneprixproduit(@PathVariable Long id) {
        log.debug("REST request to delete Ligneprixproduit : {}", id);
        ligneprixproduitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ligneprixproduit", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligneprixproduits?query=:query : search for the ligneprixproduit corresponding
     * to the query.
     *
     * @param query the query of the ligneprixproduit search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ligneprixproduits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ligneprixproduit> searchLigneprixproduits(@RequestParam String query) {
        log.debug("REST request to search Ligneprixproduits for query {}", query);
        return ligneprixproduitService.search(query);
    }

}
