package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Conditionnement;
import com.webstocker.repository.ConditionnementRepository;
import com.webstocker.service.ConditionnementService;
import com.webstocker.repository.search.ConditionnementSearchRepository;

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
 * Test class for the ConditionnementResource REST controller.
 *
 * @see ConditionnementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class ConditionnementResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION_COND = "AAAAA";
    private static final String UPDATED_DESCRIPTION_COND = "BBBBB";

    private static final Long DEFAULT_CAPACITE_CARTON = 1L;
    private static final Long UPDATED_CAPACITE_CARTON = 2L;

    private static final Long DEFAULT_CAPACITE_CARTOUCHE = 1L;
    private static final Long UPDATED_CAPACITE_CARTOUCHE = 2L;

    private static final Long DEFAULT_CAPACITE_ETUI = 1L;
    private static final Long UPDATED_CAPACITE_ETUI = 2L;

    @Inject
    private ConditionnementRepository conditionnementRepository;

    @Inject
    private ConditionnementService conditionnementService;

    @Inject
    private ConditionnementSearchRepository conditionnementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restConditionnementMockMvc;

    private Conditionnement conditionnement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConditionnementResource conditionnementResource = new ConditionnementResource();
        ReflectionTestUtils.setField(conditionnementResource, "conditionnementService", conditionnementService);
        this.restConditionnementMockMvc = MockMvcBuilders.standaloneSetup(conditionnementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        conditionnementSearchRepository.deleteAll();
        conditionnement = new Conditionnement();
        conditionnement.setLibelle(DEFAULT_LIBELLE);
        conditionnement.setDescriptionCond(DEFAULT_DESCRIPTION_COND);
        conditionnement.setCapaciteCarton(DEFAULT_CAPACITE_CARTON);
        conditionnement.setCapaciteCartouche(DEFAULT_CAPACITE_CARTOUCHE);
        conditionnement.setCapaciteEtui(DEFAULT_CAPACITE_ETUI);
    }

    @Test
    @Transactional
    public void createConditionnement() throws Exception {
        int databaseSizeBeforeCreate = conditionnementRepository.findAll().size();

        // Create the Conditionnement

        restConditionnementMockMvc.perform(post("/api/conditionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conditionnement)))
                .andExpect(status().isCreated());

        // Validate the Conditionnement in the database
        List<Conditionnement> conditionnements = conditionnementRepository.findAll();
        assertThat(conditionnements).hasSize(databaseSizeBeforeCreate + 1);
        Conditionnement testConditionnement = conditionnements.get(conditionnements.size() - 1);
        assertThat(testConditionnement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testConditionnement.getDescriptionCond()).isEqualTo(DEFAULT_DESCRIPTION_COND);
        assertThat(testConditionnement.getCapaciteCarton()).isEqualTo(DEFAULT_CAPACITE_CARTON);
        assertThat(testConditionnement.getCapaciteCartouche()).isEqualTo(DEFAULT_CAPACITE_CARTOUCHE);
        assertThat(testConditionnement.getCapaciteEtui()).isEqualTo(DEFAULT_CAPACITE_ETUI);

        // Validate the Conditionnement in ElasticSearch
        Conditionnement conditionnementEs = conditionnementSearchRepository.findOne(testConditionnement.getId());
        assertThat(conditionnementEs).isEqualToComparingFieldByField(testConditionnement);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = conditionnementRepository.findAll().size();
        // set the field null
        conditionnement.setLibelle(null);

        // Create the Conditionnement, which fails.

        restConditionnementMockMvc.perform(post("/api/conditionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conditionnement)))
                .andExpect(status().isBadRequest());

        List<Conditionnement> conditionnements = conditionnementRepository.findAll();
        assertThat(conditionnements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCapaciteCartonIsRequired() throws Exception {
        int databaseSizeBeforeTest = conditionnementRepository.findAll().size();
        // set the field null
        conditionnement.setCapaciteCarton(null);

        // Create the Conditionnement, which fails.

        restConditionnementMockMvc.perform(post("/api/conditionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conditionnement)))
                .andExpect(status().isBadRequest());

        List<Conditionnement> conditionnements = conditionnementRepository.findAll();
        assertThat(conditionnements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCapaciteCartoucheIsRequired() throws Exception {
        int databaseSizeBeforeTest = conditionnementRepository.findAll().size();
        // set the field null
        conditionnement.setCapaciteCartouche(null);

        // Create the Conditionnement, which fails.

        restConditionnementMockMvc.perform(post("/api/conditionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conditionnement)))
                .andExpect(status().isBadRequest());

        List<Conditionnement> conditionnements = conditionnementRepository.findAll();
        assertThat(conditionnements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCapaciteEtuiIsRequired() throws Exception {
        int databaseSizeBeforeTest = conditionnementRepository.findAll().size();
        // set the field null
        conditionnement.setCapaciteEtui(null);

        // Create the Conditionnement, which fails.

        restConditionnementMockMvc.perform(post("/api/conditionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conditionnement)))
                .andExpect(status().isBadRequest());

        List<Conditionnement> conditionnements = conditionnementRepository.findAll();
        assertThat(conditionnements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConditionnements() throws Exception {
        // Initialize the database
        conditionnementRepository.saveAndFlush(conditionnement);

        // Get all the conditionnements
        restConditionnementMockMvc.perform(get("/api/conditionnements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(conditionnement.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].descriptionCond").value(hasItem(DEFAULT_DESCRIPTION_COND.toString())))
                .andExpect(jsonPath("$.[*].capaciteCarton").value(hasItem(DEFAULT_CAPACITE_CARTON.intValue())))
                .andExpect(jsonPath("$.[*].capaciteCartouche").value(hasItem(DEFAULT_CAPACITE_CARTOUCHE.intValue())))
                .andExpect(jsonPath("$.[*].capaciteEtui").value(hasItem(DEFAULT_CAPACITE_ETUI.intValue())));
    }

    @Test
    @Transactional
    public void getConditionnement() throws Exception {
        // Initialize the database
        conditionnementRepository.saveAndFlush(conditionnement);

        // Get the conditionnement
        restConditionnementMockMvc.perform(get("/api/conditionnements/{id}", conditionnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(conditionnement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.descriptionCond").value(DEFAULT_DESCRIPTION_COND.toString()))
            .andExpect(jsonPath("$.capaciteCarton").value(DEFAULT_CAPACITE_CARTON.intValue()))
            .andExpect(jsonPath("$.capaciteCartouche").value(DEFAULT_CAPACITE_CARTOUCHE.intValue()))
            .andExpect(jsonPath("$.capaciteEtui").value(DEFAULT_CAPACITE_ETUI.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConditionnement() throws Exception {
        // Get the conditionnement
        restConditionnementMockMvc.perform(get("/api/conditionnements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConditionnement() throws Exception {
        // Initialize the database
        conditionnementService.save(conditionnement);

        int databaseSizeBeforeUpdate = conditionnementRepository.findAll().size();

        // Update the conditionnement
        Conditionnement updatedConditionnement = new Conditionnement();
        updatedConditionnement.setId(conditionnement.getId());
        updatedConditionnement.setLibelle(UPDATED_LIBELLE);
        updatedConditionnement.setDescriptionCond(UPDATED_DESCRIPTION_COND);
        updatedConditionnement.setCapaciteCarton(UPDATED_CAPACITE_CARTON);
        updatedConditionnement.setCapaciteCartouche(UPDATED_CAPACITE_CARTOUCHE);
        updatedConditionnement.setCapaciteEtui(UPDATED_CAPACITE_ETUI);

        restConditionnementMockMvc.perform(put("/api/conditionnements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedConditionnement)))
                .andExpect(status().isOk());

        // Validate the Conditionnement in the database
        List<Conditionnement> conditionnements = conditionnementRepository.findAll();
        assertThat(conditionnements).hasSize(databaseSizeBeforeUpdate);
        Conditionnement testConditionnement = conditionnements.get(conditionnements.size() - 1);
        assertThat(testConditionnement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testConditionnement.getDescriptionCond()).isEqualTo(UPDATED_DESCRIPTION_COND);
        assertThat(testConditionnement.getCapaciteCarton()).isEqualTo(UPDATED_CAPACITE_CARTON);
        assertThat(testConditionnement.getCapaciteCartouche()).isEqualTo(UPDATED_CAPACITE_CARTOUCHE);
        assertThat(testConditionnement.getCapaciteEtui()).isEqualTo(UPDATED_CAPACITE_ETUI);

        // Validate the Conditionnement in ElasticSearch
        Conditionnement conditionnementEs = conditionnementSearchRepository.findOne(testConditionnement.getId());
        assertThat(conditionnementEs).isEqualToComparingFieldByField(testConditionnement);
    }

    @Test
    @Transactional
    public void deleteConditionnement() throws Exception {
        // Initialize the database
        conditionnementService.save(conditionnement);

        int databaseSizeBeforeDelete = conditionnementRepository.findAll().size();

        // Get the conditionnement
        restConditionnementMockMvc.perform(delete("/api/conditionnements/{id}", conditionnement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean conditionnementExistsInEs = conditionnementSearchRepository.exists(conditionnement.getId());
        assertThat(conditionnementExistsInEs).isFalse();

        // Validate the database is empty
        List<Conditionnement> conditionnements = conditionnementRepository.findAll();
        assertThat(conditionnements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchConditionnement() throws Exception {
        // Initialize the database
        conditionnementService.save(conditionnement);

        // Search the conditionnement
        restConditionnementMockMvc.perform(get("/api/_search/conditionnements?query=id:" + conditionnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conditionnement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].descriptionCond").value(hasItem(DEFAULT_DESCRIPTION_COND.toString())))
            .andExpect(jsonPath("$.[*].capaciteCarton").value(hasItem(DEFAULT_CAPACITE_CARTON.intValue())))
            .andExpect(jsonPath("$.[*].capaciteCartouche").value(hasItem(DEFAULT_CAPACITE_CARTOUCHE.intValue())))
            .andExpect(jsonPath("$.[*].capaciteEtui").value(hasItem(DEFAULT_CAPACITE_ETUI.intValue())));
    }
}
