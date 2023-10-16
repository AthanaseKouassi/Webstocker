package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.repository.search.LigneBonDeSortieSearchRepository;
import com.webstocker.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LigneBonDeSortieResource REST controller.
 *
 * @see LigneBonDeSortieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebstockerApp.class)
public class LigneBonDeSortieResourceIntTest {

    private static final Long DEFAULT_QUANTITE = 1L;
    private static final Long UPDATED_QUANTITE = 2L;

    private static final BigDecimal DEFAULT_PRIX_VENTE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_VENTE = new BigDecimal(2);

    private static final Long DEFAULT_PRIX_DE_VENTE = 1L;
    private static final Long UPDATED_PRIX_DE_VENTE = 2L;

    @Autowired
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Autowired
    private LigneBonDeSortieService ligneBonDeSortieService;

    @Autowired
    private LigneBonDeSortieSearchRepository ligneBonDeSortieSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLigneBonDeSortieMockMvc;

    private LigneBonDeSortie ligneBonDeSortie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LigneBonDeSortieResource ligneBonDeSortieResource = new LigneBonDeSortieResource();
        this.restLigneBonDeSortieMockMvc = MockMvcBuilders.standaloneSetup(ligneBonDeSortieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneBonDeSortie createEntity(EntityManager em) {
        LigneBonDeSortie ligneBonDeSortie = new LigneBonDeSortie();
        ligneBonDeSortie.setQuantite(DEFAULT_QUANTITE);
        ligneBonDeSortie.setPrixVente(DEFAULT_PRIX_VENTE);
        ligneBonDeSortie.setPrixDeVente(DEFAULT_PRIX_DE_VENTE);
        return ligneBonDeSortie;
    }

    @Before
    public void initTest() {
        ligneBonDeSortieSearchRepository.deleteAll();
        ligneBonDeSortie = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneBonDeSortie() throws Exception {
        int databaseSizeBeforeCreate = ligneBonDeSortieRepository.findAll().size();

        // Create the LigneBonDeSortie
        restLigneBonDeSortieMockMvc.perform(post("/api/ligne-bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneBonDeSortie)))
            .andExpect(status().isCreated());

        // Validate the LigneBonDeSortie in the database
        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeCreate + 1);
        LigneBonDeSortie testLigneBonDeSortie = ligneBonDeSortieList.get(ligneBonDeSortieList.size() - 1);
        assertThat(testLigneBonDeSortie.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneBonDeSortie.getPrixVente()).isEqualTo(DEFAULT_PRIX_VENTE);
        assertThat(testLigneBonDeSortie.getPrixDeVente()).isEqualTo(DEFAULT_PRIX_DE_VENTE);

        // Validate the LigneBonDeSortie in Elasticsearch
        LigneBonDeSortie ligneBonDeSortieEs = ligneBonDeSortieSearchRepository.findOne(testLigneBonDeSortie.getId());
        assertThat(ligneBonDeSortieEs).isEqualToComparingFieldByField(testLigneBonDeSortie);
    }

    @Test
    @Transactional
    public void createLigneBonDeSortieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneBonDeSortieRepository.findAll().size();

        // Create the LigneBonDeSortie with an existing ID
        ligneBonDeSortie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneBonDeSortieMockMvc.perform(post("/api/ligne-bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneBonDeSortie)))
            .andExpect(status().isBadRequest());

        // Validate the LigneBonDeSortie in the database
        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneBonDeSortieRepository.findAll().size();
        // set the field null
        ligneBonDeSortie.setQuantite(null);

        // Create the LigneBonDeSortie, which fails.

        restLigneBonDeSortieMockMvc.perform(post("/api/ligne-bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneBonDeSortie)))
            .andExpect(status().isBadRequest());

        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixVenteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneBonDeSortieRepository.findAll().size();
        // set the field null
        ligneBonDeSortie.setPrixVente(null);

        // Create the LigneBonDeSortie, which fails.

        restLigneBonDeSortieMockMvc.perform(post("/api/ligne-bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneBonDeSortie)))
            .andExpect(status().isBadRequest());

        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixDeVenteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneBonDeSortieRepository.findAll().size();
        // set the field null
        ligneBonDeSortie.setPrixDeVente(null);

        // Create the LigneBonDeSortie, which fails.

        restLigneBonDeSortieMockMvc.perform(post("/api/ligne-bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneBonDeSortie)))
            .andExpect(status().isBadRequest());

        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLigneBonDeSorties() throws Exception {
        // Initialize the database
        ligneBonDeSortieRepository.saveAndFlush(ligneBonDeSortie);

        // Get all the ligneBonDeSortieList
        restLigneBonDeSortieMockMvc.perform(get("/api/ligne-bon-de-sorties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneBonDeSortie.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.intValue())))
            .andExpect(jsonPath("$.[*].prixDeVente").value(hasItem(DEFAULT_PRIX_DE_VENTE.intValue())));
    }

    @Test
    @Transactional
    public void getLigneBonDeSortie() throws Exception {
        // Initialize the database
        ligneBonDeSortieRepository.saveAndFlush(ligneBonDeSortie);

        // Get the ligneBonDeSortie
        restLigneBonDeSortieMockMvc.perform(get("/api/ligne-bon-de-sorties/{id}", ligneBonDeSortie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ligneBonDeSortie.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()))
            .andExpect(jsonPath("$.prixVente").value(DEFAULT_PRIX_VENTE.intValue()))
            .andExpect(jsonPath("$.prixDeVente").value(DEFAULT_PRIX_DE_VENTE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLigneBonDeSortie() throws Exception {
        // Get the ligneBonDeSortie
        restLigneBonDeSortieMockMvc.perform(get("/api/ligne-bon-de-sorties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneBonDeSortie() throws Exception {
        // Initialize the database
        ligneBonDeSortieService.save(ligneBonDeSortie);

        int databaseSizeBeforeUpdate = ligneBonDeSortieRepository.findAll().size();

        // Update the ligneBonDeSortie
        LigneBonDeSortie updatedLigneBonDeSortie = ligneBonDeSortieRepository.findOne(ligneBonDeSortie.getId());
        updatedLigneBonDeSortie.setQuantite(UPDATED_QUANTITE);
        updatedLigneBonDeSortie.setPrixVente(UPDATED_PRIX_VENTE);
        updatedLigneBonDeSortie.setPrixDeVente(UPDATED_PRIX_DE_VENTE);

        restLigneBonDeSortieMockMvc.perform(put("/api/ligne-bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLigneBonDeSortie)))
            .andExpect(status().isOk());

        // Validate the LigneBonDeSortie in the database
        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeUpdate);
        LigneBonDeSortie testLigneBonDeSortie = ligneBonDeSortieList.get(ligneBonDeSortieList.size() - 1);
        assertThat(testLigneBonDeSortie.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneBonDeSortie.getPrixVente()).isEqualTo(UPDATED_PRIX_VENTE);
        assertThat(testLigneBonDeSortie.getPrixDeVente()).isEqualTo(UPDATED_PRIX_DE_VENTE);

        // Validate the LigneBonDeSortie in Elasticsearch
        LigneBonDeSortie ligneBonDeSortieEs = ligneBonDeSortieSearchRepository.findOne(testLigneBonDeSortie.getId());
        assertThat(ligneBonDeSortieEs).isEqualToComparingFieldByField(testLigneBonDeSortie);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneBonDeSortie() throws Exception {
        int databaseSizeBeforeUpdate = ligneBonDeSortieRepository.findAll().size();

        // Create the LigneBonDeSortie

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLigneBonDeSortieMockMvc.perform(put("/api/ligne-bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneBonDeSortie)))
            .andExpect(status().isCreated());

        // Validate the LigneBonDeSortie in the database
        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLigneBonDeSortie() throws Exception {
        // Initialize the database
        ligneBonDeSortieService.save(ligneBonDeSortie);

        int databaseSizeBeforeDelete = ligneBonDeSortieRepository.findAll().size();

        // Get the ligneBonDeSortie
        restLigneBonDeSortieMockMvc.perform(delete("/api/ligne-bon-de-sorties/{id}", ligneBonDeSortie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ligneBonDeSortieExistsInEs = ligneBonDeSortieSearchRepository.exists(ligneBonDeSortie.getId());
        assertThat(ligneBonDeSortieExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneBonDeSortie> ligneBonDeSortieList = ligneBonDeSortieRepository.findAll();
        assertThat(ligneBonDeSortieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneBonDeSortie() throws Exception {
        // Initialize the database
        ligneBonDeSortieService.save(ligneBonDeSortie);

        // Search the ligneBonDeSortie
        restLigneBonDeSortieMockMvc.perform(get("/api/_search/ligne-bon-de-sorties?query=id:" + ligneBonDeSortie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneBonDeSortie.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].prixVente").value(hasItem(DEFAULT_PRIX_VENTE.intValue())))
            .andExpect(jsonPath("$.[*].prixDeVente").value(hasItem(DEFAULT_PRIX_DE_VENTE.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(LigneBonDeSortie.class);
        LigneBonDeSortie ligneBonDeSortie1 = new LigneBonDeSortie();
        ligneBonDeSortie1.setId(1L);
        LigneBonDeSortie ligneBonDeSortie2 = new LigneBonDeSortie();
        ligneBonDeSortie2.setId(ligneBonDeSortie1.getId());
        assertThat(ligneBonDeSortie1).isEqualTo(ligneBonDeSortie2);
        ligneBonDeSortie2.setId(2L);
        assertThat(ligneBonDeSortie1).isNotEqualTo(ligneBonDeSortie2);
        ligneBonDeSortie1.setId(null);
        assertThat(ligneBonDeSortie1).isNotEqualTo(ligneBonDeSortie2);
    }
}
