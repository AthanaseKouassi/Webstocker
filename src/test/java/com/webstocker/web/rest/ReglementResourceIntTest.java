package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Reglement;
import com.webstocker.repository.ReglementRepository;
import com.webstocker.service.ReglementService;
import com.webstocker.repository.search.ReglementSearchRepository;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ReglementResource REST controller.
 *
 * @see ReglementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class ReglementResourceIntTest {


    private static final LocalDate DEFAULT_DATE_REGLEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REGLEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_MONTANT_REGLEMENT = 1L;
    private static final Long UPDATED_MONTANT_REGLEMENT = new Long(2);

    @Inject
    private ReglementRepository reglementRepository;

    @Inject
    private ReglementService reglementService;

    @Inject
    private ReglementSearchRepository reglementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReglementMockMvc;

    private Reglement reglement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReglementResource reglementResource = new ReglementResource();
        ReflectionTestUtils.setField(reglementResource, "reglementService", reglementService);
        this.restReglementMockMvc = MockMvcBuilders.standaloneSetup(reglementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        reglementSearchRepository.deleteAll();
        reglement = new Reglement();
        reglement.setDateReglement(DEFAULT_DATE_REGLEMENT);
        reglement.setMontantReglement(DEFAULT_MONTANT_REGLEMENT);
    }

    @Test
    @Transactional
    public void createReglement() throws Exception {
        int databaseSizeBeforeCreate = reglementRepository.findAll().size();

        // Create the Reglement

        restReglementMockMvc.perform(post("/api/reglements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reglement)))
                .andExpect(status().isCreated());

        // Validate the Reglement in the database
        List<Reglement> reglements = reglementRepository.findAll();
        assertThat(reglements).hasSize(databaseSizeBeforeCreate + 1);
        Reglement testReglement = reglements.get(reglements.size() - 1);
        assertThat(testReglement.getDateReglement()).isEqualTo(DEFAULT_DATE_REGLEMENT);
        assertThat(testReglement.getMontantReglement()).isEqualTo(DEFAULT_MONTANT_REGLEMENT);

        // Validate the Reglement in ElasticSearch
        Reglement reglementEs = reglementSearchRepository.findOne(testReglement.getId());
        assertThat(reglementEs).isEqualToComparingFieldByField(testReglement);
    }

    @Test
    @Transactional
    public void checkDateReglementIsRequired() throws Exception {
        int databaseSizeBeforeTest = reglementRepository.findAll().size();
        // set the field null
        reglement.setDateReglement(null);

        // Create the Reglement, which fails.

        restReglementMockMvc.perform(post("/api/reglements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reglement)))
                .andExpect(status().isBadRequest());

        List<Reglement> reglements = reglementRepository.findAll();
        assertThat(reglements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantReglementIsRequired() throws Exception {
        int databaseSizeBeforeTest = reglementRepository.findAll().size();
        // set the field null
        reglement.setMontantReglement(null);

        // Create the Reglement, which fails.

        restReglementMockMvc.perform(post("/api/reglements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reglement)))
                .andExpect(status().isBadRequest());

        List<Reglement> reglements = reglementRepository.findAll();
        assertThat(reglements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReglements() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        // Get all the reglements
        restReglementMockMvc.perform(get("/api/reglements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reglement.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateReglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
                .andExpect(jsonPath("$.[*].montantReglement").value(hasItem(DEFAULT_MONTANT_REGLEMENT)));
    }

    @Test
    @Transactional
    public void getReglement() throws Exception {
        // Initialize the database
        reglementRepository.saveAndFlush(reglement);

        // Get the reglement
        restReglementMockMvc.perform(get("/api/reglements/{id}", reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(reglement.getId().intValue()))
            .andExpect(jsonPath("$.dateReglement").value(DEFAULT_DATE_REGLEMENT.toString()))
            .andExpect(jsonPath("$.montantReglement").value(DEFAULT_MONTANT_REGLEMENT));
    }
 
    @Test
    @Transactional
    public void getNonExistingReglement() throws Exception {
        // Get the reglement
        restReglementMockMvc.perform(get("/api/reglements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReglement() throws Exception {
        // Initialize the database
        reglementService.save(reglement);

        int databaseSizeBeforeUpdate = reglementRepository.findAll().size();

        // Update the reglement
        Reglement updatedReglement = new Reglement();
        updatedReglement.setId(reglement.getId());
        updatedReglement.setDateReglement(UPDATED_DATE_REGLEMENT);
        updatedReglement.setMontantReglement(UPDATED_MONTANT_REGLEMENT);

        restReglementMockMvc.perform(put("/api/reglements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReglement)))
                .andExpect(status().isOk());

        // Validate the Reglement in the database
        List<Reglement> reglements = reglementRepository.findAll();
        assertThat(reglements).hasSize(databaseSizeBeforeUpdate);
        Reglement testReglement = reglements.get(reglements.size() - 1);
        assertThat(testReglement.getDateReglement()).isEqualTo(UPDATED_DATE_REGLEMENT);
        assertThat(testReglement.getMontantReglement()).isEqualTo(UPDATED_MONTANT_REGLEMENT);

        // Validate the Reglement in ElasticSearch
        Reglement reglementEs = reglementSearchRepository.findOne(testReglement.getId());
        assertThat(reglementEs).isEqualToComparingFieldByField(testReglement);
    }

    @Test
    @Transactional
    public void deleteReglement() throws Exception {
        // Initialize the database
        reglementService.save(reglement);

        int databaseSizeBeforeDelete = reglementRepository.findAll().size();

        // Get the reglement
        restReglementMockMvc.perform(delete("/api/reglements/{id}", reglement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean reglementExistsInEs = reglementSearchRepository.exists(reglement.getId());
        assertThat(reglementExistsInEs).isFalse();

        // Validate the database is empty
        List<Reglement> reglements = reglementRepository.findAll();
        assertThat(reglements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReglement() throws Exception {
        // Initialize the database
        reglementService.save(reglement);

        // Search the reglement
        restReglementMockMvc.perform(get("/api/_search/reglements?query=id:" + reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reglement.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateReglement").value(hasItem(DEFAULT_DATE_REGLEMENT.toString())))
            .andExpect(jsonPath("$.[*].montantReglement").value(hasItem(DEFAULT_MONTANT_REGLEMENT)));
    }
}
