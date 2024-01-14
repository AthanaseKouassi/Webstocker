package com.webstocker.web.rest.dto.newfeature;

import java.time.LocalDate;

public class CreanceDto {
    private Long idFacture;
    private Long idProduit;
    private Long idUser;
    private Long idClient;
    private String nomClient;
    private String nomUser;
    private String nomProduit;
    private LocalDate dateFacture;
    private String numero;
    private Long prixDeVente;
    private Long resteApayer;
    private String libelleCategorie;

}
