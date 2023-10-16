package com.webstocker.service.impl;

import com.webstocker.service.LignefactureService;
import com.webstocker.domain.Lignefacture;
import com.webstocker.repository.LignefactureRepository;
import com.webstocker.repository.search.LignefactureSearchRepository;
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
 * Service Implementation for managing Lignefacture.
 */
@Service
@Transactional
public class LignefactureServiceImpl implements LignefactureService{

    private final Logger log = LoggerFactory.getLogger(LignefactureServiceImpl.class);
    
    @Inject
    private LignefactureRepository lignefactureRepository;
    
    @Inject
    private LignefactureSearchRepository lignefactureSearchRepository;
    
    /**
     * Save a lignefacture.
     * 
     * @param lignefacture the entity to save
     * @return the persisted entity
     */
    public Lignefacture save(Lignefacture lignefacture) {
        log.debug("Request to save Lignefacture : {}", lignefacture);
        Lignefacture result = lignefactureRepository.save(lignefacture);
        lignefactureSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the lignefactures.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Lignefacture> findAll() {
        log.debug("Request to get all Lignefactures");
        List<Lignefacture> result = lignefactureRepository.findAll();
        return result;
    }

    /**
     *  Get one lignefacture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Lignefacture findOne(Long id) {
        log.debug("Request to get Lignefacture : {}", id);
        Lignefacture lignefacture = lignefactureRepository.findOne(id);
        return lignefacture;
    }

    /**
     *  Delete the  lignefacture by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Lignefacture : {}", id);
        lignefactureRepository.delete(id);
        lignefactureSearchRepository.delete(id);
    }

    /**
     * Search for the lignefacture corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Lignefacture> search(String query) {
        log.debug("Request to search Lignefactures for query {}", query);
        return StreamSupport
            .stream(lignefactureSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
