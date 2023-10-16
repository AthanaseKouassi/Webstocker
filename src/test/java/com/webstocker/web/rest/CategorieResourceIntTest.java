package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Categorie;
import com.webstocker.repository.CategorieRepository;
import com.webstocker.service.CategorieService;
import com.webstocker.repository.search.CategorieSearchRepository;

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
 * Test class for the CategorieResource REST controller.
 *
 * @see CategorieResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class CategorieResourceIntTest {

    private static final String DEFAULT_NOM_CATEGORIE = "AAAAA";
    private static final String UPDATED_NOM_CATEGORIE = "BBBBB";

    @Inject
    private CategorieRepository categorieRepository;

    @Inject
    private CategorieService categorieService;

    @Inject
    private CategorieSearchRepository categorieSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCategorieMockMvc;

    private Categorie categorie;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategorieResource categorieResource = new CategorieResource();
        ReflectionTestUtils.setField(categorieResource, "categorieService", categorieService);
        this.restCategorieMockMvc = MockMvcBuilders.standaloneSetup(categorieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        categorieSearchRepository.deleteAll();
        categorie = new Categorie();
        categorie.setNomCategorie(DEFAULT_NOM_CATEGORIE);
    }

    @Test
    @Transactional
    public void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();

        // Create the Categorie

        restCategorieMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorie)))
                .andExpect(status().isCreated());

        // Validate the Categorie in the database
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categories.get(categories.size() - 1);
        assertThat(testCategorie.getNomCategorie()).isEqualTo(DEFAULT_NOM_CATEGORIE);

        // Validate the Categorie in ElasticSearch
        Categorie categorieEs = categorieSearchRepository.findOne(testCategorie.getId());
        assertThat(categorieEs).isEqualToComparingFieldByField(testCategorie);
    }

    @Test
    @Transactional
    public void checkNomCategorieIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieRepository.findAll().size();
        // set the field null
        categorie.setNomCategorie(null);

        // Create the Categorie, which fails.

        restCategorieMockMvc.perform(post("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categorie)))
                .andExpect(status().isBadRequest());

        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categories
        restCategorieMockMvc.perform(get("/api/categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomCategorie").value(hasItem(DEFAULT_NOM_CATEGORIE.toString())));
    }

    @Test
    @Transactional
    public void getCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categorie.getId().intValue()))
            .andExpect(jsonPath("$.nomCategorie").value(DEFAULT_NOM_CATEGORIE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategorie() throws Exception {
        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorie() throws Exception {
        // Initialize the database
        categorieService.save(categorie);

        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie
        Categorie updatedCategorie = new Categorie();
        updatedCategorie.setId(categorie.getId());
        updatedCategorie.setNomCategorie(UPDATED_NOM_CATEGORIE);

        restCategorieMockMvc.perform(put("/api/categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCategorie)))
                .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categories.get(categories.size() - 1);
        assertThat(testCategorie.getNomCategorie()).isEqualTo(UPDATED_NOM_CATEGORIE);

        // Validate the Categorie in ElasticSearch
        Categorie categorieEs = categorieSearchRepository.findOne(testCategorie.getId());
        assertThat(categorieEs).isEqualToComparingFieldByField(testCategorie);
    }

    @Test
    @Transactional
    public void deleteCategorie() throws Exception {
        // Initialize the database
        categorieService.save(categorie);

        int databaseSizeBeforeDelete = categorieRepository.findAll().size();

        // Get the categorie
        restCategorieMockMvc.perform(delete("/api/categories/{id}", categorie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean categorieExistsInEs = categorieSearchRepository.exists(categorie.getId());
        assertThat(categorieExistsInEs).isFalse();

        // Validate the database is empty
        List<Categorie> categories = categorieRepository.findAll();
        assertThat(categories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCategorie() throws Exception {
        // Initialize the database
        categorieService.save(categorie);

        // Search the categorie
        restCategorieMockMvc.perform(get("/api/_search/categories?query=id:" + categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCategorie").value(hasItem(DEFAULT_NOM_CATEGORIE.toString())));
    }
}
