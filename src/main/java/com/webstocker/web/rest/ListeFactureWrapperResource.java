package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.wrapper.ListeFactureWrapper;
import com.webstocker.service.ListeFactureWrapperService;
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
 * REST controller for managing ListeFactureWrapper.
 */
@RestController
@RequestMapping("/api")
public class ListeFactureWrapperResource {
    
     private final Logger log = LoggerFactory.getLogger(ListeFactureWrapperResource.class);
     
    @Inject
    private ListeFactureWrapperService listeFactureWrapperService;
    
     
    @RequestMapping(value = "/liste-facture-par-periode",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ListeFactureWrapper> trouverFactureParPeriode(@RequestParam(required=false)String dateDebut,
            @RequestParam(required=false)String dateFin) {
        log.debug("REST request list of facture by {}");
        return listeFactureWrapperService.trouverFactureAunePeriode(dateDebut, dateFin);
    }
    
    @RequestMapping(value = "/facture-par-numeroFacture",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ListeFactureWrapper> trouverUneFacture(@RequestParam(required=false)String numeroFacture) {
        log.debug("REST request list of facture by {}");
        return listeFactureWrapperService.trouverFactureParNumeroFacture(numeroFacture);
    }
    
    @RequestMapping(value = "/facture-zero-par-numeroFacture",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ListeFactureWrapper> trouverFactureZeroParNumeroFacture(@RequestParam(required=false)String numeroFacture) {
        log.debug("REST request list of facture-zero by {}",numeroFacture);
        return listeFactureWrapperService.trouverFactureZeroParNumeroFacture(numeroFacture);
    }
    
      @RequestMapping(value = "/liste-facture-zero-par-periode",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ListeFactureWrapper> trouverFactureZeroParPeriode(@RequestParam(required=false)String dateDebut,
            @RequestParam(required=false)String dateFin) {
        log.debug("REST request list of facture by {}");
        return listeFactureWrapperService.trouverFactureZeroParPeriode(dateDebut, dateFin);
    }
}
