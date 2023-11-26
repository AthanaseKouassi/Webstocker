package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Client;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.Region;
import com.webstocker.domain.wrapper.StockSortieWrapper;
import com.webstocker.service.*;
import com.webstocker.web.rest.dto.StockPrixDTO;
import com.webstocker.web.rest.dto.StockRestantDTO;
import com.webstocker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by komi on 09/09/16.
 */
@RestController
@RequestMapping("/api")
public class StockOperationsResource {

    private final Logger log = LoggerFactory.getLogger(RegionResource.class);

    @Inject
    private ProduitService produitService;

    @Inject
    private MagasinService magasinService;

    @Inject
    private StockOperationsService stockOperationsService;

    @Inject
    private ClientService clientService;

    @Inject
    private PrixService prixService;


    /**
     *
     * @param id
     * @param quantite
     * @return
     */
    @RequestMapping(value = "/stockop/{id}/{quantite}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StockSortieWrapper>> getProduitsParQuantiteDispo(@PathVariable Long id, @PathVariable Long quantite) {
        log.debug("REST request to get sorties wrappers : {}", id);
        Produit produit = produitService.findOne(id);
        List<StockSortieWrapper> sortieWrapperList = stockOperationsService.faireUneSortie(produit, quantite);

        return new ResponseEntity<>(sortieWrapperList, null, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/stockop/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockRestantDTO> getRegion(@PathVariable Long id) {
        log.debug("REST request to get product quantity : {}", id);
        Produit produit = produitService.findOne(id);
        Long availableProducts = stockOperationsService.getAvailableProducts(produit);
        StockRestantDTO stockRestantDTO=new StockRestantDTO();
        stockRestantDTO.setQuantiteStockProduit(availableProducts);

        return new ResponseEntity<>(stockRestantDTO, null, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/stockopm/{id}/{magasinID}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockRestantDTO> getStockProduitMagasin(@PathVariable Long id,@PathVariable Long magasinID) {
        log.debug("REST request to get product quantity par magasin : {}", id);
        Produit produit = produitService.findOne(id);
        Magasin magasin = magasinService.findOne(magasinID);
        Long availableProducts = stockOperationsService.getAvailableProductsByMagasin(produit,magasin);
        StockRestantDTO stockRestantDTO=new StockRestantDTO();
        stockRestantDTO.setQuantiteStockProduit(availableProducts);

        return new ResponseEntity<>(stockRestantDTO, null, HttpStatus.OK);
    }

    /**
     *
     * @param idClient
     * @param idProduit
     * @return
     */
    @RequestMapping(value = "/stockop/prixclient/{idClient}/{idProduit}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPrixDTO> getPrixClient(@PathVariable Long idClient, @PathVariable Long idProduit) {
        log.debug("REST request to get prix client : {}", idClient);

        Client client = clientService.findOne(idClient);
        Produit produit = produitService.findOne(idProduit);

        BigDecimal prixUnitaire = prixService.findPrixClientParProduit(client, produit);

        StockPrixDTO stockPrixDTO=new StockPrixDTO();
        stockPrixDTO.setPrixUnitaire(prixUnitaire);

        return new ResponseEntity<>(stockPrixDTO, null, HttpStatus.OK);
    }


    /**
     *
     * @param idClient
     * @param idProduit
     * @return
     */
    @RequestMapping(value = "/stockop/prixclientvalide/{idClient}/{idProduit}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPrixDTO> getPrixClientValide(@PathVariable Long idClient, @PathVariable Long idProduit) {
        log.debug("REST request to get prix client : {}", idClient);

        Client client = clientService.findOne(idClient);
        Produit produit = produitService.findOne(idProduit);

        BigDecimal prixUnitaire = prixService.findPrixClientParProduitValide(client, produit);

        StockPrixDTO stockPrixDTO=new StockPrixDTO();
        stockPrixDTO.setPrixUnitaire(prixUnitaire);

        return new ResponseEntity<>(stockPrixDTO, null, HttpStatus.OK);
    }
}

