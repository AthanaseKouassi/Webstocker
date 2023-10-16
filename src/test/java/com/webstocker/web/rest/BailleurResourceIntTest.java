package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Bailleur;
import com.webstocker.repository.BailleurRepository;
import com.webstocker.service.BailleurService;
import com.webstocker.repository.search.BailleurSearchRepository;

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
 * Test class for the BailleurResource REST controller.
 *
 * @see BailleurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class BailleurResourceIntTest {

    private static final String DEFAULT_NOM_BAILLEUR = "AAAAA";
    private static final String UPDATED_NOM_BAILLEUR = "BBBBB";

    @Inject
    private BailleurRepository bailleurRepository;

    @Inject
    private BailleurService bailleurService;

    @Inject
    private BailleurSearchRepository bailleurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBailleurMockMvc;

    private Bailleur bailleur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BailleurResource bailleurResource = new BailleurResource();
        ReflectionTestUtils.setField(bailleurResource, "bailleurService", bailleurService);
        this.restBailleurMockMvc = MockMvcBuilders.standaloneSetup(bailleurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bailleurSearchRepository.deleteAll();
        bailleur = new Bailleur();
        bailleur.setNomBailleur(DEFAULT_NOM_BAILLEUR);
    }

    @Test
    @Transactional
    public void createBailleur() throws Exception {
        int databaseSizeBeforeCreate = bailleurRepository.findAll().size();

        // Create the Bailleur

        restBailleurMockMvc.perform(post("/api/bailleurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bailleur)))
                .andExpect(status().isCreated());

        // Validate the Bailleur in the database
        List<Bailleur> bailleurs = bailleurRepository.findAll();
        assertThat(bailleurs).hasSize(databaseSizeBeforeCreate + 1);
        Bailleur testBailleur = bailleurs.get(bailleurs.size() - 1);
        assertThat(testBailleur.getNomBailleur()).isEqualTo(DEFAULT_NOM_BAILLEUR);

        // Validate the Bailleur in ElasticSearch
        Bailleur bailleurEs = bailleurSearchRepository.findOne(testBailleur.getId());
        assertThat(bailleurEs).isEqualToComparingFieldByField(testBailleur);
    }

    @Test
    @Transactional
    public void checkNomBailleurIsRequired() throws Exception {
        int databaseSizeBeforeTest = bailleurRepository.findAll().size();
        // set the field null
        bailleur.setNomBailleur(null);

        // Create the Bailleur, which fails.

        restBailleurMockMvc.perform(post("/api/bailleurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bailleur)))
                .andExpect(status().isBadRequest());

        List<Bailleur> bailleurs = bailleurRepository.findAll();
        assertThat(bailleurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBailleurs() throws Exception {
        // Initialize the database
        bailleurRepository.saveAndFlush(bailleur);

        // Get all the bailleurs
        restBailleurMockMvc.perform(get("/api/bailleurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bailleur.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomBailleur").value(hasItem(DEFAULT_NOM_BAILLEUR.toString())));
    }

    @Test
    @Transactional
    public void getBailleur() throws Exception {
        // Initialize the database
        bailleurRepository.saveAndFlush(bailleur);

        // Get the bailleur
        restBailleurMockMvc.perform(get("/api/bailleurs/{id}", bailleur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bailleur.getId().intValue()))
            .andExpect(jsonPath("$.nomBailleur").value(DEFAULT_NOM_BAILLEUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBailleur() throws Exception {
        // Get the bailleur
        restBailleurMockMvc.perform(get("/api/bailleurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBailleur() throws Exception {
        // Initialize the database
        bailleurService.save(bailleur);

        int databaseSizeBeforeUpdate = bailleurRepository.findAll().size();

        // Update the bailleur
        Bailleur updatedBailleur = new Bailleur();
        updatedBailleur.setId(bailleur.getId());
        updatedBailleur.setNomBailleur(UPDATED_NOM_BAILLEUR);

        restBailleurMockMvc.perform(put("/api/bailleurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBailleur)))
                .andExpect(status().isOk());

        // Validate the Bailleur in the database
        List<Bailleur> bailleurs = bailleurRepository.findAll();
        assertThat(bailleurs).hasSize(databaseSizeBeforeUpdate);
        Bailleur testBailleur = bailleurs.get(bailleurs.size() - 1);
        assertThat(testBailleur.getNomBailleur()).isEqualTo(UPDATED_NOM_BAILLEUR);

        // Validate the Bailleur in ElasticSearch
        Bailleur bailleurEs = bailleurSearchRepository.findOne(testBailleur.getId());
        assertThat(bailleurEs).isEqualToComparingFieldByField(testBailleur);
    }

    @Test
    @Transactional
    public void deleteBailleur() throws Exception {
        // Initialize the database
        bailleurService.save(bailleur);

        int databaseSizeBeforeDelete = bailleurRepository.findAll().size();

        // Get the bailleur
        restBailleurMockMvc.perform(delete("/api/bailleurs/{id}", bailleur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bailleurExistsInEs = bailleurSearchRepository.exists(bailleur.getId());
        assertThat(bailleurExistsInEs).isFalse();

        // Validate the database is empty
        List<Bailleur> bailleurs = bailleurRepository.findAll();
        assertThat(bailleurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBailleur() throws Exception {
        // Initialize the database
        bailleurService.save(bailleur);

        // Search the bailleur
        restBailleurMockMvc.perform(get("/api/_search/bailleurs?query=id:" + bailleur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bailleur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomBailleur").value(hasItem(DEFAULT_NOM_BAILLEUR.toString())));
    }
}
