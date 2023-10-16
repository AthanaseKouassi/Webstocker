package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.wrapper.SortieParProduitMagasinWrapper;
import com.webstocker.service.SortieParProduitMagasinService;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class SortieparProduitMagasinResource {

    private final Logger log = LoggerFactory.getLogger(SortieparProduitMagasinResource.class);
    
    @Inject
    private SortieParProduitMagasinService sortieParProduitMagasinService;
    
     @RequestMapping(value = "/vente-par-produit-par-magasin",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SortieParProduitMagasinWrapper> lesVenteParProduitMagasin(@RequestParam(required=false)String nomProduit,
            @RequestParam(required=false)String nomMagasin,
            @RequestParam(required=false)String dateDebut,
            @RequestParam(required=false)String dateFin) {
        log.debug("REST request list of vente par produit par magasin {}");
        
        return sortieParProduitMagasinService.venteParProduitparMagasin(nomProduit, nomMagasin, dateDebut, dateFin);
    }
    
    
     @RequestMapping(value = "/promotion-par-produit-par-magasin",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SortieParProduitMagasinWrapper> lesPromotionParProduitMagasin(@RequestParam(required=false)String nomProduit,
            @RequestParam(required=false)String nomMagasin,
            @RequestParam(required=false)String dateDebut,
            @RequestParam(required=false)String dateFin) {
        log.debug("REST request list of promotion par produit par magasin {}");
        
        return sortieParProduitMagasinService.promotionParProduitparMagasin(nomProduit, nomMagasin, dateDebut, dateFin);
    }
    
    
     @RequestMapping(value = "/transfert-par-produit-par-magasin",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SortieParProduitMagasinWrapper> lesTransfertParProduitMagasin(@RequestParam(required=false)String nomProduit,
            @RequestParam(required=false)String nomMagasin,
            @RequestParam(required=false)String dateDebut,
            @RequestParam(required=false)String dateFin) {
        log.debug("REST request list of promotion par produit par magasin {}");
        
        return sortieParProduitMagasinService.transfertParProduitparMagasin(nomProduit, nomMagasin, dateDebut, dateFin);
    }
    
     @RequestMapping(value = "/perte-par-produit-par-magasin",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SortieParProduitMagasinWrapper> lesPerteParProduitMagasin(@RequestParam(required=false)String nomProduit,
            @RequestParam(required=false)String nomMagasin,
            @RequestParam(required=false)String dateDebut,
            @RequestParam(required=false)String dateFin) {
        log.debug("REST request list of promotion par produit par magasin {}");
        
        return sortieParProduitMagasinService.perteParProduitparMagasin(nomProduit, nomMagasin, dateDebut, dateFin);
    }

}
