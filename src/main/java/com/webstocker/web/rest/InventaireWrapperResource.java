package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.InventaireWrapper;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.InventaireWrapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Optional;

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

    @Inject
    private ProduitRepository produitRepository;


    @RequestMapping(value = "/inventaire/quantite-theorique",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public InventaireWrapper getQuantiteTheorique(@RequestParam(required = true) String nomProduit, @RequestParam(required = true) String nomMagasin,
                                                  @RequestParam(required = true) String dateInventaire) {
        log.debug("REST request pour la quantité théorique du produit : {}", nomProduit);

        return inventaireWrapperService.situationDunProduitMagasin(nomProduit, nomMagasin, dateInventaire);
    }

    @RequestMapping(value = "/inventaires/{idProduit}/by-month/{dateInventaire}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InventaireWrapper> getSituationProduit(@PathVariable Long idProduit,
                                                                 @PathVariable String dateInventaire) {
        log.debug("REST request to get Inventaire y idProduit : {}", idProduit);

        Produit produit = produitRepository.findOne(idProduit);
        InventaireWrapper inventaire = inventaireWrapperService.getEtatProduit(produit.getNomProduit(), dateInventaire);

        return Optional.of(inventaire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
