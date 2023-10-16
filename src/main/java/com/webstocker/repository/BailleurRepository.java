package com.webstocker.repository;

import com.webstocker.domain.Bailleur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bailleur entity.
 */
public interface BailleurRepository extends JpaRepository<Bailleur,Long> {

    Bailleur findByNomBailleur(String nom);
}
