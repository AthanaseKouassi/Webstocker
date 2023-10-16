package com.webstocker.repository;

import com.webstocker.domain.Budget;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Budget entity.
 */
public interface BudgetRepository extends JpaRepository<Budget,Long> {

}
