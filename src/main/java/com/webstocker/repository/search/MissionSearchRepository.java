package com.webstocker.repository.search;

import com.webstocker.domain.Mission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Mission entity.
 */
public interface MissionSearchRepository extends ElasticsearchRepository<Mission, Long> {
}
