package com.webstocker.repository.search;

import com.webstocker.domain.Lignecommande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lignecommande entity.
 */
public interface LignecommandeSearchRepository extends ElasticsearchRepository<Lignecommande, Long> {
}
