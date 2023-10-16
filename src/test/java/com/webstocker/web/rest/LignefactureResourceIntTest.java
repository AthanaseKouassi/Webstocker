package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Lignefacture;
import com.webstocker.repository.LignefactureRepository;
import com.webstocker.service.LignefactureService;
import com.webstocker.repository.search.LignefactureSearchRepository;

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
 * Test class for the LignefactureResource REST controller.
 *
 * @see LignefactureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class LignefactureResourceIntTest {


    private static final Integer DEFAULT_QUANTITE_FACTURE = 1;
    private static final Integer UPDATED_QUANTITE_FACTURE = 2;

    @Inject
    private LignefactureRepository lignefactureRepository;

    @Inject
    private LignefactureService lignefactureService;

    @Inject
    private LignefactureSearchRepository lignefactureSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLignefactureMockMvc;

    private Lignefacture lignefacture;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LignefactureResource lignefactureResource = new LignefactureResource();
        ReflectionTestUtils.setField(lignefactureResource, "lignefactureService", lignefactureService);
        this.restLignefactureMockMvc = MockMvcBuilders.standaloneSetup(lignefactureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lignefactureSearchRepository.deleteAll();
        lignefacture = new Lignefacture();
        lignefacture.setQuantiteFacture(DEFAULT_QUANTITE_FACTURE);
    }

    @Test
    @Transactional
    public void createLignefacture() throws Exception {
        int databaseSizeBeforeCreate = lignefactureRepository.findAll().size();

        // Create the Lignefacture

        restLignefactureMockMvc.perform(post("/api/lignefactures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lignefacture)))
                .andExpect(status().isCreated());

        // Validate the Lignefacture in the database
        List<Lignefacture> lignefactures = lignefactureRepository.findAll();
        assertThat(lignefactures).hasSize(databaseSizeBeforeCreate + 1);
        Lignefacture testLignefacture = lignefactures.get(lignefactures.size() - 1);
        assertThat(testLignefacture.getQuantiteFacture()).isEqualTo(DEFAULT_QUANTITE_FACTURE);

        // Validate the Lignefacture in ElasticSearch
        Lignefacture lignefactureEs = lignefactureSearchRepository.findOne(testLignefacture.getId());
        assertThat(lignefactureEs).isEqualToComparingFieldByField(testLignefacture);
    }

    @Test
    @Transactional
    public void checkQuantiteFactureIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignefactureRepository.findAll().size();
        // set the field null
        lignefacture.setQuantiteFacture(null);

        // Create the Lignefacture, which fails.

        restLignefactureMockMvc.perform(post("/api/lignefactures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lignefacture)))
                .andExpect(status().isBadRequest());

        List<Lignefacture> lignefactures = lignefactureRepository.findAll();
        assertThat(lignefactures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLignefactures() throws Exception {
        // Initialize the database
        lignefactureRepository.saveAndFlush(lignefacture);

        // Get all the lignefactures
        restLignefactureMockMvc.perform(get("/api/lignefactures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lignefacture.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantiteFacture").value(hasItem(DEFAULT_QUANTITE_FACTURE)));
    }

    @Test
    @Transactional
    public void getLignefacture() throws Exception {
        // Initialize the database
        lignefactureRepository.saveAndFlush(lignefacture);

        // Get the lignefacture
        restLignefactureMockMvc.perform(get("/api/lignefactures/{id}", lignefacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lignefacture.getId().intValue()))
            .andExpect(jsonPath("$.quantiteFacture").value(DEFAULT_QUANTITE_FACTURE));
    }

    @Test
    @Transactional
    public void getNonExistingLignefacture() throws Exception {
        // Get the lignefacture
        restLignefactureMockMvc.perform(get("/api/lignefactures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLignefacture() throws Exception {
        // Initialize the database
        lignefactureService.save(lignefacture);

        int databaseSizeBeforeUpdate = lignefactureRepository.findAll().size();

        // Update the lignefacture
        Lignefacture updatedLignefacture = new Lignefacture();
        updatedLignefacture.setId(lignefacture.getId());
        updatedLignefacture.setQuantiteFacture(UPDATED_QUANTITE_FACTURE);

        restLignefactureMockMvc.perform(put("/api/lignefactures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLignefacture)))
                .andExpect(status().isOk());

        // Validate the Lignefacture in the database
        List<Lignefacture> lignefactures = lignefactureRepository.findAll();
        assertThat(lignefactures).hasSize(databaseSizeBeforeUpdate);
        Lignefacture testLignefacture = lignefactures.get(lignefactures.size() - 1);
        assertThat(testLignefacture.getQuantiteFacture()).isEqualTo(UPDATED_QUANTITE_FACTURE);

        // Validate the Lignefacture in ElasticSearch
        Lignefacture lignefactureEs = lignefactureSearchRepository.findOne(testLignefacture.getId());
        assertThat(lignefactureEs).isEqualToComparingFieldByField(testLignefacture);
    }

    @Test
    @Transactional
    public void deleteLignefacture() throws Exception {
        // Initialize the database
        lignefactureService.save(lignefacture);

        int databaseSizeBeforeDelete = lignefactureRepository.findAll().size();

        // Get the lignefacture
        restLignefactureMockMvc.perform(delete("/api/lignefactures/{id}", lignefacture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lignefactureExistsInEs = lignefactureSearchRepository.exists(lignefacture.getId());
        assertThat(lignefactureExistsInEs).isFalse();

        // Validate the database is empty
        List<Lignefacture> lignefactures = lignefactureRepository.findAll();
        assertThat(lignefactures).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLignefacture() throws Exception {
        // Initialize the database
        lignefactureService.save(lignefacture);

        // Search the lignefacture
        restLignefactureMockMvc.perform(get("/api/_search/lignefactures?query=id:" + lignefacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignefacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantiteFacture").value(hasItem(DEFAULT_QUANTITE_FACTURE)));
    }
}
