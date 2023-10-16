package com.webstocker.repository;

import com.webstocker.domain.Ville;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ville entity.
 */
public interface VilleRepository extends JpaRepository<Ville, Long> {

    Ville findByLibelle(String libelle);
}
