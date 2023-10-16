package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Lot;
import com.webstocker.repository.LotRepository;
import com.webstocker.service.LotService;
import com.webstocker.repository.search.LotSearchRepository;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LotResource REST controller.
 *
 * @see LotResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class LotResourceIntTest {


    private static final Long DEFAULT_NUMERO_LOT = 1L;
    private static final Long UPDATED_NUMERO_LOT = 2L;

    private static final LocalDate DEFAULT_DATE_FABRICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FABRICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_PEREMPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PEREMPTION = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DESCRIPTION_LOT = "AAAAA";
    private static final String UPDATED_DESCRIPTION_LOT = "BBBBB";

    private static final Long DEFAULT_QUANTITE_LOT = 1L;
    private static final Long UPDATED_QUANTITE_LOT = 2L;

    private static final Long DEFAULT_QUANTITE_CARTON_LOT = 1L;
    private static final Long UPDATED_QUANTITE_CARTON_LOT = 2L;

    private static final Long DEFAULT_QUANTITE_SORTIE = 1L;
    private static final Long UPDATED_QUANTITE_SORTIE = 2L;

    @Inject
    private LotRepository lotRepository;

    @Inject
    private LotService lotService;

    @Inject
    private LotSearchRepository lotSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLotMockMvc;

    private Lot lot;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LotResource lotResource = new LotResource();
        ReflectionTestUtils.setField(lotResource, "lotService", lotService);
        this.restLotMockMvc = MockMvcBuilders.standaloneSetup(lotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lotSearchRepository.deleteAll();
        lot = new Lot();
        lot.setNumeroLot(DEFAULT_NUMERO_LOT);
        lot.setDateFabrication(DEFAULT_DATE_FABRICATION);
        lot.setDatePeremption(DEFAULT_DATE_PEREMPTION);
        lot.setDescriptionLot(DEFAULT_DESCRIPTION_LOT);
        lot.setQuantiteLot(DEFAULT_QUANTITE_LOT);
        lot.setQuantiteCartonLot(DEFAULT_QUANTITE_CARTON_LOT);
        lot.setQuantiteSortie(DEFAULT_QUANTITE_SORTIE);
    }

    @Test
    @Transactional
    public void createLot() throws Exception {
        int databaseSizeBeforeCreate = lotRepository.findAll().size();

        // Create the Lot

        restLotMockMvc.perform(post("/api/lots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lot)))
                .andExpect(status().isCreated());

        // Validate the Lot in the database
        List<Lot> lots = lotRepository.findAll();
        assertThat(lots).hasSize(databaseSizeBeforeCreate + 1);
        Lot testLot = lots.get(lots.size() - 1);
        assertThat(testLot.getNumeroLot()).isEqualTo(DEFAULT_NUMERO_LOT);
        assertThat(testLot.getDateFabrication()).isEqualTo(DEFAULT_DATE_FABRICATION);
        assertThat(testLot.getDatePeremption()).isEqualTo(DEFAULT_DATE_PEREMPTION);
        assertThat(testLot.getDescriptionLot()).isEqualTo(DEFAULT_DESCRIPTION_LOT);
        assertThat(testLot.getQuantiteLot()).isEqualTo(DEFAULT_QUANTITE_LOT);
        assertThat(testLot.getQuantiteCartonLot()).isEqualTo(DEFAULT_QUANTITE_CARTON_LOT);
        assertThat(testLot.getQuantiteSortie()).isEqualTo(DEFAULT_QUANTITE_SORTIE);

        // Validate the Lot in ElasticSearch
        Lot lotEs = lotSearchRepository.findOne(testLot.getId());
        assertThat(lotEs).isEqualToComparingFieldByField(testLot);
    }

    @Test
    @Transactional
    public void checkNumeroLotIsRequired() throws Exception {
        int databaseSizeBeforeTest = lotRepository.findAll().size();
        // set the field null
        lot.setNumeroLot(null);

        // Create the Lot, which fails.

        restLotMockMvc.perform(post("/api/lots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lot)))
                .andExpect(status().isBadRequest());

        List<Lot> lots = lotRepository.findAll();
        assertThat(lots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFabricationIsRequired() throws Exception {
        int databaseSizeBeforeTest = lotRepository.findAll().size();
        // set the field null
        lot.setDateFabrication(null);

        // Create the Lot, which fails.

        restLotMockMvc.perform(post("/api/lots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lot)))
                .andExpect(status().isBadRequest());

        List<Lot> lots = lotRepository.findAll();
        assertThat(lots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatePeremptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lotRepository.findAll().size();
        // set the field null
        lot.setDatePeremption(null);

        // Create the Lot, which fails.

        restLotMockMvc.perform(post("/api/lots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lot)))
                .andExpect(status().isBadRequest());

        List<Lot> lots = lotRepository.findAll();
        assertThat(lots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantiteLotIsRequired() throws Exception {
        int databaseSizeBeforeTest = lotRepository.findAll().size();
        // set the field null
        lot.setQuantiteLot(null);

        // Create the Lot, which fails.

        restLotMockMvc.perform(post("/api/lots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lot)))
                .andExpect(status().isBadRequest());

        List<Lot> lots = lotRepository.findAll();
        assertThat(lots).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLots() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        // Get all the lots
        restLotMockMvc.perform(get("/api/lots?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lot.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroLot").value(hasItem(DEFAULT_NUMERO_LOT.intValue())))
                .andExpect(jsonPath("$.[*].dateFabrication").value(hasItem(DEFAULT_DATE_FABRICATION.toString())))
                .andExpect(jsonPath("$.[*].datePeremption").value(hasItem(DEFAULT_DATE_PEREMPTION.toString())))
                .andExpect(jsonPath("$.[*].descriptionLot").value(hasItem(DEFAULT_DESCRIPTION_LOT.toString())))
                .andExpect(jsonPath("$.[*].quantiteLot").value(hasItem(DEFAULT_QUANTITE_LOT.intValue())))
                .andExpect(jsonPath("$.[*].quantiteCartonLot").value(hasItem(DEFAULT_QUANTITE_CARTON_LOT.intValue())))
                .andExpect(jsonPath("$.[*].quantiteSortie").value(hasItem(DEFAULT_QUANTITE_SORTIE.intValue())));
    }

    @Test
    @Transactional
    public void getLot() throws Exception {
        // Initialize the database
        lotRepository.saveAndFlush(lot);

        // Get the lot
        restLotMockMvc.perform(get("/api/lots/{id}", lot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lot.getId().intValue()))
            .andExpect(jsonPath("$.numeroLot").value(DEFAULT_NUMERO_LOT.intValue()))
            .andExpect(jsonPath("$.dateFabrication").value(DEFAULT_DATE_FABRICATION.toString()))
            .andExpect(jsonPath("$.datePeremption").value(DEFAULT_DATE_PEREMPTION.toString()))
            .andExpect(jsonPath("$.descriptionLot").value(DEFAULT_DESCRIPTION_LOT.toString()))
            .andExpect(jsonPath("$.quantiteLot").value(DEFAULT_QUANTITE_LOT.intValue()))
            .andExpect(jsonPath("$.quantiteCartonLot").value(DEFAULT_QUANTITE_CARTON_LOT.intValue()))
            .andExpect(jsonPath("$.quantiteSortie").value(DEFAULT_QUANTITE_SORTIE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLot() throws Exception {
        // Get the lot
        restLotMockMvc.perform(get("/api/lots/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLot() throws Exception {
        // Initialize the database
        lotService.save(lot);

        int databaseSizeBeforeUpdate = lotRepository.findAll().size();

        // Update the lot
        Lot updatedLot = new Lot();
        updatedLot.setId(lot.getId());
        updatedLot.setNumeroLot(UPDATED_NUMERO_LOT);
        updatedLot.setDateFabrication(UPDATED_DATE_FABRICATION);
        updatedLot.setDatePeremption(UPDATED_DATE_PEREMPTION);
        updatedLot.setDescriptionLot(UPDATED_DESCRIPTION_LOT);
        updatedLot.setQuantiteLot(UPDATED_QUANTITE_LOT);
        updatedLot.setQuantiteCartonLot(UPDATED_QUANTITE_CARTON_LOT);
        updatedLot.setQuantiteSortie(UPDATED_QUANTITE_SORTIE);

        restLotMockMvc.perform(put("/api/lots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLot)))
                .andExpect(status().isOk());

        // Validate the Lot in the database
        List<Lot> lots = lotRepository.findAll();
        assertThat(lots).hasSize(databaseSizeBeforeUpdate);
        Lot testLot = lots.get(lots.size() - 1);
        assertThat(testLot.getNumeroLot()).isEqualTo(UPDATED_NUMERO_LOT);
        assertThat(testLot.getDateFabrication()).isEqualTo(UPDATED_DATE_FABRICATION);
        assertThat(testLot.getDatePeremption()).isEqualTo(UPDATED_DATE_PEREMPTION);
        assertThat(testLot.getDescriptionLot()).isEqualTo(UPDATED_DESCRIPTION_LOT);
        assertThat(testLot.getQuantiteLot()).isEqualTo(UPDATED_QUANTITE_LOT);
        assertThat(testLot.getQuantiteCartonLot()).isEqualTo(UPDATED_QUANTITE_CARTON_LOT);
        assertThat(testLot.getQuantiteSortie()).isEqualTo(UPDATED_QUANTITE_SORTIE);

        // Validate the Lot in ElasticSearch
        Lot lotEs = lotSearchRepository.findOne(testLot.getId());
        assertThat(lotEs).isEqualToComparingFieldByField(testLot);
    }

    @Test
    @Transactional
    public void deleteLot() throws Exception {
        // Initialize the database
        lotService.save(lot);

        int databaseSizeBeforeDelete = lotRepository.findAll().size();

        // Get the lot
        restLotMockMvc.perform(delete("/api/lots/{id}", lot.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lotExistsInEs = lotSearchRepository.exists(lot.getId());
        assertThat(lotExistsInEs).isFalse();

        // Validate the database is empty
        List<Lot> lots = lotRepository.findAll();
        assertThat(lots).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLot() throws Exception {
        // Initialize the database
        lotService.save(lot);

        // Search the lot
        restLotMockMvc.perform(get("/api/_search/lots?query=id:" + lot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lot.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroLot").value(hasItem(DEFAULT_NUMERO_LOT.intValue())))
            .andExpect(jsonPath("$.[*].dateFabrication").value(hasItem(DEFAULT_DATE_FABRICATION.toString())))
            .andExpect(jsonPath("$.[*].datePeremption").value(hasItem(DEFAULT_DATE_PEREMPTION.toString())))
            .andExpect(jsonPath("$.[*].descriptionLot").value(hasItem(DEFAULT_DESCRIPTION_LOT.toString())))
            .andExpect(jsonPath("$.[*].quantiteLot").value(hasItem(DEFAULT_QUANTITE_LOT.intValue())))
            .andExpect(jsonPath("$.[*].quantiteCartonLot").value(hasItem(DEFAULT_QUANTITE_CARTON_LOT.intValue())))
            .andExpect(jsonPath("$.[*].quantiteSortie").value(hasItem(DEFAULT_QUANTITE_SORTIE.intValue())));
    }
}
