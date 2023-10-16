package com.webstocker.domain.wrapper;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.User;

/**
 *
 * @author Athanase
 */
public class EtatAgentWrapper {
    Produit produit;
    User user;
    Magasin magasin;
    Long quantiteDetenue;
    Long quantiteSortie;
    Long quantiteRecue;  
    Long quantiteInitiale;

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Long getQuantiteDetenue() {
        return quantiteDetenue;
    }

    public void setQuantiteDetenue(Long quantiteDetenue) {
        this.quantiteDetenue = quantiteDetenue;
    }

    public Long getQuantiteSortie() {
        return quantiteSortie;
    }

    public void setQuantiteSortie(Long quantiteSortie) {
        this.quantiteSortie = quantiteSortie;
    }

    public Long getQuantiteRecue() {
        return quantiteRecue;
    }

    public void setQuantiteRecue(Long quantiteRecue) {
        this.quantiteRecue = quantiteRecue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getQuantiteInitiale() {
        return quantiteInitiale;
    }

    public void setQuantiteInitiale(Long quantiteInitiale) {
        this.quantiteInitiale = quantiteInitiale;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }
        
    
}
