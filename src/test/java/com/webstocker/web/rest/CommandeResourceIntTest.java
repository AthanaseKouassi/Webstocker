package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Commande;
import com.webstocker.repository.CommandeRepository;
import com.webstocker.service.CommandeService;
import com.webstocker.repository.search.CommandeSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.webstocker.domain.enumeration.StatutCommande;

/**
 * Test class for the CommandeResource REST controller.
 *
 * @see CommandeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class CommandeResourceIntTest {


    private static final LocalDate DEFAULT_DATE_COMMANDE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMMANDE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_NUMERO_COMMANDE = "AAAAA";
    private static final String UPDATED_NUMERO_COMMANDE = "BBBBB";

    private static final Long DEFAULT_QUANTITE_COMMANDE = 1L;
    private static final Long UPDATED_QUANTITE_COMMANDE = 2L;

    private static final Long DEFAULT_VALEUR_COMMANDE = 1L;
    private static final Long UPDATED_VALEUR_COMMANDE = 2L;

    private static final StatutCommande DEFAULT_STATUT = StatutCommande.EN_COURS;
    private static final StatutCommande UPDATED_STATUT = StatutCommande.PARTIELLEMENT_LIVREE;

    @Inject
    private CommandeRepository commandeRepository;

    @Inject
    private CommandeService commandeService;

    @Inject
    private CommandeSearchRepository commandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommandeMockMvc;

    private Commande commande;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommandeResource commandeResource = new CommandeResource();
        ReflectionTestUtils.setField(commandeResource, "commandeService", commandeService);
        this.restCommandeMockMvc = MockMvcBuilders.standaloneSetup(commandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        commandeSearchRepository.deleteAll();
        commande = new Commande();
        commande.setDateCommande(DEFAULT_DATE_COMMANDE);
        commande.setNumeroCommande(DEFAULT_NUMERO_COMMANDE);
        commande.setQuantiteCommande(DEFAULT_QUANTITE_COMMANDE);
        commande.setValeurCommande(DEFAULT_VALEUR_COMMANDE);
        commande.setStatut(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande

        restCommandeMockMvc.perform(post("/api/commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commande)))
                .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandes.get(commandes.size() - 1);
        assertThat(testCommande.getDateCommande()).isEqualTo(DEFAULT_DATE_COMMANDE);
        assertThat(testCommande.getNumeroCommande()).isEqualTo(DEFAULT_NUMERO_COMMANDE);
        assertThat(testCommande.getQuantiteCommande()).isEqualTo(DEFAULT_QUANTITE_COMMANDE);
        assertThat(testCommande.getValeurCommande()).isEqualTo(DEFAULT_VALEUR_COMMANDE);
        assertThat(testCommande.getStatut()).isEqualTo(DEFAULT_STATUT);

        // Validate the Commande in ElasticSearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void checkDateCommandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setDateCommande(null);

        // Create the Commande, which fails.

        restCommandeMockMvc.perform(post("/api/commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commande)))
                .andExpect(status().isBadRequest());

        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroCommandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setNumeroCommande(null);

        // Create the Commande, which fails.

        restCommandeMockMvc.perform(post("/api/commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commande)))
                .andExpect(status().isBadRequest());

        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValeurCommandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setValeurCommande(null);

        // Create the Commande, which fails.

        restCommandeMockMvc.perform(post("/api/commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commande)))
                .andExpect(status().isBadRequest());

        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandes
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateCommande").value(hasItem(DEFAULT_DATE_COMMANDE.toString())))
                .andExpect(jsonPath("$.[*].numeroCommande").value(hasItem(DEFAULT_NUMERO_COMMANDE.toString())))
                .andExpect(jsonPath("$.[*].quantiteCommande").value(hasItem(DEFAULT_QUANTITE_COMMANDE.intValue())))
                .andExpect(jsonPath("$.[*].valeurCommande").value(hasItem(DEFAULT_VALEUR_COMMANDE.intValue())))
                .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    public void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.dateCommande").value(DEFAULT_DATE_COMMANDE.toString()))
            .andExpect(jsonPath("$.numeroCommande").value(DEFAULT_NUMERO_COMMANDE.toString()))
            .andExpect(jsonPath("$.quantiteCommande").value(DEFAULT_QUANTITE_COMMANDE.intValue()))
            .andExpect(jsonPath("$.valeurCommande").value(DEFAULT_VALEUR_COMMANDE.intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = new Commande();
        updatedCommande.setId(commande.getId());
        updatedCommande.setDateCommande(UPDATED_DATE_COMMANDE);
        updatedCommande.setNumeroCommande(UPDATED_NUMERO_COMMANDE);
        updatedCommande.setQuantiteCommande(UPDATED_QUANTITE_COMMANDE);
        updatedCommande.setValeurCommande(UPDATED_VALEUR_COMMANDE);
        updatedCommande.setStatut(UPDATED_STATUT);

        restCommandeMockMvc.perform(put("/api/commandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommande)))
                .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandes.get(commandes.size() - 1);
        assertThat(testCommande.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
        assertThat(testCommande.getNumeroCommande()).isEqualTo(UPDATED_NUMERO_COMMANDE);
        assertThat(testCommande.getQuantiteCommande()).isEqualTo(UPDATED_QUANTITE_COMMANDE);
        assertThat(testCommande.getValeurCommande()).isEqualTo(UPDATED_VALEUR_COMMANDE);
        assertThat(testCommande.getStatut()).isEqualTo(UPDATED_STATUT);

        // Validate the Commande in ElasticSearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void deleteCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Get the commande
        restCommandeMockMvc.perform(delete("/api/commandes/{id}", commande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean commandeExistsInEs = commandeSearchRepository.exists(commande.getId());
        assertThat(commandeExistsInEs).isFalse();

        // Validate the database is empty
        List<Commande> commandes = commandeRepository.findAll();
        assertThat(commandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        // Search the commande
        restCommandeMockMvc.perform(get("/api/_search/commandes?query=id:" + commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCommande").value(hasItem(DEFAULT_DATE_COMMANDE.toString())))
            .andExpect(jsonPath("$.[*].numeroCommande").value(hasItem(DEFAULT_NUMERO_COMMANDE.toString())))
            .andExpect(jsonPath("$.[*].quantiteCommande").value(hasItem(DEFAULT_QUANTITE_COMMANDE.intValue())))
            .andExpect(jsonPath("$.[*].valeurCommande").value(hasItem(DEFAULT_VALEUR_COMMANDE.intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }
}
