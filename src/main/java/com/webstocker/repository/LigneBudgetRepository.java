package com.webstocker.repository;

import com.webstocker.domain.LigneBudget;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LigneBudget entity.
 */
public interface LigneBudgetRepository extends JpaRepository<LigneBudget,Long> {

}
