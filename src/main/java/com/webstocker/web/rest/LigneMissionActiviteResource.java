package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.LigneMissionActivite;
import com.webstocker.service.LigneMissionActiviteService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LigneMissionActivite.
 */
@RestController
@RequestMapping("/api")
public class LigneMissionActiviteResource {

    private final Logger log = LoggerFactory.getLogger(LigneMissionActiviteResource.class);
        
    @Inject
    private LigneMissionActiviteService ligneMissionActiviteService;
    
    /**
     * POST  /ligne-mission-activites : Create a new ligneMissionActivite.
     *
     * @param ligneMissionActivite the ligneMissionActivite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneMissionActivite, or with status 400 (Bad Request) if the ligneMissionActivite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligne-mission-activites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneMissionActivite> createLigneMissionActivite(@RequestBody LigneMissionActivite ligneMissionActivite) throws URISyntaxException {
        log.debug("REST request to save LigneMissionActivite : {}", ligneMissionActivite);
        if (ligneMissionActivite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ligneMissionActivite", "idexists", "A new ligneMissionActivite cannot already have an ID")).body(null);
        }
        LigneMissionActivite result = ligneMissionActiviteService.save(ligneMissionActivite);
        return ResponseEntity.created(new URI("/api/ligne-mission-activites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ligneMissionActivite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-mission-activites : Updates an existing ligneMissionActivite.
     *
     * @param ligneMissionActivite the ligneMissionActivite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneMissionActivite,
     * or with status 400 (Bad Request) if the ligneMissionActivite is not valid,
     * or with status 500 (Internal Server Error) if the ligneMissionActivite couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligne-mission-activites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneMissionActivite> updateLigneMissionActivite(@RequestBody LigneMissionActivite ligneMissionActivite) throws URISyntaxException {
        log.debug("REST request to update LigneMissionActivite : {}", ligneMissionActivite);
        if (ligneMissionActivite.getId() == null) {
            return createLigneMissionActivite(ligneMissionActivite);
        }
        LigneMissionActivite result = ligneMissionActiviteService.save(ligneMissionActivite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ligneMissionActivite", ligneMissionActivite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-mission-activites : get all the ligneMissionActivites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneMissionActivites in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ligne-mission-activites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LigneMissionActivite>> getAllLigneMissionActivites(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LigneMissionActivites");
        Page<LigneMissionActivite> page = ligneMissionActiviteService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-mission-activites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-mission-activites/:id : get the "id" ligneMissionActivite.
     *
     * @param id the id of the ligneMissionActivite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneMissionActivite, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ligne-mission-activites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneMissionActivite> getLigneMissionActivite(@PathVariable Long id) {
        log.debug("REST request to get LigneMissionActivite : {}", id);
        LigneMissionActivite ligneMissionActivite = ligneMissionActiviteService.findOne(id);
        return Optional.ofNullable(ligneMissionActivite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ligne-mission-activites/:id : delete the "id" ligneMissionActivite.
     *
     * @param id the id of the ligneMissionActivite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ligne-mission-activites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLigneMissionActivite(@PathVariable Long id) {
        log.debug("REST request to delete LigneMissionActivite : {}", id);
        ligneMissionActiviteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ligneMissionActivite", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-mission-activites?query=:query : search for the ligneMissionActivite corresponding
     * to the query.
     *
     * @param query the query of the ligneMissionActivite search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ligne-mission-activites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LigneMissionActivite>> searchLigneMissionActivites(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LigneMissionActivites for query {}", query);
        Page<LigneMissionActivite> page = ligneMissionActiviteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-mission-activites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
