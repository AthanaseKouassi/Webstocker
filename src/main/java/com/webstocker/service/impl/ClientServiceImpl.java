package com.webstocker.service.impl;

import com.webstocker.domain.Categorieclient;
import com.webstocker.domain.Client;
import com.webstocker.repository.ClientRepository;
import com.webstocker.repository.search.ClientSearchRepository;
import com.webstocker.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Client.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientSearchRepository clientSearchRepository;

    /**
     * Save a client.
     *
     * @param client the entity to save
     * @return the persisted entity
     */
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        Client result = clientRepository.save(client);
        clientSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the clients.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        log.debug("Request to get all Clients");
        List<Client> result = clientRepository.findAllOrderByNomClientAsc();
//        List<Client> result = clientRepository.findAll();
        return result;
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Client findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        Client client = clientRepository.findOne(id);
        return client;
    }

    /**
     * Delete the  client by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.delete(id);
        clientSearchRepository.delete(id);
    }

    /**
     * Search for the client corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Client> search(String query) {
        log.debug("Request to search Clients for query {}", query);
        return StreamSupport
            .stream(clientSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Client> findStatClientByFacture(LocalDate dateDebut, LocalDate dateFin, String nomClient) {
        return clientRepository.findStatsClientByFacture(dateDebut, dateFin, nomClient);
    }

    @Override
    public Client findByNomClientAndTelephoneClient(String nom, String telephone) {
        Client result = null;
        String nomclient = null;
        String telephoneclient = null;
        nomclient = nom;
        telephoneclient = telephone;
        return result = clientRepository.findByNomClientAndTelephoneClient(nomclient, telephoneclient);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Client> trouverUnClientParNom(String nomClient, Pageable pageable) {
        return clientRepository.findByNomClientContaining(nomClient, pageable);
//        return clientRepository.findByNomClient(nomClient,pageable);
    }

    @Override
    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Client trouverClientParNomClient(String nomClient) {
        Client client = clientRepository.findByNomClient(nomClient);
        return client;
    }

    @Override
    public List<Client> findByCategorieclientOrderByNomClientAsc(Categorieclient categorieclient) {
        return clientRepository.findByCategorieclientOrderByNomClientAsc(categorieclient);
    }

    @Override
    public List<Client> getClientParCommercialPeriod(Long idUser, String dateDebut, String dateFin) {
        return clientRepository.getClientParCommercialAndPeriode(idUser, dateDebut, dateFin);
    }


}
