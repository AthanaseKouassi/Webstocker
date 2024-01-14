package com.webstocker.web.rest.dto.newfeature;

import com.webstocker.domain.Reglement;
import com.webstocker.domain.enumeration.newfeature.StatutFacture;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FactureNDto {
    private Long id;
    private LocalDate dateFacture;
    private Integer valeurRemise;
    private Integer delaiPaiement;
    private LocalDate dateLimitePaiement;
    private StatutFacture statutFacture;
    private String numero;
    private Long idClient;
    private List<Reglement> reglements;
    private Long idBonDeSortie;


}
