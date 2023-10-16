package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Produit;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.service.ProduitService;
import com.webstocker.repository.search.ProduitSearchRepository;

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
 * Test class for the ProduitResource REST controller.
 *
 * @see ProduitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProduitResourceIntTest {

    private static final String DEFAULT_NOM_PRODUIT = "AAAAA";
    private static final String UPDATED_NOM_PRODUIT = "BBBBB";
    private static final String DEFAULT_DESCRIPTION_PRODUIT = "AAAAA";
    private static final String UPDATED_DESCRIPTION_PRODUIT = "BBBBB";

    @Inject
    private ProduitRepository produitRepository;

    @Inject
    private ProduitService produitService;

    @Inject
    private ProduitSearchRepository produitSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProduitMockMvc;

    private Produit produit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProduitResource produitResource = new ProduitResource();
        ReflectionTestUtils.setField(produitResource, "produitService", produitService);
        this.restProduitMockMvc = MockMvcBuilders.standaloneSetup(produitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        produitSearchRepository.deleteAll();
        produit = new Produit();
        produit.setNomProduit(DEFAULT_NOM_PRODUIT);
        produit.setDescriptionProduit(DEFAULT_DESCRIPTION_PRODUIT);
    }

    @Test
    @Transactional
    public void createProduit() throws Exception {
        int databaseSizeBeforeCreate = produitRepository.findAll().size();

        // Create the Produit

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isCreated());

        // Validate the Produit in the database
        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeCreate + 1);
        Produit testProduit = produits.get(produits.size() - 1);
        assertThat(testProduit.getNomProduit()).isEqualTo(DEFAULT_NOM_PRODUIT);
        assertThat(testProduit.getDescriptionProduit()).isEqualTo(DEFAULT_DESCRIPTION_PRODUIT);

        // Validate the Produit in ElasticSearch
        Produit produitEs = produitSearchRepository.findOne(testProduit.getId());
        assertThat(produitEs).isEqualToComparingFieldByField(testProduit);
    }

    @Test
    @Transactional
    public void checkNomProduitIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setNomProduit(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produit)))
                .andExpect(status().isBadRequest());

        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProduits() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produits
        restProduitMockMvc.perform(get("/api/produits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomProduit").value(hasItem(DEFAULT_NOM_PRODUIT.toString())))
                .andExpect(jsonPath("$.[*].descriptionProduit").value(hasItem(DEFAULT_DESCRIPTION_PRODUIT.toString())));
    }

    @Test
    @Transactional
    public void getProduit() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get the produit
        restProduitMockMvc.perform(get("/api/produits/{id}", produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(produit.getId().intValue()))
            .andExpect(jsonPath("$.nomProduit").value(DEFAULT_NOM_PRODUIT.toString()))
            .andExpect(jsonPath("$.descriptionProduit").value(DEFAULT_DESCRIPTION_PRODUIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProduit() throws Exception {
        // Get the produit
        restProduitMockMvc.perform(get("/api/produits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        int databaseSizeBeforeUpdate = produitRepository.findAll().size();

        // Update the produit
        Produit updatedProduit = new Produit();
        updatedProduit.setId(produit.getId());
        updatedProduit.setNomProduit(UPDATED_NOM_PRODUIT);
        updatedProduit.setDescriptionProduit(UPDATED_DESCRIPTION_PRODUIT);

        restProduitMockMvc.perform(put("/api/produits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProduit)))
                .andExpect(status().isOk());

        // Validate the Produit in the database
        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeUpdate);
        Produit testProduit = produits.get(produits.size() - 1);
        assertThat(testProduit.getNomProduit()).isEqualTo(UPDATED_NOM_PRODUIT);
        assertThat(testProduit.getDescriptionProduit()).isEqualTo(UPDATED_DESCRIPTION_PRODUIT);

        // Validate the Produit in ElasticSearch
        Produit produitEs = produitSearchRepository.findOne(testProduit.getId());
        assertThat(produitEs).isEqualToComparingFieldByField(testProduit);
    }

    @Test
    @Transactional
    public void deleteProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        int databaseSizeBeforeDelete = produitRepository.findAll().size();

        // Get the produit
        restProduitMockMvc.perform(delete("/api/produits/{id}", produit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean produitExistsInEs = produitSearchRepository.exists(produit.getId());
        assertThat(produitExistsInEs).isFalse();

        // Validate the database is empty
        List<Produit> produits = produitRepository.findAll();
        assertThat(produits).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        // Search the produit
        restProduitMockMvc.perform(get("/api/_search/produits?query=id:" + produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomProduit").value(hasItem(DEFAULT_NOM_PRODUIT.toString())))
            .andExpect(jsonPath("$.[*].descriptionProduit").value(hasItem(DEFAULT_DESCRIPTION_PRODUIT.toString())));
    }
}
