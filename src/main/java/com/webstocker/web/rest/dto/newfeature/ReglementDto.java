package com.webstocker.web.rest.dto.newfeature;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReglementDto {
    private Long id;
    private LocalDate dateReglement;
    private Long montantReglement;
    private Long idFacture;
    private Long idProduit;
}
