package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Auteur;
import com.webstocker.service.AuteurService;
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
 * REST controller for managing Auteur.
 */
@RestController
@RequestMapping("/api")
public class AuteurResource {

    private final Logger log = LoggerFactory.getLogger(AuteurResource.class);
        
    @Inject
    private AuteurService auteurService;
    
    /**
     * POST  /auteurs : Create a new auteur.
     *
     * @param auteur the auteur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auteur, or with status 400 (Bad Request) if the auteur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/auteurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Auteur> createAuteur(@RequestBody Auteur auteur) throws URISyntaxException {
        log.debug("REST request to save Auteur : {}", auteur);
        if (auteur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auteur", "idexists", "A new auteur cannot already have an ID")).body(null);
        }
        Auteur result = auteurService.save(auteur);
        return ResponseEntity.created(new URI("/api/auteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("auteur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /auteurs : Updates an existing auteur.
     *
     * @param auteur the auteur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auteur,
     * or with status 400 (Bad Request) if the auteur is not valid,
     * or with status 500 (Internal Server Error) if the auteur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/auteurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Auteur> updateAuteur(@RequestBody Auteur auteur) throws URISyntaxException {
        log.debug("REST request to update Auteur : {}", auteur);
        if (auteur.getId() == null) {
            return createAuteur(auteur);
        }
        Auteur result = auteurService.save(auteur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("auteur", auteur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /auteurs : get all the auteurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of auteurs in body
     */
    @RequestMapping(value = "/auteurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Auteur> getAllAuteurs() {
        log.debug("REST request to get all Auteurs");
        return auteurService.findAll();
    }

    /**
     * GET  /auteurs/:id : get the "id" auteur.
     *
     * @param id the id of the auteur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auteur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/auteurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Auteur> getAuteur(@PathVariable Long id) {
        log.debug("REST request to get Auteur : {}", id);
        Auteur auteur = auteurService.findOne(id);
        return Optional.ofNullable(auteur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /auteurs/:id : delete the "id" auteur.
     *
     * @param id the id of the auteur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/auteurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAuteur(@PathVariable Long id) {
        log.debug("REST request to delete Auteur : {}", id);
        auteurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("auteur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/auteurs?query=:query : search for the auteur corresponding
     * to the query.
     *
     * @param query the query of the auteur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/auteurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Auteur> searchAuteurs(@RequestParam String query) {
        log.debug("REST request to search Auteurs for query {}", query);
        return auteurService.search(query);
    }

}
