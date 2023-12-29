package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.web.rest.dto.newfeature.DetailFactureDto;
import com.webstocker.web.rest.mapper.newfeature.DetailFactureMapper;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LigneBonDeSortie.
 */
@RestController
@RequestMapping("/api")
public class LigneBonDeSortieResource {

    private static final String ENTITY_NAME = "ligneBonDeSortie";
    private final Logger log = LoggerFactory.getLogger(LigneBonDeSortieResource.class);
    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;
    @Inject
    private DetailFactureMapper detailFactureMapper;
    @Inject
    private BonDeSortieService bonDeSortieService;


    /**
     * POST  /ligne-bon-de-sorties : Create a new ligneBonDeSortie.
     *
     * @param ligneBonDeSortie the ligneBonDeSortie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneBonDeSortie, or with status 400 (Bad Request) if the ligneBonDeSortie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligne-bon-de-sorties",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneBonDeSortie> createLigneBonDeSortie(@Valid @RequestBody LigneBonDeSortie ligneBonDeSortie) throws URISyntaxException {
        log.debug("REST request to save LigneBonDeSortie : {}", ligneBonDeSortie);
        if (ligneBonDeSortie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ligneBonDeSortie cannot already have an ID")).body(null);
        }
        LigneBonDeSortie result = ligneBonDeSortieService.save(ligneBonDeSortie);
        return ResponseEntity.created(new URI("/api/ligne-bon-de-sorties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-bon-de-sorties : Updates an existing ligneBonDeSortie.
     *
     * @param ligneBonDeSortie the ligneBonDeSortie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneBonDeSortie,
     * or with status 400 (Bad Request) if the ligneBonDeSortie is not valid,
     * or with status 500 (Internal Server Error) if the ligneBonDeSortie couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ligne-bon-de-sorties",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneBonDeSortie> updateLigneBonDeSortie(@Valid @RequestBody LigneBonDeSortie ligneBonDeSortie) throws URISyntaxException {
        log.debug("REST request to update LigneBonDeSortie : {}", ligneBonDeSortie);
        if (ligneBonDeSortie.getId() == null) {
            return createLigneBonDeSortie(ligneBonDeSortie);
        }
        LigneBonDeSortie result = ligneBonDeSortieService.save(ligneBonDeSortie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ligneBonDeSortie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-bon-de-sorties : get all the ligneBonDeSorties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneBonDeSorties in body
     * @throws java.net.URISyntaxException
     */
    @RequestMapping(value = "/ligne-bon-de-sorties",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LigneBonDeSortie>> getAllLigneBonDeSorties(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LigneBonDeSorties");
        Page<LigneBonDeSortie> page = ligneBonDeSortieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-bon-de-sorties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-bon-de-sorties/:id : get the "id" ligneBonDeSortie.
     *
     * @param id the id of the ligneBonDeSortie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneBonDeSortie, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ligne-bon-de-sorties/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LigneBonDeSortie> getLigneBonDeSortie(@PathVariable Long id) {
        log.debug("REST request to get LigneBonDeSortie : {}", id);
        LigneBonDeSortie ligneBonDeSortie = ligneBonDeSortieService.findOne(id);
        return Optional.ofNullable(ligneBonDeSortie)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ligne-bon-de-sorties/:id : delete the "id" ligneBonDeSortie.
     *
     * @param id the id of the ligneBonDeSortie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ligne-bon-de-sorties/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLigneBonDeSortie(@PathVariable Long id) {
        log.debug("REST request to delete LigneBonDeSortie : {}", id);
        ligneBonDeSortieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-bon-de-sorties?query=:query : search for the ligneBonDeSortie corresponding
     * to the query.
     *
     * @param query    the query of the ligneBonDeSortie search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ligne-bon-de-sorties",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LigneBonDeSortie>> searchLigneBonDeSorties(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LigneBonDeSorties for query {}", query);
        Page<LigneBonDeSortie> page = ligneBonDeSortieService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-bon-de-sorties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * calcul la quantité total des produit vendu en un mois
     *
     * @param dateObjectif
     * @param nomProduit
     * @return
     */
    @RequestMapping(value = "/ligne-bon-de-sorties/sommequantitevendueparmois/{dateObjectif}/{nomProduit}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Long totalQuantiteVenduParMois(@PathVariable String dateObjectif, @PathVariable String nomProduit) {
        log.debug("REST request to get totalQuantiteVendueParMois : {} ,Produit : {}", dateObjectif, nomProduit);
        Long quantiteTotalVenduMois = 0L;
        try {
            quantiteTotalVenduMois = ligneBonDeSortieService.quantiteTotalProduitVenduParMois(dateObjectif, nomProduit);
        } catch (NumberFormatException er) {
            log.error("Le message d'erreur : {}", er.getMessage());
        }
        return quantiteTotalVenduMois;
    }

    /**
     * retourne la liste des produit à une période
     *
     * @param dateDebut
     * @param dateFin
     * @return
     */
    @RequestMapping(value = "/ligne-bon-de-sorties/facturesparperiode",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LigneBonDeSortie> getAllFactureParPeriode(@RequestParam(required = false) String dateDebut,
                                                          @RequestParam(required = false) String dateFin) {
        log.debug("Les factures de la période de :: {} à {}", dateDebut, dateFin);
        return ligneBonDeSortieService.getAllFactureParPeriode(dateDebut, dateFin);
    }

    //    @RequestMapping(value = "/ligne-bon-de-sorties/facture-par-date",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public Page<LigneBonDeSortie> listeFactureParPeriode(@RequestParam(required=false)String dateDebut,
//            @RequestParam(required=false)String dateFin,
//            @RequestParam(name="page",defaultValue = "0")int page,
//            @RequestParam(name="size",defaultValue = "3")int size){
//        log.debug("REST request to get all factures d'une période : {}", dateDebut, dateFin);
//        return ligneBonDeSortieService.getListVenteParDate(dateDebut, dateFin,new PageRequest(page, size));
//    }
    @RequestMapping(value = "/ligne-bon-de-sorties/facture-par-numeroFacture",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<LigneBonDeSortie> factureParNumeroFacture(@RequestParam(required = false) String numeroFacture,
                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                          @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to get all factures by numerofacturenormalise : {}", numeroFacture);
        return ligneBonDeSortieService.getListVenteParNumeroFacture(numeroFacture, new PageRequest(page, size));
    }

    @RequestMapping(value = "/ligne-bon-de-sorties/facture-par-date",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LigneBonDeSortie> getFactureParDate(@RequestParam(required = false) String dateDebut,
                                                    @RequestParam(required = false) String dateFin) {
        log.debug("REST request to get all factures d'une période : {} a {}", dateDebut, dateFin);
        return ligneBonDeSortieService.findFactureParPeriode(dateDebut, dateFin);
    }

    @RequestMapping(value = "/ligne-bon-de-sorties/lignetransfert/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LigneBonDeSortie> getLigneTransfert(@PathVariable Long id) {
        log.debug("REST request to get LigneBonDeSortie : {}", id);
        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
        return ligneBonDeSortieService.recupererLignesBonDeSortieTransfert(bonDeSortie);
    }

    @RequestMapping(value = "/ligne-bon-de-sorties/lignepromotion/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LigneBonDeSortie> getLignePromotion(@PathVariable Long id) {
        log.debug("REST request to get LigneBonDeSortie : {}", id);
        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
        return ligneBonDeSortieService.recupererLignesBonDeSortiePromotion(bonDeSortie);
    }

    @RequestMapping(value = "/ligne-bon-de-sorties/lignevente/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LigneBonDeSortie> getLigneVente(@PathVariable Long id) {
        log.debug("REST request to get LigneBonDeSortie : {}", id);

        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
        return ligneBonDeSortieService.recupererLignesFacture(bonDeSortie);
    }

    /**
     * recuperation d'une ligne bon de sortie perte
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/ligne-bon-de-sorties/ligneperte/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LigneBonDeSortie> getLignePerte(@PathVariable Long id) {
        log.debug("REST request to get LigneBonDeSortie : {}", id);
        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
        return ligneBonDeSortieService.recupererLignesPerte(bonDeSortie);
    }

    @RequestMapping(value = "/ligne-bds/detail-facture-reglement", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LigneBonDeSortie> getDetailFactureAregle(@RequestParam Long idFacture) {
        log.debug("Id de facture : {}", idFacture);
        return ligneBonDeSortieService.getDetailFactureNonReglee(idFacture);
    }

    @RequestMapping(value = "/ligne-bds/detail-facture", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DetailFactureDto> getDetailFacture(@RequestParam Long idFacture) {
        log.debug("Id de facture : {}", idFacture);
        return ligneBonDeSortieService.getDetailFacture(idFacture);
    }

}
