/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Andre Kouame
 */
public class Creance {
    
    private String nomClient;
    
    private LocalDate dateFacture;
    
//    private Long montantFacture;
    
    private BigDecimal montantFacture;
    
    private Long numFacture;

    public Creance(Long numFacture,String nomClient, LocalDate dateFacture, BigDecimal montantFacture) {
        this.nomClient = nomClient;
        this.dateFacture = dateFacture;
        this.montantFacture = montantFacture;
        this.numFacture = numFacture;
    }

    public Creance() {
    }
    
    

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public BigDecimal getMontantFacture() {
        return montantFacture;
    }
//    public Long getMontantFacture() {
//        return montantFacture;
//    }

    public void setMontantFacture(BigDecimal montantFacture) {
        this.montantFacture = montantFacture;
    }
//    public void setMontantFacture(Long montantFacture) {
//        this.montantFacture = montantFacture;
//    }
    
    
}
