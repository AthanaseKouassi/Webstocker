package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Fabricant;
import com.webstocker.domain.Produit;
import com.webstocker.service.FabricantService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Fabricant.
 */
@RestController
@RequestMapping("/api")
public class FabricantResource {

    private final Logger log = LoggerFactory.getLogger(FabricantResource.class);

    @Inject
    private FabricantService fabricantService;

    /**
     * POST  /fabricants : Create a new fabricant.
     *
     * @param fabricant the fabricant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fabricant, or with status 400 (Bad Request) if the fabricant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fabricants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fabricant> createFabricant(@Valid @RequestBody Fabricant fabricant) throws URISyntaxException {
        log.debug("REST request to save Fabricant : {}", fabricant);
        if (fabricant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fabricant", "idexists", "A new fabricant cannot already have an ID")).body(null);
        }
        Fabricant result = fabricantService.save(fabricant);
        return ResponseEntity.created(new URI("/api/fabricants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fabricant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fabricants : Updates an existing fabricant.
     *
     * @param fabricant the fabricant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fabricant,
     * or with status 400 (Bad Request) if the fabricant is not valid,
     * or with status 500 (Internal Server Error) if the fabricant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fabricants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fabricant> updateFabricant(@Valid @RequestBody Fabricant fabricant) throws URISyntaxException {
        log.debug("REST request to update Fabricant : {}", fabricant);
        if (fabricant.getId() == null) {
            return createFabricant(fabricant);
        }
        Fabricant result = fabricantService.save(fabricant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fabricant", fabricant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fabricants : get all the fabricants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fabricants in body
     */
    @RequestMapping(value = "/fabricants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fabricant> getAllFabricants() {
        log.debug("REST request to get all Fabricants");
        return fabricantService.findAll();
    }

    /**
     * GET  /fabricants/:id : get the "id" fabricant.
     *
     * @param id the id of the fabricant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fabricant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fabricants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fabricant> getFabricant(@PathVariable Long id) {
        log.debug("REST request to get Fabricant : {}", id);
        Fabricant fabricant = fabricantService.findOne(id);
        return Optional.ofNullable(fabricant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /fabricants/produit/:id : get the "id" fabricants.
     *
     * @param id the id of the fabricant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fabricant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fabricants/produit/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fabricant> getFabricantsbyProduit(@PathVariable Long id) {
        log.debug("REST request to get Fabricant  par produit id: {}", id);

        return  fabricantService.findAllByProduit(id);
    }

    /**
     * DELETE  /fabricants/:id : delete the "id" fabricant.
     *
     * @param id the id of the fabricant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fabricants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFabricant(@PathVariable Long id) {
        log.debug("REST request to delete Fabricant : {}", id);
        fabricantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fabricant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fabricants?query=:query : search for the fabricant corresponding
     * to the query.
     *
     * @param query the query of the fabricant search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/fabricants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fabricant> searchFabricants(@RequestParam String query) {
        log.debug("REST request to search Fabricants for query {}", query);
        return fabricantService.search(query);
    }

}
