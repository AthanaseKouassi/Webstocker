package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Commune;
import com.webstocker.repository.CommuneRepository;
import com.webstocker.service.CommuneService;
import com.webstocker.repository.search.CommuneSearchRepository;

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
 * Test class for the CommuneResource REST controller.
 *
 * @see CommuneResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class CommuneResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    @Inject
    private CommuneRepository communeRepository;

    @Inject
    private CommuneService communeService;

    @Inject
    private CommuneSearchRepository communeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommuneMockMvc;

    private Commune commune;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommuneResource communeResource = new CommuneResource();
        ReflectionTestUtils.setField(communeResource, "communeService", communeService);
        this.restCommuneMockMvc = MockMvcBuilders.standaloneSetup(communeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        communeSearchRepository.deleteAll();
        commune = new Commune();
        commune.setLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createCommune() throws Exception {
        int databaseSizeBeforeCreate = communeRepository.findAll().size();

        // Create the Commune

        restCommuneMockMvc.perform(post("/api/communes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commune)))
                .andExpect(status().isCreated());

        // Validate the Commune in the database
        List<Commune> communes = communeRepository.findAll();
        assertThat(communes).hasSize(databaseSizeBeforeCreate + 1);
        Commune testCommune = communes.get(communes.size() - 1);
        assertThat(testCommune.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Commune in ElasticSearch
        Commune communeEs = communeSearchRepository.findOne(testCommune.getId());
        assertThat(communeEs).isEqualToComparingFieldByField(testCommune);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = communeRepository.findAll().size();
        // set the field null
        commune.setLibelle(null);

        // Create the Commune, which fails.

        restCommuneMockMvc.perform(post("/api/communes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commune)))
                .andExpect(status().isBadRequest());

        List<Commune> communes = communeRepository.findAll();
        assertThat(communes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommunes() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communes
        restCommuneMockMvc.perform(get("/api/communes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commune.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get the commune
        restCommuneMockMvc.perform(get("/api/communes/{id}", commune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commune.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommune() throws Exception {
        // Get the commune
        restCommuneMockMvc.perform(get("/api/communes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommune() throws Exception {
        // Initialize the database
        communeService.save(commune);

        int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune
        Commune updatedCommune = new Commune();
        updatedCommune.setId(commune.getId());
        updatedCommune.setLibelle(UPDATED_LIBELLE);

        restCommuneMockMvc.perform(put("/api/communes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommune)))
                .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communes = communeRepository.findAll();
        assertThat(communes).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communes.get(communes.size() - 1);
        assertThat(testCommune.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Commune in ElasticSearch
        Commune communeEs = communeSearchRepository.findOne(testCommune.getId());
        assertThat(communeEs).isEqualToComparingFieldByField(testCommune);
    }

    @Test
    @Transactional
    public void deleteCommune() throws Exception {
        // Initialize the database
        communeService.save(commune);

        int databaseSizeBeforeDelete = communeRepository.findAll().size();

        // Get the commune
        restCommuneMockMvc.perform(delete("/api/communes/{id}", commune.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean communeExistsInEs = communeSearchRepository.exists(commune.getId());
        assertThat(communeExistsInEs).isFalse();

        // Validate the database is empty
        List<Commune> communes = communeRepository.findAll();
        assertThat(communes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommune() throws Exception {
        // Initialize the database
        communeService.save(commune);

        // Search the commune
        restCommuneMockMvc.perform(get("/api/_search/communes?query=id:" + commune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commune.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
