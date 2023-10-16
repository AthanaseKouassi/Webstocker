package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Commune;
import com.webstocker.service.CommuneService;
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
 * REST controller for managing Commune.
 */
@RestController
@RequestMapping("/api")
public class CommuneResource {

    private final Logger log = LoggerFactory.getLogger(CommuneResource.class);
        
    @Inject
    private CommuneService communeService;
    
    /**
     * POST  /communes : Create a new commune.
     *
     * @param commune the commune to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commune, or with status 400 (Bad Request) if the commune has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/communes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commune> createCommune(@Valid @RequestBody Commune commune) throws URISyntaxException {
        log.debug("REST request to save Commune : {}", commune);
        if (commune.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commune", "idexists", "A new commune cannot already have an ID")).body(null);
        }
        Commune result = communeService.save(commune);
        return ResponseEntity.created(new URI("/api/communes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commune", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /communes : Updates an existing commune.
     *
     * @param commune the commune to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commune,
     * or with status 400 (Bad Request) if the commune is not valid,
     * or with status 500 (Internal Server Error) if the commune couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/communes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commune> updateCommune(@Valid @RequestBody Commune commune) throws URISyntaxException {
        log.debug("REST request to update Commune : {}", commune);
        if (commune.getId() == null) {
            return createCommune(commune);
        }
        Commune result = communeService.save(commune);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commune", commune.getId().toString()))
            .body(result);
    }

    /**
     * GET  /communes : get all the communes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of communes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/communes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Commune>> getAllCommunes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Communes");
        Page<Commune> page = communeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/communes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /communes/:id : get the "id" commune.
     *
     * @param id the id of the commune to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commune, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/communes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commune> getCommune(@PathVariable Long id) {
        log.debug("REST request to get Commune : {}", id);
        Commune commune = communeService.findOne(id);
        return Optional.ofNullable(commune)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /communes/:id : delete the "id" commune.
     *
     * @param id the id of the commune to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/communes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommune(@PathVariable Long id) {
        log.debug("REST request to delete Commune : {}", id);
        communeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commune", id.toString())).build();
    }

    /**
     * SEARCH  /_search/communes?query=:query : search for the commune corresponding
     * to the query.
     *
     * @param query the query of the commune search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/communes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Commune>> searchCommunes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Communes for query {}", query);
        Page<Commune> page = communeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/communes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/communes/trouverCommuneparnom/{nomcommune}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commune> getCommune(@PathVariable String nomcommune) {
        log.debug("REST request to get Commune : {}", nomcommune);
        Commune commune = null;
             try{
               commune = communeService.findByLibelle(nomcommune);
        return Optional.ofNullable(commune)
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
