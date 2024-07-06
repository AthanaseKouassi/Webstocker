package com.webstocker.service.impl;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.repository.MagasinRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.repository.search.InventaireSearchRepository;
import com.webstocker.service.EtatStockGlobalService;
import com.webstocker.service.InventaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Inventaire.
 */
@Service
@Transactional
public class InventaireServiceImpl implements InventaireService {

    private final Logger log = LoggerFactory.getLogger(InventaireServiceImpl.class);

    @Inject
    private InventaireRepository inventaireRepository;

    @Inject
    private InventaireSearchRepository inventaireSearchRepository;

    @Inject
    private MagasinRepository magasinRepository;

    @Inject
    private ProduitRepository produitRepository;

    @Inject
    private EtatStockGlobalService etatStockGlobalService;


    @Override
    public Inventaire save(Inventaire inventaire) {
        log.debug("Request to save Inventaire : {}", inventaire);
        return inventaireRepository.save(inventaire);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventaire> findAll(Pageable pageable) {
        log.debug("Request to get all Inventaires");
        return inventaireRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Inventaire findOne(Long id) {
        log.debug("Request to get Inventaire : {}", id);
        return inventaireRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inventaire : {}", id);
        inventaireRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inventaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Inventaires for query {}", query);
        return inventaireSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public Page<Inventaire> findByMagasinAndDateInventaireBetween(String nomMagasin, String dateDuMois, Pageable pageable) {

        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDuMoisLocal = LocalDate.parse(dateDuMois, formatter);

        LocalDate startOfMonth = dateDuMoisLocal.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = dateDuMoisLocal.with(TemporalAdjusters.lastDayOfMonth());

        return inventaireRepository.findByMagasinAndDateInventaireBetween(magasin, startOfMonth, endOfMonth, pageable);
    }

    @Override
    public Inventaire retrouverUnInventaireParProduitEtMagasin(String nomProduit, String nomMagasin) {
        Magasin magasin = magasinRepository.findByNomMagasin(nomMagasin);
        Produit produit = produitRepository.findByNomProduit(nomProduit);

        return inventaireRepository.findByProduitAndMagasin(produit, magasin);
    }

    @Override
    public Page<Inventaire> getDateInventaireBetween(String dateDuMois, Pageable pageable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateDuMoisLocal = LocalDate.parse(dateDuMois, formatter);

        LocalDate startOfMonth = dateDuMoisLocal.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = dateDuMoisLocal.with(TemporalAdjusters.lastDayOfMonth());


        return inventaireRepository.findByDateInventaireBetween(startOfMonth, endOfMonth, pageable);

    }

    @Override
    public Inventaire create(Inventaire inventaire) {

        log.debug("Creer un Inventaire: {} du mois pour le produit {}", inventaire, inventaire.getProduit());

        // Extraire l'année et le mois de la date d'inventaire
        YearMonth yearMonth = YearMonth.from(inventaire.getDateInventaire());
        int year = yearMonth.getYear();
        int month = yearMonth.getMonthValue();

        // Vérifier si un inventaire existe déjà pour le produit et le mois spécifiés
        Optional<Inventaire> existingInventaire = inventaireRepository.findByMonth(year, month, inventaire.getProduit());

        if (existingInventaire.isPresent()) {
            throw new RuntimeException("Un inventaire existe déjà pour le mois spécifié.");
        }

        // Si l'inventaire n'existe pas encore pour ce mois, le sauvegarder
        return inventaireRepository.save(inventaire);

    }

}
