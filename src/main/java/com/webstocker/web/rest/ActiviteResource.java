package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Activite;
import com.webstocker.service.ActiviteService;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Activite.
 */
@RestController
@RequestMapping("/api")
public class ActiviteResource {

    private final Logger log = LoggerFactory.getLogger(ActiviteResource.class);
        
    @Inject
    private ActiviteService activiteService;
    
    /**
     * POST  /activites : Create a new activite.
     *
     * @param activite the activite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activite, or with status 400 (Bad Request) if the activite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activite> createActivite(@Valid @RequestBody Activite activite) throws URISyntaxException {
        log.debug("REST request to save Activite : {}", activite);
        if (activite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("activite", "idexists", "A new activite cannot already have an ID")).body(null);
        }
        Activite result = activiteService.save(activite);
        return ResponseEntity.created(new URI("/api/activites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("activite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activites : Updates an existing activite.
     *
     * @param activite the activite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activite,
     * or with status 400 (Bad Request) if the activite is not valid,
     * or with status 500 (Internal Server Error) if the activite couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activite> updateActivite(@Valid @RequestBody Activite activite) throws URISyntaxException {
        log.debug("REST request to update Activite : {}", activite);
        if (activite.getId() == null) {
            return createActivite(activite);
        }
        Activite result = activiteService.save(activite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("activite", activite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activites : get all the activites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activites in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/activites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Activite>> getAllActivites(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Activites");
        Page<Activite> page = activiteService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activites/:id : get the "id" activite.
     *
     * @param id the id of the activite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activite, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/activites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activite> getActivite(@PathVariable Long id) {
        log.debug("REST request to get Activite : {}", id);
        Activite activite = activiteService.findOne(id);
        return Optional.ofNullable(activite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activites/:id : delete the "id" activite.
     *
     * @param id the id of the activite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/activites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActivite(@PathVariable Long id) {
        log.debug("REST request to delete Activite : {}", id);
        activiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activite", id.toString())).build();
    }

    /**
     * SEARCH  /_search/activites?query=:query : search for the activite corresponding
     * to the query.
     *
     * @param query the query of the activite search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/activites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Activite>> searchActivites(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Activites for query {}", query);
        Page<Activite> page = activiteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/activites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
