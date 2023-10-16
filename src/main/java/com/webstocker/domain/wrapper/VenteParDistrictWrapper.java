package com.webstocker.domain.wrapper;

import com.webstocker.domain.BonDeSortie;

/**
 *
 * @author Athanase
 */
public class VenteParDistrictWrapper {
    String ville;
    Long quantiteVendue;
    BonDeSortie bonDeSortie;
    String produit;

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }
    
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Long getQuantiteVendue() {
        return quantiteVendue;
    }

    public void setQuantiteVendue(Long quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }

    public BonDeSortie getBonDeSortie() {
        return bonDeSortie;
    }

    public void setBonDeSortie(BonDeSortie bonDeSortie) {
        this.bonDeSortie = bonDeSortie;
    }
    
    
}
