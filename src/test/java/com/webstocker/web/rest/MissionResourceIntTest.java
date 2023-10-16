package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Mission;
import com.webstocker.repository.MissionRepository;
import com.webstocker.service.MissionService;
import com.webstocker.repository.search.MissionSearchRepository;

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
 * Test class for the MissionResource REST controller.
 *
 * @see MissionResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = WebstockerApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class MissionResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_OBJECTIF_GENERAL = "AAAAA";
    private static final String UPDATED_OBJECTIF_GENERAL = "BBBBB";
    private static final String DEFAULT_OBJECTIF_SPECIFIQUE = "AAAAA";
    private static final String UPDATED_OBJECTIF_SPECIFIQUE = "BBBBB";

    @Inject
    private MissionRepository missionRepository;

    @Inject
    private MissionService missionService;

    @Inject
    private MissionSearchRepository missionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMissionMockMvc;

    private Mission mission;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MissionResource missionResource = new MissionResource();
        ReflectionTestUtils.setField(missionResource, "missionService", missionService);
        this.restMissionMockMvc = MockMvcBuilders.standaloneSetup(missionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        missionSearchRepository.deleteAll();
        mission = new Mission();
        mission.setLibelle(DEFAULT_LIBELLE);
        mission.setDateDebut(DEFAULT_DATE_DEBUT);
        mission.setDateFin(DEFAULT_DATE_FIN);
        mission.setObjectifGeneral(DEFAULT_OBJECTIF_GENERAL);
        mission.setObjectifSpecifique(DEFAULT_OBJECTIF_SPECIFIQUE);
    }

    @Test
    @Transactional
    public void createMission() throws Exception {
        int databaseSizeBeforeCreate = missionRepository.findAll().size();

        // Create the Mission

        restMissionMockMvc.perform(post("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mission)))
                .andExpect(status().isCreated());

        // Validate the Mission in the database
        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeCreate + 1);
        Mission testMission = missions.get(missions.size() - 1);
        assertThat(testMission.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testMission.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testMission.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testMission.getObjectifGeneral()).isEqualTo(DEFAULT_OBJECTIF_GENERAL);
        assertThat(testMission.getObjectifSpecifique()).isEqualTo(DEFAULT_OBJECTIF_SPECIFIQUE);

        // Validate the Mission in ElasticSearch
        Mission missionEs = missionSearchRepository.findOne(testMission.getId());
        assertThat(missionEs).isEqualToComparingFieldByField(testMission);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = missionRepository.findAll().size();
        // set the field null
        mission.setLibelle(null);

        // Create the Mission, which fails.

        restMissionMockMvc.perform(post("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mission)))
                .andExpect(status().isBadRequest());

        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = missionRepository.findAll().size();
        // set the field null
        mission.setDateDebut(null);

        // Create the Mission, which fails.

        restMissionMockMvc.perform(post("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mission)))
                .andExpect(status().isBadRequest());

        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = missionRepository.findAll().size();
        // set the field null
        mission.setDateFin(null);

        // Create the Mission, which fails.

        restMissionMockMvc.perform(post("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mission)))
                .andExpect(status().isBadRequest());

        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkObjectifGeneralIsRequired() throws Exception {
        int databaseSizeBeforeTest = missionRepository.findAll().size();
        // set the field null
        mission.setObjectifGeneral(null);

        // Create the Mission, which fails.

        restMissionMockMvc.perform(post("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mission)))
                .andExpect(status().isBadRequest());

        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMissions() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get all the missions
        restMissionMockMvc.perform(get("/api/missions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mission.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].objectifGeneral").value(hasItem(DEFAULT_OBJECTIF_GENERAL.toString())))
                .andExpect(jsonPath("$.[*].objectifSpecifique").value(hasItem(DEFAULT_OBJECTIF_SPECIFIQUE.toString())));
    }

    @Test
    @Transactional
    public void getMission() throws Exception {
        // Initialize the database
        missionRepository.saveAndFlush(mission);

        // Get the mission
        restMissionMockMvc.perform(get("/api/missions/{id}", mission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mission.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.objectifGeneral").value(DEFAULT_OBJECTIF_GENERAL.toString()))
            .andExpect(jsonPath("$.objectifSpecifique").value(DEFAULT_OBJECTIF_SPECIFIQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMission() throws Exception {
        // Get the mission
        restMissionMockMvc.perform(get("/api/missions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMission() throws Exception {
        // Initialize the database
        missionService.save(mission);

        int databaseSizeBeforeUpdate = missionRepository.findAll().size();

        // Update the mission
        Mission updatedMission = new Mission();
        updatedMission.setId(mission.getId());
        updatedMission.setLibelle(UPDATED_LIBELLE);
        updatedMission.setDateDebut(UPDATED_DATE_DEBUT);
        updatedMission.setDateFin(UPDATED_DATE_FIN);
        updatedMission.setObjectifGeneral(UPDATED_OBJECTIF_GENERAL);
        updatedMission.setObjectifSpecifique(UPDATED_OBJECTIF_SPECIFIQUE);

        restMissionMockMvc.perform(put("/api/missions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMission)))
                .andExpect(status().isOk());

        // Validate the Mission in the database
        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeUpdate);
        Mission testMission = missions.get(missions.size() - 1);
        assertThat(testMission.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testMission.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMission.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testMission.getObjectifGeneral()).isEqualTo(UPDATED_OBJECTIF_GENERAL);
        assertThat(testMission.getObjectifSpecifique()).isEqualTo(UPDATED_OBJECTIF_SPECIFIQUE);

        // Validate the Mission in ElasticSearch
        Mission missionEs = missionSearchRepository.findOne(testMission.getId());
        assertThat(missionEs).isEqualToComparingFieldByField(testMission);
    }

    @Test
    @Transactional
    public void deleteMission() throws Exception {
        // Initialize the database
        missionService.save(mission);

        int databaseSizeBeforeDelete = missionRepository.findAll().size();

        // Get the mission
        restMissionMockMvc.perform(delete("/api/missions/{id}", mission.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean missionExistsInEs = missionSearchRepository.exists(mission.getId());
        assertThat(missionExistsInEs).isFalse();

        // Validate the database is empty
        List<Mission> missions = missionRepository.findAll();
        assertThat(missions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMission() throws Exception {
        // Initialize the database
        missionService.save(mission);

        // Search the mission
        restMissionMockMvc.perform(get("/api/_search/missions?query=id:" + mission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mission.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].objectifGeneral").value(hasItem(DEFAULT_OBJECTIF_GENERAL.toString())))
            .andExpect(jsonPath("$.[*].objectifSpecifique").value(hasItem(DEFAULT_OBJECTIF_SPECIFIQUE.toString())));
    }
}
