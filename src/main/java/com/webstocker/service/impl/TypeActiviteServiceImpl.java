package com.webstocker.service.impl;

import com.webstocker.service.TypeActiviteService;
import com.webstocker.domain.TypeActivite;
import com.webstocker.repository.TypeActiviteRepository;
import com.webstocker.repository.search.TypeActiviteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TypeActivite.
 */
@Service
@Transactional
public class TypeActiviteServiceImpl implements TypeActiviteService{

    private final Logger log = LoggerFactory.getLogger(TypeActiviteServiceImpl.class);
    
    @Inject
    private TypeActiviteRepository typeActiviteRepository;
    
    @Inject
    private TypeActiviteSearchRepository typeActiviteSearchRepository;
    
    /**
     * Save a typeActivite.
     * 
     * @param typeActivite the entity to save
     * @return the persisted entity
     */
    public TypeActivite save(TypeActivite typeActivite) {
        log.debug("Request to save TypeActivite : {}", typeActivite);
        TypeActivite result = typeActiviteRepository.save(typeActivite);
        typeActiviteSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the typeActivites.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<TypeActivite> findAll() {
        log.debug("Request to get all TypeActivites");
        List<TypeActivite> result = typeActiviteRepository.findAll();
        return result;
    }

    /**
     *  Get one typeActivite by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TypeActivite findOne(Long id) {
        log.debug("Request to get TypeActivite : {}", id);
        TypeActivite typeActivite = typeActiviteRepository.findOne(id);
        return typeActivite;
    }

    /**
     *  Delete the  typeActivite by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeActivite : {}", id);
        typeActiviteRepository.delete(id);
        typeActiviteSearchRepository.delete(id);
    }

    /**
     * Search for the typeActivite corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TypeActivite> search(String query) {
        log.debug("Request to search TypeActivites for query {}", query);
        return StreamSupport
            .stream(typeActiviteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
