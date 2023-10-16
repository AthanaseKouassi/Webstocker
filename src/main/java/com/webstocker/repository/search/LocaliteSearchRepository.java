package com.webstocker.repository.search;

import com.webstocker.domain.Localite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Localite entity.
 */
public interface LocaliteSearchRepository extends ElasticsearchRepository<Localite, Long> {
}
