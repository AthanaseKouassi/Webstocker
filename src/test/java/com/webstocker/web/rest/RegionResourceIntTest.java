package com.webstocker.web.rest;

import com.webstocker.WebstockerApp;
import com.webstocker.domain.Region;
import com.webstocker.repository.RegionRepository;
import com.webstocker.service.RegionService;
import com.webstocker.repository.search.RegionSearchRepository;

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
 * Test class for the RegionResource REST controller.
 *
 * @see RegionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebstockerApp.class)
@WebAppConfiguration
@IntegrationTest
public class RegionResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    @Inject
    private RegionRepository regionRepository;

    @Inject
    private RegionService regionService;

    @Inject
    private RegionSearchRepository regionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRegionMockMvc;

    private Region region;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegionResource regionResource = new RegionResource();
        ReflectionTestUtils.setField(regionResource, "regionService", regionService);
        this.restRegionMockMvc = MockMvcBuilders.standaloneSetup(regionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        regionSearchRepository.deleteAll();
        region = new Region();
        region.setLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // Create the Region

        restRegionMockMvc.perform(post("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isCreated());

        // Validate the Region in the database
        List<Region> regions = regionRepository.findAll();
        assertThat(regions).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regions.get(regions.size() - 1);
        assertThat(testRegion.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the Region in ElasticSearch
        Region regionEs = regionSearchRepository.findOne(testRegion.getId());
        assertThat(regionEs).isEqualToComparingFieldByField(testRegion);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = regionRepository.findAll().size();
        // set the field null
        region.setLibelle(null);

        // Create the Region, which fails.

        restRegionMockMvc.perform(post("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isBadRequest());

        List<Region> regions = regionRepository.findAll();
        assertThat(regions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegions() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regions
        restRegionMockMvc.perform(get("/api/regions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(region.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegion() throws Exception {
        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegion() throws Exception {
        // Initialize the database
        regionService.save(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region
        Region updatedRegion = new Region();
        updatedRegion.setId(region.getId());
        updatedRegion.setLibelle(UPDATED_LIBELLE);

        restRegionMockMvc.perform(put("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRegion)))
                .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regions = regionRepository.findAll();
        assertThat(regions).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regions.get(regions.size() - 1);
        assertThat(testRegion.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the Region in ElasticSearch
        Region regionEs = regionSearchRepository.findOne(testRegion.getId());
        assertThat(regionEs).isEqualToComparingFieldByField(testRegion);
    }

    @Test
    @Transactional
    public void deleteRegion() throws Exception {
        // Initialize the database
        regionService.save(region);

        int databaseSizeBeforeDelete = regionRepository.findAll().size();

        // Get the region
        restRegionMockMvc.perform(delete("/api/regions/{id}", region.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean regionExistsInEs = regionSearchRepository.exists(region.getId());
        assertThat(regionExistsInEs).isFalse();

        // Validate the database is empty
        List<Region> regions = regionRepository.findAll();
        assertThat(regions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRegion() throws Exception {
        // Initialize the database
        regionService.save(region);

        // Search the region
        restRegionMockMvc.perform(get("/api/_search/regions?query=id:" + region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
