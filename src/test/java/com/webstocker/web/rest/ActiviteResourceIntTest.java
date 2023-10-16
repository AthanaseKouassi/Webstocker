package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Activite;
import com.webstocker.repository.ActiviteRepository;
import com.webstocker.service.ActiviteService;
import com.webstocker.repository.search.ActiviteSearchRepository;

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
 * Test class for the ActiviteResource REST controller.
 *
 * @see ActiviteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class ActiviteResourceIntTest {

    private static final String DEFAULT_NOM_ACTIVITE = "AAAAA";
    private static final String UPDATED_NOM_ACTIVITE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION_ACTIVITE = "AAAAA";
    private static final String UPDATED_DESCRIPTION_ACTIVITE = "BBBBB";
    private static final String DEFAULT_RESULTAT_ATTENDU = "AAAAA";
    private static final String UPDATED_RESULTAT_ATTENDU = "BBBBB";

    @Inject
    private ActiviteRepository activiteRepository;

    @Inject
    private ActiviteService activiteService;

    @Inject
    private ActiviteSearchRepository activiteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActiviteMockMvc;

    private Activite activite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActiviteResource activiteResource = new ActiviteResource();
        ReflectionTestUtils.setField(activiteResource, "activiteService", activiteService);
        this.restActiviteMockMvc = MockMvcBuilders.standaloneSetup(activiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        activiteSearchRepository.deleteAll();
        activite = new Activite();
        activite.setNomActivite(DEFAULT_NOM_ACTIVITE);
        activite.setDescriptionActivite(DEFAULT_DESCRIPTION_ACTIVITE);
        activite.setResultatAttendu(DEFAULT_RESULTAT_ATTENDU);
    }

    @Test
    @Transactional
    public void createActivite() throws Exception {
        int databaseSizeBeforeCreate = activiteRepository.findAll().size();

        // Create the Activite

        restActiviteMockMvc.perform(post("/api/activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activite)))
                .andExpect(status().isCreated());

        // Validate the Activite in the database
        List<Activite> activites = activiteRepository.findAll();
        assertThat(activites).hasSize(databaseSizeBeforeCreate + 1);
        Activite testActivite = activites.get(activites.size() - 1);
        assertThat(testActivite.getNomActivite()).isEqualTo(DEFAULT_NOM_ACTIVITE);
        assertThat(testActivite.getDescriptionActivite()).isEqualTo(DEFAULT_DESCRIPTION_ACTIVITE);
        assertThat(testActivite.getResultatAttendu()).isEqualTo(DEFAULT_RESULTAT_ATTENDU);

        // Validate the Activite in ElasticSearch
        Activite activiteEs = activiteSearchRepository.findOne(testActivite.getId());
        assertThat(activiteEs).isEqualToComparingFieldByField(testActivite);
    }

    @Test
    @Transactional
    public void checkNomActiviteIsRequired() throws Exception {
        int databaseSizeBeforeTest = activiteRepository.findAll().size();
        // set the field null
        activite.setNomActivite(null);

        // Create the Activite, which fails.

        restActiviteMockMvc.perform(post("/api/activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activite)))
                .andExpect(status().isBadRequest());

        List<Activite> activites = activiteRepository.findAll();
        assertThat(activites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResultatAttenduIsRequired() throws Exception {
        int databaseSizeBeforeTest = activiteRepository.findAll().size();
        // set the field null
        activite.setResultatAttendu(null);

        // Create the Activite, which fails.

        restActiviteMockMvc.perform(post("/api/activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activite)))
                .andExpect(status().isBadRequest());

        List<Activite> activites = activiteRepository.findAll();
        assertThat(activites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActivites() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activites
        restActiviteMockMvc.perform(get("/api/activites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomActivite").value(hasItem(DEFAULT_NOM_ACTIVITE.toString())))
                .andExpect(jsonPath("$.[*].descriptionActivite").value(hasItem(DEFAULT_DESCRIPTION_ACTIVITE.toString())))
                .andExpect(jsonPath("$.[*].resultatAttendu").value(hasItem(DEFAULT_RESULTAT_ATTENDU.toString())));
    }

    @Test
    @Transactional
    public void getActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get the activite
        restActiviteMockMvc.perform(get("/api/activites/{id}", activite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(activite.getId().intValue()))
            .andExpect(jsonPath("$.nomActivite").value(DEFAULT_NOM_ACTIVITE.toString()))
            .andExpect(jsonPath("$.descriptionActivite").value(DEFAULT_DESCRIPTION_ACTIVITE.toString()))
            .andExpect(jsonPath("$.resultatAttendu").value(DEFAULT_RESULTAT_ATTENDU.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivite() throws Exception {
        // Get the activite
        restActiviteMockMvc.perform(get("/api/activites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivite() throws Exception {
        // Initialize the database
        activiteService.save(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite
        Activite updatedActivite = new Activite();
        updatedActivite.setId(activite.getId());
        updatedActivite.setNomActivite(UPDATED_NOM_ACTIVITE);
        updatedActivite.setDescriptionActivite(UPDATED_DESCRIPTION_ACTIVITE);
        updatedActivite.setResultatAttendu(UPDATED_RESULTAT_ATTENDU);

        restActiviteMockMvc.perform(put("/api/activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActivite)))
                .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activites = activiteRepository.findAll();
        assertThat(activites).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activites.get(activites.size() - 1);
        assertThat(testActivite.getNomActivite()).isEqualTo(UPDATED_NOM_ACTIVITE);
        assertThat(testActivite.getDescriptionActivite()).isEqualTo(UPDATED_DESCRIPTION_ACTIVITE);
        assertThat(testActivite.getResultatAttendu()).isEqualTo(UPDATED_RESULTAT_ATTENDU);

        // Validate the Activite in ElasticSearch
        Activite activiteEs = activiteSearchRepository.findOne(testActivite.getId());
        assertThat(activiteEs).isEqualToComparingFieldByField(testActivite);
    }

    @Test
    @Transactional
    public void deleteActivite() throws Exception {
        // Initialize the database
        activiteService.save(activite);

        int databaseSizeBeforeDelete = activiteRepository.findAll().size();

        // Get the activite
        restActiviteMockMvc.perform(delete("/api/activites/{id}", activite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean activiteExistsInEs = activiteSearchRepository.exists(activite.getId());
        assertThat(activiteExistsInEs).isFalse();

        // Validate the database is empty
        List<Activite> activites = activiteRepository.findAll();
        assertThat(activites).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchActivite() throws Exception {
        // Initialize the database
        activiteService.save(activite);

        // Search the activite
        restActiviteMockMvc.perform(get("/api/_search/activites?query=id:" + activite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomActivite").value(hasItem(DEFAULT_NOM_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].descriptionActivite").value(hasItem(DEFAULT_DESCRIPTION_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].resultatAttendu").value(hasItem(DEFAULT_RESULTAT_ATTENDU.toString())));
    }
}
