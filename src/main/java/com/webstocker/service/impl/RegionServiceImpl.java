package com.webstocker.service.impl;

import com.webstocker.service.RegionService;
import com.webstocker.domain.Region;
import com.webstocker.repository.RegionRepository;
import com.webstocker.repository.search.RegionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Region.
 */
@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    private final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Inject
    private RegionRepository regionRepository;

    @Inject
    private RegionSearchRepository regionSearchRepository;

    /**
     * Save a region.
     *
     * @param region the entity to save
     * @return the persisted entity
     */
    public Region save(Region region) {
        log.debug("Request to save Region : {}", region);
        Region result = regionRepository.save(region);
        regionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the regions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Region> findAll(Pageable pageable) {
        log.debug("Request to get all Regions");
        Page<Region> result = regionRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one region by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Region findOne(Long id) {
        log.debug("Request to get Region : {}", id);
        Region region = regionRepository.findOne(id);
        return region;
    }

    /**
     * Delete the region by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Region : {}", id);
        regionRepository.delete(id);
        regionSearchRepository.delete(id);
    }

    /**
     * Search for the region corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Region> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Regions for query {}", query);
        return regionSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public Region findByLibelle(String libelle) {
        Region result = null;
        String nom = null;
        nom = libelle;
        return result = regionRepository.findByLibelle(nom);
    }
}
