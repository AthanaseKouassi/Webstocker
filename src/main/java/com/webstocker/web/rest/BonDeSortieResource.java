package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.enumeration.StatusTransfert;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.web.rest.dto.BonDeSortieDTO;
import com.webstocker.web.rest.util.HeaderUtil;
import com.webstocker.web.rest.util.PaginationUtil;
import java.math.BigDecimal;
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
import org.springframework.data.domain.PageRequest;

/**
 * REST controller for managing BonDeSortie.
 */
@RestController
@RequestMapping("/api")
public class BonDeSortieResource {

    private final Logger log = LoggerFactory.getLogger(BonDeSortieResource.class);

    @Inject
    private BonDeSortieService bonDeSortieService;

    /**
     * POST /bon-de-sorties : Create a new bonDeSortie.
     *
     * @param bonDeSortie the bonDeSortie to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new bonDeSortie, or with status 400 (Bad Request) if the bonDeSortie has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bon-de-sorties",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> createBonDeSortie(@Valid @RequestBody BonDeSortie bonDeSortie) throws URISyntaxException {
        log.debug("REST request to save BonDeSortie : {}", bonDeSortie);
        if (bonDeSortie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bonDeSortie", "idexists", "A new bonDeSortie cannot already have an ID")).body(null);
        }
        BonDeSortie result = bonDeSortieService.save(bonDeSortie);
        return ResponseEntity.created(new URI("/api/bon-de-sorties/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("bonDeSortie", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /bon-de-sorties : Updates an existing bonDeSortie.
     *
     * @param bonDeSortie the bonDeSortie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * bonDeSortie, or with status 400 (Bad Request) if the bonDeSortie is not
     * valid, or with status 500 (Internal Server Error) if the bonDeSortie
     * couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bon-de-sorties",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> updateBonDeSortie(@Valid @RequestBody BonDeSortie bonDeSortie) throws URISyntaxException {
        log.debug("REST request to update BonDeSortie : {}", bonDeSortie);
        if (bonDeSortie.getId() == null) {
            return createBonDeSortie(bonDeSortie);
        }
        BonDeSortie result = bonDeSortieService.save(bonDeSortie);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("bonDeSortie", bonDeSortie.getId().toString()))
                .body(result);
    }

//    @RequestMapping(value = "/bon-de-livraisons",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public List<BonDeSortie> getAllBonDeLivraisons() {
//        log.debug("REST request to get all Livraisons");
//        return bonDeSortieService.findAll();
//    }
    @RequestMapping(value = "/bon-de-livraisons",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getAllBonDeLivraisons(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size)
            throws URISyntaxException {
        log.debug("REST request to get a page of BonDeSorties");
        Page<BonDeSortie> listBonsDeSortie = bonDeSortieService.findAll(new PageRequest(page, size));
        return listBonsDeSortie;
    }

    /**
     * GET /bon-de-sorties : get all the bonDeSorties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of
     * bonDeSorties in body
     * @throws URISyntaxException if there is an error to generate the
     * pagination HTTP headers
     */
    @RequestMapping(value = "/bon-de-sorties",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BonDeSortie>> getAllBonDeSorties(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of BonDeSorties");
        Page<BonDeSortie> page = bonDeSortieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bon-de-sorties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /bon-de-sorties/:id : get the "id" bonDeSortie.
     *
     * @param id the id of the bonDeSortie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * bonDeSortie, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bon-de-sorties/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> getBonDeSortie(@PathVariable Long id) {
        log.debug("REST request to get BonDeSortie : {}", id);
        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);
        return Optional.ofNullable(bonDeSortie)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /bon-de-sorties/:id : delete the "id" bonDeSortie.
     *
     * @param id the id of the bonDeSortie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bon-de-sorties/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBonDeSortie(@PathVariable Long id) {
        log.debug("REST request to delete BonDeSortie : {}", id);
        bonDeSortieService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bonDeSortie", id.toString())).build();
    }

    /**
     * SEARCH /_search/bon-de-sorties?query=:query : search for the bonDeSortie
     * corresponding to the query.
     *
     * @param query the query of the bonDeSortie search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/bon-de-sorties",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BonDeSortie>> searchBonDeSorties(@RequestParam String query, Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to search for a page of BonDeSorties for query {}", query);
        Page<BonDeSortie> page = bonDeSortieService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bon-de-sorties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST /commandes : Create a new commandeFournisseur.
     *
     * @param bonDeSortieDTO the BonDeSortieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new BonDeSortieDTO, or with status 400 (Bad Request) if the
     * BonDeSortieDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/venteClient",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> createVenteClient(@Valid @RequestBody BonDeSortieDTO bonDeSortieDTO) throws URISyntaxException {
        //log.debug("REST request to save BonDeSortie : {}", bonDeSortieDTO.toString());
        log .info("\nTESSTTTTT LE BON DE SORTIE :: ",bonDeSortieDTO.toString());
        if (bonDeSortieDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("venteClient", "idexists", "A new venteClient cannot already have an ID")).body(null);
        }

        BonDeSortie bonDeSortie = bonDeSortieDTO.createBonDeSortieVente();
        List<LigneBonDeSortie> ligneBonDeSorties = bonDeSortieDTO.getLigneBonDeSorties();

        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            bonDeSortie.addLigneBonDeSortie(ligneBonDeSortie);
        }

        bonDeSortie.setTypeSortie(TypeSortie.VENTE);

        BonDeSortie result = bonDeSortieService.saveVente(bonDeSortie, bonDeSortieDTO.getDelaiPaiement(), bonDeSortieDTO.getRemise(), bonDeSortieDTO.getDateFacture());
        return ResponseEntity.created(new URI("/api/venteClient/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("VenteClient", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /venteClient : Updates an existing bonDeSortie.
     *
     * @param bonDeSortieDTO the bonDeSortie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * bonDeSortie, or with status 400 (Bad Request) if the bonDeSortie is not
     * valid, or with status 500 (Internal Server Error) if the bonDeSortie
     * couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/venteClient",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> updateVenteClient(@Valid @RequestBody BonDeSortieDTO bonDeSortieDTO) throws URISyntaxException {
        log.debug("REST request to update Vente Client : {}", bonDeSortieDTO);
        if (bonDeSortieDTO.getId() == null) {

            return createVenteClient(bonDeSortieDTO);
        }
//        BonDeSortie result = bonDeSortieService.save(bonDeSortie);

        BonDeSortie bonDeSortie = bonDeSortieDTO.createBonDeSortieVente();
        List<LigneBonDeSortie> ligneBonDeSorties = bonDeSortieDTO.getLigneBonDeSorties();

        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            bonDeSortie.addLigneBonDeSortie(ligneBonDeSortie);
        }

        bonDeSortie.setTypeSortie(TypeSortie.VENTE);

        BonDeSortie result = bonDeSortieService.saveVente(bonDeSortie, bonDeSortieDTO.getDelaiPaiement(), bonDeSortieDTO.getRemise(), bonDeSortieDTO.getDateFacture());
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("bonDeSortie", bonDeSortie.getId().toString()))
                .body(result);
    }

    /**
     * POST /transfertProduit : Create a new BonDeSortie pour Transfert.
     *
     * @param bonDeSortieDTO the BonDeSortieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new BonDeSortieDTO, or with status 400 (Bad Request) if the
     * BonDeSortieDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/transfertProduit",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> createTransfertProduit(@Valid @RequestBody BonDeSortieDTO bonDeSortieDTO) throws URISyntaxException {
        log.debug("REST request to save Commande : {}", bonDeSortieDTO);
        if (bonDeSortieDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("transfertProduit", "idexists", "A new transfertProduit cannot already have an ID")).body(null);
        }

        BonDeSortie bonDeSortie = bonDeSortieDTO.createBonDeSortie();
        List<LigneBonDeSortie> ligneBonDeSorties = bonDeSortieDTO.getLigneBonDeSorties();

        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            ligneBonDeSortie.setPrixVente(BigDecimal.ZERO);
            /**
             * Ajouter la methode setPrixDeVente(0L);
             */
            ligneBonDeSortie.setPrixDeVente(0L);
            bonDeSortie.addLigneBonDeSortie(ligneBonDeSortie);
        }

        bonDeSortie.setTypeSortie(TypeSortie.TRANSFERT);
        bonDeSortie.setStatusTranfert(StatusTransfert.ENCOURS);

        BonDeSortie result = bonDeSortieService.save(bonDeSortie);
        return ResponseEntity.created(new URI("/api/transfertProduit/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("transfertProduit", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /transfertProduit : Updates an existing bonDeSortie.
     *
     * @param bonDeSortieDTO the bonDeSortie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * bonDeSortie, or with status 400 (Bad Request) if the bonDeSortie is not
     * valid, or with status 500 (Internal Server Error) if the bonDeSortie
     * couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/transfertProduit",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> updateTransfertProduit(@Valid @RequestBody BonDeSortieDTO bonDeSortieDTO) throws URISyntaxException {
        log.debug("REST request to update Transfert : {}", bonDeSortieDTO);
        if (bonDeSortieDTO.getId() == null) {

            return createTransfertProduit(bonDeSortieDTO);
        }
//        BonDeSortie result = bonDeSortieService.save(bonDeSortie);



        BonDeSortie bonDeSortie = bonDeSortieDTO.createBonDeSortie();
        List<LigneBonDeSortie> ligneBonDeSorties = bonDeSortieDTO.getLigneBonDeSorties();

        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            ligneBonDeSortie.setPrixVente(BigDecimal.ZERO);
            /**
             * Ajouter la methode setPrixDeVente(0L);
             */
            ligneBonDeSortie.setPrixDeVente(0L);
            bonDeSortie.addLigneBonDeSortie(ligneBonDeSortie);
        }

        bonDeSortie.setTypeSortie(TypeSortie.TRANSFERT);

        System.out.println("LE STATUS DU TRANSFERT "+bonDeSortie.getStatusTranfert());

        BonDeSortie result = bonDeSortieService.save(bonDeSortie);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("bonDeSortie", bonDeSortie.getId().toString()))
                .body(result);
    }

