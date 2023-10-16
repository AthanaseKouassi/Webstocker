package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Ligneobjectifvente;
import com.webstocker.repository.LigneobjectifventeRepository;
import com.webstocker.service.LigneobjectifventeService;
import com.webstocker.repository.search.LigneobjectifventeSearchRepository;

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
 * Test class for the LigneobjectifventeResource REST controller.
 *
 * @see LigneobjectifventeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class LigneobjectifventeResourceIntTest {


    private static final Integer DEFAULT_QUANTITE_OBJECTIF = 1;
    private static final Integer UPDATED_QUANTITE_OBJECTIF = 2;

    @Inject
    private LigneobjectifventeRepository ligneobjectifventeRepository;

    @Inject
    private LigneobjectifventeService ligneobjectifventeService;

    @Inject
    private LigneobjectifventeSearchRepository ligneobjectifventeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLigneobjectifventeMockMvc;

    private Ligneobjectifvente ligneobjectifvente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LigneobjectifventeResource ligneobjectifventeResource = new LigneobjectifventeResource();
        ReflectionTestUtils.setField(ligneobjectifventeResource, "ligneobjectifventeService", ligneobjectifventeService);
        this.restLigneobjectifventeMockMvc = MockMvcBuilders.standaloneSetup(ligneobjectifventeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ligneobjectifventeSearchRepository.deleteAll();
        ligneobjectifvente = new Ligneobjectifvente();
        ligneobjectifvente.setQuantiteObjectif(DEFAULT_QUANTITE_OBJECTIF);
    }

    @Test
    @Transactional
    public void createLigneobjectifvente() throws Exception {
        int databaseSizeBeforeCreate = ligneobjectifventeRepository.findAll().size();

        // Create the Ligneobjectifvente

        restLigneobjectifventeMockMvc.perform(post("/api/ligneobjectifventes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ligneobjectifvente)))
                .andExpect(status().isCreated());

        // Validate the Ligneobjectifvente in the database
        List<Ligneobjectifvente> ligneobjectifventes = ligneobjectifventeRepository.findAll();
        assertThat(ligneobjectifventes).hasSize(databaseSizeBeforeCreate + 1);
        Ligneobjectifvente testLigneobjectifvente = ligneobjectifventes.get(ligneobjectifventes.size() - 1);
        assertThat(testLigneobjectifvente.getQuantiteObjectif()).isEqualTo(DEFAULT_QUANTITE_OBJECTIF);

        // Validate the Ligneobjectifvente in ElasticSearch
        Ligneobjectifvente ligneobjectifventeEs = ligneobjectifventeSearchRepository.findOne(testLigneobjectifvente.getId());
        assertThat(ligneobjectifventeEs).isEqualToComparingFieldByField(testLigneobjectifvente);
    }

    @Test
    @Transactional
    public void getAllLigneobjectifventes() throws Exception {
        // Initialize the database
        ligneobjectifventeRepository.saveAndFlush(ligneobjectifvente);

        // Get all the ligneobjectifventes
        restLigneobjectifventeMockMvc.perform(get("/api/ligneobjectifventes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ligneobjectifvente.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantiteObjectif").value(hasItem(DEFAULT_QUANTITE_OBJECTIF)));
    }

    @Test
    @Transactional
    public void getLigneobjectifvente() throws Exception {
        // Initialize the database
        ligneobjectifventeRepository.saveAndFlush(ligneobjectifvente);

        // Get the ligneobjectifvente
        restLigneobjectifventeMockMvc.perform(get("/api/ligneobjectifventes/{id}", ligneobjectifvente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ligneobjectifvente.getId().intValue()))
            .andExpect(jsonPath("$.quantiteObjectif").value(DEFAULT_QUANTITE_OBJECTIF));
    }

    @Test
    @Transactional
    public void getNonExistingLigneobjectifvente() throws Exception {
        // Get the ligneobjectifvente
        restLigneobjectifventeMockMvc.perform(get("/api/ligneobjectifventes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneobjectifvente() throws Exception {
        // Initialize the database
        ligneobjectifventeService.save(ligneobjectifvente);

        int databaseSizeBeforeUpdate = ligneobjectifventeRepository.findAll().size();

        // Update the ligneobjectifvente
        Ligneobjectifvente updatedLigneobjectifvente = new Ligneobjectifvente();
        updatedLigneobjectifvente.setId(ligneobjectifvente.getId());
        updatedLigneobjectifvente.setQuantiteObjectif(UPDATED_QUANTITE_OBJECTIF);

        restLigneobjectifventeMockMvc.perform(put("/api/ligneobjectifventes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLigneobjectifvente)))
                .andExpect(status().isOk());

        // Validate the Ligneobjectifvente in the database
        List<Ligneobjectifvente> ligneobjectifventes = ligneobjectifventeRepository.findAll();
        assertThat(ligneobjectifventes).hasSize(databaseSizeBeforeUpdate);
        Ligneobjectifvente testLigneobjectifvente = ligneobjectifventes.get(ligneobjectifventes.size() - 1);
        assertThat(testLigneobjectifvente.getQuantiteObjectif()).isEqualTo(UPDATED_QUANTITE_OBJECTIF);

        // Validate the Ligneobjectifvente in ElasticSearch
        Ligneobjectifvente ligneobjectifventeEs = ligneobjectifventeSearchRepository.findOne(testLigneobjectifvente.getId());
        assertThat(ligneobjectifventeEs).isEqualToComparingFieldByField(testLigneobjectifvente);
    }

    @Test
    @Transactional
    public void deleteLigneobjectifvente() throws Exception {
        // Initialize the database
        ligneobjectifventeService.save(ligneobjectifvente);

        int databaseSizeBeforeDelete = ligneobjectifventeRepository.findAll().size();

        // Get the ligneobjectifvente
        restLigneobjectifventeMockMvc.perform(delete("/api/ligneobjectifventes/{id}", ligneobjectifvente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ligneobjectifventeExistsInEs = ligneobjectifventeSearchRepository.exists(ligneobjectifvente.getId());
        assertThat(ligneobjectifventeExistsInEs).isFalse();

        // Validate the database is empty
        List<Ligneobjectifvente> ligneobjectifventes = ligneobjectifventeRepository.findAll();
        assertThat(ligneobjectifventes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneobjectifvente() throws Exception {
        // Initialize the database
        ligneobjectifventeService.save(ligneobjectifvente);

        // Search the ligneobjectifvente
        restLigneobjectifventeMockMvc.perform(get("/api/_search/ligneobjectifventes?query=id:" + ligneobjectifvente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneobjectifvente.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantiteObjectif").value(hasItem(DEFAULT_QUANTITE_OBJECTIF)));
    }
}
