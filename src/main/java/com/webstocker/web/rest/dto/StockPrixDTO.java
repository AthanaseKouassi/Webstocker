package com.webstocker.web.rest.dto;

import java.math.BigDecimal;

/**
 * Created by komi on 10/09/16.
 */
public class StockPrixDTO {
    BigDecimal prixUnitaire;

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}
