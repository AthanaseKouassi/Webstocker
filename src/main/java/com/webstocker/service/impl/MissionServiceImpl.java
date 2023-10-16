package com.webstocker.service.impl;

import com.webstocker.service.MissionService;
import com.webstocker.domain.Mission;
import com.webstocker.repository.MissionRepository;
import com.webstocker.repository.search.MissionSearchRepository;
import java.time.LocalDate;
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
 * Service Implementation for managing Mission.
 */
@Service
@Transactional
public class MissionServiceImpl implements MissionService{

    private final Logger log = LoggerFactory.getLogger(MissionServiceImpl.class);
    
    @Inject
    private MissionRepository missionRepository;
    
    @Inject
    private MissionSearchRepository missionSearchRepository;
    
    /**
     * Save a mission.
     * 
     * @param mission the entity to save
     * @return the persisted entity
     */
    public Mission save(Mission mission) {
        log.debug("Request to save Mission : {}", mission);
        Mission result = missionRepository.save(mission);
        missionSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the missions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Mission> findAll(Pageable pageable) {
        log.debug("Request to get all Missions");
        Page<Mission> result = missionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one mission by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Mission findOne(Long id) {
        log.debug("Request to get Mission : {}", id);
        Mission mission = missionRepository.findOne(id);
        return mission;
    }

    /**
     *  Delete the  mission by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Mission : {}", id);
        missionRepository.delete(id);
        missionSearchRepository.delete(id);
    }

    /**
     * Search for the mission corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Mission> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Missions for query {}", query);
        return missionSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public List<Mission> findMissionByCritere(LocalDate dateDebut, LocalDate dateFin, String libelle) {
        return missionRepository.findMissionByCritere(dateDebut,dateFin,libelle);
    }
}
