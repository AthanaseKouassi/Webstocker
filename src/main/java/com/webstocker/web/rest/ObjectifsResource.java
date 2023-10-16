package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Objectifs;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.service.ObjectifsService;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import com.webstocker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Objectifs.
 */
@RestController
@RequestMapping("/api")
public class ObjectifsResource {

    private final Logger log = LoggerFactory.getLogger(ObjectifsResource.class);

    @Inject
    private ObjectifsService objectifsService;

    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;

    /**
     * POST /objectifs : Create a new objectifs.
     *
     * @param objectifs the objectifs to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new objectifs, or with status 400 (Bad Request) if the objectifs has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/objectifs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objectifs> createObjectifs(@RequestBody Objectifs objectifs) throws URISyntaxException {
        log.debug("REST request to save Objectifs : {}", objectifs);
        if (objectifs.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("objectifs", "idexists", "A new objectifs cannot already have an ID")).body(null);
        }
        Objectifs result = objectifsService.save(objectifs);
        return ResponseEntity.created(new URI("/api/objectifs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("objectifs", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /objectifs : Updates an existing objectifs.
     *
     * @param objectifs the objectifs to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * objectifs, or with status 400 (Bad Request) if the objectifs is not
     * valid, or with status 500 (Internal Server Error) if the objectifs
     * couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/objectifs",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objectifs> updateObjectifs(@RequestBody Objectifs objectifs) throws URISyntaxException {
        log.debug("REST request to update Objectifs : {}", objectifs);
        if (objectifs.getId() == null) {
            return createObjectifs(objectifs);
        }

        Objectifs result = objectifsService.save(objectifs);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("objectifs", objectifs.getId().toString()))
                .body(result);
    }

    /**
     * GET /objectifs : get all the objectifs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of objectifs
     * in body
     */
    @RequestMapping(value = "/objectifs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objectifs> getAllObjectifs() {
        log.debug("REST request to get all Objectifs");
        return objectifsService.findAll();
    }

