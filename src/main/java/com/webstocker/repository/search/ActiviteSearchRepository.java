package com.webstocker.repository.search;

import com.webstocker.domain.Activite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Activite entity.
 */
public interface ActiviteSearchRepository extends ElasticsearchRepository<Activite, Long> {
}
