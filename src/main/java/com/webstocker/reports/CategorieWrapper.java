/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.Categorie;

/**
 *
 * @author Athanase
 */
public class CategorieWrapper {
    String nomCategorie;
    
    public CategorieWrapper(Categorie categorie){
        this.nomCategorie = categorie.getNomCategorie();
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }
    
    
}
