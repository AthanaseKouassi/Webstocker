package com.webstocker.repository.search;

import com.webstocker.domain.Magasin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Magasin entity.
 */
public interface MagasinSearchRepository extends ElasticsearchRepository<Magasin, Long> {
}
