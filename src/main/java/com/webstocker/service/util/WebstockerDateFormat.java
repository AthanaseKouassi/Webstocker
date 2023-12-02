package com.webstocker.service.util;

import com.webstocker.utilitaires.WebstockerConstant;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class WebstockerDateFormat {


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(WebstockerConstant.FORMAT_DATE);

    public LocalDate convertirStingToLocalDate(String maDate) {
        return LocalDate.parse(maDate, formatter);
    }
}
