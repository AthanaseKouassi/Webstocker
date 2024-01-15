package com.webstocker.web.rest.dto.newfeature;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreanceDto {
    private Long idFacture;
    private Long idProduit;
    private Long idUser;
    private Long idClient;
    private String nomClient;
    private String nomUser;
    private LocalDate dateFacture;
    private String numero;
    private Long prixDeVente;
    private String libelleCategorie;
    private String nomProduit;
    private Long montantRegle;
    private Long resteApayer;

}
