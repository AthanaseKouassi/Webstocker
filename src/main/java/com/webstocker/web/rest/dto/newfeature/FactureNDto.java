package com.webstocker.web.rest.dto.newfeature;

import com.webstocker.domain.enumeration.newfeature.StatutFacture;

import java.time.LocalDate;

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public void setNomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public Long getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Long montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Long getResteApayer() {
        return resteApayer;
    }

    public void setResteApayer(Long resteApayer) {
        this.resteApayer = resteApayer;
    }

    public StatutFacture getStatutFacture() {
        return statutFacture;
    }

    public void setStatutFacture(StatutFacture statutFacture) {
        this.statutFacture = statutFacture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public Long getIdBonDeSortie() {
        return idBonDeSortie;
    }

    public void setIdBonDeSortie(Long idBonDeSortie) {
        this.idBonDeSortie = idBonDeSortie;
    }
}
