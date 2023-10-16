package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Ligneobjectifvente;
import com.webstocker.service.LigneobjectifventeService;
import com.webstocker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Ligneobjectifvente.
 */
@RestController
@RequestMapping("/api")
public class LigneobjectifventeResource {

    private final Logger log = LoggerFactory.getLogger(LigneobjectifventeResource.class);
        
    @Inject
    private LigneobjectifventeService ligneobjectifventeService;
    
    /**
     * POST  /ligneobjectifventes : Create a new ligneobjectifvente.
     *
     * @param ligneobjectifvente the ligneobjectifvente to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneobjectifvente, or with status 400 (Bad Request) if the ligneobjectifvente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligneobjectifventes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ligneobjectifvente> createLigneobjectifvente(@RequestBody Ligneobjectifvente ligneobjectifvente) throws URISyntaxException {
        log.debug("REST request to save Ligneobjectifvente : {}", ligneobjectifvente);
        if (ligneobjectifvente.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ligneobjectifvente", "idexists", "A new ligneobjectifvente cannot already have an ID")).body(null);
        }
        Ligneobjectifvente result = ligneobjectifventeService.save(ligneobjectifvente);
        return ResponseEntity.created(new URI("/api/ligneobjectifventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ligneobjectifvente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligneobjectifventes : Updates an existing ligneobjectifvente.
     *
     * @param ligneobjectifvente the ligneobjectifvente to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneobjectifvente,
     * or with status 400 (Bad Request) if the ligneobjectifvente is not valid,
     * or with status 500 (Internal Server Error) if the ligneobjectifvente couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligneobjectifventes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ligneobjectifvente> updateLigneobjectifvente(@RequestBody Ligneobjectifvente ligneobjectifvente) throws URISyntaxException {
        log.debug("REST request to update Ligneobjectifvente : {}", ligneobjectifvente);
        if (ligneobjectifvente.getId() == null) {
            return createLigneobjectifvente(ligneobjectifvente);
        }
        Ligneobjectifvente result = ligneobjectifventeService.save(ligneobjectifvente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ligneobjectifvente", ligneobjectifvente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligneobjectifventes : get all the ligneobjectifventes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ligneobjectifventes in body
     */
    @RequestMapping(value = "/ligneobjectifventes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ligneobjectifvente> getAllLigneobjectifventes() {
        log.debug("REST request to get all Ligneobjectifventes");
        return ligneobjectifventeService.findAll();
    }

    /**
     * GET  /ligneobjectifventes/:id : get the "id" ligneobjectifvente.
     *
     * @param id the id of the ligneobjectifvente to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneobjectifvente, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ligneobjectifventes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ligneobjectifvente> getLigneobjectifvente(@PathVariable Long id) {
        log.debug("REST request to get Ligneobjectifvente : {}", id);
        Ligneobjectifvente ligneobjectifvente = ligneobjectifventeService.findOne(id);
        return Optional.ofNullable(ligneobjectifvente)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ligneobjectifventes/:id : delete the "id" ligneobjectifvente.
     *
     * @param id the id of the ligneobjectifvente to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ligneobjectifventes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLigneobjectifvente(@PathVariable Long id) {
        log.debug("REST request to delete Ligneobjectifvente : {}", id);
        ligneobjectifventeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ligneobjectifvente", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligneobjectifventes?query=:query : search for the ligneobjectifvente corresponding
     * to the query.
     *
     * @param query the query of the ligneobjectifvente search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ligneobjectifventes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ligneobjectifvente> searchLigneobjectifventes(@RequestParam String query) {
        log.debug("REST request to search Ligneobjectifventes for query {}", query);
        return ligneobjectifventeService.search(query);
    }

}
