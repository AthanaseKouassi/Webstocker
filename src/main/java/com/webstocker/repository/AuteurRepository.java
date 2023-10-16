package com.webstocker.repository;

import com.webstocker.domain.Auteur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Auteur entity.
 */
public interface AuteurRepository extends JpaRepository<Auteur,Long> {

}
