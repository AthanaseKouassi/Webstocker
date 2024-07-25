package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Inventaire;
import com.webstocker.service.InventaireService;
import com.webstocker.service.newfeature.InventaireNewService;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Inventaire.
 */
@RestController
@RequestMapping("/api")
public class InventaireResource {

    private static final String ENTITY_NAME = "inventaire";
    private final Logger log = LoggerFactory.getLogger(InventaireResource.class);
    @Inject
    private InventaireService inventaireService;

    @Inject
    private InventaireNewService inventaireNewService;


    @RequestMapping(value = "/inventaires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventaire> createInventaire(@RequestBody Inventaire inventaire) throws URISyntaxException {
        log.debug("REST request to save Inventaire : {}", inventaire);
        if (inventaire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new inventaire cannot already have an ID")).body(null);
        }

        Inventaire result = inventaireService.create(inventaire);
        return ResponseEntity.created(new URI("/api/inventaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

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


    @RequestMapping(value = "/inventaires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventaire> getInventaire(@PathVariable Long id) {
        log.debug("REST request to get Inventaire : {}", id);
        Inventaire inventaire = inventaireService.findOne(id);
        final long ajustement = (inventaire.getStockTheoDebut() + inventaire.getArrivage())
            - inventaire.getVente() - inventaire.getPromo() - inventaire.getPerteAbime()
            - (inventaire.getStockAgent() + inventaire.getStockMagasinCentral() + inventaire.getStockAgent());
        inventaire.setAjustement(ajustement);

        return Optional.ofNullable(inventaire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @RequestMapping(value = "/inventaires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInventaire(@PathVariable Long id) {
        log.debug("REST request to delete Inventaire : {}", id);
        inventaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

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

    @RequestMapping(value = "/page/inventaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Inventaire> getTousLesInventaires(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to all inventaires to page {} and size {}", page, size);
        return inventaireService.findAll(new PageRequest(page, size));
    }


    @RequestMapping(value = "/trouver-inventaires-magasin",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Inventaire> getTrouverInventaire(@RequestParam(required = true) String nomMagasin,
                                                 @RequestParam(required = true) String dateDuMois,
                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                 @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to all inventaires ");
        return inventaireService.findByMagasinAndDateInventaireBetween(nomMagasin, dateDuMois, new PageRequest(page, size));
    }

    @RequestMapping(value = "/search/{dateDuMois}/inventaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<Inventaire>> getInventaire(@PathVariable String dateDuMois,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("Inventaire du mois {}", dateDuMois);
        PageRequest pageRequest = new PageRequest(page, size);
        Page<Inventaire> inventaires = inventaireService.getDateInventaireBetween(dateDuMois, pageRequest);

        if (!inventaires.hasContent()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(inventaires);
    }

    @RequestMapping(value = "/inventaire/{idproduit}/quantite-theorique-en-stock",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inventaire> getQuantiteTheoriqueProduit(@PathVariable long idproduit,
                                                                  @RequestParam(required = true) String dateInventaire) {
        log.debug("Request date de l'inventaire :: {}", dateInventaire);
        final Inventaire inventaire = inventaireNewService.getInventaireByProduitAndDate(idproduit, dateInventaire);

        return ResponseEntity.ok(inventaire);
    }

    @RequestMapping(value = "/_search/inventaire/{dateInventaire}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Inventaire>> getAllInventaireParMois(@PathVariable String dateInventaire) throws RuntimeException {
        final List<Inventaire> lstInventaires = inventaireNewService.getInventaireByMonth(dateInventaire);
        return ResponseEntity.status(HttpStatus.OK).body(lstInventaires);
    }


}
