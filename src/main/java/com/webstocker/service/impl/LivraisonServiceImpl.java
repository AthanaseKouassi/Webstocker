package com.webstocker.service.impl;

import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.enumeration.StatutCommande;
import com.webstocker.repository.CommandeRepository;
import com.webstocker.repository.search.CommandeSearchRepository;
import com.webstocker.service.LivraisonService;
import com.webstocker.domain.Livraison;
import com.webstocker.repository.LivraisonRepository;
import com.webstocker.repository.search.LivraisonSearchRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Livraison.
 */
@Service
@Transactional
public class LivraisonServiceImpl implements LivraisonService {

    private final Logger log = LoggerFactory.getLogger(LivraisonServiceImpl.class);

    @Inject
    private LivraisonRepository livraisonRepository;

    @Inject
    private CommandeRepository commandeRepository;

    @Inject
    private LivraisonSearchRepository livraisonSearchRepository;

    @Inject
    private CommandeSearchRepository commandeSearchRepository;

    /**
     * Save a livraison.
     *
     * @param livraison the entity to save
     * @return the persisted entity
     */
    public Livraison save(Livraison livraison) {
        log.debug("Request to save Livraison : {}", livraison);

        Commande commande = livraison.getCommande();
        commande.setStatut(miseAjourStatutCOmmande(livraison));
        Commande save = commandeRepository.save(commande);
        commandeSearchRepository.save(save);

        Livraison result = livraisonRepository.save(livraison);
        livraisonSearchRepository.save(result);
        return result;
    }

    private StatutCommande miseAjourStatutCOmmande(Livraison livraison) {

        Long quantiteCommande = livraison.getCommande().getQuantiteCommande();
        Long quantiteLivre = 0L;
        Commande commande = commandeRepository.findOne(livraison.getCommande().getId());

        Set<Livraison> livraisons = commande.getLivraisons();

        for (Livraison livraison1 : livraisons) {

            Set<Lignelivraison> lignelivraisons = livraison1.getLignelivraisons();

            for (Lignelivraison lignelivraison
                    : lignelivraisons) {
                quantiteLivre += lignelivraison.getQuantiteLotLivre();
            }
        }

        Set<Lignelivraison> lignelivraisons = livraison.getLignelivraisons();

        for (Lignelivraison lignelivraison
                : lignelivraisons) {
            quantiteLivre += lignelivraison.getQuantiteLotLivre();
        }

        if (quantiteLivre == 0L) {
            return StatutCommande.EN_COURS;
        } else if (Objects.equals(quantiteLivre, quantiteCommande)) {
            return StatutCommande.LIVREE;
        } else {
            return StatutCommande.PARTIELLEMENT_LIVREE;
        }

    }

    /**
     * Get all the livraisons.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Livraison> findAll() {
        log.debug("Request to get all Livraisons");
        List<Livraison> result = livraisonRepository.findAll();
        return result;
    }

    /**
     * Get one livraison by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Livraison findOne(Long id) {
        log.debug("Request to get Livraison : {}", id);
        Livraison livraison = livraisonRepository.findOne(id);
        return livraison;
    }

    /**
     * Delete the livraison by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Livraison : {}", id);
        livraisonRepository.delete(id);
        livraisonSearchRepository.delete(id);
    }

    /**
     * Search for the livraison corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Livraison> search(String query) {
        log.debug("Request to search Livraisons for query {}", query);
        return StreamSupport
                .stream(livraisonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Livraison> listeDesLivraisonParDate(String dateDebut, String dateFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        return livraisonRepository.findByDateLivraisonBetween(debut, fin);
    }
}
