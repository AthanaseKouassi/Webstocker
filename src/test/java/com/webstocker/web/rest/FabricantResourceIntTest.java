package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Fabricant;
import com.webstocker.repository.FabricantRepository;
import com.webstocker.service.FabricantService;
import com.webstocker.repository.search.FabricantSearchRepository;

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
 * Test class for the FabricantResource REST controller.
 *
 * @see FabricantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class FabricantResourceIntTest {

    private static final String DEFAULT_NOM_FABRICANT = "AAAAA";
    private static final String UPDATED_NOM_FABRICANT = "BBBBB";
    private static final String DEFAULT_PAYS_FABRICANT = "AAAAA";
    private static final String UPDATED_PAYS_FABRICANT = "BBBBB";

    @Inject
    private FabricantRepository fabricantRepository;

    @Inject
    private FabricantService fabricantService;

    @Inject
    private FabricantSearchRepository fabricantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFabricantMockMvc;

    private Fabricant fabricant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FabricantResource fabricantResource = new FabricantResource();
        ReflectionTestUtils.setField(fabricantResource, "fabricantService", fabricantService);
        this.restFabricantMockMvc = MockMvcBuilders.standaloneSetup(fabricantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fabricantSearchRepository.deleteAll();
        fabricant = new Fabricant();
        fabricant.setNomFabricant(DEFAULT_NOM_FABRICANT);
        fabricant.setPaysFabricant(DEFAULT_PAYS_FABRICANT);
    }

    @Test
    @Transactional
    public void createFabricant() throws Exception {
        int databaseSizeBeforeCreate = fabricantRepository.findAll().size();

        // Create the Fabricant

        restFabricantMockMvc.perform(post("/api/fabricants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fabricant)))
                .andExpect(status().isCreated());

        // Validate the Fabricant in the database
        List<Fabricant> fabricants = fabricantRepository.findAll();
        assertThat(fabricants).hasSize(databaseSizeBeforeCreate + 1);
        Fabricant testFabricant = fabricants.get(fabricants.size() - 1);
        assertThat(testFabricant.getNomFabricant()).isEqualTo(DEFAULT_NOM_FABRICANT);
        assertThat(testFabricant.getPaysFabricant()).isEqualTo(DEFAULT_PAYS_FABRICANT);

        // Validate the Fabricant in ElasticSearch
        Fabricant fabricantEs = fabricantSearchRepository.findOne(testFabricant.getId());
        assertThat(fabricantEs).isEqualToComparingFieldByField(testFabricant);
    }

    @Test
    @Transactional
    public void checkNomFabricantIsRequired() throws Exception {
        int databaseSizeBeforeTest = fabricantRepository.findAll().size();
        // set the field null
        fabricant.setNomFabricant(null);

        // Create the Fabricant, which fails.

        restFabricantMockMvc.perform(post("/api/fabricants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fabricant)))
                .andExpect(status().isBadRequest());

        List<Fabricant> fabricants = fabricantRepository.findAll();
        assertThat(fabricants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFabricants() throws Exception {
        // Initialize the database
        fabricantRepository.saveAndFlush(fabricant);

        // Get all the fabricants
        restFabricantMockMvc.perform(get("/api/fabricants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fabricant.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomFabricant").value(hasItem(DEFAULT_NOM_FABRICANT.toString())))
                .andExpect(jsonPath("$.[*].paysFabricant").value(hasItem(DEFAULT_PAYS_FABRICANT.toString())));
    }

    @Test
    @Transactional
    public void getFabricant() throws Exception {
        // Initialize the database
        fabricantRepository.saveAndFlush(fabricant);

        // Get the fabricant
        restFabricantMockMvc.perform(get("/api/fabricants/{id}", fabricant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fabricant.getId().intValue()))
            .andExpect(jsonPath("$.nomFabricant").value(DEFAULT_NOM_FABRICANT.toString()))
            .andExpect(jsonPath("$.paysFabricant").value(DEFAULT_PAYS_FABRICANT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFabricant() throws Exception {
        // Get the fabricant
        restFabricantMockMvc.perform(get("/api/fabricants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFabricant() throws Exception {
        // Initialize the database
        fabricantService.save(fabricant);

        int databaseSizeBeforeUpdate = fabricantRepository.findAll().size();

        // Update the fabricant
        Fabricant updatedFabricant = new Fabricant();
        updatedFabricant.setId(fabricant.getId());
        updatedFabricant.setNomFabricant(UPDATED_NOM_FABRICANT);
        updatedFabricant.setPaysFabricant(UPDATED_PAYS_FABRICANT);

        restFabricantMockMvc.perform(put("/api/fabricants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFabricant)))
                .andExpect(status().isOk());

        // Validate the Fabricant in the database
        List<Fabricant> fabricants = fabricantRepository.findAll();
        assertThat(fabricants).hasSize(databaseSizeBeforeUpdate);
        Fabricant testFabricant = fabricants.get(fabricants.size() - 1);
        assertThat(testFabricant.getNomFabricant()).isEqualTo(UPDATED_NOM_FABRICANT);
        assertThat(testFabricant.getPaysFabricant()).isEqualTo(UPDATED_PAYS_FABRICANT);

        // Validate the Fabricant in ElasticSearch
        Fabricant fabricantEs = fabricantSearchRepository.findOne(testFabricant.getId());
        assertThat(fabricantEs).isEqualToComparingFieldByField(testFabricant);
    }

    @Test
    @Transactional
    public void deleteFabricant() throws Exception {
        // Initialize the database
        fabricantService.save(fabricant);

        int databaseSizeBeforeDelete = fabricantRepository.findAll().size();

        // Get the fabricant
        restFabricantMockMvc.perform(delete("/api/fabricants/{id}", fabricant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean fabricantExistsInEs = fabricantSearchRepository.exists(fabricant.getId());
        assertThat(fabricantExistsInEs).isFalse();

        // Validate the database is empty
        List<Fabricant> fabricants = fabricantRepository.findAll();
        assertThat(fabricants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFabricant() throws Exception {
        // Initialize the database
        fabricantService.save(fabricant);

        // Search the fabricant
        restFabricantMockMvc.perform(get("/api/_search/fabricants?query=id:" + fabricant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fabricant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFabricant").value(hasItem(DEFAULT_NOM_FABRICANT.toString())))
            .andExpect(jsonPath("$.[*].paysFabricant").value(hasItem(DEFAULT_PAYS_FABRICANT.toString())));
    }
}
