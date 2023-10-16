/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.Localite;

/**
 *
 * @author Athanase
 */
public class LocaliteWrapper {

    private String region;
    private String ville;
    private String commune;
    private String village;
    private String quartier;

    public LocaliteWrapper(Localite localite){
        this.region = localite.getCommunes().getVille().getRegion().getLibelle();
        this.ville = localite.getCommunes().getVille().getLibelle();
        this.commune = localite.getCommunes().getLibelle();
        this.village = "";
        this.quartier = localite.getNom();
    }

//    public String getAfficherSpecialNom() {
//        return afficherSpecialNom;
//    }
//
//    public void setAfficherSpecialNom(String afficherSpecialNom) {
//        this.afficherSpecialNom = afficherSpecialNom;
//    }
    
    

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

}
