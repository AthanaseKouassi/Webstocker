package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Lignefacture;
import com.webstocker.service.LignefactureService;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Lignefacture.
 */
@RestController
@RequestMapping("/api")
public class LignefactureResource {

    private final Logger log = LoggerFactory.getLogger(LignefactureResource.class);
        
    @Inject
    private LignefactureService lignefactureService;
    
    /**
     * POST  /lignefactures : Create a new lignefacture.
     *
     * @param lignefacture the lignefacture to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lignefacture, or with status 400 (Bad Request) if the lignefacture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lignefactures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignefacture> createLignefacture(@Valid @RequestBody Lignefacture lignefacture) throws URISyntaxException {
        log.debug("REST request to save Lignefacture : {}", lignefacture);
        if (lignefacture.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lignefacture", "idexists", "A new lignefacture cannot already have an ID")).body(null);
        }
        Lignefacture result = lignefactureService.save(lignefacture);
        return ResponseEntity.created(new URI("/api/lignefactures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lignefacture", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lignefactures : Updates an existing lignefacture.
     *
     * @param lignefacture the lignefacture to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lignefacture,
     * or with status 400 (Bad Request) if the lignefacture is not valid,
     * or with status 500 (Internal Server Error) if the lignefacture couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lignefactures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignefacture> updateLignefacture(@Valid @RequestBody Lignefacture lignefacture) throws URISyntaxException {
        log.debug("REST request to update Lignefacture : {}", lignefacture);
        if (lignefacture.getId() == null) {
            return createLignefacture(lignefacture);
        }
        Lignefacture result = lignefactureService.save(lignefacture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lignefacture", lignefacture.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lignefactures : get all the lignefactures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lignefactures in body
     */
    @RequestMapping(value = "/lignefactures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lignefacture> getAllLignefactures() {
        log.debug("REST request to get all Lignefactures");
        return lignefactureService.findAll();
    }

    /**
     * GET  /lignefactures/:id : get the "id" lignefacture.
     *
     * @param id the id of the lignefacture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lignefacture, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lignefactures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignefacture> getLignefacture(@PathVariable Long id) {
        log.debug("REST request to get Lignefacture : {}", id);
        Lignefacture lignefacture = lignefactureService.findOne(id);
        return Optional.ofNullable(lignefacture)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lignefactures/:id : delete the "id" lignefacture.
     *
     * @param id the id of the lignefacture to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lignefactures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLignefacture(@PathVariable Long id) {
        log.debug("REST request to delete Lignefacture : {}", id);
        lignefactureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lignefacture", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lignefactures?query=:query : search for the lignefacture corresponding
     * to the query.
     *
     * @param query the query of the lignefacture search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/lignefactures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lignefacture> searchLignefactures(@RequestParam String query) {
        log.debug("REST request to search Lignefactures for query {}", query);
        return lignefactureService.search(query);
    }

}
