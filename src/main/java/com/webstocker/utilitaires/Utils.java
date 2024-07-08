package com.webstocker.utilitaires;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

}
