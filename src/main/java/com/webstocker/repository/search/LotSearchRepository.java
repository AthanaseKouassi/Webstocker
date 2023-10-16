package com.webstocker.repository.search;

import com.webstocker.domain.Lot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lot entity.
 */
public interface LotSearchRepository extends ElasticsearchRepository<Lot, Long> {
}
