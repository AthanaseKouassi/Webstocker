package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.LigneMissionActivite;
import com.webstocker.repository.LigneMissionActiviteRepository;
import com.webstocker.service.LigneMissionActiviteService;
import com.webstocker.repository.search.LigneMissionActiviteSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LigneMissionActiviteResource REST controller.
 *
 * @see LigneMissionActiviteResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class LigneMissionActiviteResourceIntTest {

    private static final String DEFAULT_RESULTAT_OBTENU = "AAAAA";
    private static final String UPDATED_RESULTAT_OBTENU = "BBBBB";

    private static final LocalDate DEFAULT_DATE_RESULTAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RESULTAT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private LigneMissionActiviteRepository ligneMissionActiviteRepository;

    @Inject
    private LigneMissionActiviteService ligneMissionActiviteService;

    @Inject
    private LigneMissionActiviteSearchRepository ligneMissionActiviteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLigneMissionActiviteMockMvc;

    private LigneMissionActivite ligneMissionActivite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LigneMissionActiviteResource ligneMissionActiviteResource = new LigneMissionActiviteResource();
        ReflectionTestUtils.setField(ligneMissionActiviteResource, "ligneMissionActiviteService", ligneMissionActiviteService);
        this.restLigneMissionActiviteMockMvc = MockMvcBuilders.standaloneSetup(ligneMissionActiviteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ligneMissionActiviteSearchRepository.deleteAll();
        ligneMissionActivite = new LigneMissionActivite();
        ligneMissionActivite.setResultatObtenu(DEFAULT_RESULTAT_OBTENU);
        ligneMissionActivite.setDateResultat(DEFAULT_DATE_RESULTAT);
    }

    @Test
    @Transactional
    public void createLigneMissionActivite() throws Exception {
        int databaseSizeBeforeCreate = ligneMissionActiviteRepository.findAll().size();

        // Create the LigneMissionActivite

        restLigneMissionActiviteMockMvc.perform(post("/api/ligne-mission-activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ligneMissionActivite)))
                .andExpect(status().isCreated());

        // Validate the LigneMissionActivite in the database
        List<LigneMissionActivite> ligneMissionActivites = ligneMissionActiviteRepository.findAll();
        assertThat(ligneMissionActivites).hasSize(databaseSizeBeforeCreate + 1);
        LigneMissionActivite testLigneMissionActivite = ligneMissionActivites.get(ligneMissionActivites.size() - 1);
        assertThat(testLigneMissionActivite.getResultatObtenu()).isEqualTo(DEFAULT_RESULTAT_OBTENU);
        assertThat(testLigneMissionActivite.getDateResultat()).isEqualTo(DEFAULT_DATE_RESULTAT);

        // Validate the LigneMissionActivite in ElasticSearch
        LigneMissionActivite ligneMissionActiviteEs = ligneMissionActiviteSearchRepository.findOne(testLigneMissionActivite.getId());
        assertThat(ligneMissionActiviteEs).isEqualToComparingFieldByField(testLigneMissionActivite);
    }

    @Test
    @Transactional
    public void getAllLigneMissionActivites() throws Exception {
        // Initialize the database
        ligneMissionActiviteRepository.saveAndFlush(ligneMissionActivite);

        // Get all the ligneMissionActivites
        restLigneMissionActiviteMockMvc.perform(get("/api/ligne-mission-activites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ligneMissionActivite.getId().intValue())))
                .andExpect(jsonPath("$.[*].resultatObtenu").value(hasItem(DEFAULT_RESULTAT_OBTENU.toString())))
                .andExpect(jsonPath("$.[*].dateResultat").value(hasItem(DEFAULT_DATE_RESULTAT.toString())));
    }

    @Test
    @Transactional
    public void getLigneMissionActivite() throws Exception {
        // Initialize the database
        ligneMissionActiviteRepository.saveAndFlush(ligneMissionActivite);

        // Get the ligneMissionActivite
        restLigneMissionActiviteMockMvc.perform(get("/api/ligne-mission-activites/{id}", ligneMissionActivite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ligneMissionActivite.getId().intValue()))
            .andExpect(jsonPath("$.resultatObtenu").value(DEFAULT_RESULTAT_OBTENU.toString()))
            .andExpect(jsonPath("$.dateResultat").value(DEFAULT_DATE_RESULTAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLigneMissionActivite() throws Exception {
        // Get the ligneMissionActivite
        restLigneMissionActiviteMockMvc.perform(get("/api/ligne-mission-activites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneMissionActivite() throws Exception {
        // Initialize the database
        ligneMissionActiviteService.save(ligneMissionActivite);

        int databaseSizeBeforeUpdate = ligneMissionActiviteRepository.findAll().size();

        // Update the ligneMissionActivite
        LigneMissionActivite updatedLigneMissionActivite = new LigneMissionActivite();
        updatedLigneMissionActivite.setId(ligneMissionActivite.getId());
        updatedLigneMissionActivite.setResultatObtenu(UPDATED_RESULTAT_OBTENU);
        updatedLigneMissionActivite.setDateResultat(UPDATED_DATE_RESULTAT);

        restLigneMissionActiviteMockMvc.perform(put("/api/ligne-mission-activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLigneMissionActivite)))
                .andExpect(status().isOk());

        // Validate the LigneMissionActivite in the database
        List<LigneMissionActivite> ligneMissionActivites = ligneMissionActiviteRepository.findAll();
        assertThat(ligneMissionActivites).hasSize(databaseSizeBeforeUpdate);
        LigneMissionActivite testLigneMissionActivite = ligneMissionActivites.get(ligneMissionActivites.size() - 1);
        assertThat(testLigneMissionActivite.getResultatObtenu()).isEqualTo(UPDATED_RESULTAT_OBTENU);
        assertThat(testLigneMissionActivite.getDateResultat()).isEqualTo(UPDATED_DATE_RESULTAT);

        // Validate the LigneMissionActivite in ElasticSearch
        LigneMissionActivite ligneMissionActiviteEs = ligneMissionActiviteSearchRepository.findOne(testLigneMissionActivite.getId());
        assertThat(ligneMissionActiviteEs).isEqualToComparingFieldByField(testLigneMissionActivite);
    }

    @Test
    @Transactional
    public void deleteLigneMissionActivite() throws Exception {
        // Initialize the database
        ligneMissionActiviteService.save(ligneMissionActivite);

        int databaseSizeBeforeDelete = ligneMissionActiviteRepository.findAll().size();

        // Get the ligneMissionActivite
        restLigneMissionActiviteMockMvc.perform(delete("/api/ligne-mission-activites/{id}", ligneMissionActivite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ligneMissionActiviteExistsInEs = ligneMissionActiviteSearchRepository.exists(ligneMissionActivite.getId());
        assertThat(ligneMissionActiviteExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneMissionActivite> ligneMissionActivites = ligneMissionActiviteRepository.findAll();
        assertThat(ligneMissionActivites).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneMissionActivite() throws Exception {
        // Initialize the database
        ligneMissionActiviteService.save(ligneMissionActivite);

        // Search the ligneMissionActivite
        restLigneMissionActiviteMockMvc.perform(get("/api/_search/ligne-mission-activites?query=id:" + ligneMissionActivite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneMissionActivite.getId().intValue())))
            .andExpect(jsonPath("$.[*].resultatObtenu").value(hasItem(DEFAULT_RESULTAT_OBTENU.toString())))
            .andExpect(jsonPath("$.[*].dateResultat").value(hasItem(DEFAULT_DATE_RESULTAT.toString())));
    }
}
