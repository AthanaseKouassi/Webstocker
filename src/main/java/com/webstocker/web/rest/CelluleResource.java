package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Cellule;
import com.webstocker.service.CelluleService;
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
 * REST controller for managing Cellule.
 */
@RestController
@RequestMapping("/api")
public class CelluleResource {

    private final Logger log = LoggerFactory.getLogger(CelluleResource.class);
        
    @Inject
    private CelluleService celluleService;
    
    /**
     * POST  /cellules : Create a new cellule.
     *
     * @param cellule the cellule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cellule, or with status 400 (Bad Request) if the cellule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cellules",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cellule> createCellule(@Valid @RequestBody Cellule cellule) throws URISyntaxException {
        log.debug("REST request to save Cellule : {}", cellule);
        if (cellule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cellule", "idexists", "A new cellule cannot already have an ID")).body(null);
        }
        Cellule result = celluleService.save(cellule);
        return ResponseEntity.created(new URI("/api/cellules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cellule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cellules : Updates an existing cellule.
     *
     * @param cellule the cellule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cellule,
     * or with status 400 (Bad Request) if the cellule is not valid,
     * or with status 500 (Internal Server Error) if the cellule couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cellules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cellule> updateCellule(@Valid @RequestBody Cellule cellule) throws URISyntaxException {
        log.debug("REST request to update Cellule : {}", cellule);
        if (cellule.getId() == null) {
            return createCellule(cellule);
        }
        Cellule result = celluleService.save(cellule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cellule", cellule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cellules : get all the cellules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cellules in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cellules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cellule>> getAllCellules(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cellules");
        Page<Cellule> page = celluleService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cellules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cellules/:id : get the "id" cellule.
     *
     * @param id the id of the cellule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cellule, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cellules/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cellule> getCellule(@PathVariable Long id) {
        log.debug("REST request to get Cellule : {}", id);
        Cellule cellule = celluleService.findOne(id);
        return Optional.ofNullable(cellule)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cellules/:id : delete the "id" cellule.
     *
     * @param id the id of the cellule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cellules/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCellule(@PathVariable Long id) {
        log.debug("REST request to delete Cellule : {}", id);
        celluleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cellule", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cellules?query=:query : search for the cellule corresponding
     * to the query.
     *
     * @param query the query of the cellule search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cellules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cellule>> searchCellules(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Cellules for query {}", query);
        Page<Cellule> page = celluleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cellules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
