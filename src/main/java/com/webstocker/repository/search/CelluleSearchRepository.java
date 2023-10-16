package com.webstocker.repository.search;

import com.webstocker.domain.Cellule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cellule entity.
 */
public interface CelluleSearchRepository extends ElasticsearchRepository<Cellule, Long> {
}
