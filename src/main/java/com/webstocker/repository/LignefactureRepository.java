package com.webstocker.repository;

import com.webstocker.domain.Lignefacture;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lignefacture entity.
 */
public interface LignefactureRepository extends JpaRepository<Lignefacture,Long> {

}
