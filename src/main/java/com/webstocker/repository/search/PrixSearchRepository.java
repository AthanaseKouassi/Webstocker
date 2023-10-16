package com.webstocker.repository.search;

import com.webstocker.domain.Prix;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Prix entity.
 */
public interface PrixSearchRepository extends ElasticsearchRepository<Prix, Long> {
}
