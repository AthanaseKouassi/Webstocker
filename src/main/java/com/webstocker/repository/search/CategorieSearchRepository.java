package com.webstocker.repository.search;

import com.webstocker.domain.Categorie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Categorie entity.
 */
public interface CategorieSearchRepository extends ElasticsearchRepository<Categorie, Long> {
}
