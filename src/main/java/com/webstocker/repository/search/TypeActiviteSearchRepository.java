package com.webstocker.repository.search;

import com.webstocker.domain.TypeActivite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeActivite entity.
 */
public interface TypeActiviteSearchRepository extends ElasticsearchRepository<TypeActivite, Long> {
}
