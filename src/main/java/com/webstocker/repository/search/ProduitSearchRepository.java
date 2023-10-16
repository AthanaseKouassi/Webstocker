package com.webstocker.repository.search;

import com.webstocker.domain.Produit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Produit entity.
 */
public interface ProduitSearchRepository extends ElasticsearchRepository<Produit, Long> {
}
