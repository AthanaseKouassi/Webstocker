package com.webstocker.repository;

import com.webstocker.domain.Commune;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Commune entity.
 */
public interface CommuneRepository extends JpaRepository<Commune,Long> {

    Commune findByLibelle(String libelle);
}
