package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.LigneBudget;
import com.webstocker.domain.LigneMissionActivite;
import com.webstocker.domain.Mission;
import com.webstocker.service.MissionService;
import com.webstocker.web.rest.dto.MissionDTO;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Mission.
 */
@RestController
@RequestMapping("/api")
public class MissionResource {

    private final Logger log = LoggerFactory.getLogger(MissionResource.class);
        
    @Inject
    private MissionService missionService;
    
    /**
     * POST  /missions : Create a new mission.
     *
     * @param missionDTO
     * @return the ResponseEntity with status 201 (Created) and with body the new mission, or with status 400 (Bad Request) if the mission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/missions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mission> createMission(@Valid @RequestBody MissionDTO missionDTO) throws URISyntaxException {
        log.debug("REST request to save Mission : {}", missionDTO);
        if (missionDTO.getMission().getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mission", "idexists", "A new mission cannot already have an ID")).body(null);
        }
        Mission mission = missionDTO.getMission();
        List<LigneBudget> ligneBudgets = missionDTO.getLigneBudgets();
        
        ligneBudgets.stream().forEach((ligneBudget) -> {
            System.out.println(" Les Lignes de mission "+ligneBudget.getLibelleLigneBudget());
            mission.addLigneBudgets(ligneBudget);
        });
        
        List<LigneMissionActivite> ligneActivites  = missionDTO.getLigneMissionActivites();
        ligneActivites.stream().forEach((ligneActivite)->{
            mission.addLigneActivite(ligneActivite);
        });
        Mission result = missionService.save(mission);
        return ResponseEntity.created(new URI("/api/missions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mission", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /missions : Updates an existing mission.
     *
     * @param mission the mission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mission,
     * or with status 400 (Bad Request) if the mission is not valid,
     * or with status 500 (Internal Server Error) if the mission couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/missions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mission> updateMission(@Valid @RequestBody Mission mission) throws URISyntaxException {
        log.debug("REST request to update Mission : {}", mission);
        if (mission.getId() == null) {
            //return createMission(mission);
        }
        Mission result = missionService.save(mission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mission", mission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /missions : get all the missions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of missions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/missions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Mission>> getAllMissions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Missions");
        Page<Mission> page = missionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/missions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /missions/:id : get the "id" mission.
     *
     * @param id the id of the mission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mission, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/missions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mission> getMission(@PathVariable Long id) {
        log.debug("REST request to get Mission : {}", id);
        Mission mission = missionService.findOne(id);
        return Optional.ofNullable(mission)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /missions/:id : delete the "id" mission.
     *
     * @param id the id of the mission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/missions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMission(@PathVariable Long id) {
        log.debug("REST request to delete Mission : {}", id);
        missionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mission", id.toString())).build();
    }

    /**
     * SEARCH  /_search/missions?query=:query : search for the mission corresponding
     * to the query.
     *
     * @param query the query of the mission search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/missions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Mission>> searchMissions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Missions for query {}", query);
        Page<Mission> page = missionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/missions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/mission-critere",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Mission> findMissionByCritere(@RequestParam(required=false)String dateDebut,
            @RequestParam(required=false)String dateFin,
            @RequestParam(required=false)String libelle) {
        log.debug("REST request to stat facture");
        LocalDate date1 = (dateDebut!=null && !"undefined".equals(dateDebut) && !dateDebut.trim().isEmpty())?LocalDate.parse(dateDebut):null;
        LocalDate date2 = (dateFin!=null && !"undefined".equals(dateFin) && !dateFin.trim().isEmpty())?LocalDate.parse(dateFin):null;
        String libmission = (libelle!=null && !"undefined".equals(libelle) && !libelle.trim().isEmpty())?libelle:null;
        System.out.println("La date convertie est "+date1); 
        System.out.println("La date2 convertie est "+date2);
        System.out.println("Le libelle de la mission "+libmission);
        return missionService.findMissionByCritere(date1, date2, libmission);
    }

}
