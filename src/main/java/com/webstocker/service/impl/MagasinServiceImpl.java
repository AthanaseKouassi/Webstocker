package com.webstocker.service.impl;

import com.webstocker.service.MagasinService;
import com.webstocker.domain.Magasin;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.search.MagasinSearchRepository;
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
 * Service Implementation for managing Magasin.
 */
@Service
@Transactional
public class MagasinServiceImpl implements MagasinService {

    private final Logger log = LoggerFactory.getLogger(MagasinServiceImpl.class);

    @Inject
    private MagasinRepository magasinRepository;

    @Inject
    private MagasinSearchRepository magasinSearchRepository;

    /**
     * Save a magasin.
     *
     * @param magasin the entity to save
     * @return the persisted entity
     */
    public Magasin save(Magasin magasin) {
        log.debug("Request to save Magasin : {}", magasin);
        Magasin result = magasinRepository.save(magasin);
        magasinSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the magasins.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Magasin> findAll() {
        log.debug("Request to get all Magasins");
        List<Magasin> result = magasinRepository.findAll();
        return result;
    }

    /**
     * Get one magasin by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Magasin findOne(Long id) {
        log.debug("Request to get Magasin : {}", id);
        Magasin magasin = magasinRepository.findOne(id);
        return magasin;
    }

    /**
     * Delete the magasin by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Magasin : {}", id);
        magasinRepository.delete(id);
        magasinSearchRepository.delete(id);
    }

    /**
     * Search for the magasin corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Magasin> search(String query) {
        log.debug("Request to search Magasins for query {}", query);
        return StreamSupport
                .stream(magasinSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Magasin findByNomMagasin(String nom) {
        Magasin result = null;
        String nomMagasin = null;
        nomMagasin = nom;
        return result = magasinRepository.findByNomMagasin(nomMagasin);

    }
}
