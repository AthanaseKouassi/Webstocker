package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.repository.search.BonDeSortieSearchRepository;
import com.webstocker.web.rest.errors.ExceptionTranslator;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.enumeration.TypeVente;
import com.webstocker.domain.enumeration.StatusTransfert;
import javax.persistence.EntityManager;
/**
 * Test class for the BonDeSortieResource REST controller.
 *
 * @see BonDeSortieResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class BonDeSortieResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAA";
    private static final String UPDATED_NUMERO = "BBBBB";

    private static final LocalDate DEFAULT_DAATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DAATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final TypeSortie DEFAULT_TYPE_SORTIE = TypeSortie.TRANSFERT;
    private static final TypeSortie UPDATED_TYPE_SORTIE = TypeSortie.VENTE;

    private static final TypeVente DEFAULT_TYPE_VENTE = TypeVente.CASH;
    private static final TypeVente UPDATED_TYPE_VENTE = TypeVente.CREDIT;

    private static final Boolean DEFAULT_PRINT_STATUS = false;
    private static final Boolean UPDATED_PRINT_STATUS = true;

    private static final String DEFAULT_NUMERO_FACTURE_NORMALISE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_FACTURE_NORMALISE = "BBBBBBBBBB";

    private static final StatusTransfert DEFAULT_STATUS_TRANFERT = StatusTransfert.RECU;
    private static final StatusTransfert UPDATED_STATUS_TRANFERT = StatusTransfert.ENCOURS;

    
    @Inject
    private BonDeSortieRepository bonDeSortieRepository;

    @Inject
    private BonDeSortieService bonDeSortieService;

    @Inject
    private BonDeSortieSearchRepository bonDeSortieSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private ExceptionTranslator exceptionTranslator;

    @Inject
    private EntityManager em;

    private MockMvc restBonDeSortieMockMvc;

    private BonDeSortie bonDeSortie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BonDeSortieResource bonDeSortieResource = new BonDeSortieResource();
        ReflectionTestUtils.setField(bonDeSortieResource, "bonDeSortieService", bonDeSortieService);
        this.restBonDeSortieMockMvc = MockMvcBuilders.standaloneSetup(bonDeSortieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bonDeSortieSearchRepository.deleteAll();
        bonDeSortie = new BonDeSortie();
        bonDeSortie.setNumero(DEFAULT_NUMERO);
        bonDeSortie.setDaateCreation(DEFAULT_DAATE_CREATION);
        bonDeSortie.setTypeSortie(DEFAULT_TYPE_SORTIE);
        bonDeSortie.setTypeVente(DEFAULT_TYPE_VENTE);
        bonDeSortie.setPrintStatus(DEFAULT_PRINT_STATUS);
        bonDeSortie.setNumeroFactureNormalise(DEFAULT_NUMERO_FACTURE_NORMALISE);
        bonDeSortie.setStatusTranfert(DEFAULT_STATUS_TRANFERT);
    }

    @Test
    @Transactional
    public void createBonDeSortie() throws Exception {
        int databaseSizeBeforeCreate = bonDeSortieRepository.findAll().size();

        // Create the BonDeSortie

        restBonDeSortieMockMvc.perform(post("/api/bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonDeSortie)))
            .andExpect(status().isCreated());

        // Validate the BonDeSortie in the database
        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeCreate + 1);
        BonDeSortie testBonDeSortie = bonDeSortieList.get(bonDeSortieList.size() - 1);
        assertThat(testBonDeSortie.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testBonDeSortie.getDaateCreation()).isEqualTo(DEFAULT_DAATE_CREATION);
        assertThat(testBonDeSortie.getTypeSortie()).isEqualTo(DEFAULT_TYPE_SORTIE);
        assertThat(testBonDeSortie.getTypeVente()).isEqualTo(DEFAULT_TYPE_VENTE);
        assertThat(testBonDeSortie.isPrintStatus()).isEqualTo(DEFAULT_PRINT_STATUS);
        assertThat(testBonDeSortie.getNumeroFactureNormalise()).isEqualTo(DEFAULT_NUMERO_FACTURE_NORMALISE);
        assertThat(testBonDeSortie.getStatusTranfert()).isEqualTo(DEFAULT_STATUS_TRANFERT);

        // Validate the BonDeSortie in Elasticsearch
        BonDeSortie bonDeSortieEs = bonDeSortieSearchRepository.findOne(testBonDeSortie.getId());
        assertThat(bonDeSortieEs).isEqualToComparingFieldByField(testBonDeSortie);
    }

    @Test
    @Transactional
    public void createBonDeSortieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bonDeSortieRepository.findAll().size();

        // Create the BonDeSortie with an existing ID
        BonDeSortie existingBonDeSortie = new BonDeSortie();
        existingBonDeSortie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBonDeSortieMockMvc.perform(post("/api/bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBonDeSortie)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonDeSortieRepository.findAll().size();
        // set the field null
        bonDeSortie.setNumero(null);

        // Create the BonDeSortie, which fails.

        restBonDeSortieMockMvc.perform(post("/api/bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonDeSortie)))
            .andExpect(status().isBadRequest());

        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDaateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonDeSortieRepository.findAll().size();
        // set the field null
        bonDeSortie.setDaateCreation(null);

        // Create the BonDeSortie, which fails.

        restBonDeSortieMockMvc.perform(post("/api/bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonDeSortie)))
            .andExpect(status().isBadRequest());

        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeSortieIsRequired() throws Exception {
        int databaseSizeBeforeTest = bonDeSortieRepository.findAll().size();
        // set the field null
        bonDeSortie.setTypeSortie(null);

        // Create the BonDeSortie, which fails.

        restBonDeSortieMockMvc.perform(post("/api/bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonDeSortie)))
            .andExpect(status().isBadRequest());

        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBonDeSorties() throws Exception {
        // Initialize the database
        bonDeSortieRepository.saveAndFlush(bonDeSortie);

        // Get all the bonDeSortieList
        restBonDeSortieMockMvc.perform(get("/api/bon-de-sorties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonDeSortie.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].daateCreation").value(hasItem(DEFAULT_DAATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].typeSortie").value(hasItem(DEFAULT_TYPE_SORTIE.toString())))
            .andExpect(jsonPath("$.[*].typeVente").value(hasItem(DEFAULT_TYPE_VENTE.toString())))
            .andExpect(jsonPath("$.[*].printStatus").value(hasItem(DEFAULT_PRINT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].numeroFactureNormalise").value(hasItem(DEFAULT_NUMERO_FACTURE_NORMALISE.toString())))
            .andExpect(jsonPath("$.[*].statusTranfert").value(hasItem(DEFAULT_STATUS_TRANFERT.toString())));
    }

    @Test
    @Transactional
    public void getBonDeSortie() throws Exception {
        // Initialize the database
        bonDeSortieRepository.saveAndFlush(bonDeSortie);

        // Get the bonDeSortie
        restBonDeSortieMockMvc.perform(get("/api/bon-de-sorties/{id}", bonDeSortie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bonDeSortie.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.daateCreation").value(DEFAULT_DAATE_CREATION.toString()))
            .andExpect(jsonPath("$.typeSortie").value(DEFAULT_TYPE_SORTIE.toString()))
            .andExpect(jsonPath("$.typeVente").value(DEFAULT_TYPE_VENTE.toString()))
            .andExpect(jsonPath("$.printStatus").value(DEFAULT_PRINT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.numeroFactureNormalise").value(DEFAULT_NUMERO_FACTURE_NORMALISE.toString()))
            .andExpect(jsonPath("$.statusTranfert").value(DEFAULT_STATUS_TRANFERT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBonDeSortie() throws Exception {
        // Get the bonDeSortie
        restBonDeSortieMockMvc.perform(get("/api/bon-de-sorties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBonDeSortie() throws Exception {
        // Initialize the database
        bonDeSortieService.save(bonDeSortie);

        int databaseSizeBeforeUpdate = bonDeSortieRepository.findAll().size();

        // Update the bonDeSortie
        BonDeSortie updatedBonDeSortie = bonDeSortieRepository.findOne(bonDeSortie.getId());
        updatedBonDeSortie.setNumero(UPDATED_NUMERO);
        updatedBonDeSortie.setDaateCreation(UPDATED_DAATE_CREATION);
        updatedBonDeSortie.setTypeSortie(UPDATED_TYPE_SORTIE);
        updatedBonDeSortie.setTypeVente(UPDATED_TYPE_VENTE);
        updatedBonDeSortie.setPrintStatus(UPDATED_PRINT_STATUS);
        updatedBonDeSortie.setNumeroFactureNormalise(UPDATED_NUMERO_FACTURE_NORMALISE);
        updatedBonDeSortie.setStatusTranfert(UPDATED_STATUS_TRANFERT);

        restBonDeSortieMockMvc.perform(put("/api/bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBonDeSortie)))
            .andExpect(status().isOk());

        // Validate the BonDeSortie in the database
        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeUpdate);
        BonDeSortie testBonDeSortie = bonDeSortieList.get(bonDeSortieList.size() - 1);
        assertThat(testBonDeSortie.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testBonDeSortie.getDaateCreation()).isEqualTo(UPDATED_DAATE_CREATION);
        assertThat(testBonDeSortie.getTypeSortie()).isEqualTo(UPDATED_TYPE_SORTIE);
        assertThat(testBonDeSortie.getTypeVente()).isEqualTo(UPDATED_TYPE_VENTE);
        assertThat(testBonDeSortie.isPrintStatus()).isEqualTo(UPDATED_PRINT_STATUS);
        assertThat(testBonDeSortie.getNumeroFactureNormalise()).isEqualTo(UPDATED_NUMERO_FACTURE_NORMALISE);
        assertThat(testBonDeSortie.getStatusTranfert()).isEqualTo(UPDATED_STATUS_TRANFERT);

        // Validate the BonDeSortie in Elasticsearch
        BonDeSortie bonDeSortieEs = bonDeSortieSearchRepository.findOne(testBonDeSortie.getId());
        assertThat(bonDeSortieEs).isEqualToComparingFieldByField(testBonDeSortie);
    }

    @Test
    @Transactional
    public void updateNonExistingBonDeSortie() throws Exception {
        int databaseSizeBeforeUpdate = bonDeSortieRepository.findAll().size();

        // Create the BonDeSortie

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBonDeSortieMockMvc.perform(put("/api/bon-de-sorties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bonDeSortie)))
            .andExpect(status().isCreated());

        // Validate the BonDeSortie in the database
        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBonDeSortie() throws Exception {
        // Initialize the database
        bonDeSortieService.save(bonDeSortie);

        int databaseSizeBeforeDelete = bonDeSortieRepository.findAll().size();

        // Get the bonDeSortie
        restBonDeSortieMockMvc.perform(delete("/api/bon-de-sorties/{id}", bonDeSortie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bonDeSortieExistsInEs = bonDeSortieSearchRepository.exists(bonDeSortie.getId());
        assertThat(bonDeSortieExistsInEs).isFalse();

        // Validate the database is empty
        List<BonDeSortie> bonDeSortieList = bonDeSortieRepository.findAll();
        assertThat(bonDeSortieList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBonDeSortie() throws Exception {
        // Initialize the database
        bonDeSortieService.save(bonDeSortie);

        // Search the bonDeSortie
        restBonDeSortieMockMvc.perform(get("/api/_search/bon-de-sorties?query=id:" + bonDeSortie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bonDeSortie.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].daateCreation").value(hasItem(DEFAULT_DAATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].typeSortie").value(hasItem(DEFAULT_TYPE_SORTIE.toString())))
            .andExpect(jsonPath("$.[*].typeVente").value(hasItem(DEFAULT_TYPE_VENTE.toString())))
            .andExpect(jsonPath("$.[*].printStatus").value(hasItem(DEFAULT_PRINT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].numeroFactureNormalise").value(hasItem(DEFAULT_NUMERO_FACTURE_NORMALISE.toString())))
            .andExpect(jsonPath("$.[*].statusTranfert").value(hasItem(DEFAULT_STATUS_TRANFERT.toString())));
    }
}
