package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Lot;
import com.webstocker.service.LotService;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * REST controller for managing Lot.
 */
@RestController
@RequestMapping("/api")
public class LotResource {

    private final Logger log = LoggerFactory.getLogger(LotResource.class);
        
    @Inject
    private LotService lotService;
    
    /**
     * POST  /lots : Create a new lot.
     *
     * @param lot the lot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lot, or with status 400 (Bad Request) if the lot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lots",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lot> createLot(@Valid @RequestBody Lot lot) throws URISyntaxException {
        log.debug("REST request to save Lot : {}", lot);
        if (lot.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lot", "idexists", "A new lot cannot already have an ID")).body(null);
        }
        Lot result = lotService.save(lot);
        return ResponseEntity.created(new URI("/api/lots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lot", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lots : Updates an existing lot.
     *
     * @param lot the lot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lot,
     * or with status 400 (Bad Request) if the lot is not valid,
     * or with status 500 (Internal Server Error) if the lot couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lots",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lot> updateLot(@Valid @RequestBody Lot lot) throws URISyntaxException {
        log.debug("REST request to update Lot : {}", lot);
        if (lot.getId() == null) {
            return createLot(lot);
        }
        Lot result = lotService.save(lot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lot", lot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lots : get all the lots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of lots in body
     */
    @RequestMapping(value = "/lots",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lot> getAllLots() {
        log.debug("REST request to get all Lots");
        return lotService.findAll();
    }

    /**
     * GET  /lots/:id : get the "id" lot.
     *
     * @param id the id of the lot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lot, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lots/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lot> getLot(@PathVariable Long id) {
        log.debug("REST request to get Lot : {}", id);
        Lot lot = lotService.findOne(id);
        return Optional.ofNullable(lot)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lots/:id : delete the "id" lot.
     *
     * @param id the id of the lot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lots/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLot(@PathVariable Long id) {
        log.debug("REST request to delete Lot : {}", id);
        lotService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lot", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lots?query=:query : search for the lot corresponding
     * to the query.
     *
     * @param query the query of the lot search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/lots",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Lot> searchLots(@RequestParam String query) {
        log.debug("REST request to search Lots for query {}", query);
        return lotService.search(query);
    }
    
     @RequestMapping(value = "/lot-par-page",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Lot> getLotParPage(@RequestParam (name="page",defaultValue = "0")int page, @RequestParam (name="size",defaultValue = "20")int size)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lot");
        Page<Lot> listPage= lotService.findAll(new PageRequest(page, size)); 
        return listPage;
    }

    
//    @RequestMapping(value = "/lot/lot-par-numerolot",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public ResponseEntity<List<Lot>> getLotParNumeroLot(@RequestParam Long numeroLot, Pageable pageable)
//        throws URISyntaxException {
//        log.debug("REST request to search for a page of Lots {}", numeroLot);
//        Page<Lot> page = lotService.findByNumeroLotContaining(numeroLot, pageable);
//       
//        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(numeroLot+"", page, "/api/lot/lot-par-numerolot");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }
    
}
