package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.service.FactureService;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.service.ReglementService;
import com.webstocker.web.rest.dto.FactureDTO;
import com.webstocker.web.rest.dto.newfeature.CreanceDto;
import com.webstocker.web.rest.dto.newfeature.FactureNDto;
import com.webstocker.web.rest.mapper.newfeature.CreanceDtoMapper;
import com.webstocker.web.rest.mapper.newfeature.FactureNDtoMapper;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Facture.
 */
@RestController
@RequestMapping("/api")
public class FactureResource {

    private final Logger log = LoggerFactory.getLogger(FactureResource.class);
    @Autowired
    private FactureNDtoMapper factureNDtoMapper;
    @Autowired
    private CreanceDtoMapper creanceDtoMapper;
    @Inject
    private FactureService factureService;
    @Inject
    private BonDeSortieService bonDeSortieService;
    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;
    @Inject
    private ReglementService reglementService;

    /**
     * POST  /factures : Create a new facture.
     *
     * @param facture the facture to create
     * @return the ResponseEntity with status 201 (Created) and with body the new facture, or with status 400 (Bad Request) if the facture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/factures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Facture> createFacture(@Valid @RequestBody Facture facture) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", facture);
        if (facture.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("facture", "idexists", "A new facture cannot already have an ID")).body(null);
        }
        Facture result = factureService.save(facture);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("facture", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /factures : Updates an existing facture.
     *
     * @param facture the facture to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated facture,
     * or with status 400 (Bad Request) if the facture is not valid,
     * or with status 500 (Internal Server Error) if the facture couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/factures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Facture> updateFacture(@Valid @RequestBody Facture facture) throws URISyntaxException {
        log.debug("REST request to update Facture : {}", facture);
        if (facture.getId() == null) {
            return createFacture(facture);
        }
        Facture result = factureService.save(facture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("facture", facture.getId().toString()))
            .body(result);
    }

    /**
     * GET  /factures : get all the factures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of factures in body
     */
    @RequestMapping(value = "/factures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Facture> getAllFactures() {
        log.debug("REST request to get all Factures");
        return factureService.findAll();
    }

    /**
     * GET  /factures : get all the factures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of factures in body
     */
    @RequestMapping(value = "/factures-all",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FactureDTO> getAllFacturesCustom() {
        log.debug("REST request to get all Factures");
        List<Facture> factures = factureService.findAll();
        return getall(factures);
    }

    private List<FactureDTO> getall(List<Facture> factures) {
        List<FactureDTO> factureDTOs = new LinkedList<>();
        for (Facture facture :
            factures) {
            Long id = facture.getBonDeSortie().getId();
            BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
            List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.findAllByBonDeSortie(bonDeSortie);

            FactureDTO factureDTO = new FactureDTO(facture.getDateFacture(), facture.getId(), facture.getClient());
            factureDTO.setNormalise(facture.getBonDeSortie().getNumeroFactureNormalise());

            Long montant = 0L;
//            BigDecimal montant=BigDecimal.ZERO;

            for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
//                montant=montant.add(ligneBonDeSortie.getMontantVente());
                montant += ligneBonDeSortie.getPrixDeVente();

            }

            factureDTO.setMontantTotal(montant);

            List<Reglement> reglements = reglementService.findByFacture(facture);
//            BigDecimal montantPaye=BigDecimal.ZERO;
            Long montantPaye = 0L;
            for (Reglement reglement : reglements) {
//                montantPaye=montantPaye.add(reglement.getMontantReglement());
                montantPaye += reglement.getMontantReglement();
            }

            factureDTO.setMontantPaye(montantPaye);

            if (montantPaye.compareTo(montant) != 0) {
                factureDTOs.add(factureDTO);
            }
        }

        return factureDTOs;

    }


    @RequestMapping(value = "/creances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Facture> getAllCreancesThirtyDayAgo(@RequestParam(required = true) Integer critere) {
        log.debug("REST request to get all Factures");
        LocalDate localDate = LocalDate.now();
        return factureService.findAllCreancesThirtyDayAgo(localDate, critere);
    }

    @RequestMapping(value = "/factures-non-reglees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Facture> FindFactureNonReglees(@RequestParam(required = true) Long id) {
        log.debug("REST request to get all Factures");
        return factureService.findFactureNonReglees(id);
    }

