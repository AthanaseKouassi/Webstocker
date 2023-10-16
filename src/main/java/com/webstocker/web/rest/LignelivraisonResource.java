package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.service.LignelivraisonService;
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
 * REST controller for managing Lignelivraison.
 */
@RestController
@RequestMapping("/api")
public class LignelivraisonResource {

    private final Logger log = LoggerFactory.getLogger(LignelivraisonResource.class);
        
    @Inject
    private LignelivraisonService lignelivraisonService;
    
    /**
     * POST  /lignelivraisons : Create a new lignelivraison.
     *
     * @param lignelivraison the lignelivraison to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lignelivraison, or with status 400 (Bad Request) if the lignelivraison has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lignelivraisons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignelivraison> createLignelivraison(@Valid @RequestBody Lignelivraison lignelivraison) throws URISyntaxException {
        log.debug("REST request to save Lignelivraison : {}", lignelivraison);
        if (lignelivraison.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lignelivraison", "idexists", "A new lignelivraison cannot already have an ID")).body(null);
        }
        Lignelivraison result = lignelivraisonService.save(lignelivraison);
        return ResponseEntity.created(new URI("/api/lignelivraisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lignelivraison", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lignelivraisons : Updates an existing lignelivraison.
     *
     * @param lignelivraison the lignelivraison to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lignelivraison,
     * or with status 400 (Bad Request) if the lignelivraison is not valid,
     * or with status 500 (Internal Server Error) if the lignelivraison couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lignelivraisons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignelivraison> updateLignelivraison(@Valid @RequestBody Lignelivraison lignelivraison) throws URISyntaxException {
        log.debug("REST request to update Lignelivraison : {}", lignelivraison);
        if (lignelivraison.getId() == null) {
            return createLignelivraison(lignelivraison);
        }
        Lignelivraison result = lignelivraisonService.save(lignelivraison);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lignelivraison", lignelivraison.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lignelivraisons : get all the lignelivraisons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lignelivraisons in body
     */
    @RequestMapping(value = "/lignelivraisons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lignelivraison> getAllLignelivraisons() {
        log.debug("REST request to get all Lignelivraisons");
        return lignelivraisonService.findAll();
    }

    /**
     * GET  /lignelivraisons/:id : get the "id" lignelivraison.
     *
     * @param id the id of the lignelivraison to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lignelivraison, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lignelivraisons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignelivraison> getLignelivraison(@PathVariable Long id) {
        log.debug("REST request to get Lignelivraison : {}", id);
        Lignelivraison lignelivraison = lignelivraisonService.findOne(id);
        return Optional.ofNullable(lignelivraison)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lignelivraisons/:id : delete the "id" lignelivraison.
     *
     * @param id the id of the lignelivraison to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lignelivraisons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLignelivraison(@PathVariable Long id) {
        log.debug("REST request to delete Lignelivraison : {}", id);
        lignelivraisonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lignelivraison", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lignelivraisons?query=:query : search for the lignelivraison corresponding
     * to the query.
     *
     * @param query the query of the lignelivraison search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/lignelivraisons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lignelivraison> searchLignelivraisons(@RequestParam String query) {
        log.debug("REST request to search Lignelivraisons for query {}", query);
        return lignelivraisonService.search(query);
    }
    
   

}
