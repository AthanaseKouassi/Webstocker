package com.webstocker.repository;

import com.webstocker.domain.Ligneprixproduit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ligneprixproduit entity.
 */
public interface LigneprixproduitRepository extends JpaRepository<Ligneprixproduit,Long> {

}
