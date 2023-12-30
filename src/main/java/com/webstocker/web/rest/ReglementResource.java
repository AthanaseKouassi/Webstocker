package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Reglement;
import com.webstocker.service.ReglementService;
import com.webstocker.web.rest.dto.newfeature.ReglementFactureDto;
import com.webstocker.web.rest.mapper.newfeature.ReglementMapper;
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

/**
 * REST controller for managing Reglement.
 */
@RestController
@RequestMapping("/api")
public class ReglementResource {

    private final Logger log = LoggerFactory.getLogger(ReglementResource.class);

    @Inject
    private ReglementService reglementService;
    @Inject
    private ReglementMapper reglementMapper;


    /**
     * POST  /reglements : Create a new reglement.
     *
     * @param reglement the reglement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reglement, or with status 400 (Bad Request) if the reglement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reglements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Reglement> createReglement(@Valid @RequestBody Reglement reglement) throws URISyntaxException {
        log.debug("REST request to save Reglement : {}", reglement);
        if (reglement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reglement", "idexists", "A new reglement cannot already have an ID")).body(null);
        }

        System.out.println("***************facture**********************");
        System.out.println(reglement.getFacture());
        System.out.println("***************facture**********************");
        Reglement result = reglementService.save(reglement);
        return ResponseEntity.created(new URI("/api/reglements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reglement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reglements : Updates an existing reglement.
     *
     * @param reglement the reglement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reglement,
     * or with status 400 (Bad Request) if the reglement is not valid,
     * or with status 500 (Internal Server Error) if the reglement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/reglements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Reglement> updateReglement(@Valid @RequestBody Reglement reglement) throws URISyntaxException {
        log.debug("REST request to update Reglement : {}", reglement);
        if (reglement.getId() == null) {
            return createReglement(reglement);
        }
        Reglement result = reglementService.save(reglement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reglement", reglement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reglements : get all the reglements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reglements in body
     */
    @RequestMapping(value = "/reglements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Reglement> getAllReglements() {
        log.debug("REST request to get all Reglements");
        return reglementService.findAll();
    }

    /**
     * GET  /reglements/:id : get the "id" reglement.
     *
     * @param id the id of the reglement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reglement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/reglements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Reglement> getReglement(@PathVariable Long id) {
        log.debug("REST request to get Reglement : {}", id);
        Reglement reglement = reglementService.findOne(id);
        return Optional.ofNullable(reglement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reglements/:id : delete the "id" reglement.
     *
     * @param id the id of the reglement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/reglements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReglement(@PathVariable Long id) {
        log.debug("REST request to delete Reglement : {}", id);
        reglementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reglement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/reglements?query=:query : search for the reglement corresponding
     * to the query.
     *
     * @param query the query of the reglement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/reglements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Reglement> searchReglements(@RequestParam String query) {
        log.debug("REST request to search Reglements for query {}", query);
        return reglementService.search(query);
    }

    @RequestMapping(value = "/reglement/facture-credit",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ReglementFactureDto> reglerFactureCredit(@RequestBody ReglementFactureDto reglementFactureDto) {
        List<Reglement> listReglements = reglementMapper.listDtoTo(reglementFactureDto.getReglementDtos());
        return new ResponseEntity<>(reglementService
            .reglementFactureCredit(reglementFactureDto.getIdFacture(), listReglements), HttpStatus.CREATED);
    }

}
