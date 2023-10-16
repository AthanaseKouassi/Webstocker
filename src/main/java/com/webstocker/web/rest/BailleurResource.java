package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Bailleur;
import com.webstocker.service.BailleurService;
import com.webstocker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * REST controller for managing Bailleur.
 */
@RestController
@RequestMapping("/api")
public class BailleurResource {

    private final Logger log = LoggerFactory.getLogger(BailleurResource.class);
        
    @Inject
    private BailleurService bailleurService;
    
    /**
     * POST  /bailleurs : Create a new bailleur.
     *
     * @param bailleur the bailleur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bailleur, or with status 400 (Bad Request) if the bailleur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bailleurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bailleur> createBailleur(@Valid @RequestBody Bailleur bailleur) throws URISyntaxException {
        log.debug("REST request to save Bailleur : {}", bailleur);
        if (bailleur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bailleur", "idexists", "A new bailleur cannot already have an ID")).body(null);
        }
        Bailleur result = bailleurService.save(bailleur);
        return ResponseEntity.created(new URI("/api/bailleurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bailleur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bailleurs : Updates an existing bailleur.
     *
     * @param bailleur the bailleur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bailleur,
     * or with status 400 (Bad Request) if the bailleur is not valid,
     * or with status 500 (Internal Server Error) if the bailleur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bailleurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bailleur> updateBailleur(@Valid @RequestBody Bailleur bailleur) throws URISyntaxException {
        log.debug("REST request to update Bailleur : {}", bailleur);
        if (bailleur.getId() == null) {
            return createBailleur(bailleur);
        }
        Bailleur result = bailleurService.save(bailleur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bailleur", bailleur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bailleurs : get all the bailleurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bailleurs in body
     */
    @RequestMapping(value = "/bailleurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bailleur> getAllBailleurs() {
        log.debug("REST request to get all Bailleurs");
        return bailleurService.findAll();
    }

    /**
     * GET  /bailleurs/:id : get the "id" bailleur.
     *
     * @param id the id of the bailleur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bailleur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bailleurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bailleur> getBailleur(@PathVariable Long id) {
        log.debug("REST request to get Bailleur : {}", id);
        Bailleur bailleur = bailleurService.findOne(id);
        return Optional.ofNullable(bailleur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bailleurs/:id : delete the "id" bailleur.
     *
     * @param id the id of the bailleur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bailleurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBailleur(@PathVariable Long id) {
        log.debug("REST request to delete Bailleur : {}", id);
        bailleurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bailleur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bailleurs?query=:query : search for the bailleur corresponding
     * to the query.
     *
     * @param query the query of the bailleur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/bailleurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bailleur> searchBailleurs(@RequestParam String query) {
        log.debug("REST request to search Bailleurs for query {}", query);
        return bailleurService.search(query);
    }
}
