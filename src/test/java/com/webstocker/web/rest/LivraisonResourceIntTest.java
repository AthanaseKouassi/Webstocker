package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Livraison;
import com.webstocker.repository.LivraisonRepository;
import com.webstocker.service.LivraisonService;
import com.webstocker.repository.search.LivraisonSearchRepository;

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
 * Test class for the LivraisonResource REST controller.
 *
 * @see LivraisonResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class LivraisonResourceIntTest {


    private static final LocalDate DEFAULT_DATE_LIVRAISON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIVRAISON = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DESCRIPTION_LIVRAISON = "AAAAA";
    private static final String UPDATED_DESCRIPTION_LIVRAISON = "BBBBB";
    private static final String DEFAULT_NUMERO_LIVRAISON = "AAAAA";
    private static final String UPDATED_NUMERO_LIVRAISON = "BBBBB";

    private static final Long DEFAULT_VALEUR_LIVRAISON = 1L;
    private static final Long UPDATED_VALEUR_LIVRAISON = 2L;

    private static final Long DEFAULT_FRAIS_TEST = 1L;
    private static final Long UPDATED_FRAIS_TEST = 2L;

    private static final Long DEFAULT_FRAIS_TRANSIT = 1L;
    private static final Long UPDATED_FRAIS_TRANSIT = 2L;

    private static final Long DEFAULT_FRAIS_ASSURANCE_LOCALE = 1L;
    private static final Long UPDATED_FRAIS_ASSURANCE_LOCALE = 2L;

    private static final Long DEFAULT_FRAIS_MANUTENTION = 1L;
    private static final Long UPDATED_FRAIS_MANUTENTION = 2L;

    @Inject
    private LivraisonRepository livraisonRepository;

    @Inject
    private LivraisonService livraisonService;

    @Inject
    private LivraisonSearchRepository livraisonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLivraisonMockMvc;

    private Livraison livraison;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LivraisonResource livraisonResource = new LivraisonResource();
        ReflectionTestUtils.setField(livraisonResource, "livraisonService", livraisonService);
        this.restLivraisonMockMvc = MockMvcBuilders.standaloneSetup(livraisonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        livraisonSearchRepository.deleteAll();
        livraison = new Livraison();
        livraison.setDateLivraison(DEFAULT_DATE_LIVRAISON);
        livraison.setDescriptionLivraison(DEFAULT_DESCRIPTION_LIVRAISON);
        livraison.setNumeroLivraison(DEFAULT_NUMERO_LIVRAISON);
        livraison.setValeurLivraison(DEFAULT_VALEUR_LIVRAISON);
        livraison.setFraisTest(DEFAULT_FRAIS_TEST);
        livraison.setFraisTransit(DEFAULT_FRAIS_TRANSIT);
        livraison.setFraisAssuranceLocale(DEFAULT_FRAIS_ASSURANCE_LOCALE);
        livraison.setFraisManutention(DEFAULT_FRAIS_MANUTENTION);
    }

    @Test
    @Transactional
    public void createLivraison() throws Exception {
        int databaseSizeBeforeCreate = livraisonRepository.findAll().size();

        // Create the Livraison

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isCreated());

        // Validate the Livraison in the database
        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeCreate + 1);
        Livraison testLivraison = livraisons.get(livraisons.size() - 1);
        assertThat(testLivraison.getDateLivraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
        assertThat(testLivraison.getDescriptionLivraison()).isEqualTo(DEFAULT_DESCRIPTION_LIVRAISON);
        assertThat(testLivraison.getNumeroLivraison()).isEqualTo(DEFAULT_NUMERO_LIVRAISON);
        assertThat(testLivraison.getValeurLivraison()).isEqualTo(DEFAULT_VALEUR_LIVRAISON);
        assertThat(testLivraison.getFraisTest()).isEqualTo(DEFAULT_FRAIS_TEST);
        assertThat(testLivraison.getFraisTransit()).isEqualTo(DEFAULT_FRAIS_TRANSIT);
        assertThat(testLivraison.getFraisAssuranceLocale()).isEqualTo(DEFAULT_FRAIS_ASSURANCE_LOCALE);
        assertThat(testLivraison.getFraisManutention()).isEqualTo(DEFAULT_FRAIS_MANUTENTION);

        // Validate the Livraison in ElasticSearch
        Livraison livraisonEs = livraisonSearchRepository.findOne(testLivraison.getId());
        assertThat(livraisonEs).isEqualToComparingFieldByField(testLivraison);
    }

    @Test
    @Transactional
    public void checkDateLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().size();
        // set the field null
        livraison.setDateLivraison(null);

        // Create the Livraison, which fails.

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isBadRequest());

        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().size();
        // set the field null
        livraison.setNumeroLivraison(null);

        // Create the Livraison, which fails.

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isBadRequest());

        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValeurLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().size();
        // set the field null
        livraison.setValeurLivraison(null);

        // Create the Livraison, which fails.

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isBadRequest());

        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFraisTestIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().size();
        // set the field null
        livraison.setFraisTest(null);

        // Create the Livraison, which fails.

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isBadRequest());

        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFraisTransitIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().size();
        // set the field null
        livraison.setFraisTransit(null);

        // Create the Livraison, which fails.

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isBadRequest());

        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFraisAssuranceLocaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().size();
        // set the field null
        livraison.setFraisAssuranceLocale(null);

        // Create the Livraison, which fails.

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isBadRequest());

        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFraisManutentionIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonRepository.findAll().size();
        // set the field null
        livraison.setFraisManutention(null);

        // Create the Livraison, which fails.

        restLivraisonMockMvc.perform(post("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(livraison)))
                .andExpect(status().isBadRequest());

        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLivraisons() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        // Get all the livraisons
        restLivraisonMockMvc.perform(get("/api/livraisons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(livraison.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
                .andExpect(jsonPath("$.[*].descriptionLivraison").value(hasItem(DEFAULT_DESCRIPTION_LIVRAISON.toString())))
                .andExpect(jsonPath("$.[*].numeroLivraison").value(hasItem(DEFAULT_NUMERO_LIVRAISON.toString())))
                .andExpect(jsonPath("$.[*].valeurLivraison").value(hasItem(DEFAULT_VALEUR_LIVRAISON.intValue())))
                .andExpect(jsonPath("$.[*].fraisTest").value(hasItem(DEFAULT_FRAIS_TEST.intValue())))
                .andExpect(jsonPath("$.[*].fraisTransit").value(hasItem(DEFAULT_FRAIS_TRANSIT.intValue())))
                .andExpect(jsonPath("$.[*].fraisAssuranceLocale").value(hasItem(DEFAULT_FRAIS_ASSURANCE_LOCALE.intValue())))
                .andExpect(jsonPath("$.[*].fraisManutention").value(hasItem(DEFAULT_FRAIS_MANUTENTION.intValue())));
    }

    @Test
    @Transactional
    public void getLivraison() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        // Get the livraison
        restLivraisonMockMvc.perform(get("/api/livraisons/{id}", livraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(livraison.getId().intValue()))
            .andExpect(jsonPath("$.dateLivraison").value(DEFAULT_DATE_LIVRAISON.toString()))
            .andExpect(jsonPath("$.descriptionLivraison").value(DEFAULT_DESCRIPTION_LIVRAISON.toString()))
            .andExpect(jsonPath("$.numeroLivraison").value(DEFAULT_NUMERO_LIVRAISON.toString()))
            .andExpect(jsonPath("$.valeurLivraison").value(DEFAULT_VALEUR_LIVRAISON.intValue()))
            .andExpect(jsonPath("$.fraisTest").value(DEFAULT_FRAIS_TEST.intValue()))
            .andExpect(jsonPath("$.fraisTransit").value(DEFAULT_FRAIS_TRANSIT.intValue()))
            .andExpect(jsonPath("$.fraisAssuranceLocale").value(DEFAULT_FRAIS_ASSURANCE_LOCALE.intValue()))
            .andExpect(jsonPath("$.fraisManutention").value(DEFAULT_FRAIS_MANUTENTION.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLivraison() throws Exception {
        // Get the livraison
        restLivraisonMockMvc.perform(get("/api/livraisons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLivraison() throws Exception {
        // Initialize the database
        livraisonService.save(livraison);

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();

        // Update the livraison
        Livraison updatedLivraison = new Livraison();
        updatedLivraison.setId(livraison.getId());
        updatedLivraison.setDateLivraison(UPDATED_DATE_LIVRAISON);
        updatedLivraison.setDescriptionLivraison(UPDATED_DESCRIPTION_LIVRAISON);
        updatedLivraison.setNumeroLivraison(UPDATED_NUMERO_LIVRAISON);
        updatedLivraison.setValeurLivraison(UPDATED_VALEUR_LIVRAISON);
        updatedLivraison.setFraisTest(UPDATED_FRAIS_TEST);
        updatedLivraison.setFraisTransit(UPDATED_FRAIS_TRANSIT);
        updatedLivraison.setFraisAssuranceLocale(UPDATED_FRAIS_ASSURANCE_LOCALE);
        updatedLivraison.setFraisManutention(UPDATED_FRAIS_MANUTENTION);

        restLivraisonMockMvc.perform(put("/api/livraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLivraison)))
                .andExpect(status().isOk());

        // Validate the Livraison in the database
        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisons.get(livraisons.size() - 1);
        assertThat(testLivraison.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testLivraison.getDescriptionLivraison()).isEqualTo(UPDATED_DESCRIPTION_LIVRAISON);
        assertThat(testLivraison.getNumeroLivraison()).isEqualTo(UPDATED_NUMERO_LIVRAISON);
        assertThat(testLivraison.getValeurLivraison()).isEqualTo(UPDATED_VALEUR_LIVRAISON);
        assertThat(testLivraison.getFraisTest()).isEqualTo(UPDATED_FRAIS_TEST);
        assertThat(testLivraison.getFraisTransit()).isEqualTo(UPDATED_FRAIS_TRANSIT);
        assertThat(testLivraison.getFraisAssuranceLocale()).isEqualTo(UPDATED_FRAIS_ASSURANCE_LOCALE);
        assertThat(testLivraison.getFraisManutention()).isEqualTo(UPDATED_FRAIS_MANUTENTION);

        // Validate the Livraison in ElasticSearch
        Livraison livraisonEs = livraisonSearchRepository.findOne(testLivraison.getId());
        assertThat(livraisonEs).isEqualToComparingFieldByField(testLivraison);
    }

    @Test
    @Transactional
    public void deleteLivraison() throws Exception {
        // Initialize the database
        livraisonService.save(livraison);

        int databaseSizeBeforeDelete = livraisonRepository.findAll().size();

        // Get the livraison
        restLivraisonMockMvc.perform(delete("/api/livraisons/{id}", livraison.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean livraisonExistsInEs = livraisonSearchRepository.exists(livraison.getId());
        assertThat(livraisonExistsInEs).isFalse();

        // Validate the database is empty
        List<Livraison> livraisons = livraisonRepository.findAll();
        assertThat(livraisons).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLivraison() throws Exception {
        // Initialize the database
        livraisonService.save(livraison);

        // Search the livraison
        restLivraisonMockMvc.perform(get("/api/_search/livraisons?query=id:" + livraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livraison.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].descriptionLivraison").value(hasItem(DEFAULT_DESCRIPTION_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].numeroLivraison").value(hasItem(DEFAULT_NUMERO_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].valeurLivraison").value(hasItem(DEFAULT_VALEUR_LIVRAISON.intValue())))
            .andExpect(jsonPath("$.[*].fraisTest").value(hasItem(DEFAULT_FRAIS_TEST.intValue())))
            .andExpect(jsonPath("$.[*].fraisTransit").value(hasItem(DEFAULT_FRAIS_TRANSIT.intValue())))
            .andExpect(jsonPath("$.[*].fraisAssuranceLocale").value(hasItem(DEFAULT_FRAIS_ASSURANCE_LOCALE.intValue())))
            .andExpect(jsonPath("$.[*].fraisManutention").value(hasItem(DEFAULT_FRAIS_MANUTENTION.intValue())));
    }
}
