package com.webstocker.repository.search;

import com.webstocker.domain.LigneMissionActivite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LigneMissionActivite entity.
 */
public interface LigneMissionActiviteSearchRepository extends ElasticsearchRepository<LigneMissionActivite, Long> {
}
