package com.webstocker.repository;

import com.webstocker.domain.TypeActivite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeActivite entity.
 */
public interface TypeActiviteRepository extends JpaRepository<TypeActivite,Long> {

}
