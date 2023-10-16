package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Cellule;
import com.webstocker.repository.CelluleRepository;
import com.webstocker.service.CelluleService;
import com.webstocker.repository.search.CelluleSearchRepository;

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
 * Test class for the CelluleResource REST controller.
 *
 * @see CelluleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class CelluleResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";

    @Inject
    private CelluleRepository celluleRepository;

    @Inject
    private CelluleService celluleService;

    @Inject
    private CelluleSearchRepository celluleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCelluleMockMvc;

    private Cellule cellule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CelluleResource celluleResource = new CelluleResource();
        ReflectionTestUtils.setField(celluleResource, "celluleService", celluleService);
        this.restCelluleMockMvc = MockMvcBuilders.standaloneSetup(celluleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        celluleSearchRepository.deleteAll();
        cellule = new Cellule();
        cellule.setNom(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createCellule() throws Exception {
        int databaseSizeBeforeCreate = celluleRepository.findAll().size();

        // Create the Cellule

        restCelluleMockMvc.perform(post("/api/cellules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cellule)))
                .andExpect(status().isCreated());

        // Validate the Cellule in the database
        List<Cellule> cellules = celluleRepository.findAll();
        assertThat(cellules).hasSize(databaseSizeBeforeCreate + 1);
        Cellule testCellule = cellules.get(cellules.size() - 1);
        assertThat(testCellule.getNom()).isEqualTo(DEFAULT_NOM);

        // Validate the Cellule in ElasticSearch
        Cellule celluleEs = celluleSearchRepository.findOne(testCellule.getId());
        assertThat(celluleEs).isEqualToComparingFieldByField(testCellule);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = celluleRepository.findAll().size();
        // set the field null
        cellule.setNom(null);

        // Create the Cellule, which fails.

        restCelluleMockMvc.perform(post("/api/cellules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cellule)))
                .andExpect(status().isBadRequest());

        List<Cellule> cellules = celluleRepository.findAll();
        assertThat(cellules).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCellules() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get all the cellules
        restCelluleMockMvc.perform(get("/api/cellules?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cellule.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }

    @Test
    @Transactional
    public void getCellule() throws Exception {
        // Initialize the database
        celluleRepository.saveAndFlush(cellule);

        // Get the cellule
        restCelluleMockMvc.perform(get("/api/cellules/{id}", cellule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cellule.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCellule() throws Exception {
        // Get the cellule
        restCelluleMockMvc.perform(get("/api/cellules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCellule() throws Exception {
        // Initialize the database
        celluleService.save(cellule);

        int databaseSizeBeforeUpdate = celluleRepository.findAll().size();

        // Update the cellule
        Cellule updatedCellule = new Cellule();
        updatedCellule.setId(cellule.getId());
        updatedCellule.setNom(UPDATED_NOM);

        restCelluleMockMvc.perform(put("/api/cellules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCellule)))
                .andExpect(status().isOk());

        // Validate the Cellule in the database
        List<Cellule> cellules = celluleRepository.findAll();
        assertThat(cellules).hasSize(databaseSizeBeforeUpdate);
        Cellule testCellule = cellules.get(cellules.size() - 1);
        assertThat(testCellule.getNom()).isEqualTo(UPDATED_NOM);

        // Validate the Cellule in ElasticSearch
        Cellule celluleEs = celluleSearchRepository.findOne(testCellule.getId());
        assertThat(celluleEs).isEqualToComparingFieldByField(testCellule);
    }

    @Test
    @Transactional
    public void deleteCellule() throws Exception {
        // Initialize the database
        celluleService.save(cellule);

        int databaseSizeBeforeDelete = celluleRepository.findAll().size();

        // Get the cellule
        restCelluleMockMvc.perform(delete("/api/cellules/{id}", cellule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean celluleExistsInEs = celluleSearchRepository.exists(cellule.getId());
        assertThat(celluleExistsInEs).isFalse();

        // Validate the database is empty
        List<Cellule> cellules = celluleRepository.findAll();
        assertThat(cellules).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCellule() throws Exception {
        // Initialize the database
        celluleService.save(cellule);

        // Search the cellule
        restCelluleMockMvc.perform(get("/api/_search/cellules?query=id:" + cellule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cellule.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())));
    }
}
