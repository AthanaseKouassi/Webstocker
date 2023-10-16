package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Ligneprixproduit;
import com.webstocker.repository.LigneprixproduitRepository;
import com.webstocker.service.LigneprixproduitService;
import com.webstocker.repository.search.LigneprixproduitSearchRepository;

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
 * Test class for the LigneprixproduitResource REST controller.
 *
 * @see LigneprixproduitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class LigneprixproduitResourceIntTest {


    @Inject
    private LigneprixproduitRepository ligneprixproduitRepository;

    @Inject
    private LigneprixproduitService ligneprixproduitService;

    @Inject
    private LigneprixproduitSearchRepository ligneprixproduitSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLigneprixproduitMockMvc;

    private Ligneprixproduit ligneprixproduit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LigneprixproduitResource ligneprixproduitResource = new LigneprixproduitResource();
        ReflectionTestUtils.setField(ligneprixproduitResource, "ligneprixproduitService", ligneprixproduitService);
        this.restLigneprixproduitMockMvc = MockMvcBuilders.standaloneSetup(ligneprixproduitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ligneprixproduitSearchRepository.deleteAll();
        ligneprixproduit = new Ligneprixproduit();
    }

    @Test
    @Transactional
    public void createLigneprixproduit() throws Exception {
        int databaseSizeBeforeCreate = ligneprixproduitRepository.findAll().size();

        // Create the Ligneprixproduit

        restLigneprixproduitMockMvc.perform(post("/api/ligneprixproduits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ligneprixproduit)))
                .andExpect(status().isCreated());

        // Validate the Ligneprixproduit in the database
        List<Ligneprixproduit> ligneprixproduits = ligneprixproduitRepository.findAll();
        assertThat(ligneprixproduits).hasSize(databaseSizeBeforeCreate + 1);
        Ligneprixproduit testLigneprixproduit = ligneprixproduits.get(ligneprixproduits.size() - 1);

        // Validate the Ligneprixproduit in ElasticSearch
        Ligneprixproduit ligneprixproduitEs = ligneprixproduitSearchRepository.findOne(testLigneprixproduit.getId());
        assertThat(ligneprixproduitEs).isEqualToComparingFieldByField(testLigneprixproduit);
    }

    @Test
    @Transactional
    public void getAllLigneprixproduits() throws Exception {
        // Initialize the database
        ligneprixproduitRepository.saveAndFlush(ligneprixproduit);

        // Get all the ligneprixproduits
        restLigneprixproduitMockMvc.perform(get("/api/ligneprixproduits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ligneprixproduit.getId().intValue())));
    }

    @Test
    @Transactional
    public void getLigneprixproduit() throws Exception {
        // Initialize the database
        ligneprixproduitRepository.saveAndFlush(ligneprixproduit);

        // Get the ligneprixproduit
        restLigneprixproduitMockMvc.perform(get("/api/ligneprixproduits/{id}", ligneprixproduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ligneprixproduit.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLigneprixproduit() throws Exception {
        // Get the ligneprixproduit
        restLigneprixproduitMockMvc.perform(get("/api/ligneprixproduits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneprixproduit() throws Exception {
        // Initialize the database
        ligneprixproduitService.save(ligneprixproduit);

        int databaseSizeBeforeUpdate = ligneprixproduitRepository.findAll().size();

        // Update the ligneprixproduit
        Ligneprixproduit updatedLigneprixproduit = new Ligneprixproduit();
        updatedLigneprixproduit.setId(ligneprixproduit.getId());

        restLigneprixproduitMockMvc.perform(put("/api/ligneprixproduits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLigneprixproduit)))
                .andExpect(status().isOk());

        // Validate the Ligneprixproduit in the database
        List<Ligneprixproduit> ligneprixproduits = ligneprixproduitRepository.findAll();
        assertThat(ligneprixproduits).hasSize(databaseSizeBeforeUpdate);
        Ligneprixproduit testLigneprixproduit = ligneprixproduits.get(ligneprixproduits.size() - 1);

        // Validate the Ligneprixproduit in ElasticSearch
        Ligneprixproduit ligneprixproduitEs = ligneprixproduitSearchRepository.findOne(testLigneprixproduit.getId());
        assertThat(ligneprixproduitEs).isEqualToComparingFieldByField(testLigneprixproduit);
    }

    @Test
    @Transactional
    public void deleteLigneprixproduit() throws Exception {
        // Initialize the database
        ligneprixproduitService.save(ligneprixproduit);

        int databaseSizeBeforeDelete = ligneprixproduitRepository.findAll().size();

        // Get the ligneprixproduit
        restLigneprixproduitMockMvc.perform(delete("/api/ligneprixproduits/{id}", ligneprixproduit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ligneprixproduitExistsInEs = ligneprixproduitSearchRepository.exists(ligneprixproduit.getId());
        assertThat(ligneprixproduitExistsInEs).isFalse();

        // Validate the database is empty
        List<Ligneprixproduit> ligneprixproduits = ligneprixproduitRepository.findAll();
        assertThat(ligneprixproduits).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneprixproduit() throws Exception {
        // Initialize the database
        ligneprixproduitService.save(ligneprixproduit);

        // Search the ligneprixproduit
        restLigneprixproduitMockMvc.perform(get("/api/_search/ligneprixproduits?query=id:" + ligneprixproduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneprixproduit.getId().intValue())));
    }
}
