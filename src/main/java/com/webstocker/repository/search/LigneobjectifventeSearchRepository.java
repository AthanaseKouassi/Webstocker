package com.webstocker.repository.search;

import com.webstocker.domain.Ligneobjectifvente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ligneobjectifvente entity.
 */
public interface LigneobjectifventeSearchRepository extends ElasticsearchRepository<Ligneobjectifvente, Long> {
}
