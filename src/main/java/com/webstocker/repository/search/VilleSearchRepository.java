package com.webstocker.repository.search;

import com.webstocker.domain.Ville;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ville entity.
 */
public interface VilleSearchRepository extends ElasticsearchRepository<Ville, Long> {
}
