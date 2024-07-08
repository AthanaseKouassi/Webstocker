package com.webstocker.service.newfeature;

import com.webstocker.domain.Inventaire;
import com.webstocker.repository.InventaireRepository;
import com.webstocker.utilitaires.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public List<Inventaire> getInventaireByMonth(String dateInventaire) {

        final LocalDate startOfMonth = utils.getStartOfMonth(dateInventaire);
        final LocalDate endOfMonth = utils.getEndOfMonth(dateInventaire);
        log.info("Date début du mois: {} et date fin du mois: {}", startOfMonth, endOfMonth);

        return inventaireRepository.findByDateInventaireBetween(startOfMonth, endOfMonth);
    }

    public Map<String, List<Inventaire>> allInventairesfirstMonthToActuallyOfYear(String dateInventaire) {
        Map<String, List<Inventaire>> mapList = new HashMap<>();
        final List<LocalDate> listmonth = utils.getMonthsAndYearsUpToDate(dateInventaire);

        final LocalDate startOfMonth = utils.getStartOfMonth(dateInventaire);
        final LocalDate endOfMonth = utils.getEndOfMonth(dateInventaire);

        Locale locale = Locale.FRENCH;

        for (LocalDate month : listmonth) {
            final String monthText = month.format(DateTimeFormatter.ofPattern(PATTERN_MOIS, locale));
            final List<Inventaire> listInventaireMensuels = inventaireRepository
                .findByDateInventaireBetween(startOfMonth, endOfMonth);

            mapList.put(monthText, listInventaireMensuels);
        }

        return mapList;
    }


}
