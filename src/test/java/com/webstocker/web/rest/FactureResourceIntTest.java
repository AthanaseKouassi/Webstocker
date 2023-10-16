package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Facture;
import com.webstocker.repository.FactureRepository;
import com.webstocker.service.FactureService;
import com.webstocker.repository.search.FactureSearchRepository;

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


/**
 * Test class for the FactureResource REST controller.
 *
 * @see FactureResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class FactureResourceIntTest {


    private static final LocalDate DEFAULT_DATE_FACTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FACTURE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VALEUR_REMISE = 1;
    private static final Integer UPDATED_VALEUR_REMISE = 2;

    private static final Integer DEFAULT_DELAI_PAIEMENT = 1;
    private static final Integer UPDATED_DELAI_PAIEMENT = 2;

    private static final LocalDate DEFAULT_DATE_LIMITE_PAIEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIMITE_PAIEMENT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private FactureRepository factureRepository;

    @Inject
    private FactureService factureService;

    @Inject
    private FactureSearchRepository factureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFactureMockMvc;

    private Facture facture;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FactureResource factureResource = new FactureResource();
        ReflectionTestUtils.setField(factureResource, "factureService", factureService);
        this.restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        factureSearchRepository.deleteAll();
        facture = new Facture();
        facture.setDateFacture(DEFAULT_DATE_FACTURE);
        facture.setValeurRemise(DEFAULT_VALEUR_REMISE);
        facture.setDelaiPaiement(DEFAULT_DELAI_PAIEMENT);
        facture.setDateLimitePaiement(DEFAULT_DATE_LIMITE_PAIEMENT);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture

        restFactureMockMvc.perform(post("/api/factures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(facture)))
                .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factures.get(factures.size() - 1);
        assertThat(testFacture.getDateFacture()).isEqualTo(DEFAULT_DATE_FACTURE);
        assertThat(testFacture.getValeurRemise()).isEqualTo(DEFAULT_VALEUR_REMISE);
        assertThat(testFacture.getDelaiPaiement()).isEqualTo(DEFAULT_DELAI_PAIEMENT);
        assertThat(testFacture.getDateLimitePaiement()).isEqualTo(DEFAULT_DATE_LIMITE_PAIEMENT);

        // Validate the Facture in ElasticSearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void checkDateFactureIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateFacture(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(facture)))
                .andExpect(status().isBadRequest());

        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateLimitePaiementIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateLimitePaiement(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(facture)))
                .andExpect(status().isBadRequest());

        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factures
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateFacture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
                .andExpect(jsonPath("$.[*].valeurRemise").value(hasItem(DEFAULT_VALEUR_REMISE)))
                .andExpect(jsonPath("$.[*].delaiPaiement").value(hasItem(DEFAULT_DELAI_PAIEMENT)))
                .andExpect(jsonPath("$.[*].dateLimitePaiement").value(hasItem(DEFAULT_DATE_LIMITE_PAIEMENT.toString())));
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.dateFacture").value(DEFAULT_DATE_FACTURE.toString()))
            .andExpect(jsonPath("$.valeurRemise").value(DEFAULT_VALEUR_REMISE))
            .andExpect(jsonPath("$.delaiPaiement").value(DEFAULT_DELAI_PAIEMENT))
            .andExpect(jsonPath("$.dateLimitePaiement").value(DEFAULT_DATE_LIMITE_PAIEMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = new Facture();
        updatedFacture.setId(facture.getId());
        updatedFacture.setDateFacture(UPDATED_DATE_FACTURE);
        updatedFacture.setValeurRemise(UPDATED_VALEUR_REMISE);
        updatedFacture.setDelaiPaiement(UPDATED_DELAI_PAIEMENT);
        updatedFacture.setDateLimitePaiement(UPDATED_DATE_LIMITE_PAIEMENT);

        restFactureMockMvc.perform(put("/api/factures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFacture)))
                .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factures.get(factures.size() - 1);
        assertThat(testFacture.getDateFacture()).isEqualTo(UPDATED_DATE_FACTURE);
        assertThat(testFacture.getValeurRemise()).isEqualTo(UPDATED_VALEUR_REMISE);
        assertThat(testFacture.getDelaiPaiement()).isEqualTo(UPDATED_DELAI_PAIEMENT);
        assertThat(testFacture.getDateLimitePaiement()).isEqualTo(UPDATED_DATE_LIMITE_PAIEMENT);

        // Validate the Facture in ElasticSearch
        Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Get the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean factureExistsInEs = factureSearchRepository.exists(facture.getId());
        assertThat(factureExistsInEs).isFalse();

        // Validate the database is empty
        List<Facture> factures = factureRepository.findAll();
        assertThat(factures).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        // Search the facture
        restFactureMockMvc.perform(get("/api/_search/factures?query=id:" + facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFacture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].valeurRemise").value(hasItem(DEFAULT_VALEUR_REMISE)))
            .andExpect(jsonPath("$.[*].delaiPaiement").value(hasItem(DEFAULT_DELAI_PAIEMENT)))
            .andExpect(jsonPath("$.[*].dateLimitePaiement").value(hasItem(DEFAULT_DATE_LIMITE_PAIEMENT.toString())));
    }
}
