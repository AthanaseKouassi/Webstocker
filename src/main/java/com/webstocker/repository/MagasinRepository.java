package com.webstocker.repository;

import com.webstocker.domain.Magasin;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Magasin entity.
 */
public interface MagasinRepository extends JpaRepository<Magasin,Long> {

    Magasin findByNomMagasin(String nom);
}
