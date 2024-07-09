package com.webstocker.utilitaires;

import com.webstocker.domain.Inventaire;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class Utils {

    public LocalDate getStartOfMonth(String dateDuMois) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.PATTERN_DATE);
        final LocalDate localDate = LocalDate.parse(dateDuMois, formatter);
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    public LocalDate getEndOfMonth(String dateDuMois) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.PATTERN_DATE);
        final LocalDate localDate = LocalDate.parse(dateDuMois, formatter);
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    public List<LocalDate> getMonthsAndYearsUpToDate(String dateInventaire) {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.PATTERN_DATE);
        final LocalDate localDate = LocalDate.parse(dateInventaire, formatter);

        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        // Ce stream génère les dates de 1 à month
        return IntStream.rangeClosed(1, month)
            .mapToObj(m -> LocalDate.of(year, m, 1)) // Conversion en localDate
            .collect(Collectors.toList());
    }

    public Long getDistributionMoyenneTrimestre(List<Inventaire> inventairesDuTrimestre) {
        if (inventairesDuTrimestre == null || inventairesDuTrimestre.isEmpty()) {
            return 0L;
        }

        // Calculer la somme des ventes pour le trimestre
        long sommeVente = inventairesDuTrimestre.stream()
            .mapToLong(Inventaire::getVente)
            .sum();
        long sommePromo = inventairesDuTrimestre.stream()
            .mapToLong(Inventaire::getPromo)
            .sum();

        // Nombre de mois dans le trimestre
        final int nombreDeMois = inventairesDuTrimestre.size();
        long moyenneTrimestrielle = 0L;

        if (nombreDeMois == 3) {
            moyenneTrimestrielle = (sommeVente + sommePromo) / nombreDeMois;
        }
        return moyenneTrimestrielle;
    }

    public Map<Integer, Long> calculerMoyennesTrimestrielles(List<Inventaire> inventaires) {
        // Regrouper les inventaires par trimestre
        Map<Integer, List<Inventaire>> inventairesParTrimestre = inventaires.stream()
            .collect(Collectors.groupingBy(inv -> (inv.getDateInventaire().getMonthValue() - 1) / 3 + 1));

        // Calculer la moyenne pour chaque trimestre
        return inventairesParTrimestre.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> getDistributionMoyenneTrimestre(entry.getValue())
            ));
    }

    public boolean lastMonthQuarter(LocalDate date) {
        int month = date.getMonthValue();
        return month == 3 || month == 6 || month == 9 || month == 12;
    }

    // Retourne le trimestre pour une date donnée
    public int getQuarter(LocalDate date) {
        int month = date.getMonthValue();
        return (month - 1) / 3 + 1;
    }

    public double arrondir(double valeur, int decimales) {
        double facteur = Math.pow(10, decimales);
        return Math.round(valeur * facteur) / facteur;
    }
}
