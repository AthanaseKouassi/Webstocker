package com.webstocker.web.rest.dto.newfeature;

import java.time.LocalDate;

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
    private String telClient;

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getPrixDeVente() {
        return prixDeVente;
    }

    public void setPrixDeVente(Long prixDeVente) {
        this.prixDeVente = prixDeVente;
    }

    public String getLibelleCategorie() {
        return libelleCategorie;
    }

    public void setLibelleCategorie(String libelleCategorie) {
        this.libelleCategorie = libelleCategorie;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getMontantRegle() {
        return montantRegle;
    }

    public void setMontantRegle(Long montantRegle) {
        this.montantRegle = montantRegle;
    }

    public Long getResteApayer() {
        return resteApayer;
    }

    public void setResteApayer(Long resteApayer) {
        this.resteApayer = resteApayer;
    }

    public String getTelClient() {
        return telClient;
    }

    public void setTelClient(String telClient) {
        this.telClient = telClient;
    }
}
