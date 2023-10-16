package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Categorieclient;
import com.webstocker.repository.CategorieclientRepository;
import com.webstocker.service.CategorieclientService;
import com.webstocker.repository.search.CategorieclientSearchRepository;

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
 * Test class for the CategorieclientResource REST controller.
 *
 * @see CategorieclientResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class CategorieclientResourceIntTest {

    private static final String DEFAULT_LIBELLE_CATEGORIECLIENT = "AAAAA";
    private static final String UPDATED_LIBELLE_CATEGORIECLIENT = "BBBBB";

    @Inject
    private CategorieclientRepository categorieclientRepository;

    @Inject
    private CategorieclientService categorieclientService;

    @Inject
    private CategorieclientSearchRepository categorieclientSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCategorieclientMockMvc;

    private Categorieclient categorieclient;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategorieclientResource categorieclientResource = new CategorieclientResource();
        ReflectionTestUtils.setField(categorieclientResource, "categorieclientService", categorieclientService);
        this.restCategorieclientMockMvc = MockMvcBuilders.standaloneSetup(categorieclientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        categorieclientSearchRepository.deleteAll();
        categorieclient = new Categorieclient();
        categorieclient.setLibelleCategorieclient(DEFAULT_LIBELLE_CATEGORIECLIENT);
    }

    @Test
    @Transactional
    public void createCategorieclient() throws Exception {
        int databaseSizeBeforeCreate = categorieclientRepository.findAll().size();

        // Create the Categorieclient

        restCategorieclientMockMvc.perform(post("/api/categorieclients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorieclient)))
                .andExpect(status().isCreated());

        // Validate the Categorieclient in the database
        List<Categorieclient> categorieclients = categorieclientRepository.findAll();
        assertThat(categorieclients).hasSize(databaseSizeBeforeCreate + 1);
        Categorieclient testCategorieclient = categorieclients.get(categorieclients.size() - 1);
        assertThat(testCategorieclient.getLibelleCategorieclient()).isEqualTo(DEFAULT_LIBELLE_CATEGORIECLIENT);

        // Validate the Categorieclient in ElasticSearch
        Categorieclient categorieclientEs = categorieclientSearchRepository.findOne(testCategorieclient.getId());
        assertThat(categorieclientEs).isEqualToComparingFieldByField(testCategorieclient);
    }

    @Test
    @Transactional
    public void checkLibelleCategorieClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieclientRepository.findAll().size();
        // set the field null
        categorieclient.setLibelleCategorieclient(null);
        categorieclient.setLibelleCategorieclient(DEFAULT_LIBELLE_CATEGORIECLIENT);
        // Create the Categorieclient, which fails.

        restCategorieclientMockMvc.perform(post("/api/categorieclients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorieclient)))
                .andExpect(status().isBadRequest());

        List<Categorieclient> categorieclients = categorieclientRepository.findAll();
        assertThat(categorieclients).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategorieclients() throws Exception {
        // Initialize the database
        categorieclientRepository.saveAndFlush(categorieclient);

        // Get all the categorieclients
        restCategorieclientMockMvc.perform(get("/api/categorieclients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categorieclient.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelleCategorieclient").value(hasItem(DEFAULT_LIBELLE_CATEGORIECLIENT.toString())));
    }

    @Test
    @Transactional
    public void getCategorieclient() throws Exception {
        // Initialize the database
        categorieclientRepository.saveAndFlush(categorieclient);

        // Get the categorieclient
        restCategorieclientMockMvc.perform(get("/api/categorieclients/{id}", categorieclient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categorieclient.getId().intValue()))
            .andExpect(jsonPath("$.libelleCategorieclient").value(DEFAULT_LIBELLE_CATEGORIECLIENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategorieclient() throws Exception {
        // Get the categorieclient
        restCategorieclientMockMvc.perform(get("/api/categorieclients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorieclient() throws Exception {
        // Initialize the database
        categorieclientService.save(categorieclient);

        int databaseSizeBeforeUpdate = categorieclientRepository.findAll().size();

        // Update the categorieclient
        Categorieclient updatedCategorieclient = new Categorieclient();
        updatedCategorieclient.setId(categorieclient.getId());
        updatedCategorieclient.setLibelleCategorieclient(UPDATED_LIBELLE_CATEGORIECLIENT);

        restCategorieclientMockMvc.perform(put("/api/categorieclients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCategorieclient)))
                .andExpect(status().isOk());

        // Validate the Categorieclient in the database
        List<Categorieclient> categorieclients = categorieclientRepository.findAll();
        assertThat(categorieclients).hasSize(databaseSizeBeforeUpdate);
        Categorieclient testCategorieclient = categorieclients.get(categorieclients.size() - 1);
        assertThat(testCategorieclient.getLibelleCategorieclient()).isEqualTo(UPDATED_LIBELLE_CATEGORIECLIENT);

        // Validate the Categorieclient in ElasticSearch
        Categorieclient categorieclientEs = categorieclientSearchRepository.findOne(testCategorieclient.getId());
        assertThat(categorieclientEs).isEqualToComparingFieldByField(testCategorieclient);
    }

    @Test
    @Transactional
    public void deleteCategorieclient() throws Exception {
        // Initialize the database
        categorieclientService.save(categorieclient);

        int databaseSizeBeforeDelete = categorieclientRepository.findAll().size();

        // Get the categorieclient
        restCategorieclientMockMvc.perform(delete("/api/categorieclients/{id}", categorieclient.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean categorieclientExistsInEs = categorieclientSearchRepository.exists(categorieclient.getId());
        assertThat(categorieclientExistsInEs).isFalse();

        // Validate the database is empty
        List<Categorieclient> categorieclients = categorieclientRepository.findAll();
        assertThat(categorieclients).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCategorieclient() throws Exception {
        // Initialize the database
        categorieclientService.save(categorieclient);

        // Search the categorieclient
        restCategorieclientMockMvc.perform(get("/api/_search/categorieclients?query=id:" + categorieclient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieclient.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleCategorieclient").value(hasItem(DEFAULT_LIBELLE_CATEGORIECLIENT.toString())));
    }
}
