package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;

import com.webstocker.domain.Inventaire;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.service.InventaireService;
import com.webstocker.repository.search.InventaireSearchRepository;
import com.webstocker.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InventaireResource REST controller.
 *
 * @see InventaireResource
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WebstockerApp.class)
public class InventaireResourceIntTest {

    private static final LocalDate DEFAULT_DATE_INVENTAIRE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INVENTAIRE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_STOCK_FINAL_THEORIQUE = 1L;
    private static final Long UPDATED_STOCK_FINAL_THEORIQUE = 2L;

    private static final Long DEFAULT_STOCK_REEL = 1L;
    private static final Long UPDATED_STOCK_REEL = 2L;

//    @Autowired
    @Inject
    private InventaireRepository inventaireRepository;

//    @Autowired
    @Inject
    private InventaireService inventaireService;

//    @Autowired
    @Inject
    private InventaireSearchRepository inventaireSearchRepository;

//    @Autowired
    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

//    @Autowired
    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

//    @Autowired
    @Inject
    private ExceptionTranslator exceptionTranslator;

//    @Autowired
    @Inject
    private EntityManager em;

    private MockMvc restInventaireMockMvc;

    private Inventaire inventaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        InventaireResource inventaireResource = new InventaireResource(inventaireService);
        InventaireResource inventaireResource = new InventaireResource();
        this.restInventaireMockMvc = MockMvcBuilders.standaloneSetup(inventaireResource)
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
    public static Inventaire createEntity(EntityManager em) {
        Inventaire inventaire = new Inventaire()
                .dateInventaire(DEFAULT_DATE_INVENTAIRE)
                .stockFinalTheorique(DEFAULT_STOCK_FINAL_THEORIQUE)
                .stockReel(DEFAULT_STOCK_REEL);
        return inventaire;
    }

    @Before
    public void initTest() {
        inventaireSearchRepository.deleteAll();
        inventaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createInventaire() throws Exception {
        int databaseSizeBeforeCreate = inventaireRepository.findAll().size();

        // Create the Inventaire

        restInventaireMockMvc.perform(post("/api/inventaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventaire)))
            .andExpect(status().isCreated());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeCreate + 1);
        Inventaire testInventaire = inventaireList.get(inventaireList.size() - 1);
        assertThat(testInventaire.getDateInventaire()).isEqualTo(DEFAULT_DATE_INVENTAIRE);
        assertThat(testInventaire.getStockFinalTheorique()).isEqualTo(DEFAULT_STOCK_FINAL_THEORIQUE);
        assertThat(testInventaire.getStockReel()).isEqualTo(DEFAULT_STOCK_REEL);

        // Validate the Inventaire in Elasticsearch
        Inventaire inventaireEs = inventaireSearchRepository.findOne(testInventaire.getId());
        assertThat(inventaireEs).isEqualToComparingFieldByField(testInventaire);
    }

    @Test
    @Transactional
    public void createInventaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inventaireRepository.findAll().size();

        // Create the Inventaire with an existing ID
        Inventaire existingInventaire = new Inventaire();
        existingInventaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventaireMockMvc.perform(post("/api/inventaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingInventaire)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInventaires() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        // Get all the inventaireList
        restInventaireMockMvc.perform(get("/api/inventaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInventaire").value(hasItem(DEFAULT_DATE_INVENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].stockFinalTheorique").value(hasItem(DEFAULT_STOCK_FINAL_THEORIQUE.intValue())))
            .andExpect(jsonPath("$.[*].stockReel").value(hasItem(DEFAULT_STOCK_REEL.intValue())));
    }

    @Test
    @Transactional
    public void getInventaire() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        // Get the inventaire
        restInventaireMockMvc.perform(get("/api/inventaires/{id}", inventaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inventaire.getId().intValue()))
            .andExpect(jsonPath("$.dateInventaire").value(DEFAULT_DATE_INVENTAIRE.toString()))
            .andExpect(jsonPath("$.stockFinalTheorique").value(DEFAULT_STOCK_FINAL_THEORIQUE.intValue()))
            .andExpect(jsonPath("$.stockReel").value(DEFAULT_STOCK_REEL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInventaire() throws Exception {
        // Get the inventaire
        restInventaireMockMvc.perform(get("/api/inventaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInventaire() throws Exception {
        // Initialize the database
        inventaireService.save(inventaire);

        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();

        // Update the inventaire
        Inventaire updatedInventaire = inventaireRepository.findOne(inventaire.getId());
        updatedInventaire
                .dateInventaire(UPDATED_DATE_INVENTAIRE)
                .stockFinalTheorique(UPDATED_STOCK_FINAL_THEORIQUE)
                .stockReel(UPDATED_STOCK_REEL);

        restInventaireMockMvc.perform(put("/api/inventaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInventaire)))
            .andExpect(status().isOk());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
        Inventaire testInventaire = inventaireList.get(inventaireList.size() - 1);
        assertThat(testInventaire.getDateInventaire()).isEqualTo(UPDATED_DATE_INVENTAIRE);
        assertThat(testInventaire.getStockFinalTheorique()).isEqualTo(UPDATED_STOCK_FINAL_THEORIQUE);
        assertThat(testInventaire.getStockReel()).isEqualTo(UPDATED_STOCK_REEL);

        // Validate the Inventaire in Elasticsearch
        Inventaire inventaireEs = inventaireSearchRepository.findOne(testInventaire.getId());
        assertThat(inventaireEs).isEqualToComparingFieldByField(testInventaire);
    }

    @Test
    @Transactional
    public void updateNonExistingInventaire() throws Exception {
        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();

        // Create the Inventaire

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInventaireMockMvc.perform(put("/api/inventaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventaire)))
            .andExpect(status().isCreated());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInventaire() throws Exception {
        // Initialize the database
        inventaireService.save(inventaire);

        int databaseSizeBeforeDelete = inventaireRepository.findAll().size();

        // Get the inventaire
        restInventaireMockMvc.perform(delete("/api/inventaires/{id}", inventaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean inventaireExistsInEs = inventaireSearchRepository.exists(inventaire.getId());
        assertThat(inventaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInventaire() throws Exception {
        // Initialize the database
        inventaireService.save(inventaire);

        // Search the inventaire
        restInventaireMockMvc.perform(get("/api/_search/inventaires?query=id:" + inventaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInventaire").value(hasItem(DEFAULT_DATE_INVENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].stockFinalTheorique").value(hasItem(DEFAULT_STOCK_FINAL_THEORIQUE.intValue())))
            .andExpect(jsonPath("$.[*].stockReel").value(hasItem(DEFAULT_STOCK_REEL.intValue())));
    }

//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Inventaire.class);
//    }
}
