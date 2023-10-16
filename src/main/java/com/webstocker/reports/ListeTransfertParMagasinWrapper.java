/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

/**
 *
 * @author Athanase
 */
public class ListeTransfertParMagasinWrapper {
    
   String numeroBondeSortie;
   String dateTransfert;
   Long quantite;
    String magasinOrigine;
    String magasindestination;
    String nomProduit;
    String numeroLot;
    
    public ListeTransfertParMagasinWrapper(LigneBonDeSortie lignebondesortie){
        this.numeroBondeSortie = lignebondesortie.getBonDeSortie().getNumero();
        this.dateTransfert = lignebondesortie.getBonDeSortie().getDaateCreation().toString();
        this.quantite = lignebondesortie.getQuantite();
        this.magasinOrigine = lignebondesortie.getBonDeSortie().getMagasin()!=null? lignebondesortie.getBonDeSortie().getMagasin().getNomMagasin():"";
        this.magasindestination = lignebondesortie.getBonDeSortie().getDestination()!=null?lignebondesortie.getBonDeSortie().getDestination().getNomMagasin():"";
        this.nomProduit = lignebondesortie.getLot().getProduit().getNomProduit();
        this.numeroLot = lignebondesortie.getLot().getNumeroLot().toString();
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    public String getNumeroBondeSortie() {
        return numeroBondeSortie;
    }

    public void setNumeroBondeSortie(String numeroBondeSortie) {
        this.numeroBondeSortie = numeroBondeSortie;
    }

    public String getDateTransfert() {
        return dateTransfert;
    }

    public void setDateTransfert(String dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public String getMagasinOrigine() {
        return magasinOrigine;
    }

    public void setMagasinOrigine(String magasinOrigine) {
        this.magasinOrigine = magasinOrigine;
    }

    public String getMagasindestination() {
        return magasindestination;
    }

    public void setMagasindestination(String magasindestination) {
        this.magasindestination = magasindestination;
    }
    
    
}
