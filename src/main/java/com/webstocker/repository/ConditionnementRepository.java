package com.webstocker.repository;

import com.webstocker.domain.Conditionnement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Conditionnement entity.
 */
public interface ConditionnementRepository extends JpaRepository<Conditionnement,Long> {

        Conditionnement findByLibelle(String libelle);
}
