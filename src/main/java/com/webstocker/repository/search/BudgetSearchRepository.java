package com.webstocker.repository.search;

import com.webstocker.domain.Budget;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Budget entity.
 */
public interface BudgetSearchRepository extends ElasticsearchRepository<Budget, Long> {
}
