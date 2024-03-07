package com.webstocker.web.rest.dto.newfeature;

import com.webstocker.domain.enumeration.newfeature.StatutFacture;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FactureNDto {
    private Long id;
    private LocalDate dateFacture;
    private String nomCommercial;
    private String nomClient;
    private Long montantTotal;
    private Long resteApayer;
    private StatutFacture statutFacture;
    private String numero;
    private Long idClient;
    private Long idBonDeSortie;


}
