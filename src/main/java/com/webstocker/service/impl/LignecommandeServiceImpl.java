package com.webstocker.service.impl;

import com.webstocker.domain.Commande;
import com.webstocker.service.LignecommandeService;
import com.webstocker.domain.Lignecommande;
import com.webstocker.repository.LignecommandeRepository;
import com.webstocker.repository.search.LignecommandeSearchRepository;
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
 * Service Implementation for managing Lignecommande.
 */
@Service
@Transactional
public class LignecommandeServiceImpl implements LignecommandeService{

    private final Logger log = LoggerFactory.getLogger(LignecommandeServiceImpl.class);

    @Inject
    private LignecommandeRepository lignecommandeRepository;

    @Inject
    private LignecommandeSearchRepository lignecommandeSearchRepository;

    /**
     * Save a lignecommande.
     *
     * @param lignecommande the entity to save
     * @return the persisted entity
     */
    public Lignecommande save(Lignecommande lignecommande) {
        log.debug("Request to save Lignecommande : {}", lignecommande);
        Lignecommande result = lignecommandeRepository.save(lignecommande);
        lignecommandeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the lignecommandes.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Lignecommande> findAll() {
        log.debug("Request to get all Lignecommandes");
        List<Lignecommande> result = lignecommandeRepository.findAll();
        return result;
    }

    /**
     *  Get one lignecommande by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Lignecommande findOne(Long id) {
        log.debug("Request to get Lignecommande : {}", id);
        Lignecommande lignecommande = lignecommandeRepository.findOne(id);
        return lignecommande;
    }

    /**
     *  Delete the  lignecommande by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Lignecommande : {}", id);
        lignecommandeRepository.delete(id);
        lignecommandeSearchRepository.delete(id);
    }

    /**
     * Search for the lignecommande corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Lignecommande> search(String query) {
        log.debug("Request to search Lignecommandes for query {}", query);
        return StreamSupport
            .stream(lignecommandeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Lignecommande> findByCommande(Commande commande) {
        return lignecommandeRepository.findByCommande(commande);
    }

    
}
