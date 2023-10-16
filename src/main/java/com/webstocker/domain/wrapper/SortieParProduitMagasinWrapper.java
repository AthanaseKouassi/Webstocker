package com.webstocker.domain.wrapper;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;

/**
 *
 * @author Athanase
 */
public class SortieParProduitMagasinWrapper {
    
    BonDeSortie bonDeSortie;
    Produit produit;
    Magasin magasin;
    String nomClient;

    public BonDeSortie getBonDeSortie() {
        return bonDeSortie;
    }

    public void setBonDeSortie(BonDeSortie bonDeSortie) {
        this.bonDeSortie = bonDeSortie;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
    
    
}
