package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.service.LignelivraisonService;
import com.webstocker.repository.search.LignelivraisonSearchRepository;
import com.webstocker.web.rest.reports.LignelivraisonReportResource;

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
 * Test class for the LignelivraisonResource REST controller.
 *
 * @see LignelivraisonReportResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class LignelivraisonResourceIntTest {


    private static final Integer DEFAULT_QUANTITE_LOT_LIVRE = 1;
    private static final Integer UPDATED_QUANTITE_LOT_LIVRE = 2;

    private static final Integer DEFAULT_QUANTITE_CARTON_LOT = 1;
    private static final Integer UPDATED_QUANTITE_CARTON_LOT = 2;

    @Inject
    private LignelivraisonRepository lignelivraisonRepository;

    @Inject
    private LignelivraisonService lignelivraisonService;

    @Inject
    private LignelivraisonSearchRepository lignelivraisonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLignelivraisonMockMvc;

    private Lignelivraison lignelivraison;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LignelivraisonReportResource lignelivraisonResource = new LignelivraisonReportResource();
        ReflectionTestUtils.setField(lignelivraisonResource, "lignelivraisonService", lignelivraisonService);
        this.restLignelivraisonMockMvc = MockMvcBuilders.standaloneSetup(lignelivraisonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lignelivraisonSearchRepository.deleteAll();
        lignelivraison = new Lignelivraison();
        lignelivraison.setQuantiteLotLivre(DEFAULT_QUANTITE_LOT_LIVRE);
        lignelivraison.setQuantiteCartonLot(DEFAULT_QUANTITE_CARTON_LOT);
    }

    @Test
    @Transactional
    public void createLignelivraison() throws Exception {
        int databaseSizeBeforeCreate = lignelivraisonRepository.findAll().size();

        // Create the Lignelivraison

        restLignelivraisonMockMvc.perform(post("/api/lignelivraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lignelivraison)))
                .andExpect(status().isCreated());

        // Validate the Lignelivraison in the database
        List<Lignelivraison> lignelivraisons = lignelivraisonRepository.findAll();
        assertThat(lignelivraisons).hasSize(databaseSizeBeforeCreate + 1);
        Lignelivraison testLignelivraison = lignelivraisons.get(lignelivraisons.size() - 1);
        assertThat(testLignelivraison.getQuantiteLotLivre()).isEqualTo(DEFAULT_QUANTITE_LOT_LIVRE);
        assertThat(testLignelivraison.getQuantiteCartonLot()).isEqualTo(DEFAULT_QUANTITE_CARTON_LOT);

        // Validate the Lignelivraison in ElasticSearch
        Lignelivraison lignelivraisonEs = lignelivraisonSearchRepository.findOne(testLignelivraison.getId());
        assertThat(lignelivraisonEs).isEqualToComparingFieldByField(testLignelivraison);
    }

    @Test
    @Transactional
    public void checkQuantiteLotLivreIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignelivraisonRepository.findAll().size();
        // set the field null
        lignelivraison.setQuantiteLotLivre(null);

        // Create the Lignelivraison, which fails.

        restLignelivraisonMockMvc.perform(post("/api/lignelivraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lignelivraison)))
                .andExpect(status().isBadRequest());

        List<Lignelivraison> lignelivraisons = lignelivraisonRepository.findAll();
        assertThat(lignelivraisons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLignelivraisons() throws Exception {
        // Initialize the database
        lignelivraisonRepository.saveAndFlush(lignelivraison);

        // Get all the lignelivraisons
        restLignelivraisonMockMvc.perform(get("/api/lignelivraisons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lignelivraison.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantiteLotLivre").value(hasItem(DEFAULT_QUANTITE_LOT_LIVRE)))
                .andExpect(jsonPath("$.[*].quantiteCartonLot").value(hasItem(DEFAULT_QUANTITE_CARTON_LOT)));
    }

    @Test
    @Transactional
    public void getLignelivraison() throws Exception {
        // Initialize the database
        lignelivraisonRepository.saveAndFlush(lignelivraison);

        // Get the lignelivraison
        restLignelivraisonMockMvc.perform(get("/api/lignelivraisons/{id}", lignelivraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lignelivraison.getId().intValue()))
            .andExpect(jsonPath("$.quantiteLotLivre").value(DEFAULT_QUANTITE_LOT_LIVRE))
            .andExpect(jsonPath("$.quantiteCartonLot").value(DEFAULT_QUANTITE_CARTON_LOT));
    }

    @Test
    @Transactional
    public void getNonExistingLignelivraison() throws Exception {
        // Get the lignelivraison
        restLignelivraisonMockMvc.perform(get("/api/lignelivraisons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLignelivraison() throws Exception {
        // Initialize the database
        lignelivraisonService.save(lignelivraison);

        int databaseSizeBeforeUpdate = lignelivraisonRepository.findAll().size();

        // Update the lignelivraison
        Lignelivraison updatedLignelivraison = new Lignelivraison();
        updatedLignelivraison.setId(lignelivraison.getId());
        updatedLignelivraison.setQuantiteLotLivre(UPDATED_QUANTITE_LOT_LIVRE);
        updatedLignelivraison.setQuantiteCartonLot(UPDATED_QUANTITE_CARTON_LOT);

        restLignelivraisonMockMvc.perform(put("/api/lignelivraisons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLignelivraison)))
                .andExpect(status().isOk());

        // Validate the Lignelivraison in the database
        List<Lignelivraison> lignelivraisons = lignelivraisonRepository.findAll();
        assertThat(lignelivraisons).hasSize(databaseSizeBeforeUpdate);
        Lignelivraison testLignelivraison = lignelivraisons.get(lignelivraisons.size() - 1);
        assertThat(testLignelivraison.getQuantiteLotLivre()).isEqualTo(UPDATED_QUANTITE_LOT_LIVRE);
        assertThat(testLignelivraison.getQuantiteCartonLot()).isEqualTo(UPDATED_QUANTITE_CARTON_LOT);

        // Validate the Lignelivraison in ElasticSearch
        Lignelivraison lignelivraisonEs = lignelivraisonSearchRepository.findOne(testLignelivraison.getId());
        assertThat(lignelivraisonEs).isEqualToComparingFieldByField(testLignelivraison);
    }

    @Test
    @Transactional
    public void deleteLignelivraison() throws Exception {
        // Initialize the database
        lignelivraisonService.save(lignelivraison);

        int databaseSizeBeforeDelete = lignelivraisonRepository.findAll().size();

        // Get the lignelivraison
        restLignelivraisonMockMvc.perform(delete("/api/lignelivraisons/{id}", lignelivraison.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lignelivraisonExistsInEs = lignelivraisonSearchRepository.exists(lignelivraison.getId());
        assertThat(lignelivraisonExistsInEs).isFalse();

        // Validate the database is empty
        List<Lignelivraison> lignelivraisons = lignelivraisonRepository.findAll();
        assertThat(lignelivraisons).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLignelivraison() throws Exception {
        // Initialize the database
        lignelivraisonService.save(lignelivraison);

        // Search the lignelivraison
        restLignelivraisonMockMvc.perform(get("/api/_search/lignelivraisons?query=id:" + lignelivraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignelivraison.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantiteLotLivre").value(hasItem(DEFAULT_QUANTITE_LOT_LIVRE)))
            .andExpect(jsonPath("$.[*].quantiteCartonLot").value(hasItem(DEFAULT_QUANTITE_CARTON_LOT)));
    }
}
