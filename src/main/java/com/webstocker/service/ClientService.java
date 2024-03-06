package com.webstocker.service;

import com.webstocker.domain.Categorieclient;
import com.webstocker.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing Client.
 */
public interface ClientService {

    /**
     * Save a client.
     *
     * @param client the entity to save
     * @return the persisted entity
     */
    Client save(Client client);

    /**
     * Get all the clients.
     *
     * @return the list of entities
     */
    List<Client> findAll();

    List<Client> findStatClientByFacture(LocalDate dateDebut, LocalDate dateFin, String nomClient);

    /**
     * Get the "id" client.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Client findOne(Long id);

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the client corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    List<Client> search(String query);

    Page<Client> findAll(Pageable pageable);

    Client findByNomClientAndTelephoneClient(String nom, String telephone);

    Page<Client> trouverUnClientParNom(String nomClient, Pageable pageable);

    Client trouverClientParNomClient(String nomClient);

    /**
     * les clients d'une catégorie
     *
     * @param categorieclient
     * @return
     */
    List<Client> findByCategorieclientOrderByNomClientAsc(Categorieclient categorieclient);

    List<Client> getClientParCommercialPeriod(Long idUser, String dateDebut, String dateFin);
}
