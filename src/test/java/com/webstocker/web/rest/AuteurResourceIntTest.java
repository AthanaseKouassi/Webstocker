package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Auteur;
import com.webstocker.repository.AuteurRepository;
import com.webstocker.service.AuteurService;
import com.webstocker.repository.search.AuteurSearchRepository;

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
 * Test class for the AuteurResource REST controller.
 *
 * @see AuteurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class AuteurResourceIntTest {


    @Inject
    private AuteurRepository auteurRepository;

    @Inject
    private AuteurService auteurService;

    @Inject
    private AuteurSearchRepository auteurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAuteurMockMvc;

    private Auteur auteur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuteurResource auteurResource = new AuteurResource();
        ReflectionTestUtils.setField(auteurResource, "auteurService", auteurService);
        this.restAuteurMockMvc = MockMvcBuilders.standaloneSetup(auteurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        auteurSearchRepository.deleteAll();
        auteur = new Auteur();
    }

    @Test
    @Transactional
    public void createAuteur() throws Exception {
        int databaseSizeBeforeCreate = auteurRepository.findAll().size();

        // Create the Auteur

        restAuteurMockMvc.perform(post("/api/auteurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auteur)))
                .andExpect(status().isCreated());

        // Validate the Auteur in the database
        List<Auteur> auteurs = auteurRepository.findAll();
        assertThat(auteurs).hasSize(databaseSizeBeforeCreate + 1);
        Auteur testAuteur = auteurs.get(auteurs.size() - 1);

        // Validate the Auteur in ElasticSearch
        Auteur auteurEs = auteurSearchRepository.findOne(testAuteur.getId());
        assertThat(auteurEs).isEqualToComparingFieldByField(testAuteur);
    }

    @Test
    @Transactional
    public void getAllAuteurs() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get all the auteurs
        restAuteurMockMvc.perform(get("/api/auteurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auteur.getId().intValue())));
    }

    @Test
    @Transactional
    public void getAuteur() throws Exception {
        // Initialize the database
        auteurRepository.saveAndFlush(auteur);

        // Get the auteur
        restAuteurMockMvc.perform(get("/api/auteurs/{id}", auteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(auteur.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAuteur() throws Exception {
        // Get the auteur
        restAuteurMockMvc.perform(get("/api/auteurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuteur() throws Exception {
        // Initialize the database
        auteurService.save(auteur);

        int databaseSizeBeforeUpdate = auteurRepository.findAll().size();

        // Update the auteur
        Auteur updatedAuteur = new Auteur();
        updatedAuteur.setId(auteur.getId());

        restAuteurMockMvc.perform(put("/api/auteurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuteur)))
                .andExpect(status().isOk());

        // Validate the Auteur in the database
        List<Auteur> auteurs = auteurRepository.findAll();
        assertThat(auteurs).hasSize(databaseSizeBeforeUpdate);
        Auteur testAuteur = auteurs.get(auteurs.size() - 1);

        // Validate the Auteur in ElasticSearch
        Auteur auteurEs = auteurSearchRepository.findOne(testAuteur.getId());
        assertThat(auteurEs).isEqualToComparingFieldByField(testAuteur);
    }

    @Test
    @Transactional
    public void deleteAuteur() throws Exception {
        // Initialize the database
        auteurService.save(auteur);

        int databaseSizeBeforeDelete = auteurRepository.findAll().size();

        // Get the auteur
        restAuteurMockMvc.perform(delete("/api/auteurs/{id}", auteur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean auteurExistsInEs = auteurSearchRepository.exists(auteur.getId());
        assertThat(auteurExistsInEs).isFalse();

        // Validate the database is empty
        List<Auteur> auteurs = auteurRepository.findAll();
        assertThat(auteurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAuteur() throws Exception {
        // Initialize the database
        auteurService.save(auteur);

        // Search the auteur
        restAuteurMockMvc.perform(get("/api/_search/auteurs?query=id:" + auteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auteur.getId().intValue())));
    }
}
