package com.webstocker.repository.search;

import com.webstocker.domain.Commune;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Commune entity.
 */
public interface CommuneSearchRepository extends ElasticsearchRepository<Commune, Long> {
}
