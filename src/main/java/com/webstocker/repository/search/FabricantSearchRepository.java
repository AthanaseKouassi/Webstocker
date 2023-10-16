package com.webstocker.repository.search;

import com.webstocker.domain.Fabricant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fabricant entity.
 */
public interface FabricantSearchRepository extends ElasticsearchRepository<Fabricant, Long> {
}
