package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.wrapper.InventaireWrapper;
import com.webstocker.service.InventaireWrapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * REST controller for managing Categorie.
 *
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class InventaireWrapperResource {

    private final Logger log = LoggerFactory.getLogger(InventaireWrapperResource.class);

    @Inject
    private InventaireWrapperService inventaireWrapperService;


    @RequestMapping(value = "/inventaire/quantite-theorique",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public InventaireWrapper getQuantiteTheorique(@RequestParam(required = true) String nomProduit, @RequestParam(required = true) String nomMagasin,
                                                  @RequestParam(required = true) String dateInventaire) {
        log.debug("REST request pour la quantité théorique du produit : {}", nomProduit);

        return inventaireWrapperService.situationDunProduitMagasin(nomProduit, nomMagasin, dateInventaire);
    }


}
