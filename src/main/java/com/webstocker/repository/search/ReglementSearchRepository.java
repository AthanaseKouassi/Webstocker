package com.webstocker.repository.search;

import com.webstocker.domain.Reglement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Reglement entity.
 */
public interface ReglementSearchRepository extends ElasticsearchRepository<Reglement, Long> {
}
