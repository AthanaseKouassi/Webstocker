package com.webstocker.repository;

import com.webstocker.domain.Categorieclient;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Categorieclient entity.
 */
public interface CategorieclientRepository extends JpaRepository<Categorieclient, Long> {

    Categorieclient findByLibelleCategorieClient(String nomLibelle);
}
