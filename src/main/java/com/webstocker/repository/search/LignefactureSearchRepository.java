package com.webstocker.repository.search;

import com.webstocker.domain.Lignefacture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lignefacture entity.
 */
public interface LignefactureSearchRepository extends ElasticsearchRepository<Lignefacture, Long> {
}
