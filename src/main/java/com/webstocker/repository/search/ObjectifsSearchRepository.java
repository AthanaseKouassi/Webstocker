package com.webstocker.repository.search;

import com.webstocker.domain.Objectifs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Objectifs entity.
 */
public interface ObjectifsSearchRepository extends ElasticsearchRepository<Objectifs, Long> {
}