    /**
     * GET /objectifs/:id : get the "id" objectifs.
     *
     * @param id the id of the objectifs to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * objectifs, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/objectifs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Objectifs> getObjectifs(@PathVariable Long id) {
        log.debug("REST request to get Objectifs : {}", id);
        Objectifs objectifs = objectifsService.findOne(id);
        return Optional.ofNullable(objectifs)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /objectifs/:id : delete the "id" objectifs.
     *
     * @param id the id of the objectifs to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/objectifs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteObjectifs(@PathVariable Long id) {
        log.debug("REST request to delete Objectifs : {}", id);
        objectifsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("objectifs", id.toString())).build();
    }

    /**
     * SEARCH /_search/objectifs?query=:query : search for the objectifs
     * corresponding to the query.
     *
     * @param query the query of the objectifs search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/objectifs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objectifs> searchObjectifs(@RequestParam String query) {
        log.debug("REST request to search Objectifs for query {}", query);
        return objectifsService.search(query);
    }

    /**
     * Retourne la liste des objectifs de ventes mis à jours
     *
     * @return
     */
    @RequestMapping(value = "/objectifs/objectifs_mettre_a_jour",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objectifs> listeDesObjectifsAmettreaJour() throws URISyntaxException {
        log.debug("REST request to get all Objectifs à mettre à jour");
        int size = 0;
        String periode = null;
        Long quantiteObtenue = 0L;
        Long quantiteAttendue = 0L;
        double taux = 0;
        List<Objectifs> listObjectifs = objectifsService.lesObjectifsAMettreAjour();
        size = listObjectifs.size();
        periode = listObjectifs.get(size).getPeriode().toString();
        
        System.out.println("LISTES DES OBJECTIFS "+ listObjectifs);
        System.out.println("LA PERIODE DES OBJECTIFS "+ periode);
        System.out.println("LA TAILLE DE LA LISTE DES OBJECTIFS "+ size);
        
        Iterator it = listObjectifs.iterator();
        while (it.hasNext()) {
            Objectifs objet = (Objectifs) it.next();
            // if (objet.getQuantiteObtenu() == null) {

            try {
                quantiteObtenue = ligneBonDeSortieService.quantiteTotalProduitVenduParMois(objet.getPeriode().toString(), objet.getProduit().getNomProduit());
            } catch (NumberFormatException er) {
                er.printStackTrace();
            }
//            if (quantiteObtenue != null) {

                quantiteAttendue = objet.getQuantiteAttendue();
                System.out.println("LA QUANTITE ATTENDUE " + quantiteAttendue);
                System.out.println("LA QUANTITE OBTENUE " + quantiteObtenue);
                taux = (quantiteObtenue.doubleValue() / quantiteAttendue.doubleValue()) * 100;

                System.out.println("LE TAUX " + taux);

                objet.setQuantiteObtenu(quantiteObtenue);
                objet.setTaux(String.valueOf(taux));
                updateObjectifs(objet);
//            } else {
//                quantiteObtenue = 0L;
//                taux = 0;
//                System.out.println("LA QUANTITE OBTENUE " + quantiteObtenue);
//                System.out.println("LE TAUX " + taux);
//                objet.setQuantiteObtenu(quantiteObtenue);
//                objet.setTaux(String.valueOf(taux));
//                updateObjectifs(objet);
//            }
            //}
            quantiteObtenue = 0L;
            quantiteAttendue = 0L;
            taux = 0;
        }

//         return objectifsService.lesObjectifsAMettreAjour();
        return objectifsService.getObjectifsDuMois(periode);
    }
    
    
    @RequestMapping(value = "/objectifs/mise-a-jour-taux-objectifs/{madate}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objectifs> tauxMisAjour(@PathVariable String madate) throws URISyntaxException {
        log.debug("REST request to get all Objectifs à mettre à jour");
        int size = 0;
        String periode = null;
        Long quantiteObtenue = 0L;
        Long quantiteAttendue = 0L;
        double taux = 0;
        
        String dateDebut;
        String dateFin;

        PremierEtDernierJourDuMois dateUtilitaire = new PremierEtDernierJourDuMois();

        List<Objectifs> listObjectifs = objectifsService.getObjectifsDuMois(madate);
        size = listObjectifs.size();
//        periode = listObjectifs.get(0).getPeriode().toString();
        
        System.out.println("LISTES DES OBJECTIFS "+ listObjectifs);
        System.out.println("LA PERIODE DES OBJECTIFS "+ periode);
        System.out.println("LA TAILLE DE LA LISTE DES OBJECTIFS "+ size);
        
        Iterator it = listObjectifs.iterator();
        while (it.hasNext()) {
            Objectifs objet = (Objectifs) it.next();
            // if (objet.getQuantiteObtenu() == null) {

            try {
                quantiteObtenue = ligneBonDeSortieService.quantiteTotalProduitVenduParMois(objet.getPeriode().toString(), objet.getProduit().getNomProduit());
            } catch (NumberFormatException er) {
                er.printStackTrace();
            }
            if (quantiteObtenue != null) {

                quantiteAttendue = objet.getQuantiteAttendue();
                System.out.println("LA QUANTITE ATTENDUE " + quantiteAttendue);
                System.out.println("LA QUANTITE OBTENUE " + quantiteObtenue);
                taux = (quantiteObtenue.doubleValue() / quantiteAttendue.doubleValue()) * 100;

                System.out.println("LE TAUX " + taux);

                objet.setQuantiteObtenu(quantiteObtenue);
                objet.setTaux(String.valueOf(taux));
                updateObjectifs(objet);
            } else {
                quantiteObtenue = 0L;
                taux = 0;
                System.out.println("LA QUANTITE OBTENUE " + quantiteObtenue);
                System.out.println("LE TAUX " + taux);
                objet.setQuantiteObtenu(quantiteObtenue);
                objet.setTaux(String.valueOf(taux));
                updateObjectifs(objet);
            }
            //}
            quantiteObtenue = 0L;
            quantiteAttendue = 0L;
            taux = 0;
        }

//         return objectifsService.lesObjectifsAMettreAjour();
        return objectifsService.getObjectifsDuMois(madate);
    }

    @RequestMapping(value = "/objectifs/objectifquantiteobtenue/{madate}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Objectifs> getObjectifsDuMois(@PathVariable String madate) {
        log.debug("REST request to get Objectifs à mettre à jour : {}", madate);
        List<Objectifs> objectif = null;
        if (madate.isEmpty()) {
            System.out.println("La date ne doit pas être null");
        } else {
            objectif = objectifsService.getObjectifsDuMois(madate);
        }

        return objectif;
    }

}
