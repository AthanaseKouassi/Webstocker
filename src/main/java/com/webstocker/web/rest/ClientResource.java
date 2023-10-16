package com.webstocker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.webstocker.domain.Categorieclient;
import com.webstocker.domain.Client;
import com.webstocker.service.CategorieclientService;
import com.webstocker.service.ClientService;
import com.webstocker.service.FactureService;
import com.webstocker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.persistence.NonUniqueResultException;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * REST controller for managing Client.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    @Inject
    private ClientService clientService;

    @Inject
    private FactureService factureService;

    @Inject
    private CategorieclientService categorieclientService;

    /**
     * POST /clients : Create a new client.
     *
     * @param client the client to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new client, or with status 400 (Bad Request) if the client has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to save Client : {}", client);
    
        if (client.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("client", "idexists", "A new client cannot already have an ID")).body(null);
        }       
            Client result = clientService.save(client);
            return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("client", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT /clients : Updates an existing client.
     *
     * @param client the client to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * client, or with status 400 (Bad Request) if the client is not valid, or
     * with status 500 (Internal Server Error) if the client couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/clients",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to update Client : {}", client);
        if (client.getId() == null) {
            return createClient(client);
        }
        Client result = clientService.save(client);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("client", client.getId().toString()))
                .body(result);
    }

    /**
     * GET /clients : get all the clients.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clients
     * in body
     */
    @RequestMapping(value = "/clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Client> getAllClients() {
        log.debug("REST request to get all Clients");
        return clientService.findAll();
    }

    /**
     *
     * @param dateDebut
     * @param dateFin
     * @param nomClient
     * @return
     */
    @RequestMapping(value = "/facture-clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Client> getStatClientsByFacture(@RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin,
            @RequestParam(required = false) String nomClient) {
        log.debug("REST request to stat facture");
        LocalDate date1 = (dateDebut != null && !"undefined".equals(dateDebut) && !dateDebut.trim().isEmpty()) ? LocalDate.parse(dateDebut) : null;
        LocalDate date2 = (dateFin != null && !"undefined".equals(dateFin) && !dateFin.trim().isEmpty()) ? LocalDate.parse(dateFin) : null;
        String noClient = (nomClient != null && !"undefined".equals(nomClient) && !nomClient.trim().isEmpty()) ? nomClient : null;
        System.out.println("La date convertie est " + date1);
        System.out.println("La date2 convertie est " + date2);
        System.out.println("nom client " + noClient);
        return clientService.findStatClientByFacture(date1, date2, noClient);
    }

    @RequestMapping(value = "/facture-client-detail",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Client getClientFactureDetail(@RequestParam(required = true) Long id) {
        log.debug("REST request to detail facture");
        Client client = clientService.findOne(id);
        return client;
    }  

    /**
     * GET /clients/:id : get the "id" client.
     *
     * @param id the id of the client to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the client,
     * or with status 404 (Not Found)
     */
    @RequestMapping(value = "/clients/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Client client = clientService.findOne(id);
        return Optional.ofNullable(client)
                .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /clients/:id : delete the "id" client.
     *
     * @param id the id of the client to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/clients/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("client", id.toString())).build();
    }

    /**
     * SEARCH /_search/clients?query=:query : search for the client
     * corresponding to the query.
     *
     * @param query the query of the client search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Client> searchClients(@RequestParam String query) {
        log.debug("REST request to search Clients for query {}", query);
        return clientService.search(query);
    }

    /**
     *
     * @param nomclient
     * @param telephoneClient
     * @return the ResponseEntity with status 200 (OK) and with body the client,
     * or with status 404 (Not Found)
     */
    @RequestMapping(value = "/clients/clientparNometTelephone",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> trouverClientparNomAndTelephone(@RequestParam(required = false) String nomclient,
            @RequestParam(required = false) String telephoneClient) {
        log.debug("REST request to get Client : {}", nomclient);
        Client client = null;
        try {
            client = clientService.findByNomClientAndTelephoneClient(nomclient, telephoneClient);
            return Optional.ofNullable(client)
                    .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IncorrectResultSizeDataAccessException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        }
    }
    
    @RequestMapping(value = "/clients/retrouver-un-client/{nomClient}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Client> retrouverUnClient(@PathVariable String nomClient) {
        log.debug("REST request to get Client : {}", nomClient );
       Client client = null;       
       client = clientService.trouverClientParNomClient(nomClient);
          if(client == null) {
              System.out.println("NOT FOUND");
          } 
            return Optional.ofNullable(client)
                    .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
         
//        try {
//            client = clientService.trouverClientParNomClient(nomClient);
//            
//            return Optional.ofNullable(client)
//                    .map(result -> new ResponseEntity<>(
//                    result,
//                    HttpStatus.OK))
//                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        } catch (IncorrectResultSizeDataAccessException ex) {
//            return null;
//        } catch (NonUniqueResultException er) {
//            return null;
//        }
    }
    

    /**
     * retourne un client par page
     *
     * @param nomClient
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/retrouver-clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Client> retrouverClientParNom(@RequestParam(required = true) String nomClient,
            @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) {
        log.debug("REST request to search Clients for query {}", nomClient);
        String nom = (nomClient != null && !"undefined".equals(nomClient) && !nomClient.trim().isEmpty()) ? nomClient : null;
        return clientService.trouverUnClientParNom(nom, new PageRequest(page, size));
    }

    /**
     * tous les clients par page
     *
     * @param page
     * @param size
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/tous-les-clients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Page<Client> tousLesClientsParPage(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "3") int size) throws URISyntaxException {
        log.debug("REST request to all clients ");
        return clientService.findAll(new PageRequest(page, size));
    }

    /**
     * retourne les clients d'une catégorie de client
     *
     * @param libelleCategorieclient
     * @return
     */
    @RequestMapping(value = "/client-d-une-categorieclient",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Client> clientDuneCategorieClient(@RequestParam(required = false) String libelleCategorieclient) {
        log.debug("REST request CategorieCleint : ", libelleCategorieclient);
        Categorieclient categorieclient = categorieclientService.findByLibelleCategorieClient(libelleCategorieclient);
        return clientService.findByCategorieclientOrderByNomClientAsc(categorieclient);
    }
}
