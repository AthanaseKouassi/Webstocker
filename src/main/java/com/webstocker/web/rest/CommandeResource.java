package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignecommande;
import com.webstocker.domain.enumeration.StatutCommande;
import com.webstocker.service.CommandeService;
import com.webstocker.web.rest.dto.CommandeDTO;
import com.webstocker.web.rest.dto.CommandeFournisseur;
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
 * REST controller for managing Commande.
 */
@RestController
@RequestMapping("/api")
public class CommandeResource {

    private final Logger log = LoggerFactory.getLogger(CommandeResource.class);

    @Inject
    private CommandeService commandeService;

    /**
     * POST  /commandes : Create a new commande.
     *
     * @param commande the commande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commande, or with status 400 (Bad Request) if the commande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commandes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commande> createCommande(@Valid @RequestBody Commande commande) throws URISyntaxException {
        log.debug("REST request to save Commande : {}", commande);
        if (commande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commande", "idexists", "A new commande cannot already have an ID")).body(null);
        }
        Commande result = commandeService.save(commande);
        return ResponseEntity.created(new URI("/api/commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commande", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /commandes : Create a new commandeFournisseur.
     *
     * @param commandeFournisseur the commandeFournisseur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commandeFournisseur, or with status 400 (Bad Request) if the commandeFournisseur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commandesFournisseur",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commande> createCommandeForunisseur(@Valid @RequestBody CommandeFournisseur commandeFournisseur) throws URISyntaxException {
        log.debug("REST request to save Commande : {}", commandeFournisseur);
        if (commandeFournisseur.getCommande().getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commandeFournisseur", "idexists", "A new commandeFournisseur cannot already have an ID")).body(null);
        }

        Commande commande=commandeFournisseur.getCommande();
        List<Lignecommande> lignecommandes = commandeFournisseur.getLignecommandes();

        for(Lignecommande lignecommande: lignecommandes){
            commande.addLigneCommande(lignecommande);
        }


        Commande result = commandeService.save(commande);
        return ResponseEntity.created(new URI("/api/commandesFournisseur/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commandeFournisseur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commandes : Updates an existing commande.
     *
     * @param commande the commande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commande,
     * or with status 400 (Bad Request) if the commande is not valid,
     * or with status 500 (Internal Server Error) if the commande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commandes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commande> updateCommande(@Valid @RequestBody Commande commande) throws URISyntaxException {
        log.debug("REST request to update Commande : {}", commande);
        if (commande.getId() == null) {
            return createCommande(commande);
        }
        Commande result = commandeService.save(commande);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commande", commande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commandes : get all the commandes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of commandes in body
     */
    @RequestMapping(value = "/commandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commande> getAllCommandes() {
        log.debug("REST request to get all Commandes");
        return commandeService.findAll();
    }

    /**
     * GET  /commandes/:id : get the "id" commande.
     *
     * @param id the id of the commande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commande, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/commandes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commande> getCommande(@PathVariable Long id) {
        log.debug("REST request to get Commande : {}", id);
        Commande commande = commandeService.findOne(id);
        return Optional.ofNullable(commande)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commandes/:id : delete the "id" commande.
     *
     * @param id the id of the commande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/commandes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        log.debug("REST request to delete Commande : {}", id);
        commandeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/commandes?query=:query : search for the commande corresponding
     * to the query.
     *
     * @param query the query of the commande search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/commandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commande> searchCommandes(@RequestParam String query) {
        log.debug("REST request to search Commandes for query {}", query);
        return commandeService.search(query);
    }
    /**
     * SEARCH  /_search/commandes?query=:query : search for the commande corresponding
     * to the query.
     *
     * @param query the query of the commande search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/commandes/active",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commande> searchCommandesEnCours(@RequestParam String query) {
        log.debug("REST request to search Commandes for query {}", query);
        StatutCommande.valueOf(query);
        return commandeService.findActiveCommandes(StatutCommande.valueOf(query));
    }

    @RequestMapping(value = "/commandes/livraison/restant",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommandeDTO> quantiteRestateAlivrer(@RequestParam Long id) {
        log.debug("REST request to search Commandes quantite restant a livrer for {}", id);
//        Commande commande = commandeService.findOne(id);
//        String jsonValue="{qte :"++" }";
        CommandeDTO commandeDTO=new CommandeDTO();
        commandeDTO.setQuantiteRestanteAlivrer(commandeService.quantiteRestanteAlivrer(id));
        return Optional.ofNullable(commandeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
