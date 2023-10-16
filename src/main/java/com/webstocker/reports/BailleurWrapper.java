/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.Bailleur;

/**
 *
 * @author Athanase
 */
public class BailleurWrapper {

    private String nomBailleur;

    public BailleurWrapper(Bailleur bailleur) {
        this.nomBailleur = bailleur.getNomBailleur();
    }

    public String getNomBailleur() {
        return nomBailleur;
    }

    public void setNomBailleur(String nomBailleur) {
        this.nomBailleur = nomBailleur;
    }

    
}
