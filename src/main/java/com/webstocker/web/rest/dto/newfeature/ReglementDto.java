package com.webstocker.web.rest.dto.newfeature;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReglementDto {
    private Long id;
    private LocalDate dateReglement;
    private Long montantReglement;
    private Long idFacture;
    private Long idProduit;
}
