package com.webstocker.repository;

import com.webstocker.domain.LigneMissionActivite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LigneMissionActivite entity.
 */
public interface LigneMissionActiviteRepository extends JpaRepository<LigneMissionActivite,Long> {

}
