package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Ville;
import com.webstocker.service.VilleService;
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
import javax.persistence.NonUniqueResultException;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * REST controller for managing Ville.
 */
@RestController
@RequestMapping("/api")
public class VilleResource {

    private final Logger log = LoggerFactory.getLogger(VilleResource.class);
        
    @Inject
    private VilleService villeService;
    
    /**
     * POST  /villes : Create a new ville.
     *
     * @param ville the ville to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ville, or with status 400 (Bad Request) if the ville has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/villes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ville> createVille(@Valid @RequestBody Ville ville) throws URISyntaxException {
        log.debug("REST request to save Ville : {}", ville);
        if (ville.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ville", "idexists", "A new ville cannot already have an ID")).body(null);
        }
        Ville result = villeService.save(ville);
        return ResponseEntity.created(new URI("/api/villes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ville", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /villes : Updates an existing ville.
     *
     * @param ville the ville to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ville,
     * or with status 400 (Bad Request) if the ville is not valid,
     * or with status 500 (Internal Server Error) if the ville couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/villes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ville> updateVille(@Valid @RequestBody Ville ville) throws URISyntaxException {
        log.debug("REST request to update Ville : {}", ville);
        if (ville.getId() == null) {
            return createVille(ville);
        }
        Ville result = villeService.save(ville);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ville", ville.getId().toString()))
            .body(result);
    }

    /**
     * GET  /villes : get all the villes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of villes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/villes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ville>> getAllVilles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Villes");
        Page<Ville> page = villeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/villes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /villes/:id : get the "id" ville.
     *
     * @param id the id of the ville to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ville, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/villes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ville> getVille(@PathVariable Long id) {
        log.debug("REST request to get Ville : {}", id);
        Ville ville = villeService.findOne(id);
        return Optional.ofNullable(ville)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /villes/:id : delete the "id" ville.
     *
     * @param id the id of the ville to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/villes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVille(@PathVariable Long id) {
        log.debug("REST request to delete Ville : {}", id);
        villeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ville", id.toString())).build();
    }

    /**
     * SEARCH  /_search/villes?query=:query : search for the ville corresponding
     * to the query.
     *
     * @param query the query of the ville search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/villes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ville>> searchVilles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Villes for query {}", query);
        Page<Ville> page = villeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/villes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/villes/villeTrouverParNom/{nomville}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ville> getLocaliteParNom(@PathVariable String nomville) {
        log.debug("REST request to get Region : {}", nomville);
        Ville ville = null;
        try{
        ville = villeService.findByLibelle(nomville);
        return Optional.ofNullable(ville)
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
