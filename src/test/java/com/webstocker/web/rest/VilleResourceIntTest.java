package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Ville;
import com.webstocker.repository.VilleRepository;
import com.webstocker.service.VilleService;
import com.webstocker.repository.search.VilleSearchRepository;

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
 * Test class for the VilleResource REST controller.
 *
 * @see VilleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class VilleResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    @Inject
    private VilleRepository villeRepository;

    @Inject
    private VilleService villeService;

    @Inject
    private VilleSearchRepository villeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVilleMockMvc;

    private Ville ville;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VilleResource villeResource = new VilleResource();
        ReflectionTestUtils.setField(villeResource, "villeService", villeService);
        this.restVilleMockMvc = MockMvcBuilders.standaloneSetup(villeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        villeSearchRepository.deleteAll();
        ville = new Ville();
        ville.setLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createVille() throws Exception {
        int databaseSizeBeforeCreate = villeRepository.findAll().size();

        // Create the Ville

        restVilleMockMvc.perform(post("/api/villes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ville)))
                .andExpect(status().isCreated());

        // Validate the Ville in the database
        List<Ville> villes = villeRepository.findAll();
        assertThat(villes).hasSize(databaseSizeBeforeCreate + 1);
        Ville testVille = villes.get(villes.size() - 1);
        assertThat(testVille.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Ville in ElasticSearch
        Ville villeEs = villeSearchRepository.findOne(testVille.getId());
        assertThat(villeEs).isEqualToComparingFieldByField(testVille);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = villeRepository.findAll().size();
        // set the field null
        ville.setLibelle(null);

        // Create the Ville, which fails.

        restVilleMockMvc.perform(post("/api/villes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ville)))
                .andExpect(status().isBadRequest());

        List<Ville> villes = villeRepository.findAll();
        assertThat(villes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVilles() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villes
        restVilleMockMvc.perform(get("/api/villes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ville.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getVille() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get the ville
        restVilleMockMvc.perform(get("/api/villes/{id}", ville.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ville.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVille() throws Exception {
        // Get the ville
        restVilleMockMvc.perform(get("/api/villes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVille() throws Exception {
        // Initialize the database
        villeService.save(ville);

        int databaseSizeBeforeUpdate = villeRepository.findAll().size();

        // Update the ville
        Ville updatedVille = new Ville();
        updatedVille.setId(ville.getId());
        updatedVille.setLibelle(UPDATED_LIBELLE);

        restVilleMockMvc.perform(put("/api/villes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVille)))
                .andExpect(status().isOk());

        // Validate the Ville in the database
        List<Ville> villes = villeRepository.findAll();
        assertThat(villes).hasSize(databaseSizeBeforeUpdate);
        Ville testVille = villes.get(villes.size() - 1);
        assertThat(testVille.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Ville in ElasticSearch
        Ville villeEs = villeSearchRepository.findOne(testVille.getId());
        assertThat(villeEs).isEqualToComparingFieldByField(testVille);
    }

    @Test
    @Transactional
    public void deleteVille() throws Exception {
        // Initialize the database
        villeService.save(ville);

        int databaseSizeBeforeDelete = villeRepository.findAll().size();

        // Get the ville
        restVilleMockMvc.perform(delete("/api/villes/{id}", ville.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean villeExistsInEs = villeSearchRepository.exists(ville.getId());
        assertThat(villeExistsInEs).isFalse();

        // Validate the database is empty
        List<Ville> villes = villeRepository.findAll();
        assertThat(villes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVille() throws Exception {
        // Initialize the database
        villeService.save(ville);

        // Search the ville
        restVilleMockMvc.perform(get("/api/_search/villes?query=id:" + ville.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ville.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
