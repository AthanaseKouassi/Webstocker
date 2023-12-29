package com.webstocker.web.rest.dto.newfeature;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DetailFactureDto {
    private BigDecimal resteApaye;
    private String nomProduit;
    private BigDecimal prixDeVente;
    private Long montantRegle;
    private Long idProduit;
    private Long idFacture;
    private LocalDate dateReglement;

}
