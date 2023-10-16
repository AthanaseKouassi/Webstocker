package com.webstocker.service.impl;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.service.ObjectifsService;
import com.webstocker.domain.Objectifs;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ObjectifsRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.repository.search.ObjectifsSearchRepository;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
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
 * Service Implementation for managing Objectifs.
 */
@Service
@Transactional
public class ObjectifsServiceImpl implements ObjectifsService {

    private final Logger log = LoggerFactory.getLogger(ObjectifsServiceImpl.class);

    @Inject
    private ObjectifsRepository objectifsRepository;

    @Inject
    private ObjectifsSearchRepository objectifsSearchRepository;

    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    private ProduitRepository produitRepository;

    /**
     * Save a objectifs.
     *
     * @param objectifs the entity to save
     * @return the persisted entity
     */
    public Objectifs save(Objectifs objectifs) {
        log.debug("Request to save Objectifs : {}", objectifs);
        Objectifs result = objectifsRepository.save(objectifs);
        objectifsSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the objectifs.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Objectifs> findAll() {
        log.debug("Request to get all Objectifs");
        List<Objectifs> result = objectifsRepository.findAll();
        return result;
    }

    /**
     * Get one objectifs by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Objectifs findOne(Long id) {
        log.debug("Request to get Objectifs : {}", id);
        Objectifs objectifs = objectifsRepository.findOne(id);
        return objectifs;
    }

    /**
     * Delete the objectifs by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Objectifs : {}", id);
        objectifsRepository.delete(id);
        objectifsSearchRepository.delete(id);
    }

    /**
     * Search for the objectifs corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Objectifs> search(String query) {
        log.debug("Request to search Objectifs for query {}", query);
        return StreamSupport
                .stream(objectifsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Objectifs> lesObjectifsAMettreAjour() {
        return objectifsRepository.findByQuantiteObtenuIsNull();
    }

    @Override
    public List<Objectifs> lesTauxAtteinteObjectifs() {

        String maDate;
        String dateDebutAnnee;
        String dateFinAnnee;

        PremierEtDernierJourDuMois dateUtilitaire = new PremierEtDernierJourDuMois();

        dateDebutAnnee = dateUtilitaire.getDateDebutAnnee();
        dateFinAnnee = dateUtilitaire.getDateFinAnnee();

        return objectifsRepository.findAllTauxParProduit(dateDebutAnnee, dateFinAnnee);
    }

    @Override
    public List<Objectifs> tauxAtteinteObjectifsParMoisEtParProduit() {
        String maDate;
        String dateDebutAnnee;
        String dateFinAnnee;

        PremierEtDernierJourDuMois dateUtilitaire = new PremierEtDernierJourDuMois();

        dateDebutAnnee = dateUtilitaire.getDateDebutAnnee();
        dateFinAnnee = dateUtilitaire.getDateFinAnnee();

        return objectifsRepository.findAllTauxParMoisetParProduit(dateDebutAnnee, dateFinAnnee);
    }

    @Override
    public List<Objectifs> tauxAtteinteObjectifsParProduitPourUnMois(String maDate) {
        int numeroMois = 0;
        String dateDebut;
        String dateFin;

        PremierEtDernierJourDuMois dateUtilitaire = new PremierEtDernierJourDuMois();

        numeroMois = dateUtilitaire.getMois(maDate);
        System.out.println("Le NUMERO DU MOIS :" + numeroMois);
        dateDebut = dateUtilitaire.getDateDebutAnnee();
        dateFin = dateUtilitaire.getDateFinAnnee();

        return objectifsRepository.findAllTauxParProduitPourUnMois(dateDebut, dateFin, numeroMois);
    }

    @Override
    public List<Objectifs> miseaJourTauxAtteinte(int quantiteVendue) {
        String nomProduit;
        List<Objectifs> tabmaj = null;
        tabmaj = objectifsRepository.findByQuantiteObtenuIsNull();

        return null;
    }

    @Transactional(readOnly = true)
    public List<Objectifs> getObjectifsDuMois(String madate) {

        String dateDebut;
        String dateFin;

        PremierEtDernierJourDuMois dateUtilitaire = new PremierEtDernierJourDuMois();

        dateDebut = dateUtilitaire.getDateDebutDuMois(madate);
        dateFin = dateUtilitaire.getDateFinDuMois(madate);

        //convertir string en localDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return StreamSupport
                .stream(objectifsRepository.findByPeriodeBetween(debut, fin).spliterator(), false)
                .collect(Collectors.toList());
        // return objectifsRepository.findByPeriodeBetween(debut, fin);
    }
//

    @Override
    public List<Objectifs> getMiseAJourDesTaux(String maDate) {

        String dateDebut;
        String dateFin;
        Long qteVente = 0L, qteAttendue = 0L, taux = 0L;
      

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

//        List<Objectifs> listeObjectifs = objectifsRepository.findByQuantiteObtenuIsNull();
        List<Objectifs> listeObjectifs = objectifsRepository.findByPeriodeBetween(debut, fin);
//        List<Produit> listeproduit = produitRepository.findAll();
        List<LigneBonDeSortie> ligneBonDeSortie = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);

//         Iterator<Produit> produitIterator = listeproduit.iterator();
        Iterator<Objectifs> objetIt = listeObjectifs.iterator();
        while (objetIt.hasNext()) {
            Objectifs objectifs = objetIt.next();
//            if (objectifs.getQuantiteObtenu() == null) {
                for (LigneBonDeSortie lbs : ligneBonDeSortie) {
                    if (lbs.getBonDeSortie().getTypeSortie().equals(TypeSortie.VENTE) && lbs.getLot().getProduit().getNomProduit().equals(objectifs.getProduit().getNomProduit())) {
                        qteVente += lbs.getQuantite();
                        //etatReconWrapper.setQuantiteVendue(qteVente);
                    }
                }
                qteAttendue = objectifs.getQuantiteAttendue();
                taux = (qteAttendue/ qteVente)* 100;
                objectifs.setQuantiteObtenu(qteVente);
                objectifs.setTaux(taux.toString());
                save(objectifs);
                qteVente = 0L;
                qteAttendue = 0L;
                taux = 0L;
//            }
        }
        return StreamSupport
                .stream(objectifsRepository.findByPeriodeBetween(debut, fin).spliterator(), false)
                .collect(Collectors.toList());
    }

}
