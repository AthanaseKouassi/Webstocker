package com.webstocker.service.impl;

import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;
import com.webstocker.domain.enumeration.StatutCommande;
import com.webstocker.service.CommandeService;
import com.webstocker.domain.Commande;
import com.webstocker.repository.CommandeRepository;
import com.webstocker.repository.search.CommandeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Commande.
 */
@Service
@Transactional
public class CommandeServiceImpl implements CommandeService{

    private final Logger log = LoggerFactory.getLogger(CommandeServiceImpl.class);

    @Inject
    private CommandeRepository commandeRepository;

    @Inject
    private CommandeSearchRepository commandeSearchRepository;

    /**
     * Save a commande.
     *
     * @param commande the entity to save
     * @return the persisted entity
     */
    public Commande save(Commande commande) {
        log.debug("Request to save Commande : {}", commande);
        Commande result = commandeRepository.save(commande);
//        commandeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the commandes.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Commande> findAll() {
        log.debug("Request to get all Commandes");
        List<Commande> result = commandeRepository.findAll();
        return result;
    }

    /**
     *  Get one commande by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Commande findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        Commande commande = commandeRepository.findOne(id);
        return commande;
    }

    /**
     *  Delete the  commande by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        commandeRepository.delete(id);
//        commandeSearchRepository.delete(id);
    }

    /**
     * Search for the commande corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Commande> search(String query) {
        log.debug("Request to search Commandes for query {}", query);
        return StreamSupport
            .stream(commandeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Commande> findActiveCommandes(StatutCommande statutCommande) {
        return commandeRepository.findAllByStatutNotIn(statutCommande);
    }


    @Transactional(readOnly = true)
    public Long quantiteRestanteAlivrer(Long commandeID){
       
        Long qteRestanteAlivrer = 0L;
        
        Commande commande = commandeRepository.findOne(commandeID);

        Set<Livraison> livraisons = commande.getLivraisons();

        Long quantiteCommande = commande.getQuantiteCommande();
        Long quantiteLivre=0L;

        for (Livraison livraison :
            livraisons) {
            Set<Lignelivraison> lignelivraisons = livraison.getLignelivraisons();

            for (Lignelivraison lignelivraison :
                lignelivraisons) {
                quantiteLivre += lignelivraison.getQuantiteLotLivre();
            }
        }
        qteRestanteAlivrer = quantiteCommande-quantiteLivre;
        return  qteRestanteAlivrer;

    }

}
