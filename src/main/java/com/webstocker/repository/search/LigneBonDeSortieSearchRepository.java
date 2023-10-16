package com.webstocker.repository.search;

import com.webstocker.domain.LigneBonDeSortie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LigneBonDeSortie entity.
 */
public interface LigneBonDeSortieSearchRepository extends ElasticsearchRepository<LigneBonDeSortie, Long> {
}
