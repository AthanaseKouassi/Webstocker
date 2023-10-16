package com.webstocker.repository.search;

import com.webstocker.domain.Bailleur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bailleur entity.
 */
public interface BailleurSearchRepository extends ElasticsearchRepository<Bailleur, Long> {
}
