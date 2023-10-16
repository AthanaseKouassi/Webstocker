package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignecommande;
import com.webstocker.service.CommandeService;
import com.webstocker.service.LignecommandeService;
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
 * REST controller for managing Lignecommande.
 */
@RestController
@RequestMapping("/api")
public class LignecommandeResource {

    private final Logger log = LoggerFactory.getLogger(LignecommandeResource.class);

    @Inject
    private LignecommandeService lignecommandeService;

    @Inject
    private CommandeService commandeService;

    /**
     * POST  /lignecommandes : Create a new lignecommande.
     *
     * @param lignecommande the lignecommande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lignecommande, or with status 400 (Bad Request) if the lignecommande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lignecommandes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignecommande> createLignecommande(@Valid @RequestBody Lignecommande lignecommande) throws URISyntaxException {
        log.debug("REST request to save Lignecommande : {}", lignecommande);
        if (lignecommande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lignecommande", "idexists", "A new lignecommande cannot already have an ID")).body(null);
        }
        Lignecommande result = lignecommandeService.save(lignecommande);
        return ResponseEntity.created(new URI("/api/lignecommandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lignecommande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lignecommandes : Updates an existing lignecommande.
     *
     * @param lignecommande the lignecommande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lignecommande,
     * or with status 400 (Bad Request) if the lignecommande is not valid,
     * or with status 500 (Internal Server Error) if the lignecommande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lignecommandes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignecommande> updateLignecommande(@Valid @RequestBody Lignecommande lignecommande) throws URISyntaxException {
        log.debug("REST request to update Lignecommande : {}", lignecommande);
        if (lignecommande.getId() == null) {
            return createLignecommande(lignecommande);
        }
        Lignecommande result = lignecommandeService.save(lignecommande);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lignecommande", lignecommande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lignecommandes : get all the lignecommandes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lignecommandes in body
     */
    @RequestMapping(value = "/lignecommandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lignecommande> getAllLignecommandes() {
        log.debug("REST request to get all Lignecommandes");
        return lignecommandeService.findAll();
    }

    /**
     * GET  /lignecommandes/:id : get the "id" lignecommande.
     *
     * @param id the id of the lignecommande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lignecommande, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lignecommandes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignecommande> getLignecommande(@PathVariable Long id) {
        log.debug("REST request to get Lignecommande : {}", id);
        Lignecommande lignecommande = lignecommandeService.findOne(id);
        return Optional.ofNullable(lignecommande)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * GET  /lignecommandes/:id : get the "id" lignecommande.
     *
     * @param id the id of the lignecommande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lignecommande, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lignecommandes/commande/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lignecommande> getLignecommandesByCOmmande(@PathVariable Long id) {
        log.debug("REST request to get Lignecommande par commande : {}", id);
        Commande commande = commandeService.findOne(id);

        List<Lignecommande> lignecommandes = lignecommandeService.findByCommande(commande);

        return Optional.ofNullable(lignecommandes.get(0))
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lignecommandes/:id : delete the "id" lignecommande.
     *
     * @param id the id of the lignecommande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lignecommandes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLignecommande(@PathVariable Long id) {
        log.debug("REST request to delete Lignecommande : {}", id);
        lignecommandeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lignecommande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lignecommandes?query=:query : search for the lignecommande corresponding
     * to the query.
     *
     * @param query the query of the lignecommande search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/lignecommandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lignecommande> searchLignecommandes(@RequestParam String query) {
        log.debug("REST request to search Lignecommandes for query {}", query);
        return lignecommandeService.search(query);
    }

}
