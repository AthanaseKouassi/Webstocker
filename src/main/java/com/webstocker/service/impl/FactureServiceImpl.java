package com.webstocker.service.impl;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;
import com.webstocker.domain.enumeration.newfeature.StatutFacture;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.search.FactureSearchRepository;
import com.webstocker.service.FactureService;
import com.webstocker.utilitaires.Constantes;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Facture.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureServiceImpl.class);
    private final String PATTERN_DATE = "yyyy-MM-dd";
    @Inject
    private FactureRepository factureRepository;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private FactureSearchRepository factureSearchRepository;

    public Facture save(Facture facture) {
        log.debug("Request to save Facture : {}", facture);
        Facture result = factureRepository.save(facture);
        factureSearchRepository.save(result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Facture> findAll() {
        log.debug("Request to get all Factures");
        List<Facture> result = factureRepository.findAll();
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Facture> findFactureByDate(LocalDate dateDebut, LocalDate dateFin) {
        List<Facture> result = factureRepository.findFactureByDate(dateDebut, dateFin);
        return result;
    }

    @Override
    public List<Facture> findAllCreancesThirtyDayAgo(LocalDate localDate, Integer critere) {
        log.debug("Request to get all Creances");
        String query = "SELECT NEW com.webstocker.domain.Creance(f.id,f.client.nomClient, f.dateFacture, SUM(l.quantite * l.prixVente))"
            + " FROM Facture f JOIN f.bonDeSortie b JOIN b.ligneBonDeSorties l "
            + " WHERE f.reglements IS EMPTY";
        LocalDate locDate = null;
        List<Facture> result = null;
        switch (critere) {
            case 4:
            case 2:
                if (critere == 2) {
                    locDate = localDate.minusDays(Constantes.THIRTYDAYS);
                    System.out.println(locDate);
                } else {
                    locDate = localDate.minusDays(Constantes.NINETYDAYS);
                }
                query += " AND f.dateFacture =:localDate GROUP BY f.id";
                result = em.createQuery(query)
                    .setParameter("localDate", locDate)
                    .getResultList();
                break;
            case 1:
            case 5:
                String comparator;
                if (critere == 1) {
                    comparator = ">";
                    locDate = localDate.minusDays(Constantes.THIRTYDAYS);
                } else {
                    comparator = "<";
                    locDate = localDate.minusDays(Constantes.NINETYDAYS);
                }
                query += " AND f.dateFacture " + comparator + ":localDate AND f.dateFacture<=:localDate1 GROUP BY f.id";
                result = em.createQuery(query)
                    .setParameter("localDate", locDate)
                    .setParameter("localDate1", localDate)
                    .getResultList();
                break;
            case 3:
                locDate = localDate.minusDays(Constantes.THIRTYDAYS);
                LocalDate locDate1 = localDate.minusDays(Constantes.NINETYDAYS);
                System.out.println("Date debut " + locDate1);
                System.out.println("Date fin " + locDate);
                query += " AND f.dateFacture BETWEEN :localDate1 AND :localDate GROUP BY f.id";
                result = em.createQuery(query)
                    .setParameter("localDate", locDate)
                    .setParameter("localDate1", locDate1)
                    .getResultList();
                break;
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Facture findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findOne(id);

    }

    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.delete(id);
        factureSearchRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Facture> search(String query) {
        log.debug("Request to search Factures for query {}", query);
        return StreamSupport
            .stream(factureSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    @Override
    public List<Facture> findFactureNonReglees(Long id) {
        return factureRepository.findFactureNonReglees(id);
    }

    @Override
    public List<Facture> findFisrtFacture(Long id) {
        return factureRepository.findFisrtFacture(id);
    }

    @Override
    public List<Facture> getFatureAregleeParPeriode(String maDate) {
        String dateDebut;
        String dateFin;

        PremierEtDernierJourDuMois premierEtDernier = new PremierEtDernierJourDuMois(maDate);
        dateDebut = premierEtDernier.getDateDebutDuMois(maDate);
        dateFin = premierEtDernier.getDateFinDuMois(maDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);

        return factureRepository.findByDateLimitePaiementBetween(debut, fin);
    }

    @Override
    public List<Facture> getFactureParPeriode(String dateDebut, String dateFin) {
        return factureRepository.findAllFactureByPeriode(dateDebut, dateFin);
    }

    @Override
    public Facture getFactureParBonDeSortie(BonDeSortie bonDesortie) {
        return factureRepository.findByBonDeSortie(bonDesortie);
    }

//    @Override
//    public List<Facture> getFactureNonRegleeParNumero(String numero) {
//        return null;
//    }

    @Override
    public List<Facture> getFactureNonSoldeParPeriode(String dateDebut, String dateFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        return factureRepository.findByStatutFactureAndDateFactureBetween(StatutFacture.NON_SOLDE, debut, fin);
    }

    @Override
    public List<Facture> getFactureNonSoldeParNumero(String numero) {
        return factureRepository.findByStatutFactureAndNumero(StatutFacture.NON_SOLDE, numero);
    }

    @Override
    public List<Facture> getFactureCreance(int categorieCreance) {
        if (categorieCreance == 1) {
            // Créances moins de 30 jours
            return factureRepository.getFactureMoinsdetrentejour();
        }
        if (categorieCreance == 2) {
            // Créances entre 30 et 45 jours
            return factureRepository.getFactureEntreTrenteEtQuaranteCinqJours();
        }
        if (categorieCreance == 3) {
            // Créances plus de 45 jours
            return factureRepository.getFacturePlusDeQuaranteCinqJours();
        }
        throw new IllegalArgumentException("La catégorie creance :: " + categorieCreance + " n'est pas supportée");
    }


    public List<Facture> listFactureNonRegleeParPeriode(String numero, String dateDebut, String dateFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
        LocalDate debut = LocalDate.parse(dateDebut, formatter);
        LocalDate fin = LocalDate.parse(dateFin, formatter);
        if (!numero.isEmpty()) {
            return factureRepository.findByStatutFactureAndNumero(StatutFacture.NON_SOLDE, numero);
        } else {
            return factureRepository.findByDateLimitePaiementBetween(debut, fin);
        }

    }


}
