package com.webstocker.repository.search;

import com.webstocker.domain.Categorieclient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Categorieclient entity.
 */
public interface CategorieclientSearchRepository extends ElasticsearchRepository<Categorieclient, Long> {
}
