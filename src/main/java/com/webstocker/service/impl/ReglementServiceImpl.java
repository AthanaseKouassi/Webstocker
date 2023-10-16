package com.webstocker.service.impl;

import com.webstocker.domain.Facture;
import com.webstocker.service.ReglementService;
import com.webstocker.domain.Reglement;
import com.webstocker.repository.ReglementRepository;
import com.webstocker.repository.search.ReglementSearchRepository;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * Service Implementation for managing Reglement.
 */
@Service
@Transactional
public class ReglementServiceImpl implements ReglementService{

    private final Logger log = LoggerFactory.getLogger(ReglementServiceImpl.class);

    @Inject
    private ReglementRepository reglementRepository;

    @Inject
    private ReglementSearchRepository reglementSearchRepository;

    /**
     * Save a reglement.
     *
     * @param reglement the entity to save
     * @return the persisted entity
     */
    public Reglement save(Reglement reglement) {
        log.debug("Request to save Reglement : {}", reglement);
        System.out.println("***************facture**********************");
        System.out.println(reglement.getFacture());
        System.out.println("***************facture**********************");
        Reglement result = reglementRepository.save(reglement);
//        reglementSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the reglements.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Reglement> findAll() {
        log.debug("Request to get all Reglements");
        List<Reglement> result = reglementRepository.findAll();
        return result;
    }

    /**
     *  Get one reglement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Reglement findOne(Long id) {
        log.debug("Request to get Reglement : {}", id);
        Reglement reglement = reglementRepository.findOne(id);
        return reglement;
    }

    /**
     *  Delete the  reglement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Reglement : {}", id);
        reglementRepository.delete(id);
//        reglementSearchRepository.delete(id);
    }

    /**
     * Search for the reglement corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Reglement> search(String query) {
        log.debug("Request to search Reglements for query {}", query);
        return StreamSupport
            .stream(reglementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Reglement> getTousLesReglementDuMois(String maDate) {

        String dateDebut;
         String dateFin;

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return reglementRepository.findByDateReglementBetween(debut, fin);
    }

    @Override
    public List<Reglement> findByFacture(Facture facture) {
        return reglementRepository.findByFacture(facture);
    }
}
