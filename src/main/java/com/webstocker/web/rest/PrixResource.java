package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Prix;
import com.webstocker.service.PrixService;
import com.webstocker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.elasticsearch.action.UnavailableShardsException;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Prix.
 */
@RestController
@RequestMapping("/api")
public class PrixResource {

    private final Logger log = LoggerFactory.getLogger(PrixResource.class);

    private static final String ENTITY_NAME = "prix";

    private final PrixService prixService;

    @Autowired
    public PrixResource(PrixService prixService) {
        this.prixService = prixService;
    }

    /**
     * POST  /prixes : Create a new prix.
     *
     * @param prix the prix to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prix, or with status 400 (Bad Request) if the prix has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/prixes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prix> createPrix(@Valid @RequestBody Prix prix) throws URISyntaxException {
        log.debug("REST request to save Prix : {}", prix);
        if (prix.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new prix cannot already have an ID")).body(null);
        }
        Prix result = prixService.save(prix);
        return ResponseEntity.created(new URI("/api/prixes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prixes : Updates an existing prix.
     *
     * @param prix the prix to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prix,
     * or with status 400 (Bad Request) if the prix is not valid,
     * or with status 500 (Internal Server Error) if the prix couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/prixes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prix> updatePrix(@Valid @RequestBody Prix prix) throws URISyntaxException {
        log.debug("REST request to update Prix : {}", prix);
        if (prix.getId() == null) {
            return createPrix(prix);
        }
        Prix result = prixService.save(prix);
       
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prix.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prixes : get all the prixes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of prixes in body
     */
    @RequestMapping(value = "/prixes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Prix> getAllPrixes() {
        log.debug("REST request to get all Prixes");
        return prixService.findAll();
    }

    /**
     * GET  /prixes/:id : get the "id" prix.
     *
     * @param id the id of the prix to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prix, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/prixes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prix> getPrix(@PathVariable Long id) {
        log.debug("REST request to get Prix : {}", id);
        Prix prix = prixService.findOne(id);
        return Optional.ofNullable(prix)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prixes/:id : delete the "id" prix.
     *
     * @param id the id of the prix to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/prixes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrix(@PathVariable Long id) {
        log.debug("REST request to delete Prix : {}", id);
        prixService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/prixes?query=:query : search for the prix corresponding
     * to the query.
     *
     * @param query the query of the prix search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/prixes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Prix> searchPrixes(@RequestParam String query) {
        log.debug("REST request to search Prixes for query {}", query);
        return prixService.search(query);
    }


}
