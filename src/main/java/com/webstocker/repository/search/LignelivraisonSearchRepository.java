package com.webstocker.repository.search;

import com.webstocker.domain.Lignelivraison;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lignelivraison entity.
 */
public interface LignelivraisonSearchRepository extends ElasticsearchRepository<Lignelivraison, Long> {
}
