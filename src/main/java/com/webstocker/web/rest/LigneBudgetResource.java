package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.LigneBudget;
import com.webstocker.service.LigneBudgetService;
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
 * REST controller for managing LigneBudget.
 */
@RestController
@RequestMapping("/api")
public class LigneBudgetResource {

    private final Logger log = LoggerFactory.getLogger(LigneBudgetResource.class);
        
    @Inject
    private LigneBudgetService ligneBudgetService;
    
    /**
     * POST  /ligne-budgets : Create a new ligneBudget.
     *
     * @param ligneBudget the ligneBudget to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneBudget, or with status 400 (Bad Request) if the ligneBudget has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligne-budgets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneBudget> createLigneBudget(@Valid @RequestBody LigneBudget ligneBudget) throws URISyntaxException {
        log.debug("REST request to save LigneBudget : {}", ligneBudget);
        if (ligneBudget.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ligneBudget", "idexists", "A new ligneBudget cannot already have an ID")).body(null);
        }
        LigneBudget result = ligneBudgetService.save(ligneBudget);
        return ResponseEntity.created(new URI("/api/ligne-budgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ligneBudget", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-budgets : Updates an existing ligneBudget.
     *
     * @param ligneBudget the ligneBudget to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneBudget,
     * or with status 400 (Bad Request) if the ligneBudget is not valid,
     * or with status 500 (Internal Server Error) if the ligneBudget couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligne-budgets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneBudget> updateLigneBudget(@Valid @RequestBody LigneBudget ligneBudget) throws URISyntaxException {
        log.debug("REST request to update LigneBudget : {}", ligneBudget);
        if (ligneBudget.getId() == null) {
            return createLigneBudget(ligneBudget);
        }
        LigneBudget result = ligneBudgetService.save(ligneBudget);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ligneBudget", ligneBudget.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-budgets : get all the ligneBudgets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneBudgets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ligne-budgets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LigneBudget>> getAllLigneBudgets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LigneBudgets");
        Page<LigneBudget> page = ligneBudgetService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-budgets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-budgets/:id : get the "id" ligneBudget.
     *
     * @param id the id of the ligneBudget to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneBudget, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ligne-budgets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneBudget> getLigneBudget(@PathVariable Long id) {
        log.debug("REST request to get LigneBudget : {}", id);
        LigneBudget ligneBudget = ligneBudgetService.findOne(id);
        return Optional.ofNullable(ligneBudget)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ligne-budgets/:id : delete the "id" ligneBudget.
     *
     * @param id the id of the ligneBudget to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ligne-budgets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLigneBudget(@PathVariable Long id) {
        log.debug("REST request to delete LigneBudget : {}", id);
        ligneBudgetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ligneBudget", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-budgets?query=:query : search for the ligneBudget corresponding
     * to the query.
     *
     * @param query the query of the ligneBudget search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ligne-budgets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LigneBudget>> searchLigneBudgets(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LigneBudgets for query {}", query);
        Page<LigneBudget> page = ligneBudgetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-budgets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
