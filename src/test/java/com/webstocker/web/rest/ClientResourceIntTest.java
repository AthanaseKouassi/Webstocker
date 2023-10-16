package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Client;
import com.webstocker.repository.ClientRepository;
import com.webstocker.service.ClientService;
import com.webstocker.repository.search.ClientSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ClientResource REST controller.
 *
 * @see ClientResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class ClientResourceIntTest {

    private static final String DEFAULT_NOM_CLIENT = "AAAAA";
    private static final String UPDATED_NOM_CLIENT = "BBBBB";
    private static final String DEFAULT_TELEPHONE_CLIENT = "AAAAA";
    private static final String UPDATED_TELEPHONE_CLIENT = "BBBBB";
    private static final String DEFAULT_BOITEPOSTALE = "AAAAA";
    private static final String UPDATED_BOITEPOSTALE = "BBBBB";
    private static final String DEFAULT_LOCALITE_CLIENT = "AAAAA";
    private static final String UPDATED_LOCALITE_CLIENT = "BBBBB";

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientService clientService;

    @Inject
    private ClientSearchRepository clientSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientMockMvc;

    private Client client;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientResource clientResource = new ClientResource();
        ReflectionTestUtils.setField(clientResource, "clientService", clientService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clientSearchRepository.deleteAll();
        client = new Client();
        client.setNomClient(DEFAULT_NOM_CLIENT);
        client.setTelephoneClient(DEFAULT_TELEPHONE_CLIENT);
        client.setBoitepostale(DEFAULT_BOITEPOSTALE);
//        client.setLocaliteClient(DEFAULT_LOCALITE_CLIENT);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client

        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getNomClient()).isEqualTo(DEFAULT_NOM_CLIENT);
        assertThat(testClient.getTelephoneClient()).isEqualTo(DEFAULT_TELEPHONE_CLIENT);
        assertThat(testClient.getBoitepostale()).isEqualTo(DEFAULT_BOITEPOSTALE);
//        assertThat(testClient.getLocaliteClient()).isEqualTo(DEFAULT_LOCALITE_CLIENT);

        // Validate the Client in ElasticSearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToComparingFieldByField(testClient);
    }

    @Test
    @Transactional
    public void checkNomClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setNomClient(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isBadRequest());

        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clients
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT.toString())))
                .andExpect(jsonPath("$.[*].telephoneClient").value(hasItem(DEFAULT_TELEPHONE_CLIENT.toString())))
                .andExpect(jsonPath("$.[*].boitepostale").value(hasItem(DEFAULT_BOITEPOSTALE.toString())))
                .andExpect(jsonPath("$.[*].localiteClient").value(hasItem(DEFAULT_LOCALITE_CLIENT.toString())));
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.nomClient").value(DEFAULT_NOM_CLIENT.toString()))
            .andExpect(jsonPath("$.telephoneClient").value(DEFAULT_TELEPHONE_CLIENT.toString()))
            .andExpect(jsonPath("$.boitepostale").value(DEFAULT_BOITEPOSTALE.toString()))
            .andExpect(jsonPath("$.localiteClient").value(DEFAULT_LOCALITE_CLIENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = new Client();
        updatedClient.setId(client.getId());
        updatedClient.setNomClient(UPDATED_NOM_CLIENT);
        updatedClient.setTelephoneClient(UPDATED_TELEPHONE_CLIENT);
        updatedClient.setBoitepostale(UPDATED_BOITEPOSTALE);
//        updatedClient.setLocaliteClient(UPDATED_LOCALITE_CLIENT);

        restClientMockMvc.perform(put("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClient)))
                .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getNomClient()).isEqualTo(UPDATED_NOM_CLIENT);
        assertThat(testClient.getTelephoneClient()).isEqualTo(UPDATED_TELEPHONE_CLIENT);
        assertThat(testClient.getBoitepostale()).isEqualTo(UPDATED_BOITEPOSTALE);
//        assertThat(testClient.getLocaliteClient()).isEqualTo(UPDATED_LOCALITE_CLIENT);

        // Validate the Client in ElasticSearch
        Client clientEs = clientSearchRepository.findOne(testClient.getId());
        assertThat(clientEs).isEqualToComparingFieldByField(testClient);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clientExistsInEs = clientSearchRepository.exists(client.getId());
        assertThat(clientExistsInEs).isFalse();

        // Validate the database is empty
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        // Search the client
        restClientMockMvc.perform(get("/api/_search/clients?query=id:" + client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].telephoneClient").value(hasItem(DEFAULT_TELEPHONE_CLIENT.toString())))
            .andExpect(jsonPath("$.[*].boitepostale").value(hasItem(DEFAULT_BOITEPOSTALE.toString())))
            .andExpect(jsonPath("$.[*].localiteClient").value(hasItem(DEFAULT_LOCALITE_CLIENT.toString())));
    }
}
