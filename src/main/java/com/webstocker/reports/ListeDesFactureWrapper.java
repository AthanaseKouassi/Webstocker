/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ListeDesFactureWrapper {
    String nomClient;
    String dateFacture;
    Long numerofacture;
    Long montantFacture;
//    BigDecimal montantFacture;
    Long numeroFacture;
//    BigDecimal prixVente;
    Long quantite;
    String numeroBondeSortie;
    String numeroFactureNormalise ;

    public ListeDesFactureWrapper(LigneBonDeSortie lignebs){
        this.nomClient = lignebs.getBonDeSortie().getFacture().getClient().getNomClient();
        this.dateFacture = lignebs.getBonDeSortie().getFacture().getDateFacture().toString();
        this.numeroFacture = lignebs.getBonDeSortie().getFacture().getId();
        this.montantFacture = lignebs.getPrixDeVente();
//        this.prixVente = lignebs.getPrixVente();
        this.quantite = lignebs.getQuantite();
        this.numeroBondeSortie = lignebs.getBonDeSortie().getNumero();
        this.numeroFactureNormalise = lignebs.getBonDeSortie().getNumeroFactureNormalise()!= null? lignebs.getBonDeSortie().getNumeroFactureNormalise().toString():" ";
    }

    public String getNumeroBondeSortie() {
        return numeroBondeSortie;
    }

    public void setNumeroBondeSortie(String numeroBondeSortie) {
        this.numeroBondeSortie = numeroBondeSortie;
    }

    public String getNumeroFactureNormalise() {
        return numeroFactureNormalise;
    }

    public void setNumeroFactureNormalise(String numeroFactureNormalise) {
        this.numeroFactureNormalise = numeroFactureNormalise;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Long getNumerofacture() {
        return numerofacture;
    }

    public void setNumerofacture(Long numerofacture) {
        this.numerofacture = numerofacture;
    }

    public Long getMontantFacture() {
        return montantFacture;
    }

    public void setMontantFacture(Long montantFacture) {
        this.montantFacture = montantFacture;
    }

    public Long getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(Long numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

//    public BigDecimal getPrixVente() {
//        return prixVente;
//    }
//
//    public void setPrixVente(BigDecimal prixVente) {
//        this.prixVente = prixVente;
//    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }


}
