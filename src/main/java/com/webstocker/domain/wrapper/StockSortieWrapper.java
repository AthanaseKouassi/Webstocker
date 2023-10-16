package com.webstocker.domain.wrapper;

import com.webstocker.domain.Lot;
import com.webstocker.domain.Produit;

/**
 * Cette classe permet d'encapsuler une sortie en precisant la quantite sortie pour un produit dans un lot donné
 * Created by komi on 09/09/16.
 */
public class StockSortieWrapper {
    private Produit produit;
    private Long quantite;
    private Lot lot;

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }
}
