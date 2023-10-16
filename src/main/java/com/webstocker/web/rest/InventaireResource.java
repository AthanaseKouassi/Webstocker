package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Inventaire;
import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import com.webstocker.service.EtatDeTousLesProduitsDunMagasinService;
import com.webstocker.service.InventaireService;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
//import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

/**
 * REST controller for managing Inventaire.
 */
@RestController
@RequestMapping("/api")
public class InventaireResource {

    private final Logger log = LoggerFactory.getLogger(InventaireResource.class);

    private static final String ENTITY_NAME = "inventaire";

    @Inject
    private InventaireService inventaireService;
    
    @Inject 
    private EtatDeTousLesProduitsDunMagasinService etatDeTousLesProduitsDunMagasinService;

//    public InventaireResource(InventaireService inventaireService) {
//        this.inventaireService = inventaireService;
//    }
    /**
     * POST /inventaires : Create a new inventaire.
     *
     * @param inventaire the inventaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new inventaire, or with status 400 (Bad Request) if the inventaire has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PostMapping("/inventaires")
    @RequestMapping(value = "/inventaires",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventaire> createInventaire(@RequestBody Inventaire inventaire) throws URISyntaxException {
        log.debug("REST request to save Inventaire : {}", inventaire);
        if (inventaire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new inventaire cannot already have an ID")).body(null);
        }
        
        Inventaire result = inventaireService.save(inventaire);
        return ResponseEntity.created(new URI("/api/inventaires/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /inventaires : Updates an existing inventaire.
     *
     * @param inventaire the inventaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * inventaire, or with status 400 (Bad Request) if the inventaire is not
     * valid, or with status 500 (Internal Server Error) if the inventaire
     * couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PutMapping("/inventaires")
    @RequestMapping(value = "/inventaires",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventaire> updateInventaire(@RequestBody Inventaire inventaire) throws URISyntaxException {
        log.debug("REST request to update Inventaire : {}", inventaire);
        if (inventaire.getId() == null) {
            return createInventaire(inventaire);
        }
        Inventaire result = inventaireService.save(inventaire);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inventaire.getId().toString()))
                .body(result);
    }

    /**
     * GET /inventaires : get all the inventaires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of
     * inventaires in body
     * @throws URISyntaxException if there is an error to generate the
     * pagination HTTP headers
     */
//    @GetMapping("/inventaires")
    @RequestMapping(value = "/inventaires",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Inventaire>> getAllInventaires(@ApiParam Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of Inventaires");
        Page<Inventaire> page = inventaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/inventaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /inventaires/:id : get the "id" inventaire.
     *
     * @param id the id of the inventaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * inventaire, or with status 404 (Not Found)
     */
//    @GetMapping("/inventaires/{id}")
    @RequestMapping(value = "/inventaires/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventaire> getInventaire(@PathVariable Long id) {
        log.debug("REST request to get Inventaire : {}", id);
        Inventaire inventaire = inventaireService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(inventaire));
        return Optional.ofNullable(inventaire)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /inventaires/:id : delete the "id" inventaire.
     *
     * @param id the id of the inventaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
//    @DeleteMapping("/inventaires/{id}")
    @RequestMapping(value = "/inventaires/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInventaire(@PathVariable Long id) {
        log.debug("REST request to delete Inventaire : {}", id);
        inventaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH /_search/inventaires?query=:query : search for the inventaire
     * corresponding to the query.
     *
     * @param query the query of the inventaire search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the
     * pagination HTTP headers
     */
//    @GetMapping("/_search/inventaires")
    @RequestMapping(value = "/_search/inventaires",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Inventaire>> searchInventaires(@RequestParam String query, @ApiParam Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to search for a page of Inventaires for query {}", query);
        Page<Inventaire> page = inventaireService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/inventaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/tous-les-inventaires",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Inventaire> getTousLesInventaires(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) throws URISyntaxException {
        log.debug("REST request to all inventaires ");
        return inventaireService.findAll(new PageRequest(page, size));      
    }
    
    
    @RequestMapping(value = "/trouver-inventaires-magasin",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Inventaire>getTrouverInventaire(@RequestParam(required = true) String nomMagasin,@RequestParam(required = true) String dateDuMois,
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) throws URISyntaxException {
        log.debug("REST request to all inventaires ");
        Page<Inventaire> resultat = inventaireService.findByMagasinAndDateInventaireBetween(nomMagasin, dateDuMois, new PageRequest(page, size));
        return resultat;
    }
 
    
}
