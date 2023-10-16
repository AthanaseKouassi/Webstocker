package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Categorie;
import com.webstocker.reports.CategoriesReport;
import com.webstocker.service.CategorieService;
import com.webstocker.web.rest.util.HeaderUtil;
import java.io.IOException;
import java.io.OutputStream;
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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * REST controller for managing Categorie.
 */
@RestController
@RequestMapping("/api")
public class CategorieResource {

    private final Logger log = LoggerFactory.getLogger(CategorieResource.class);

    @Inject
    private CategorieService categorieService;

    /**
     * POST /categories : Create a new categorie.
     *
     * @param categorie the categorie to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new categorie, or with status 400 (Bad Request) if the categorie has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categories",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorie> createCategorie(@Valid @RequestBody Categorie categorie) throws URISyntaxException {
        log.debug("REST request to save Categorie : {}", categorie);
        if (categorie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("categorie", "idexists", "A new categorie cannot already have an ID")).body(null);
        }

        Categorie result = categorieService.save(categorie);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("categorie", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /categories : Updates an existing categorie.
     *
     * @param categorie the categorie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * categorie, or with status 400 (Bad Request) if the categorie is not
     * valid, or with status 500 (Internal Server Error) if the categorie
     * couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/categories",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorie> updateCategorie(@Valid @RequestBody Categorie categorie) throws URISyntaxException {
        log.debug("REST request to update Categorie : {}", categorie);
        if (categorie.getId() == null) {
            return createCategorie(categorie);
        }
        Categorie result = categorieService.save(categorie);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("categorie", categorie.getId().toString()))
                .body(result);
    }

    /**
     * GET /categories : get all the categories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * categories in body
     */
    @RequestMapping(value = "/categories",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Categorie> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categorieService.findAll();
    }

    /**
     * GET /categories/:id : get the "id" categorie.
     *
     * @param id the id of the categorie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * categorie, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/categories/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorie> getCategorie(@PathVariable Long id) {
        log.debug("REST request to get Categorie : {}", id);
        Categorie categorie = categorieService.findOne(id);
        return Optional.ofNullable(categorie)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /categories/:id : delete the "id" categorie.
     *
     * @param id the id of the categorie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/categories/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        log.debug("REST request to delete Categorie : {}", id);
        categorieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categorie", id.toString())).build();
    }

    /**
     * SEARCH /_search/categories?query=:query : search for the categorie
     * corresponding to the query.
     *
     * @param query the query of the categorie search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/categories",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Categorie> searchCategories(@RequestParam String query) {
        log.debug("REST request to search Categories for query {}", query);
        return categorieService.search(query);
    }

    /**
     * 
     * @param nomCategorie
     * @return 
     */
    @RequestMapping(value = "/categories/parnomcategorie/{nomCategorie}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categorie> rechercherParNom(@PathVariable String nomCategorie) {
        log.debug("REST request to get Categorie by nomCategorie : {}", nomCategorie);
         Categorie categorie = null;
        try {
           categorie = categorieService.findByNomCategorie(nomCategorie);
            return Optional.ofNullable(categorie)
                    .map(result -> new ResponseEntity<>(
                            result,
                            HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        } catch (IncorrectResultSizeDataAccessException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            // Code for handling NonUniqueResultException
            return null;
        }

    }
}
