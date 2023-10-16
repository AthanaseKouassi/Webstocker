package com.webstocker.repository.search;

import com.webstocker.domain.BonDeSortie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BonDeSortie entity.
 */
public interface BonDeSortieSearchRepository extends ElasticsearchRepository<BonDeSortie, Long> {
}
