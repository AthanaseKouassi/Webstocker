package com.webstocker.service.newfeature;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Produit;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.utilitaires.Constantes;
import com.webstocker.utilitaires.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Transactional
@Service
public class InventaireNewService {

    private static final String PATTERN_MOIS = "MMMM yyyy";
    @Inject
    private InventaireRepository inventaireRepository;
    @Inject
    private Utils utils;
    @Inject
    private ProduitRepository produitRepository;

    public List<Inventaire> getInventaireByMonth(String dateInventaire) {

        final LocalDate startOfMonth = utils.getStartOfMonth(dateInventaire);
        final LocalDate endOfMonth = utils.getEndOfMonth(dateInventaire);
        log.info("Date début du mois: {} et date fin du mois: {}", startOfMonth, endOfMonth);

        return inventaireRepository.findByDateInventaireBetween(startOfMonth, endOfMonth);
    }

    public Map<String, List<Inventaire>> allInventairesfirstMonthToActuallyOfYear(String dateInventaire) {
        Map<String, List<Inventaire>> mapList = new HashMap<>();
        final List<LocalDate> listmonth = utils.getMonthsAndYearsUpToDate(dateInventaire);

        Locale locale = Locale.FRENCH;

        for (LocalDate month : listmonth) {
            final String monthText = month.format(DateTimeFormatter.ofPattern(PATTERN_MOIS, locale));
            final List<Inventaire> listInventaireMensuels = inventaireRepository
                .findByDateInventaireBetween(month.with(TemporalAdjusters.firstDayOfMonth()),
                    month.with(TemporalAdjusters.lastDayOfMonth()));

            mapList.put(monthText, listInventaireMensuels);
        }

        return mapList;
    }


    public Map<Produit, List<Inventaire>> getAllInventaireParProduit(String dateInventaire) {

        final Map<Produit, List<Inventaire>> mapInventaire = new HashMap<>();
        final List<Produit> lstProduit = produitRepository.findAll();
        final DateTimeFormatter format = DateTimeFormatter.ofPattern(Constantes.PATTERN_DATE);
        final LocalDate dateLocalInventaire = LocalDate.parse(dateInventaire, format);
        final int year = dateLocalInventaire.getYear();

        for (Produit produit : lstProduit) {
            final List<Inventaire> inventaires = inventaireRepository.findByInventaireByYearAndProduit(year, produit);
            if (!inventaires.isEmpty()) {
                mapInventaire.put(produit, inventaires);
            }
        }
        return mapInventaire;
    }


}
