package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;

import com.webstocker.domain.Prix;
import com.webstocker.repository.PrixRepository;
import com.webstocker.service.PrixService;
import com.webstocker.repository.search.PrixSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PrixResource REST controller.
 *
 * @see PrixResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class PrixResourceIntTest {

    private static final LocalDate DEFAULT_DATE_FIXATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIXATION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(2);

    @Autowired
    private PrixRepository prixRepository;

    @Autowired
    private PrixService prixService;

    @Autowired
    private PrixSearchRepository prixSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restPrixMockMvc;

    private Prix prix;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrixResource prixResource = new PrixResource(prixService);
        this.restPrixMockMvc = MockMvcBuilders.standaloneSetup(prixResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prix createEntity(EntityManager em) {
        Prix prix = new Prix();
        prix.setDateFixation(DEFAULT_DATE_FIXATION);
        prix.setActif(DEFAULT_ACTIF);
        prix.setPrixUnitaire(DEFAULT_PRIX_UNITAIRE);
        return prix;
    }

    @Before
    public void initTest() {
        prixSearchRepository.deleteAll();
        prix = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrix() throws Exception {
        int databaseSizeBeforeCreate = prixRepository.findAll().size();

        // Create the Prix

        restPrixMockMvc.perform(post("/api/prixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isCreated());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeCreate + 1);
        Prix testPrix = prixList.get(prixList.size() - 1);
        assertThat(testPrix.getDateFixation()).isEqualTo(DEFAULT_DATE_FIXATION);
        assertThat(testPrix.isActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testPrix.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);

        // Validate the Prix in Elasticsearch
        Prix prixEs = prixSearchRepository.findOne(testPrix.getId());
        assertThat(prixEs).isEqualToComparingFieldByField(testPrix);
    }

    @Test
    @Transactional
    public void createPrixWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prixRepository.findAll().size();

        // Create the Prix with an existing ID
        Prix existingPrix = new Prix();
        existingPrix.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrixMockMvc.perform(post("/api/prixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPrix)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateFixationIsRequired() throws Exception {
        int databaseSizeBeforeTest = prixRepository.findAll().size();
        // set the field null
        prix.setDateFixation(null);

        // Create the Prix, which fails.

        restPrixMockMvc.perform(post("/api/prixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isBadRequest());

        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActifIsRequired() throws Exception {
        int databaseSizeBeforeTest = prixRepository.findAll().size();
        // set the field null
        prix.setActif(null);

        // Create the Prix, which fails.

        restPrixMockMvc.perform(post("/api/prixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isBadRequest());

        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = prixRepository.findAll().size();
        // set the field null
        prix.setPrixUnitaire(null);

        // Create the Prix, which fails.

        restPrixMockMvc.perform(post("/api/prixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isBadRequest());

        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrixes() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        // Get all the prixList
        restPrixMockMvc.perform(get("/api/prixes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prix.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFixation").value(hasItem(DEFAULT_DATE_FIXATION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())));
    }

    @Test
    @Transactional
    public void getPrix() throws Exception {
        // Initialize the database
        prixRepository.saveAndFlush(prix);

        // Get the prix
        restPrixMockMvc.perform(get("/api/prixes/{id}", prix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prix.getId().intValue()))
            .andExpect(jsonPath("$.dateFixation").value(DEFAULT_DATE_FIXATION.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrix() throws Exception {
        // Get the prix
        restPrixMockMvc.perform(get("/api/prixes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrix() throws Exception {
        // Initialize the database
        prixService.save(prix);

        int databaseSizeBeforeUpdate = prixRepository.findAll().size();

        // Update the prix
        Prix updatedPrix = prixRepository.findOne(prix.getId());
        updatedPrix.setDateFixation(UPDATED_DATE_FIXATION);
        updatedPrix.setActif(UPDATED_ACTIF);
        updatedPrix.setPrixUnitaire(UPDATED_PRIX_UNITAIRE);

        restPrixMockMvc.perform(put("/api/prixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrix)))
            .andExpect(status().isOk());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate);
        Prix testPrix = prixList.get(prixList.size() - 1);
        assertThat(testPrix.getDateFixation()).isEqualTo(UPDATED_DATE_FIXATION);
        assertThat(testPrix.isActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testPrix.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);

        // Validate the Prix in Elasticsearch
        Prix prixEs = prixSearchRepository.findOne(testPrix.getId());
        assertThat(prixEs).isEqualToComparingFieldByField(testPrix);
    }

    @Test
    @Transactional
    public void updateNonExistingPrix() throws Exception {
        int databaseSizeBeforeUpdate = prixRepository.findAll().size();

        // Create the Prix

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrixMockMvc.perform(put("/api/prixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prix)))
            .andExpect(status().isCreated());

        // Validate the Prix in the database
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrix() throws Exception {
        // Initialize the database
        prixService.save(prix);

        int databaseSizeBeforeDelete = prixRepository.findAll().size();

        // Get the prix
        restPrixMockMvc.perform(delete("/api/prixes/{id}", prix.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean prixExistsInEs = prixSearchRepository.exists(prix.getId());
        assertThat(prixExistsInEs).isFalse();

        // Validate the database is empty
        List<Prix> prixList = prixRepository.findAll();
        assertThat(prixList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrix() throws Exception {
        // Initialize the database
        prixService.save(prix);

        // Search the prix
        restPrixMockMvc.perform(get("/api/_search/prixes?query=id:" + prix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prix.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFixation").value(hasItem(DEFAULT_DATE_FIXATION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }
}