    /**
     * POST /promotionProduit : Create a new bonDeSortieDTO.
     *
     * @param bonDeSortieDTO the BonDeSortieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new BonDeSortieDTO, or with status 400 (Bad Request) if the
     * BonDeSortieDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/promotionProduit",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> createPromotionProduit(@Valid @RequestBody BonDeSortieDTO bonDeSortieDTO) throws URISyntaxException {
        log.debug("REST request to save Commande : {}", bonDeSortieDTO);
        if (bonDeSortieDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("promotionProduit", "idexists", "A new promotionProduit cannot already have an ID")).body(null);
        }

        BonDeSortie bonDeSortie = bonDeSortieDTO.createBonDeSortie();
        System.out.println("LE BONDE SORTIE OOO " + bonDeSortie);
        List<LigneBonDeSortie> ligneBonDeSorties = bonDeSortieDTO.getLigneBonDeSorties();

        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            ligneBonDeSortie.setPrixVente(BigDecimal.ZERO);
            /**
             * Ajouter la methode setPrixDeVente(0L);
             */
            ligneBonDeSortie.setPrixDeVente(0L);
            bonDeSortie.addLigneBonDeSortie(ligneBonDeSortie);
        }

        bonDeSortie.setTypeSortie(TypeSortie.PROMOTION);

