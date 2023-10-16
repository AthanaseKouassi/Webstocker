package com.webstocker.reports;

import com.webstocker.domain.Produit;

import java.util.List;

/**
 * Created by komi on 13/05/16.
 */
public class ProduitWrapper {
    
    String nomProduit;
    String categorie;    

    public ProduitWrapper(Produit produit) {
        this.categorie= produit.getCategorie().getNomCategorie();
        this.nomProduit=produit.getNomProduit();
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    
}
