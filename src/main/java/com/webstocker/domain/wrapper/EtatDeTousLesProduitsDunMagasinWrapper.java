package com.webstocker.domain.wrapper;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;

/**
 *
 * @author Athanase
 */
public class EtatDeTousLesProduitsDunMagasinWrapper {
    Produit produit;
    Magasin magasin;
    Inventaire inventaire;
    Long quantiteTotalEnStock;
    Long quantiteTransfert;
    Long quantiteVendue;
    Long quantitePromo;
    Long quantitePerte;
    Long quantiteTransfertRecuMagasin;
    Long stockReel;
    Long ecart;
    Long stockInitial;
    Long arrivage;

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

    public Long getQuantiteTotalEnStock() {
        return quantiteTotalEnStock;
    }

    public void setQuantiteTotalEnStock(Long quantiteTotalEnStock) {
        this.quantiteTotalEnStock = quantiteTotalEnStock;
    }

    public Long getQuantiteTransfert() {
        return quantiteTransfert;
    }

    public void setQuantiteTransfert(Long quantiteTransfert) {
        this.quantiteTransfert = quantiteTransfert;
    }

    public Long getQuantiteVendue() {
        return quantiteVendue;
    }

    public void setQuantiteVendue(Long quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }

    public Long getQuantitePromo() {
        return quantitePromo;
    }

    public void setQuantitePromo(Long quantitePromo) {
        this.quantitePromo = quantitePromo;
    }

    public Long getQuantitePerte() {
        return quantitePerte;
    }

    public void setQuantitePerte(Long quantitePerte) {
        this.quantitePerte = quantitePerte;
    }

    public Long getQuantiteTransfertRecuMagasin() {
        return quantiteTransfertRecuMagasin;
    }

    public void setQuantiteTransfertRecuMagasin(Long quantiteTransfertRecuMagasin) {
        this.quantiteTransfertRecuMagasin = quantiteTransfertRecuMagasin;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    public Long getStockReel() {
        return stockReel;
    }

    public void setStockReel(Long stockReel) {
        this.stockReel = stockReel;
    }

    public Long getEcart() {
        return ecart;
    }

    public void setEcart(Long ecart) {
        this.ecart = ecart;
    }

    public Long getStockInitial() {
        return stockInitial;
    }

    public void setStockInitial(Long stockInitial) {
        this.stockInitial = stockInitial;
    }

    public Long getArrivage() {
        return arrivage;
    }

    public void setArrivage(Long arrivage) {
        this.arrivage = arrivage;
    }
    
    
    
    
}
