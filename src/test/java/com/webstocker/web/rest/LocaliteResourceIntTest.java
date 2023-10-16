package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Localite;
import com.webstocker.repository.LocaliteRepository;
import com.webstocker.service.LocaliteService;
import com.webstocker.repository.search.LocaliteSearchRepository;

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
 * Test class for the LocaliteResource REST controller.
 *
 * @see LocaliteResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class LocaliteResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";

    @Inject
    private LocaliteRepository localiteRepository;

    @Inject
    private LocaliteService localiteService;

    @Inject
    private LocaliteSearchRepository localiteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocaliteMockMvc;

    private Localite localite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocaliteResource localiteResource = new LocaliteResource();
        ReflectionTestUtils.setField(localiteResource, "localiteService", localiteService);
        this.restLocaliteMockMvc = MockMvcBuilders.standaloneSetup(localiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        localiteSearchRepository.deleteAll();
        localite = new Localite();
        localite.setNom(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createLocalite() throws Exception {
        int databaseSizeBeforeCreate = localiteRepository.findAll().size();

        // Create the Localite

        restLocaliteMockMvc.perform(post("/api/localites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(localite)))
                .andExpect(status().isCreated());

        // Validate the Localite in the database
        List<Localite> localites = localiteRepository.findAll();
        assertThat(localites).hasSize(databaseSizeBeforeCreate + 1);
        Localite testLocalite = localites.get(localites.size() - 1);
        assertThat(testLocalite.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Localite in ElasticSearch
        Localite localiteEs = localiteSearchRepository.findOne(testLocalite.getId());
        assertThat(localiteEs).isEqualToComparingFieldByField(testLocalite);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = localiteRepository.findAll().size();
        // set the field null
        localite.setNom(null);

        // Create the Localite, which fails.

        restLocaliteMockMvc.perform(post("/api/localites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(localite)))
                .andExpect(status().isBadRequest());

        List<Localite> localites = localiteRepository.findAll();
        assertThat(localites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocalites() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localites
        restLocaliteMockMvc.perform(get("/api/localites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(localite.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get the localite
        restLocaliteMockMvc.perform(get("/api/localites/{id}", localite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(localite.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocalite() throws Exception {
        // Get the localite
        restLocaliteMockMvc.perform(get("/api/localites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocalite() throws Exception {
        // Initialize the database
        localiteService.save(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite
        Localite updatedLocalite = new Localite();
        updatedLocalite.setId(localite.getId());
        updatedLocalite.setNom(UPDATED_NOM);

        restLocaliteMockMvc.perform(put("/api/localites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLocalite)))
                .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localites = localiteRepository.findAll();
        assertThat(localites).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localites.get(localites.size() - 1);
        assertThat(testLocalite.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Localite in ElasticSearch
        Localite localiteEs = localiteSearchRepository.findOne(testLocalite.getId());
        assertThat(localiteEs).isEqualToComparingFieldByField(testLocalite);
    }

    @Test
    @Transactional
    public void deleteLocalite() throws Exception {
        // Initialize the database
        localiteService.save(localite);

        int databaseSizeBeforeDelete = localiteRepository.findAll().size();

        // Get the localite
        restLocaliteMockMvc.perform(delete("/api/localites/{id}", localite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean localiteExistsInEs = localiteSearchRepository.exists(localite.getId());
        assertThat(localiteExistsInEs).isFalse();

        // Validate the database is empty
        List<Localite> localites = localiteRepository.findAll();
        assertThat(localites).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocalite() throws Exception {
        // Initialize the database
        localiteService.save(localite);

        // Search the localite
        restLocaliteMockMvc.perform(get("/api/_search/localites?query=id:" + localite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }
}
