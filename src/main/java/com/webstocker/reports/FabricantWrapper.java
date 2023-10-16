/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.Fabricant;

/**
 *
 * @author Athanase
 */
public class FabricantWrapper {

    private String nomFabricant;
    private String paysFabricant;
    private String nomProduit;

    public FabricantWrapper(Fabricant fabricant) {
        this.nomFabricant = fabricant.getNomFabricant();
        this.paysFabricant = fabricant.getPaysFabricant();
        // TODO revoir la liste produits par fabricants
        //this.nomProduit = fabricant.getProduit().getNomProduit();
    }

    public String getNomFabricant() {
        return nomFabricant;
    }

    public void setNomFabricant(String nomFabricant) {
        this.nomFabricant = nomFabricant;
    }

    public String getPaysFabricant() {
        return paysFabricant;
    }

    public void setPaysFabricant(String paysFabricant) {
        this.paysFabricant = paysFabricant;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

}
