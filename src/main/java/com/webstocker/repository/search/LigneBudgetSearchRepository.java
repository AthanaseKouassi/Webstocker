package com.webstocker.repository.search;

import com.webstocker.domain.LigneBudget;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LigneBudget entity.
 */
public interface LigneBudgetSearchRepository extends ElasticsearchRepository<LigneBudget, Long> {
}
