package com.webstocker.repository.search;

import com.webstocker.domain.Commande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Commande entity.
 */
public interface CommandeSearchRepository extends ElasticsearchRepository<Commande, Long> {
}
