package com.webstocker.service.impl;

import com.webstocker.service.LigneobjectifventeService;
import com.webstocker.domain.Ligneobjectifvente;
import com.webstocker.repository.LigneobjectifventeRepository;
import com.webstocker.repository.search.LigneobjectifventeSearchRepository;
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
 * Service Implementation for managing Ligneobjectifvente.
 */
@Service
@Transactional
public class LigneobjectifventeServiceImpl implements LigneobjectifventeService{

    private final Logger log = LoggerFactory.getLogger(LigneobjectifventeServiceImpl.class);
    
    @Inject
    private LigneobjectifventeRepository ligneobjectifventeRepository;
    
    @Inject
    private LigneobjectifventeSearchRepository ligneobjectifventeSearchRepository;
    
    /**
     * Save a ligneobjectifvente.
     * 
     * @param ligneobjectifvente the entity to save
     * @return the persisted entity
     */
    public Ligneobjectifvente save(Ligneobjectifvente ligneobjectifvente) {
        log.debug("Request to save Ligneobjectifvente : {}", ligneobjectifvente);
        Ligneobjectifvente result = ligneobjectifventeRepository.save(ligneobjectifvente);
        ligneobjectifventeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ligneobjectifventes.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Ligneobjectifvente> findAll() {
        log.debug("Request to get all Ligneobjectifventes");
        List<Ligneobjectifvente> result = ligneobjectifventeRepository.findAll();
        return result;
    }

    /**
     *  Get one ligneobjectifvente by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Ligneobjectifvente findOne(Long id) {
        log.debug("Request to get Ligneobjectifvente : {}", id);
        Ligneobjectifvente ligneobjectifvente = ligneobjectifventeRepository.findOne(id);
        return ligneobjectifvente;
    }

    /**
     *  Delete the  ligneobjectifvente by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ligneobjectifvente : {}", id);
        ligneobjectifventeRepository.delete(id);
        ligneobjectifventeSearchRepository.delete(id);
    }

    /**
     * Search for the ligneobjectifvente corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Ligneobjectifvente> search(String query) {
        log.debug("Request to search Ligneobjectifventes for query {}", query);
        return StreamSupport
            .stream(ligneobjectifventeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
