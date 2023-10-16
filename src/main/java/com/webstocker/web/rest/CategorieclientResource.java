package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Categorieclient;
import com.webstocker.service.CategorieclientService;
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
 * REST controller for managing Categorieclient.
 */
@RestController
@RequestMapping("/api")
public class CategorieclientResource {

    private final Logger log = LoggerFactory.getLogger(CategorieclientResource.class);
        
    @Inject
    private CategorieclientService categorieclientService;
    
    /**
     * POST  /categorieclients : Create a new categorieclient.
     *
     * @param categorieclient the categorieclient to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categorieclient, or with status 400 (Bad Request) if the categorieclient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categorieclients",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorieclient> createCategorieclient(@Valid @RequestBody Categorieclient categorieclient) throws URISyntaxException {
        log.debug("REST request to save Categorieclient : {}", categorieclient);
        if (categorieclient.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("categorieclient", "idexists", "A new categorieclient cannot already have an ID")).body(null);
        }
        Categorieclient result = categorieclientService.save(categorieclient);
        return ResponseEntity.created(new URI("/api/categorieclients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categorieclient", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorieclients : Updates an existing categorieclient.
     *
     * @param categorieclient the categorieclient to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categorieclient,
     * or with status 400 (Bad Request) if the categorieclient is not valid,
     * or with status 500 (Internal Server Error) if the categorieclient couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categorieclients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorieclient> updateCategorieclient(@Valid @RequestBody Categorieclient categorieclient) throws URISyntaxException {
        log.debug("REST request to update Categorieclient : {}", categorieclient);
        if (categorieclient.getId() == null) {
            return createCategorieclient(categorieclient);
        }
        Categorieclient result = categorieclientService.save(categorieclient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categorieclient", categorieclient.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorieclients : get all the categorieclients.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categorieclients in body
     */
    @RequestMapping(value = "/categorieclients",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Categorieclient> getAllCategorieclients() {
        log.debug("REST request to get all Categorieclients");
        return categorieclientService.findAll();
    }

    /**
     * GET  /categorieclients/:id : get the "id" categorieclient.
     *
     * @param id the id of the categorieclient to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categorieclient, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/categorieclients/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorieclient> getCategorieclient(@PathVariable Long id) {
        log.debug("REST request to get Categorieclient : {}", id);
        Categorieclient categorieclient = categorieclientService.findOne(id);
        return Optional.ofNullable(categorieclient)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categorieclients/:id : delete the "id" categorieclient.
     *
     * @param id the id of the categorieclient to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/categorieclients/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategorieclient(@PathVariable Long id) {
        log.debug("REST request to delete Categorieclient : {}", id);
        categorieclientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categorieclient", id.toString())).build();
    }

    /**
     * SEARCH  /_search/categorieclients?query=:query : search for the categorieclient corresponding
     * to the query.
     *
     * @param query the query of the categorieclient search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/categorieclients",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Categorieclient> searchCategorieclients(@RequestParam String query) {
        log.debug("REST request to search Categorieclients for query {}", query);
        return categorieclientService.search(query);
    }

    @RequestMapping(value = "/categorieclients/typeClient/{libellecategorieclient}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorieclient> typeClientParNom(@PathVariable String libellecategorieclient) {
        log.debug("REST request to get Categorie Client by nomCategorie : {}", libellecategorieclient);
         Categorieclient categorieclient = null;
        try {
           categorieclient = categorieclientService.findByLibelleCategorieClient(libellecategorieclient);
            return Optional.ofNullable(categorieclient)
                    .map(result -> new ResponseEntity<>(
                            result,
                            HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        } catch (IncorrectResultSizeDataAccessException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            // Code for handling NonUniqueResultException
            return null;
        }

    }
}
