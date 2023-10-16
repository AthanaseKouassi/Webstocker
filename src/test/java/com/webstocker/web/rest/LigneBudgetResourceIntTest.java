package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.LigneBudget;
import com.webstocker.repository.LigneBudgetRepository;
import com.webstocker.service.LigneBudgetService;
import com.webstocker.repository.search.LigneBudgetSearchRepository;

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
 * Test class for the LigneBudgetResource REST controller.
 *
 * @see LigneBudgetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class LigneBudgetResourceIntTest {

    private static final String DEFAULT_LIBELLE_LIGNE_BUDGET = "AAAAA";
    private static final String UPDATED_LIBELLE_LIGNE_BUDGET = "BBBBB";

    private static final Long DEFAULT_MONTANT_LIGNE_BUDGET = 1L;
    private static final Long UPDATED_MONTANT_LIGNE_BUDGET = 2L;

    @Inject
    private LigneBudgetRepository ligneBudgetRepository;

    @Inject
    private LigneBudgetService ligneBudgetService;

    @Inject
    private LigneBudgetSearchRepository ligneBudgetSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLigneBudgetMockMvc;

    private LigneBudget ligneBudget;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LigneBudgetResource ligneBudgetResource = new LigneBudgetResource();
        ReflectionTestUtils.setField(ligneBudgetResource, "ligneBudgetService", ligneBudgetService);
        this.restLigneBudgetMockMvc = MockMvcBuilders.standaloneSetup(ligneBudgetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ligneBudgetSearchRepository.deleteAll();
        ligneBudget = new LigneBudget();
        ligneBudget.setLibelleLigneBudget(DEFAULT_LIBELLE_LIGNE_BUDGET);
        ligneBudget.setMontantLigneBudget(DEFAULT_MONTANT_LIGNE_BUDGET);
    }

    @Test
    @Transactional
    public void createLigneBudget() throws Exception {
        int databaseSizeBeforeCreate = ligneBudgetRepository.findAll().size();

        // Create the LigneBudget

        restLigneBudgetMockMvc.perform(post("/api/ligne-budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ligneBudget)))
                .andExpect(status().isCreated());

        // Validate the LigneBudget in the database
        List<LigneBudget> ligneBudgets = ligneBudgetRepository.findAll();
        assertThat(ligneBudgets).hasSize(databaseSizeBeforeCreate + 1);
        LigneBudget testLigneBudget = ligneBudgets.get(ligneBudgets.size() - 1);
        assertThat(testLigneBudget.getLibelleLigneBudget()).isEqualTo(DEFAULT_LIBELLE_LIGNE_BUDGET);
        assertThat(testLigneBudget.getMontantLigneBudget()).isEqualTo(DEFAULT_MONTANT_LIGNE_BUDGET);

        // Validate the LigneBudget in ElasticSearch
        LigneBudget ligneBudgetEs = ligneBudgetSearchRepository.findOne(testLigneBudget.getId());
        assertThat(ligneBudgetEs).isEqualToComparingFieldByField(testLigneBudget);
    }

    @Test
    @Transactional
    public void checkLibelleLigneBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneBudgetRepository.findAll().size();
        // set the field null
        ligneBudget.setLibelleLigneBudget(null);

        // Create the LigneBudget, which fails.

        restLigneBudgetMockMvc.perform(post("/api/ligne-budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ligneBudget)))
                .andExpect(status().isBadRequest());

        List<LigneBudget> ligneBudgets = ligneBudgetRepository.findAll();
        assertThat(ligneBudgets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantLigneBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = ligneBudgetRepository.findAll().size();
        // set the field null
        ligneBudget.setMontantLigneBudget(null);

        // Create the LigneBudget, which fails.

        restLigneBudgetMockMvc.perform(post("/api/ligne-budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ligneBudget)))
                .andExpect(status().isBadRequest());

        List<LigneBudget> ligneBudgets = ligneBudgetRepository.findAll();
        assertThat(ligneBudgets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLigneBudgets() throws Exception {
        // Initialize the database
        ligneBudgetRepository.saveAndFlush(ligneBudget);

        // Get all the ligneBudgets
        restLigneBudgetMockMvc.perform(get("/api/ligne-budgets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ligneBudget.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelleLigneBudget").value(hasItem(DEFAULT_LIBELLE_LIGNE_BUDGET.toString())))
                .andExpect(jsonPath("$.[*].montantLigneBudget").value(hasItem(DEFAULT_MONTANT_LIGNE_BUDGET.intValue())));
    }

    @Test
    @Transactional
    public void getLigneBudget() throws Exception {
        // Initialize the database
        ligneBudgetRepository.saveAndFlush(ligneBudget);

        // Get the ligneBudget
        restLigneBudgetMockMvc.perform(get("/api/ligne-budgets/{id}", ligneBudget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ligneBudget.getId().intValue()))
            .andExpect(jsonPath("$.libelleLigneBudget").value(DEFAULT_LIBELLE_LIGNE_BUDGET.toString()))
            .andExpect(jsonPath("$.montantLigneBudget").value(DEFAULT_MONTANT_LIGNE_BUDGET.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLigneBudget() throws Exception {
        // Get the ligneBudget
        restLigneBudgetMockMvc.perform(get("/api/ligne-budgets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneBudget() throws Exception {
        // Initialize the database
        ligneBudgetService.save(ligneBudget);

        int databaseSizeBeforeUpdate = ligneBudgetRepository.findAll().size();

        // Update the ligneBudget
        LigneBudget updatedLigneBudget = new LigneBudget();
        updatedLigneBudget.setId(ligneBudget.getId());
        updatedLigneBudget.setLibelleLigneBudget(UPDATED_LIBELLE_LIGNE_BUDGET);
        updatedLigneBudget.setMontantLigneBudget(UPDATED_MONTANT_LIGNE_BUDGET);

        restLigneBudgetMockMvc.perform(put("/api/ligne-budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLigneBudget)))
                .andExpect(status().isOk());

        // Validate the LigneBudget in the database
        List<LigneBudget> ligneBudgets = ligneBudgetRepository.findAll();
        assertThat(ligneBudgets).hasSize(databaseSizeBeforeUpdate);
        LigneBudget testLigneBudget = ligneBudgets.get(ligneBudgets.size() - 1);
        assertThat(testLigneBudget.getLibelleLigneBudget()).isEqualTo(UPDATED_LIBELLE_LIGNE_BUDGET);
        assertThat(testLigneBudget.getMontantLigneBudget()).isEqualTo(UPDATED_MONTANT_LIGNE_BUDGET);

        // Validate the LigneBudget in ElasticSearch
        LigneBudget ligneBudgetEs = ligneBudgetSearchRepository.findOne(testLigneBudget.getId());
        assertThat(ligneBudgetEs).isEqualToComparingFieldByField(testLigneBudget);
    }

    @Test
    @Transactional
    public void deleteLigneBudget() throws Exception {
        // Initialize the database
        ligneBudgetService.save(ligneBudget);

        int databaseSizeBeforeDelete = ligneBudgetRepository.findAll().size();

        // Get the ligneBudget
        restLigneBudgetMockMvc.perform(delete("/api/ligne-budgets/{id}", ligneBudget.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ligneBudgetExistsInEs = ligneBudgetSearchRepository.exists(ligneBudget.getId());
        assertThat(ligneBudgetExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneBudget> ligneBudgets = ligneBudgetRepository.findAll();
        assertThat(ligneBudgets).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneBudget() throws Exception {
        // Initialize the database
        ligneBudgetService.save(ligneBudget);

        // Search the ligneBudget
        restLigneBudgetMockMvc.perform(get("/api/_search/ligne-budgets?query=id:" + ligneBudget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneBudget.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleLigneBudget").value(hasItem(DEFAULT_LIBELLE_LIGNE_BUDGET.toString())))
            .andExpect(jsonPath("$.[*].montantLigneBudget").value(hasItem(DEFAULT_MONTANT_LIGNE_BUDGET.intValue())));
    }
}
