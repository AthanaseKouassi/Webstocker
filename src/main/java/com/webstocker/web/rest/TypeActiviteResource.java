package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.TypeActivite;
import com.webstocker.service.TypeActiviteService;
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
 * REST controller for managing TypeActivite.
 */
@RestController
@RequestMapping("/api")
public class TypeActiviteResource {

    private final Logger log = LoggerFactory.getLogger(TypeActiviteResource.class);
        
    @Inject
    private TypeActiviteService typeActiviteService;
    
    /**
     * POST  /type-activites : Create a new typeActivite.
     *
     * @param typeActivite the typeActivite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeActivite, or with status 400 (Bad Request) if the typeActivite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-activites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeActivite> createTypeActivite(@Valid @RequestBody TypeActivite typeActivite) throws URISyntaxException {
        log.debug("REST request to save TypeActivite : {}", typeActivite);
        if (typeActivite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeActivite", "idexists", "A new typeActivite cannot already have an ID")).body(null);
        }
        TypeActivite result = typeActiviteService.save(typeActivite);
        return ResponseEntity.created(new URI("/api/type-activites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeActivite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-activites : Updates an existing typeActivite.
     *
     * @param typeActivite the typeActivite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeActivite,
     * or with status 400 (Bad Request) if the typeActivite is not valid,
     * or with status 500 (Internal Server Error) if the typeActivite couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-activites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeActivite> updateTypeActivite(@Valid @RequestBody TypeActivite typeActivite) throws URISyntaxException {
        log.debug("REST request to update TypeActivite : {}", typeActivite);
        if (typeActivite.getId() == null) {
            return createTypeActivite(typeActivite);
        }
        TypeActivite result = typeActiviteService.save(typeActivite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeActivite", typeActivite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-activites : get all the typeActivites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeActivites in body
     */
    @RequestMapping(value = "/type-activites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeActivite> getAllTypeActivites() {
        log.debug("REST request to get all TypeActivites");
        return typeActiviteService.findAll();
    }

    /**
     * GET  /type-activites/:id : get the "id" typeActivite.
     *
     * @param id the id of the typeActivite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeActivite, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-activites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeActivite> getTypeActivite(@PathVariable Long id) {
        log.debug("REST request to get TypeActivite : {}", id);
        TypeActivite typeActivite = typeActiviteService.findOne(id);
        return Optional.ofNullable(typeActivite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-activites/:id : delete the "id" typeActivite.
     *
     * @param id the id of the typeActivite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-activites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeActivite(@PathVariable Long id) {
        log.debug("REST request to delete TypeActivite : {}", id);
        typeActiviteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeActivite", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-activites?query=:query : search for the typeActivite corresponding
     * to the query.
     *
     * @param query the query of the typeActivite search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-activites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeActivite> searchTypeActivites(@RequestParam String query) {
        log.debug("REST request to search TypeActivites for query {}", query);
        return typeActiviteService.search(query);
    }

}
