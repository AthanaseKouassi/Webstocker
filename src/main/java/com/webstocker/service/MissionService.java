package com.webstocker.service;

import com.webstocker.domain.Mission;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Mission.
 */
public interface MissionService {

    /**
     * Save a mission.
     * 
     * @param mission the entity to save
     * @return the persisted entity
     */
    Mission save(Mission mission);

    /**
     *  Get all the missions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Mission> findAll(Pageable pageable);

    /**
     *  Get the "id" mission.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Mission findOne(Long id);

    /**
     *  Delete the "id" mission.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    List<Mission> findMissionByCritere(LocalDate dateDebut,LocalDate dateFin, String libelle);

    /**
     * Search for the mission corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Mission> search(String query, Pageable pageable);
}
