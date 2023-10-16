package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.TypeActivite;
import com.webstocker.repository.TypeActiviteRepository;
import com.webstocker.service.TypeActiviteService;
import com.webstocker.repository.search.TypeActiviteSearchRepository;

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
 * Test class for the TypeActiviteResource REST controller.
 *
 * @see TypeActiviteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeActiviteResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    @Inject
    private TypeActiviteRepository typeActiviteRepository;

    @Inject
    private TypeActiviteService typeActiviteService;

    @Inject
    private TypeActiviteSearchRepository typeActiviteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeActiviteMockMvc;

    private TypeActivite typeActivite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeActiviteResource typeActiviteResource = new TypeActiviteResource();
        ReflectionTestUtils.setField(typeActiviteResource, "typeActiviteService", typeActiviteService);
        this.restTypeActiviteMockMvc = MockMvcBuilders.standaloneSetup(typeActiviteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeActiviteSearchRepository.deleteAll();
        typeActivite = new TypeActivite();
        typeActivite.setLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createTypeActivite() throws Exception {
        int databaseSizeBeforeCreate = typeActiviteRepository.findAll().size();

        // Create the TypeActivite

        restTypeActiviteMockMvc.perform(post("/api/type-activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeActivite)))
                .andExpect(status().isCreated());

        // Validate the TypeActivite in the database
        List<TypeActivite> typeActivites = typeActiviteRepository.findAll();
        assertThat(typeActivites).hasSize(databaseSizeBeforeCreate + 1);
        TypeActivite testTypeActivite = typeActivites.get(typeActivites.size() - 1);
        assertThat(testTypeActivite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeActivite in ElasticSearch
        TypeActivite typeActiviteEs = typeActiviteSearchRepository.findOne(testTypeActivite.getId());
        assertThat(typeActiviteEs).isEqualToComparingFieldByField(testTypeActivite);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeActiviteRepository.findAll().size();
        // set the field null
        typeActivite.setLibelle(null);

        // Create the TypeActivite, which fails.

        restTypeActiviteMockMvc.perform(post("/api/type-activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeActivite)))
                .andExpect(status().isBadRequest());

        List<TypeActivite> typeActivites = typeActiviteRepository.findAll();
        assertThat(typeActivites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeActivites() throws Exception {
        // Initialize the database
        typeActiviteRepository.saveAndFlush(typeActivite);

        // Get all the typeActivites
        restTypeActiviteMockMvc.perform(get("/api/type-activites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeActivite.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getTypeActivite() throws Exception {
        // Initialize the database
        typeActiviteRepository.saveAndFlush(typeActivite);

        // Get the typeActivite
        restTypeActiviteMockMvc.perform(get("/api/type-activites/{id}", typeActivite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeActivite.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeActivite() throws Exception {
        // Get the typeActivite
        restTypeActiviteMockMvc.perform(get("/api/type-activites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeActivite() throws Exception {
        // Initialize the database
        typeActiviteService.save(typeActivite);

        int databaseSizeBeforeUpdate = typeActiviteRepository.findAll().size();

        // Update the typeActivite
        TypeActivite updatedTypeActivite = new TypeActivite();
        updatedTypeActivite.setId(typeActivite.getId());
        updatedTypeActivite.setLibelle(UPDATED_LIBELLE);

        restTypeActiviteMockMvc.perform(put("/api/type-activites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeActivite)))
                .andExpect(status().isOk());

        // Validate the TypeActivite in the database
        List<TypeActivite> typeActivites = typeActiviteRepository.findAll();
        assertThat(typeActivites).hasSize(databaseSizeBeforeUpdate);
        TypeActivite testTypeActivite = typeActivites.get(typeActivites.size() - 1);
        assertThat(testTypeActivite.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeActivite in ElasticSearch
        TypeActivite typeActiviteEs = typeActiviteSearchRepository.findOne(testTypeActivite.getId());
        assertThat(typeActiviteEs).isEqualToComparingFieldByField(testTypeActivite);
    }

    @Test
    @Transactional
    public void deleteTypeActivite() throws Exception {
        // Initialize the database
        typeActiviteService.save(typeActivite);

        int databaseSizeBeforeDelete = typeActiviteRepository.findAll().size();

        // Get the typeActivite
        restTypeActiviteMockMvc.perform(delete("/api/type-activites/{id}", typeActivite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeActiviteExistsInEs = typeActiviteSearchRepository.exists(typeActivite.getId());
        assertThat(typeActiviteExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeActivite> typeActivites = typeActiviteRepository.findAll();
        assertThat(typeActivites).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeActivite() throws Exception {
        // Initialize the database
        typeActiviteService.save(typeActivite);

        // Search the typeActivite
        restTypeActiviteMockMvc.perform(get("/api/_search/type-activites?query=id:" + typeActivite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeActivite.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
