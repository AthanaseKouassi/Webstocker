package com.webstocker.repository;

import com.webstocker.domain.Activite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Activite entity.
 */
public interface ActiviteRepository extends JpaRepository<Activite,Long> {

}
