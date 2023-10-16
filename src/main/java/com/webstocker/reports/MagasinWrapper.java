/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.Magasin;

/**
 *
 * @author Athanase
 */
public class MagasinWrapper {
    String nomMagasin;
    String localite;

    public MagasinWrapper(Magasin magasin){
        this.nomMagasin = magasin.getNomMagasin();
        this.localite = magasin.getLocalite().getDisplayName();
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

//    public void setVillage(String village) {
//        this.village = village;
//    }
//
//    public void setVille(String ville) {
//        this.ville = ville;
//    }


}


