package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Magasin;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.service.MagasinService;
import com.webstocker.repository.search.MagasinSearchRepository;

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
 * Test class for the MagasinResource REST controller.
 *
 * @see MagasinResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class MagasinResourceIntTest {

    private static final String DEFAULT_NOM_MAGASIN = "AAAAA";
    private static final String UPDATED_NOM_MAGASIN = "BBBBB";

    @Inject
    private MagasinRepository magasinRepository;

    @Inject
    private MagasinService magasinService;

    @Inject
    private MagasinSearchRepository magasinSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMagasinMockMvc;

    private Magasin magasin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MagasinResource magasinResource = new MagasinResource();
        ReflectionTestUtils.setField(magasinResource, "magasinService", magasinService);
        this.restMagasinMockMvc = MockMvcBuilders.standaloneSetup(magasinResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        magasinSearchRepository.deleteAll();
        magasin = new Magasin();
        magasin.setNomMagasin(DEFAULT_NOM_MAGASIN);
    }

    @Test
    @Transactional
    public void createMagasin() throws Exception {
        int databaseSizeBeforeCreate = magasinRepository.findAll().size();

        // Create the Magasin

        restMagasinMockMvc.perform(post("/api/magasins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(magasin)))
                .andExpect(status().isCreated());

        // Validate the Magasin in the database
        List<Magasin> magasins = magasinRepository.findAll();
        assertThat(magasins).hasSize(databaseSizeBeforeCreate + 1);
        Magasin testMagasin = magasins.get(magasins.size() - 1);
        assertThat(testMagasin.getNomMagasin()).isEqualTo(DEFAULT_NOM_MAGASIN);

        // Validate the Magasin in ElasticSearch
        Magasin magasinEs = magasinSearchRepository.findOne(testMagasin.getId());
        assertThat(magasinEs).isEqualToComparingFieldByField(testMagasin);
    }

    @Test
    @Transactional
    public void checkNomMagasinIsRequired() throws Exception {
        int databaseSizeBeforeTest = magasinRepository.findAll().size();
        // set the field null
        magasin.setNomMagasin(null);

        // Create the Magasin, which fails.

        restMagasinMockMvc.perform(post("/api/magasins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(magasin)))
                .andExpect(status().isBadRequest());

        List<Magasin> magasins = magasinRepository.findAll();
        assertThat(magasins).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMagasins() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        // Get all the magasins
        restMagasinMockMvc.perform(get("/api/magasins?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(magasin.getId().intValue())))
                .andExpect(jsonPath("$.[*].nomMagasin").value(hasItem(DEFAULT_NOM_MAGASIN.toString())));
    }

    @Test
    @Transactional
    public void getMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        // Get the magasin
        restMagasinMockMvc.perform(get("/api/magasins/{id}", magasin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(magasin.getId().intValue()))
            .andExpect(jsonPath("$.nomMagasin").value(DEFAULT_NOM_MAGASIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMagasin() throws Exception {
        // Get the magasin
        restMagasinMockMvc.perform(get("/api/magasins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMagasin() throws Exception {
        // Initialize the database
        magasinService.save(magasin);

        int databaseSizeBeforeUpdate = magasinRepository.findAll().size();

        // Update the magasin
        Magasin updatedMagasin = new Magasin();
        updatedMagasin.setId(magasin.getId());
        updatedMagasin.setNomMagasin(UPDATED_NOM_MAGASIN);

        restMagasinMockMvc.perform(put("/api/magasins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMagasin)))
                .andExpect(status().isOk());

        // Validate the Magasin in the database
        List<Magasin> magasins = magasinRepository.findAll();
        assertThat(magasins).hasSize(databaseSizeBeforeUpdate);
        Magasin testMagasin = magasins.get(magasins.size() - 1);
        assertThat(testMagasin.getNomMagasin()).isEqualTo(UPDATED_NOM_MAGASIN);

        // Validate the Magasin in ElasticSearch
        Magasin magasinEs = magasinSearchRepository.findOne(testMagasin.getId());
        assertThat(magasinEs).isEqualToComparingFieldByField(testMagasin);
    }

    @Test
    @Transactional
    public void deleteMagasin() throws Exception {
        // Initialize the database
        magasinService.save(magasin);

        int databaseSizeBeforeDelete = magasinRepository.findAll().size();

        // Get the magasin
        restMagasinMockMvc.perform(delete("/api/magasins/{id}", magasin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean magasinExistsInEs = magasinSearchRepository.exists(magasin.getId());
        assertThat(magasinExistsInEs).isFalse();

        // Validate the database is empty
        List<Magasin> magasins = magasinRepository.findAll();
        assertThat(magasins).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMagasin() throws Exception {
        // Initialize the database
        magasinService.save(magasin);

        // Search the magasin
        restMagasinMockMvc.perform(get("/api/_search/magasins?query=id:" + magasin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magasin.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomMagasin").value(hasItem(DEFAULT_NOM_MAGASIN.toString())));
    }
}
