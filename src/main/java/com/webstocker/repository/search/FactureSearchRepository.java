package com.webstocker.repository.search;

import com.webstocker.domain.Facture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Facture entity.
 */
public interface FactureSearchRepository extends ElasticsearchRepository<Facture, Long> {
}
