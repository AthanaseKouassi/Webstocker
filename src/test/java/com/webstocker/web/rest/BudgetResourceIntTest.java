package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Budget;
import com.webstocker.repository.BudgetRepository;
import com.webstocker.service.BudgetService;
import com.webstocker.repository.search.BudgetSearchRepository;

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
 * Test class for the BudgetResource REST controller.
 *
 * @see BudgetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class BudgetResourceIntTest {


    private static final Long DEFAULT_MONTANT_BUDGET = 1L;
    private static final Long UPDATED_MONTANT_BUDGET = 2L;

    @Inject
    private BudgetRepository budgetRepository;

    @Inject
    private BudgetService budgetService;

    @Inject
    private BudgetSearchRepository budgetSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBudgetMockMvc;

    private Budget budget;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BudgetResource budgetResource = new BudgetResource();
        ReflectionTestUtils.setField(budgetResource, "budgetService", budgetService);
        this.restBudgetMockMvc = MockMvcBuilders.standaloneSetup(budgetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        budgetSearchRepository.deleteAll();
        budget = new Budget();
        budget.setMontantBudget(DEFAULT_MONTANT_BUDGET);
    }

    @Test
    @Transactional
    public void createBudget() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().size();

        // Create the Budget

        restBudgetMockMvc.perform(post("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budget)))
                .andExpect(status().isCreated());

        // Validate the Budget in the database
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeCreate + 1);
        Budget testBudget = budgets.get(budgets.size() - 1);
        assertThat(testBudget.getMontantBudget()).isEqualTo(DEFAULT_MONTANT_BUDGET);

        // Validate the Budget in ElasticSearch
        Budget budgetEs = budgetSearchRepository.findOne(testBudget.getId());
        assertThat(budgetEs).isEqualToComparingFieldByField(testBudget);
    }

    @Test
    @Transactional
    public void getAllBudgets() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get all the budgets
        restBudgetMockMvc.perform(get("/api/budgets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
                .andExpect(jsonPath("$.[*].montantBudget").value(hasItem(DEFAULT_MONTANT_BUDGET.intValue())));
    }

    @Test
    @Transactional
    public void getBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(budget.getId().intValue()))
            .andExpect(jsonPath("$.montantBudget").value(DEFAULT_MONTANT_BUDGET.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBudget() throws Exception {
        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudget() throws Exception {
        // Initialize the database
        budgetService.save(budget);

        int databaseSizeBeforeUpdate = budgetRepository.findAll().size();

        // Update the budget
        Budget updatedBudget = new Budget();
        updatedBudget.setId(budget.getId());
        updatedBudget.setMontantBudget(UPDATED_MONTANT_BUDGET);

        restBudgetMockMvc.perform(put("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBudget)))
                .andExpect(status().isOk());

        // Validate the Budget in the database
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgets.get(budgets.size() - 1);
        assertThat(testBudget.getMontantBudget()).isEqualTo(UPDATED_MONTANT_BUDGET);

        // Validate the Budget in ElasticSearch
        Budget budgetEs = budgetSearchRepository.findOne(testBudget.getId());
        assertThat(budgetEs).isEqualToComparingFieldByField(testBudget);
    }

    @Test
    @Transactional
    public void deleteBudget() throws Exception {
        // Initialize the database
        budgetService.save(budget);

        int databaseSizeBeforeDelete = budgetRepository.findAll().size();

        // Get the budget
        restBudgetMockMvc.perform(delete("/api/budgets/{id}", budget.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean budgetExistsInEs = budgetSearchRepository.exists(budget.getId());
        assertThat(budgetExistsInEs).isFalse();

        // Validate the database is empty
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBudget() throws Exception {
        // Initialize the database
        budgetService.save(budget);

        // Search the budget
        restBudgetMockMvc.perform(get("/api/_search/budgets?query=id:" + budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].montantBudget").value(hasItem(DEFAULT_MONTANT_BUDGET.intValue())));
    }
}
