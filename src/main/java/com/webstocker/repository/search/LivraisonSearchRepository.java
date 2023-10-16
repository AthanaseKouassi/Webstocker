package com.webstocker.repository.search;

import com.webstocker.domain.Livraison;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Livraison entity.
 */
public interface LivraisonSearchRepository extends ElasticsearchRepository<Livraison, Long> {
}