//        BonDeSortie result = bonDeSortieService.save(bonDeSortie);
        BonDeSortie result = bonDeSortieService.savePromotion(bonDeSortie, bonDeSortieDTO.getDateFacture());
//        System.out.println("LA DATE AHHHH " + bonDeSortieDTO.getDateFacture());
//        System.out.println("LE BON DE SORTIE OOOH  " + result);
        return ResponseEntity.created(new URI("/api/promotionProduit/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("promotionProduit", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/promotionProduit",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> updatePromotionProduit(@Valid @RequestBody BonDeSortieDTO bonDeSortieDTO) throws URISyntaxException {
        log.debug("REST request to update promotion : {}", bonDeSortieDTO);
        if (bonDeSortieDTO.getId() == null) {
            return createPromotionProduit(bonDeSortieDTO);
        }

        BonDeSortie bonDeSortie = bonDeSortieDTO.createBonDeSortie();
        System.out.println("La promotion " + bonDeSortie);
        List<LigneBonDeSortie> ligneBonDeSorties = bonDeSortieDTO.getLigneBonDeSorties();

        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            ligneBonDeSortie.setPrixVente(BigDecimal.ZERO);
            /**
             * Ajouter la methode setPrixDeVente(0L);
             */
            ligneBonDeSortie.setPrixDeVente(0L);
            bonDeSortie.addLigneBonDeSortie(ligneBonDeSortie);
        }

        bonDeSortie.setTypeSortie(TypeSortie.PROMOTION);

        BonDeSortie result = bonDeSortieService.savePromotion(bonDeSortie, bonDeSortieDTO.getDateFacture());

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("bonDeSortie", bonDeSortie.getId().toString()))
                .body(result);
    }

    /**
     * POST /commandes : Create a new commandeFournisseur.
     *
     * @param bonDeSortieDTO the BonDeSortieDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new BonDeSortieDTO, or with status 400 (Bad Request) if the
     * BonDeSortieDTO has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/perteProduit",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BonDeSortie> createPerteProduit(@Valid @RequestBody BonDeSortieDTO bonDeSortieDTO) throws URISyntaxException {
        log.debug("REST request to save perte : {}", bonDeSortieDTO);
        if (bonDeSortieDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("perteProduit", "idexists", "A new perteProduit cannot already have an ID")).body(null);
        }

        BonDeSortie bonDeSortie = bonDeSortieDTO.createBonDeSortie();
        List<LigneBonDeSortie> ligneBonDeSorties = bonDeSortieDTO.getLigneBonDeSorties();

        for (LigneBonDeSortie ligneBonDeSortie : ligneBonDeSorties) {
            ligneBonDeSortie.setPrixVente(BigDecimal.ZERO);
            ligneBonDeSortie.setPrixDeVente(0L);
            bonDeSortie.addLigneBonDeSortie(ligneBonDeSortie);
        }

        bonDeSortie.setTypeSortie(TypeSortie.PERTE);

        BonDeSortie result = bonDeSortieService.save(bonDeSortie);
        return ResponseEntity.created(new URI("/api/perteProduit/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("perteProduit", result.getId().toString()))
                .body(result);
    }

    /**
     * retourne les bons de sortie et bordereaux de livraison par magasin entre
     * les date dateDebut et dateFin
     *
     * @param nomMagasin
     * @param dateDebut
     * @param dateFin
     * @return
     */
    @RequestMapping(value = "/bondesortieparmagasin/{nomMagasin}/{dateDebut}/{dateFin}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BonDeSortie> bonDeSortieParMagasin(@PathVariable String nomMagasin, @PathVariable String dateDebut, @PathVariable String dateFin) {
        log.debug("REST request to list of BonDeSortie par Magasin : {}", nomMagasin);
        return bonDeSortieService.bonEtBordereauParMagasin(nomMagasin, dateDebut, dateFin);
    }

    @RequestMapping(value = "/bon-de-livraisons-numero",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBonDeLivraisonsByNumber(@RequestParam(required = true) String numeroBon,
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to get all Livraisons");
        String numero = (numeroBon != null && !"undefined".equals(numeroBon) && !numeroBon.trim().isEmpty()) ? numeroBon : null;
        return bonDeSortieService.findBonByNumero(numero, new PageRequest(page, size));
    }

    @RequestMapping(value = "/transfert-par-magasin/{nomMagasin}/{dateDebut}/{dateFin}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BonDeSortie> listeTransfertParMagasin(@PathVariable String nomMagasin, @PathVariable String dateDebut, @PathVariable String dateFin) {
        log.debug("REST request to list of BonDeSortie par Magasin : {}", nomMagasin);
        return bonDeSortieService.listeTransfertParMagasin(nomMagasin, dateDebut, dateFin);
    }

    /**
     * retourne les bons de sortie vente
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/bon-de-sortie-vente",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBonDeSortieVente(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size) {
        log.debug("REST request to BonDeSortie vente");
        return bonDeSortieService.listeBonDeSortieVente(new PageRequest(page, size));
    }

    /**
     * retourne les bon de sortie promotion
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/bon-de-sortie-promotion",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBonDeSortiePromotion(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size) {
        log.debug("REST request to BonDeSortie promotion");
        return bonDeSortieService.listeBonDeSortiePromotion(new PageRequest(page, size));
    }

    /**
     * retourne tous les bon de sortie promotion
     * @return
     */
    @RequestMapping(value = "/tout-bon-de-sortie-promotion",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BonDeSortie> getAllBonDeSortiePromotion() {
        log.debug("REST request to BonDeSortie promotion");
        return bonDeSortieService.listeBonDeSortiePromotion();
    }

    /**
     * retourne les bon de sortie transfert
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/bon-de-sortie-transfert",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBonDeSortieVenteTransfert(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size) {
        log.debug("REST request to BonDeSortie vente");
        return bonDeSortieService.listeBonDeSortieTransfert(new PageRequest(page, size));
    }

    @RequestMapping(value = "/bon-de-sortie-perte",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBonDeSortiePerte(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size) {
        log.debug("REST request to BonDeSortie Perte");
        return bonDeSortieService.listeBonDeSortiePerte(new PageRequest(page, size));
    }

    @RequestMapping(value = "/bon-de-sortie-vente/trouver-par-numero",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBondeSortieVenteParNumero(@RequestParam(required = true) String numeroBon,
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to get all Livraisons");
        String numero = (numeroBon != null && !"undefined".equals(numeroBon) && !numeroBon.trim().isEmpty()) ? numeroBon : null;
        return bonDeSortieService.retrouverBonDeSortieVenteParNumero(numero, new PageRequest(page, size));
    }

    @RequestMapping(value = "/bon-de-sortie-promotion/trouver-par-numero",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBondeSortiePromotionParnumero(@RequestParam(required = true) String numeroBon,
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to get all bon de sortie promotion");
        String numero = (numeroBon != null && !"undefined".equals(numeroBon) && !numeroBon.trim().isEmpty()) ? numeroBon : null;
        return bonDeSortieService.retrouverBonDeSortiePromotionParNumero(numero, new PageRequest(page, size));
    }

    /**
     *
     * @param numeroBon
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/bon-de-sortie-transfert/trouver-par-numero",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBondeSortieTransfertParNumero(@RequestParam(required = true) String numeroBon,
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to get all bon de sortie transfert");
        String numero = (numeroBon != null && !"undefined".equals(numeroBon) && !numeroBon.trim().isEmpty()) ? numeroBon : null;
        return bonDeSortieService.retrouverBonDeSortieTRansfertParNumero(numero, new PageRequest(page, size));
    }

    /**
     *
     * @param numeroBon
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/bon-de-sortie-perte/trouver-par-numero",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getBondeSortiePerteParNumero(@RequestParam(required = true) String numeroBon,
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to get all bon de sortie transfert");
        String numero = (numeroBon != null && !"undefined".equals(numeroBon) && !numeroBon.trim().isEmpty()) ? numeroBon : null;
        return bonDeSortieService.retrouverBonDeSortiePerteParNumero(numero, new PageRequest(page, size));
    }

    /**
     *
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/bon-de-sortie-transfert/transfert-encours",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<BonDeSortie> getTransfertEncours(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size){
        log.debug("REST request to get all bon de sortie transfert enCours");
        return bonDeSortieService.transfertEncours(new PageRequest(page, size));
    }

}
