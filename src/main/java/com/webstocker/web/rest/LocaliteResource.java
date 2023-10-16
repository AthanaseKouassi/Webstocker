package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Localite;
import com.webstocker.service.LocaliteService;
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
import javax.persistence.NonUniqueResultException;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * REST controller for managing Localite.
 */
@RestController
@RequestMapping("/api")
public class LocaliteResource {

    private final Logger log = LoggerFactory.getLogger(LocaliteResource.class);
        
    @Inject
    private LocaliteService localiteService;
    
    /**
     * POST  /localites : Create a new localite.
     *
     * @param localite the localite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new localite, or with status 400 (Bad Request) if the localite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/localites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Localite> createLocalite(@RequestBody Localite localite) throws URISyntaxException {
        log.debug("REST request to save Localite : {}", localite);
        if (localite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("localite", "idexists", "A new localite cannot already have an ID")).body(null);
        }
        Localite result = localiteService.save(localite);
        return ResponseEntity.created(new URI("/api/localites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("localite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /localites : Updates an existing localite.
     *
     * @param localite the localite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated localite,
     * or with status 400 (Bad Request) if the localite is not valid,
     * or with status 500 (Internal Server Error) if the localite couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/localites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Localite> updateLocalite(@RequestBody Localite localite) throws URISyntaxException {
        log.debug("REST request to update Localite : {}", localite);
        if (localite.getId() == null) {
            return createLocalite(localite);
        }
        Localite result = localiteService.save(localite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("localite", localite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /localites : get all the localites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of localites in body
     */
    @RequestMapping(value = "/localites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Localite> getAllLocalites() {
        log.debug("REST request to get all Localites");
        return localiteService.findAll();
    }

    /**
     * GET  /localites/:id : get the "id" localite.
     *
     * @param id the id of the localite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the localite, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/localites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Localite> getLocalite(@PathVariable Long id) {
        log.debug("REST request to get Localite : {}", id);
        Localite localite = localiteService.findOne(id);
        return Optional.ofNullable(localite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /localites/:id : delete the "id" localite.
     *
     * @param id the id of the localite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/localites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLocalite(@PathVariable Long id) {
        log.debug("REST request to delete Localite : {}", id);
        localiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("localite", id.toString())).build();
    }

    /**
     * SEARCH  /_search/localites?query=:query : search for the localite corresponding
     * to the query.
     *
     * @param query the query of the localite search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/localites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Localite> searchLocalites(@RequestParam String query) {
        log.debug("REST request to search Localites for query {}", query);
        return localiteService.search(query);
    }

    /**
     * 
     * @param nomlocalite
     * @return 
     */
    @RequestMapping(value = "/localites/localiteparNom/{nomlocalite}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Localite> getLocaliteParNom(@PathVariable String nomlocalite) {
        log.debug("REST request to get Region : {}", nomlocalite);
        Localite localite = null;
        try{
        localite = localiteService.findByNom(nomlocalite);
        return Optional.ofNullable(localite)
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
