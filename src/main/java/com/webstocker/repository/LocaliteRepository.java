package com.webstocker.repository;

import com.webstocker.domain.Localite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Localite entity.
 */
public interface LocaliteRepository extends JpaRepository<Localite, Long> {

    Localite findByNom(String nom);
}
