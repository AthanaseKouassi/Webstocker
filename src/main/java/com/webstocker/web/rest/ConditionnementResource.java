package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Conditionnement;
import com.webstocker.service.ConditionnementService;
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
 * REST controller for managing Conditionnement.
 */
@RestController
@RequestMapping("/api")
public class ConditionnementResource {

    private final Logger log = LoggerFactory.getLogger(ConditionnementResource.class);
        
    @Inject
    private ConditionnementService conditionnementService;
    
    /**
     * POST  /conditionnements : Create a new conditionnement.
     *
     * @param conditionnement the conditionnement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conditionnement, or with status 400 (Bad Request) if the conditionnement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/conditionnements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conditionnement> createConditionnement(@Valid @RequestBody Conditionnement conditionnement) throws URISyntaxException {
        log.debug("REST request to save Conditionnement : {}", conditionnement);
        if (conditionnement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("conditionnement", "idexists", "A new conditionnement cannot already have an ID")).body(null);
        }
        Conditionnement result = conditionnementService.save(conditionnement);
        return ResponseEntity.created(new URI("/api/conditionnements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("conditionnement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conditionnements : Updates an existing conditionnement.
     *
     * @param conditionnement the conditionnement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conditionnement,
     * or with status 400 (Bad Request) if the conditionnement is not valid,
     * or with status 500 (Internal Server Error) if the conditionnement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/conditionnements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conditionnement> updateConditionnement(@Valid @RequestBody Conditionnement conditionnement) throws URISyntaxException {
        log.debug("REST request to update Conditionnement : {}", conditionnement);
        if (conditionnement.getId() == null) {
            return createConditionnement(conditionnement);
        }
        Conditionnement result = conditionnementService.save(conditionnement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("conditionnement", conditionnement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conditionnements : get all the conditionnements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of conditionnements in body
     */
    @RequestMapping(value = "/conditionnements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Conditionnement> getAllConditionnements() {
        log.debug("REST request to get all Conditionnements");
        return conditionnementService.findAll();
    }

    /**
     * GET  /conditionnements/:id : get the "id" conditionnement.
     *
     * @param id the id of the conditionnement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conditionnement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/conditionnements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conditionnement> getConditionnement(@PathVariable Long id) {
        log.debug("REST request to get Conditionnement : {}", id);
        Conditionnement conditionnement = conditionnementService.findOne(id);
        return Optional.ofNullable(conditionnement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /conditionnements/:id : delete the "id" conditionnement.
     *
     * @param id the id of the conditionnement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/conditionnements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteConditionnement(@PathVariable Long id) {
        log.debug("REST request to delete Conditionnement : {}", id);
        conditionnementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("conditionnement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/conditionnements?query=:query : search for the conditionnement corresponding
     * to the query.
     *
     * @param query the query of the conditionnement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/conditionnements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Conditionnement> searchConditionnements(@RequestParam String query) {
        log.debug("REST request to search Conditionnements for query {}", query);
        return conditionnementService.search(query);
    }
    
     @RequestMapping(value = "/conditionnements/conditionnementParLibelle/{libelle}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conditionnement> getConditionnement(@PathVariable String libelle) {
        log.debug("REST request to get Conditionnement : {}", libelle);
        Conditionnement conditionnement= null;
        try{
        conditionnement = conditionnementService.findByLibelle(libelle);
        return Optional.ofNullable(conditionnement)
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
