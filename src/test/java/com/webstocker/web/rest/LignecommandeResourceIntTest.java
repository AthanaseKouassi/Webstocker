package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Lignecommande;
import com.webstocker.repository.LignecommandeRepository;
import com.webstocker.service.LignecommandeService;
import com.webstocker.repository.search.LignecommandeSearchRepository;

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
 * Test class for the LignecommandeResource REST controller.
 *
 * @see LignecommandeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class LignecommandeResourceIntTest {


    private static final LocalDate DEFAULT_DATE_FABRICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FABRICATION = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_QUANTITE_LIGNE_COMMANDE = 1;
    private static final Integer UPDATED_QUANTITE_LIGNE_COMMANDE = 2;

    @Inject
    private LignecommandeRepository lignecommandeRepository;

    @Inject
    private LignecommandeService lignecommandeService;

    @Inject
    private LignecommandeSearchRepository lignecommandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLignecommandeMockMvc;

    private Lignecommande lignecommande;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LignecommandeResource lignecommandeResource = new LignecommandeResource();
        ReflectionTestUtils.setField(lignecommandeResource, "lignecommandeService", lignecommandeService);
        this.restLignecommandeMockMvc = MockMvcBuilders.standaloneSetup(lignecommandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lignecommandeSearchRepository.deleteAll();
        lignecommande = new Lignecommande();
        lignecommande.setDateFabrication(DEFAULT_DATE_FABRICATION);
        lignecommande.setQuantiteLigneCommande(DEFAULT_QUANTITE_LIGNE_COMMANDE);
    }

    @Test
    @Transactional
    public void createLignecommande() throws Exception {
        int databaseSizeBeforeCreate = lignecommandeRepository.findAll().size();

        // Create the Lignecommande

        restLignecommandeMockMvc.perform(post("/api/lignecommandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lignecommande)))
                .andExpect(status().isCreated());

        // Validate the Lignecommande in the database
        List<Lignecommande> lignecommandes = lignecommandeRepository.findAll();
        assertThat(lignecommandes).hasSize(databaseSizeBeforeCreate + 1);
        Lignecommande testLignecommande = lignecommandes.get(lignecommandes.size() - 1);
        assertThat(testLignecommande.getDateFabrication()).isEqualTo(DEFAULT_DATE_FABRICATION);
        assertThat(testLignecommande.getQuantiteLigneCommande()).isEqualTo(DEFAULT_QUANTITE_LIGNE_COMMANDE);

        // Validate the Lignecommande in ElasticSearch
        Lignecommande lignecommandeEs = lignecommandeSearchRepository.findOne(testLignecommande.getId());
        assertThat(lignecommandeEs).isEqualToComparingFieldByField(testLignecommande);
    }

    @Test
    @Transactional
    public void checkDateFabricationIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignecommandeRepository.findAll().size();
        // set the field null
        lignecommande.setDateFabrication(null);

        // Create the Lignecommande, which fails.

        restLignecommandeMockMvc.perform(post("/api/lignecommandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lignecommande)))
                .andExpect(status().isBadRequest());

        List<Lignecommande> lignecommandes = lignecommandeRepository.findAll();
        assertThat(lignecommandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantiteLigneCommandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lignecommandeRepository.findAll().size();
        // set the field null
        lignecommande.setQuantiteLigneCommande(null);

        // Create the Lignecommande, which fails.

        restLignecommandeMockMvc.perform(post("/api/lignecommandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lignecommande)))
                .andExpect(status().isBadRequest());

        List<Lignecommande> lignecommandes = lignecommandeRepository.findAll();
        assertThat(lignecommandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLignecommandes() throws Exception {
        // Initialize the database
        lignecommandeRepository.saveAndFlush(lignecommande);

        // Get all the lignecommandes
        restLignecommandeMockMvc.perform(get("/api/lignecommandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lignecommande.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateFabrication").value(hasItem(DEFAULT_DATE_FABRICATION.toString())))
                .andExpect(jsonPath("$.[*].quantiteLigneCommande").value(hasItem(DEFAULT_QUANTITE_LIGNE_COMMANDE)));
    }

    @Test
    @Transactional
    public void getLignecommande() throws Exception {
        // Initialize the database
        lignecommandeRepository.saveAndFlush(lignecommande);

        // Get the lignecommande
        restLignecommandeMockMvc.perform(get("/api/lignecommandes/{id}", lignecommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lignecommande.getId().intValue()))
            .andExpect(jsonPath("$.dateFabrication").value(DEFAULT_DATE_FABRICATION.toString()))
            .andExpect(jsonPath("$.quantiteLigneCommande").value(DEFAULT_QUANTITE_LIGNE_COMMANDE));
    }

    @Test
    @Transactional
    public void getNonExistingLignecommande() throws Exception {
        // Get the lignecommande
        restLignecommandeMockMvc.perform(get("/api/lignecommandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLignecommande() throws Exception {
        // Initialize the database
        lignecommandeService.save(lignecommande);

        int databaseSizeBeforeUpdate = lignecommandeRepository.findAll().size();

        // Update the lignecommande
        Lignecommande updatedLignecommande = new Lignecommande();
        updatedLignecommande.setId(lignecommande.getId());
        updatedLignecommande.setDateFabrication(UPDATED_DATE_FABRICATION);
        updatedLignecommande.setQuantiteLigneCommande(UPDATED_QUANTITE_LIGNE_COMMANDE);

        restLignecommandeMockMvc.perform(put("/api/lignecommandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLignecommande)))
                .andExpect(status().isOk());

        // Validate the Lignecommande in the database
        List<Lignecommande> lignecommandes = lignecommandeRepository.findAll();
        assertThat(lignecommandes).hasSize(databaseSizeBeforeUpdate);
        Lignecommande testLignecommande = lignecommandes.get(lignecommandes.size() - 1);
        assertThat(testLignecommande.getDateFabrication()).isEqualTo(UPDATED_DATE_FABRICATION);
        assertThat(testLignecommande.getQuantiteLigneCommande()).isEqualTo(UPDATED_QUANTITE_LIGNE_COMMANDE);

        // Validate the Lignecommande in ElasticSearch
        Lignecommande lignecommandeEs = lignecommandeSearchRepository.findOne(testLignecommande.getId());
        assertThat(lignecommandeEs).isEqualToComparingFieldByField(testLignecommande);
    }

    @Test
    @Transactional
    public void deleteLignecommande() throws Exception {
        // Initialize the database
        lignecommandeService.save(lignecommande);

        int databaseSizeBeforeDelete = lignecommandeRepository.findAll().size();

        // Get the lignecommande
        restLignecommandeMockMvc.perform(delete("/api/lignecommandes/{id}", lignecommande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lignecommandeExistsInEs = lignecommandeSearchRepository.exists(lignecommande.getId());
        assertThat(lignecommandeExistsInEs).isFalse();

        // Validate the database is empty
        List<Lignecommande> lignecommandes = lignecommandeRepository.findAll();
        assertThat(lignecommandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLignecommande() throws Exception {
        // Initialize the database
        lignecommandeService.save(lignecommande);

        // Search the lignecommande
        restLignecommandeMockMvc.perform(get("/api/_search/lignecommandes?query=id:" + lignecommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lignecommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFabrication").value(hasItem(DEFAULT_DATE_FABRICATION.toString())))
            .andExpect(jsonPath("$.[*].quantiteLigneCommande").value(hasItem(DEFAULT_QUANTITE_LIGNE_COMMANDE)));
    }
}
