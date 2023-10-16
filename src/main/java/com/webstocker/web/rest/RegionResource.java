package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Region;
import com.webstocker.service.RegionService;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import javax.persistence.NonUniqueResultException;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Region.
 */
@RestController
@RequestMapping("/api")
public class RegionResource {

    private final Logger log = LoggerFactory.getLogger(RegionResource.class);

    @Inject
    private RegionService regionService;

    /**
     * POST  /regions : Create a new region.
     *
     * @param region the region to create
     * @return the ResponseEntity with status 201 (Created) and with body the new region, or with status 400 (Bad Request) if the region has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/regions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Region> createRegion(@Valid @RequestBody Region region) throws URISyntaxException {
        log.debug("REST request to save Region : {}", region);
        if (region.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("region", "idexists", "A new region cannot already have an ID")).body(null);
        }
        Region result = regionService.save(region);
        return ResponseEntity.created(new URI("/api/regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("region", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /regions : Updates an existing region.
     *
     * @param region the region to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated region,
     * or with status 400 (Bad Request) if the region is not valid,
     * or with status 500 (Internal Server Error) if the region couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/regions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Region> updateRegion(@Valid @RequestBody Region region) throws URISyntaxException {
        log.debug("REST request to update Region : {}", region);
        if (region.getId() == null) {
            return createRegion(region);
        }
        Region result = regionService.save(region);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("region", region.getId().toString()))
            .body(result);
    }

    /**
     * GET  /regions : get all the regions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of regions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/regions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    public Page<Region> getAllRegions(@RequestParam(name="page",defaultValue="0") int page,
//            @RequestParam(name="size",defaultValue="3")int size)
//        throws URISyntaxException {
    public ResponseEntity<List<Region>> getAllRegions(Pageable pageable)
        throws URISyntaxException {

        log.debug("REST request to get a page of Regions");
//        Page<Region> pages = regionService.findAll(new PageRequest(page, size));
        Page<Region> page = regionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/regions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//        return pages;
    }



    @RequestMapping(value = "/regions-par-page",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Region> getTousLesRegions(@RequestParam(name="page",defaultValue="0") int page,
            @RequestParam(name="size",defaultValue="20")int size)  throws URISyntaxException {
        log.debug("REST request to get a page of Regions");
        Page<Region> ListeRegions = regionService.findAll(new PageRequest(page, size));
        return ListeRegions;
    }

    /**
     * GET  /regions/:id : get the "id" region.
     *
     * @param id the id of the region to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the region, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/regions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Region> getRegion(@PathVariable Long id) {
        log.debug("REST request to get Region : {}", id);
        Region region = regionService.findOne(id);
        return Optional.ofNullable(region)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /regions/:id : delete the "id" region.
     *
     * @param id the id of the region to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/regions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        log.debug("REST request to delete Region : {}", id);
        regionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("region", id.toString())).build();
    }

    /**
     * SEARCH  /_search/regions?query=:query : search for the region corresponding
     * to the query.
     *
     * @param query the query of the region search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/regions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Region>> searchRegions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Regions for query {}", query);
        Page<Region> page = regionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/regions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     *
     * @param nomRegion
     * @return
     */
    @RequestMapping(value = "/regions/regionParNom/{nomRegion}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Region> getRegion(@PathVariable String nomRegion) {
        log.debug("REST request to get Region : {}", nomRegion);
        Region region = null;
        try{
        region = regionService.findByLibelle(nomRegion);
        return Optional.ofNullable(region)
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
