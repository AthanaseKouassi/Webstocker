package com.webstocker.repository.search;

import com.webstocker.domain.Conditionnement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Conditionnement entity.
 */
public interface ConditionnementSearchRepository extends ElasticsearchRepository<Conditionnement, Long> {
}
