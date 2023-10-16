package com.webstocker.repository.search;

import com.webstocker.domain.Inventaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Inventaire entity.
 */
public interface InventaireSearchRepository extends ElasticsearchRepository<Inventaire, Long> {
}
