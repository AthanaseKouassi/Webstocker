package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;
import com.webstocker.service.LivraisonService;
import com.webstocker.web.rest.dto.CommandeLivraison;
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
 * REST controller for managing Livraison.
 */
@RestController
@RequestMapping("/api")
public class LivraisonResource {

    private final Logger log = LoggerFactory.getLogger(LivraisonResource.class);

    @Inject
    private LivraisonService livraisonService;

    /**
     * POST /livraisons : Create a new livraison.
     *
     * @param livraison the livraison to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new livraison, or with status 400 (Bad Request) if the livraison has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/livraisons",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Livraison> createLivraison(@Valid @RequestBody Livraison livraison) throws URISyntaxException {
        log.debug("REST request to save Livraison : {}", livraison);
        if (livraison.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("livraison", "idexists", "A new livraison cannot already have an ID")).body(null);
        }
        Livraison result = livraisonService.save(livraison);
        return ResponseEntity.created(new URI("/api/livraisons/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("livraison", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/livraisonsCommande",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Livraison> createLivraisonCommande(@Valid @RequestBody CommandeLivraison commandeLivraison) throws URISyntaxException {
//        log.debug("REST request to save Livraison : {}", livraison);
//        if (livraison.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("livraison", "idexists", "A new livraison cannot already have an ID")).body(null);
//        }

        Livraison livraison = commandeLivraison.getLivraison();
        List<Lignelivraison> lignelivraisons = commandeLivraison.getLignelivraisons();

        for (Lignelivraison l
                : lignelivraisons) {
            livraison.addLigneLivraison(l);
        }

        Livraison result = livraisonService.save(livraison);
        return ResponseEntity.created(new URI("/api/livraisons/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("livraison", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /livraisons : Updates an existing livraison.
     *
     * @param livraison the livraison to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * livraison, or with status 400 (Bad Request) if the livraison is not
     * valid, or with status 500 (Internal Server Error) if the livraison
     * couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/livraisons",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Livraison> updateLivraison(@Valid @RequestBody Livraison livraison) throws URISyntaxException {
        log.debug("REST request to update Livraison : {}", livraison);
        if (livraison.getId() == null) {
            return createLivraison(livraison);
        }
        Livraison result = livraisonService.save(livraison);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("livraison", livraison.getId().toString()))
                .body(result);
    }

    /**
     * GET /livraisons : get all the livraisons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * livraisons in body
     */
    @RequestMapping(value = "/livraisons",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Livraison> getAllLivraisons() {
        log.debug("REST request to get all Livraisons");
        return livraisonService.findAll();
    }

    /**
     * GET /livraisons/:id : get the "id" livraison.
     *
     * @param id the id of the livraison to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * livraison, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/livraisons/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Livraison> getLivraison(@PathVariable Long id) {
        log.debug("REST request to get Livraison : {}", id);
        Livraison livraison = livraisonService.findOne(id);
        return Optional.ofNullable(livraison)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /livraisons/:id : delete the "id" livraison.
     *
     * @param id the id of the livraison to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/livraisons/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        log.debug("REST request to delete Livraison : {}", id);
        livraisonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("livraison", id.toString())).build();
    }

    /**
     * SEARCH /_search/livraisons?query=:query : search for the livraison
     * corresponding to the query.
     *
     * @param query the query of the livraison search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/livraisons",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Livraison> searchLivraisons(@RequestParam String query) {
        log.debug("REST request to search Livraisons for query {}", query);
        return livraisonService.search(query);
    }

    /**
     *
     * @param dateDebut
     * @param dateFin
     * @return
     */
    @RequestMapping(value = "/livraison-par-dates",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Livraison> getLivraisonParDate(@RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin) {
        log.debug("REST request to  Livraisons between dateLivraison {}", dateDebut);
        return livraisonService.listeDesLivraisonParDate(dateDebut, dateFin);
    }
}
