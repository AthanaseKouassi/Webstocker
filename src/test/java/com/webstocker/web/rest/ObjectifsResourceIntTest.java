package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Objectifs;
import com.webstocker.repository.ObjectifsRepository;
import com.webstocker.service.ObjectifsService;
import com.webstocker.repository.search.ObjectifsSearchRepository;

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
 * Test class for the ObjectifsResource REST controller.
 *
 * @see ObjectifsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class ObjectifsResourceIntTest {


    private static final LocalDate DEFAULT_PERIODE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_QUANTITE_ATTENDUE = 1L;
    private static final Long UPDATED_QUANTITE_ATTENDUE = 2L;

    private static final Long DEFAULT_QUANTITE_OBTENU = 1L;
    private static final Long UPDATED_QUANTITE_OBTENU = 2L;
    private static final String DEFAULT_TAUX = "AAAAA";
    private static final String UPDATED_TAUX = "BBBBB";

    @Inject
    private ObjectifsRepository objectifsRepository;

    @Inject
    private ObjectifsService objectifsService;

    @Inject
    private ObjectifsSearchRepository objectifsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restObjectifsMockMvc;

    private Objectifs objectifs;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjectifsResource objectifsResource = new ObjectifsResource();
        ReflectionTestUtils.setField(objectifsResource, "objectifsService", objectifsService);
        this.restObjectifsMockMvc = MockMvcBuilders.standaloneSetup(objectifsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        objectifsSearchRepository.deleteAll();
        objectifs = new Objectifs();
        objectifs.setPeriode(DEFAULT_PERIODE);
        objectifs.setQuantiteAttendue(DEFAULT_QUANTITE_ATTENDUE);
        objectifs.setQuantiteObtenu(DEFAULT_QUANTITE_OBTENU);
        objectifs.setTaux(DEFAULT_TAUX);
    }

    @Test
    @Transactional
    public void createObjectifs() throws Exception {
        int databaseSizeBeforeCreate = objectifsRepository.findAll().size();

        // Create the Objectifs

        restObjectifsMockMvc.perform(post("/api/objectifs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(objectifs)))
                .andExpect(status().isCreated());

        // Validate the Objectifs in the database
        List<Objectifs> objectifs = objectifsRepository.findAll();
        assertThat(objectifs).hasSize(databaseSizeBeforeCreate + 1);
        Objectifs testObjectifs = objectifs.get(objectifs.size() - 1);
        assertThat(testObjectifs.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testObjectifs.getQuantiteAttendue()).isEqualTo(DEFAULT_QUANTITE_ATTENDUE);
        assertThat(testObjectifs.getQuantiteObtenu()).isEqualTo(DEFAULT_QUANTITE_OBTENU);
        assertThat(testObjectifs.getTaux()).isEqualTo(DEFAULT_TAUX);

        // Validate the Objectifs in ElasticSearch
        Objectifs objectifsEs = objectifsSearchRepository.findOne(testObjectifs.getId());
        assertThat(objectifsEs).isEqualToComparingFieldByField(testObjectifs);
    }

    @Test
    @Transactional
    public void getAllObjectifs() throws Exception {
        // Initialize the database
        objectifsRepository.saveAndFlush(objectifs);

        // Get all the objectifs
        restObjectifsMockMvc.perform(get("/api/objectifs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(objectifs.getId().intValue())))
                .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
                .andExpect(jsonPath("$.[*].quantiteAttendue").value(hasItem(DEFAULT_QUANTITE_ATTENDUE.intValue())))
                .andExpect(jsonPath("$.[*].quantiteObtenu").value(hasItem(DEFAULT_QUANTITE_OBTENU.intValue())))
                .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX.toString())));
    }

    @Test
    @Transactional
    public void getObjectifs() throws Exception {
        // Initialize the database
        objectifsRepository.saveAndFlush(objectifs);

        // Get the objectifs
        restObjectifsMockMvc.perform(get("/api/objectifs/{id}", objectifs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(objectifs.getId().intValue()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.quantiteAttendue").value(DEFAULT_QUANTITE_ATTENDUE.intValue()))
            .andExpect(jsonPath("$.quantiteObtenu").value(DEFAULT_QUANTITE_OBTENU.intValue()))
            .andExpect(jsonPath("$.taux").value(DEFAULT_TAUX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObjectifs() throws Exception {
        // Get the objectifs
        restObjectifsMockMvc.perform(get("/api/objectifs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObjectifs() throws Exception {
        // Initialize the database
        objectifsService.save(objectifs);

        int databaseSizeBeforeUpdate = objectifsRepository.findAll().size();

        // Update the objectifs
        Objectifs updatedObjectifs = new Objectifs();
        updatedObjectifs.setId(objectifs.getId());
        updatedObjectifs.setPeriode(UPDATED_PERIODE);
        updatedObjectifs.setQuantiteAttendue(UPDATED_QUANTITE_ATTENDUE);
        updatedObjectifs.setQuantiteObtenu(UPDATED_QUANTITE_OBTENU);
        updatedObjectifs.setTaux(UPDATED_TAUX);

        restObjectifsMockMvc.perform(put("/api/objectifs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedObjectifs)))
                .andExpect(status().isOk());

        // Validate the Objectifs in the database
        List<Objectifs> objectifs = objectifsRepository.findAll();
        assertThat(objectifs).hasSize(databaseSizeBeforeUpdate);
        Objectifs testObjectifs = objectifs.get(objectifs.size() - 1);
        assertThat(testObjectifs.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testObjectifs.getQuantiteAttendue()).isEqualTo(UPDATED_QUANTITE_ATTENDUE);
        assertThat(testObjectifs.getQuantiteObtenu()).isEqualTo(UPDATED_QUANTITE_OBTENU);
        assertThat(testObjectifs.getTaux()).isEqualTo(UPDATED_TAUX);

        // Validate the Objectifs in ElasticSearch
        Objectifs objectifsEs = objectifsSearchRepository.findOne(testObjectifs.getId());
        assertThat(objectifsEs).isEqualToComparingFieldByField(testObjectifs);
    }

    @Test
    @Transactional
    public void deleteObjectifs() throws Exception {
        // Initialize the database
        objectifsService.save(objectifs);

        int databaseSizeBeforeDelete = objectifsRepository.findAll().size();

        // Get the objectifs
        restObjectifsMockMvc.perform(delete("/api/objectifs/{id}", objectifs.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean objectifsExistsInEs = objectifsSearchRepository.exists(objectifs.getId());
        assertThat(objectifsExistsInEs).isFalse();

        // Validate the database is empty
        List<Objectifs> objectifs = objectifsRepository.findAll();
        assertThat(objectifs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchObjectifs() throws Exception {
        // Initialize the database
        objectifsService.save(objectifs);

        // Search the objectifs
        restObjectifsMockMvc.perform(get("/api/_search/objectifs?query=id:" + objectifs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objectifs.getId().intValue())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].quantiteAttendue").value(hasItem(DEFAULT_QUANTITE_ATTENDUE.intValue())))
            .andExpect(jsonPath("$.[*].quantiteObtenu").value(hasItem(DEFAULT_QUANTITE_OBTENU.intValue())))
            .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX.toString())));
    }
}
