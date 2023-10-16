/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.Lot;

/**
 *
 * @author Athanase
 */
public class LotWrapper {
    String numeroLot;    
    String dateFabrication;
    String datePeremption;
    Long quantiteLot;
    String nomProduit;
    
    public LotWrapper(Lot lot){
        this.numeroLot = lot.getNumeroLot().toString();
        this.dateFabrication = lot.getDateFabrication().toString();
        this.datePeremption = lot.getDatePeremption().toString();
       this.quantiteLot = lot.getQuantiteLot();        
        this.nomProduit = lot.getProduit().getNomProduit();
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

    public String getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(String dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public String getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(String datePeremption) {
        this.datePeremption = datePeremption;
    }

    public Long getQuantiteLot() {
        return quantiteLot;
    }

    public void setQuantiteLot(Long quantiteLot) {
        this.quantiteLot = quantiteLot;
    }
    
            
}