    @RequestMapping(value = "/all-factures-client",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Facture> FindAllFactureClient(@RequestParam(required = true) Long id) {
        log.debug("REST request to get all Factures");
        return factureService.findFisrtFacture(id);
    }

    /**
     * GET  /factures/:id : get the "id" facture.
     *
     * @param id the id of the facture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the facture, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/factures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Facture> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        Facture facture = factureService.findOne(id);
        return Optional.ofNullable(facture)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /factures/:id : delete the "id" facture.
     *
     * @param id the id of the facture to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/factures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("facture", id.toString())).build();
    }

    /**
     * SEARCH  /_search/factures?query=:query : search for the facture corresponding
     * to the query.
     *
     * @param query the query of the facture search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/factures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Facture> searchFactures(@RequestParam String query) {
        log.debug("REST request to search Factures for query {}", query);
        return factureService.search(query);
    }


    @RequestMapping(value = "/facture-by-date",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Facture> findFactureByDate(@RequestParam(required = false) String dateDebut,
                                           @RequestParam(required = false) String dateFin) {
        log.debug("REST request to stat facture");
        LocalDate date1 = (dateDebut != null && !"undefined".equals(dateDebut) && !dateDebut.trim().isEmpty()) ? LocalDate.parse(dateDebut) : null;
        LocalDate date2 = (dateFin != null && !"undefined".equals(dateFin) && !dateFin.trim().isEmpty()) ? LocalDate.parse(dateFin) : null;

        return factureService.findFactureByDate(date1, date2);
    }

    @RequestMapping(value = "/factureparperiode",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Facture> getFactureParPeriode(@RequestParam(required = false) String dateDebut,
                                              @RequestParam(required = false) String dateFin) {
        log.debug("REST request to stat facture");
//        LocalDate date1 = (dateDebut!=null && !"undefined".equals(dateDebut) && !dateDebut.trim().isEmpty())?LocalDate.parse(dateDebut):null;
//        LocalDate date2 = (dateFin!=null && !"undefined".equals(dateFin) && !dateFin.trim().isEmpty())?LocalDate.parse(dateFin):null;

        //return null;
        return factureService.getFactureParPeriode(dateDebut, dateFin);
    }

    @RequestMapping(value = "/facture-by-bon-de-sortie/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Facture> factureParBondeSortie(@PathVariable Long id) {
        log.debug("REST request to facture by id bonDeSortie: ");
        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
        Facture facture = factureService.getFactureParBonDeSortie(bonDeSortie);
        return Optional.ofNullable(facture)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "facture/factures-non-solde",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FactureNDto> getFactureNonReglees(@RequestParam String dateDebut, @RequestParam String dateFin) {
        return factureNDtoMapper.toFactureDTOs(factureService.getFactureNonSoldeParPeriode(dateDebut, dateFin));
    }

    @RequestMapping(value = "/facture/{numero}/factures-non-solde",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FactureNDto> getFactureNonReglesParNumero(@PathVariable String numero) {
        return factureNDtoMapper.toFactureDTOs(factureService.getFactureNonSoldeParNumero(numero));
    }

    @RequestMapping(value = "/facture/{categorie}/categorie-creance",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CreanceDto> getCreanceCatégorie(@PathVariable int categorie) {
        return factureService.getFactureCreance(categorie);
    }


    @RequestMapping(value = "facture/factures-non-solde-page",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FactureNDto>> getFactureNonReglees(@RequestParam String dateDebut, @RequestParam String dateFin,
                                                                  Pageable pageable) throws URISyntaxException {
        Page<Facture> page = factureService.getFactureNonSoldeParPeriode(dateDebut, dateFin, pageable);
        Page<FactureNDto> pageNDto = factureNDtoMapper.toFactureDTOsPage(page);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageNDto, "/api/facture/factures-non-solde-page");
        headers.set("X-Total-Count", String.valueOf(pageNDto.getTotalElements()));
        headers.set("X-Total-Page", String.valueOf(pageNDto.getTotalPages()));
        return new ResponseEntity<>(pageNDto.getContent(), headers, HttpStatus.OK);
    }

}
