package com.webstocker.repository.search;

import com.webstocker.domain.Ligneprixproduit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ligneprixproduit entity.
 */
public interface LigneprixproduitSearchRepository extends ElasticsearchRepository<Ligneprixproduit, Long> {
}
