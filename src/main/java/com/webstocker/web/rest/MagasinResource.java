package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Magasin;
import com.webstocker.service.MagasinService;
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
import javax.persistence.NonUniqueResultException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * REST controller for managing Magasin.
 */
@RestController
@RequestMapping("/api")
public class MagasinResource {

    private final Logger log = LoggerFactory.getLogger(MagasinResource.class);
        
    @Inject
    private MagasinService magasinService;
    
    /**
     * POST  /magasins : Create a new magasin.
     *
     * @param magasin the magasin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new magasin, or with status 400 (Bad Request) if the magasin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/magasins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Magasin> createMagasin(@Valid @RequestBody Magasin magasin) throws URISyntaxException {
        log.debug("REST request to save Magasin : {}", magasin);
        if (magasin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("magasin", "idexists", "A new magasin cannot already have an ID")).body(null);
        }
//        if(magasinService.findByNomMagasin(magasin.getNomMagasin())!= null){
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("magasin", "idexists", "A new magasin cannot already have an ID")).body(null);
//        }
        Magasin result = magasinService.save(magasin);
        return ResponseEntity.created(new URI("/api/magasins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("magasin", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /magasins : Updates an existing magasin.
     *
     * @param magasin the magasin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated magasin,
     * or with status 400 (Bad Request) if the magasin is not valid,
     * or with status 500 (Internal Server Error) if the magasin couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/magasins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Magasin> updateMagasin(@Valid @RequestBody Magasin magasin) throws URISyntaxException {
        log.debug("REST request to update Magasin : {}", magasin);
        if (magasin.getId() == null) {
            return createMagasin(magasin);
        }
//        if(magasinService.findByNomMagasin(magasin.getNomMagasin())!= null){
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("magasin", "idexists", "A new magasin cannot already have an ID")).body(null);
//        }
        Magasin result = magasinService.save(magasin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("magasin", magasin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /magasins : get all the magasins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of magasins in body
     */
    @RequestMapping(value = "/magasins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Magasin> getAllMagasins() {
        log.debug("REST request to get all Magasins");
        return magasinService.findAll();
    }

    /**
     * GET  /magasins/:id : get the "id" magasin.
     *
     * @param id the id of the magasin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the magasin, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/magasins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Magasin> getMagasin(@PathVariable Long id) {
        log.debug("REST request to get Magasin : {}", id);
        Magasin magasin = magasinService.findOne(id);
        return Optional.ofNullable(magasin)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /magasins/:id : delete the "id" magasin.
     *
     * @param id the id of the magasin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/magasins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMagasin(@PathVariable Long id) {
        log.debug("REST request to delete Magasin : {}", id);
        magasinService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("magasin", id.toString())).build();
    }

        
    /**
     * SEARCH  /_search/magasins?query=:query : search for the magasin corresponding
     * to the query.
     *
     * @param query the query of the magasin search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/magasins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Magasin> searchMagasins(@RequestParam String query) {
        log.debug("REST request to search Magasins for query {}", query);
        return magasinService.search(query);
    }

    
    @RequestMapping(value = "/magasins/trouverMagasinParNom/{nommagasin}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Magasin> getMagasin(@PathVariable String nommagasin) {
        log.debug("REST request to get Magasin : {}", nommagasin);
        Magasin magasin = null;
        try{
        magasin = magasinService.findByNomMagasin(nommagasin);
        return Optional.ofNullable(magasin)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IncorrectResultSizeDataAccessException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        }
    }
}
