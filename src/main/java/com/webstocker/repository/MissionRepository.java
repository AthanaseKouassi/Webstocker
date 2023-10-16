package com.webstocker.repository;

import com.webstocker.domain.Mission;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mission entity.
 */
public interface MissionRepository extends JpaRepository<Mission,Long> {
   
    @Query(" SELECT m "
            + " FROM Mission m "
            + "WHERE ((?3 is null) OR (m.libelle LIKE concat(concat('%',?3),'%'))) AND "
            + " ((?1 is null) OR (m.dateDebut >= ?1)) AND"
            + " ((?2 is null) OR (m.dateFin <= ?2)) ")
    List<Mission> findMissionByCritere(LocalDate DateDebut,LocalDate dateFin,String libelle);
}
