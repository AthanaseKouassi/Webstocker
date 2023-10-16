package com.webstocker.domain.wrapper;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;

/**
 *
 * @author Athanase
 */
public class EtatProduitParMagasinWrapper {
    Produit produit;
    Magasin magasin;
    Long quantiteTotalEnStock;
    Long quantiteTransfert;
    Long quantiteVendue;
    Long quantitePromo;
    Long quantitePerte;
    Long quantiteTransfertRecuMagasin;
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
