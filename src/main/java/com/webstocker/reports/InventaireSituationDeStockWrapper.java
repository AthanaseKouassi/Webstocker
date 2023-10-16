/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;

/**
 *
 * @author Athanase
 */
public class InventaireSituationDeStockWrapper {
     
    private String magasin;
    private String nomProduit;
    private Long quantiteVente;
    private Long quantitePromo;
    private Long quantitePerte;
    private Long quantiteTotalEnStock;
    private Long quantiteTransfere;
    private Long quantiteRecuparTransfert;
    private Long stockreel;
    private Long stockInitial;
    private Long ecart;

    public InventaireSituationDeStockWrapper(EtatDeTousLesProduitsDunMagasinWrapper etatSituation) {
        this.magasin = etatSituation.getMagasin().getNomMagasin();
        this.nomProduit =  etatSituation.getProduit()!= null? etatSituation.getProduit().getNomProduit():" ";
        this.quantiteVente = etatSituation.getQuantiteVendue();
        this.quantitePromo = etatSituation.getQuantitePromo();
        this.quantitePerte = etatSituation.getQuantitePerte();
        this.quantiteTotalEnStock = etatSituation.getQuantiteTotalEnStock();
        this.quantiteTransfere = etatSituation.getQuantiteTransfert();
        this.quantiteRecuparTransfert = etatSituation.getQuantiteTransfertRecuMagasin();
        this.stockreel = etatSituation.getStockReel();
        this.ecart = etatSituation.getEcart();
        this.stockInitial = etatSituation.getStockInitial();
    }

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getQuantiteVente() {
        return quantiteVente;
    }

    public void setQuantiteVente(Long quantiteVente) {
        this.quantiteVente = quantiteVente;
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

    public Long getQuantiteTotalEnStock() {
        return quantiteTotalEnStock;
    }

    public void setQuantiteTotalEnStock(Long quantiteTotalEnStock) {
        this.quantiteTotalEnStock = quantiteTotalEnStock;
    }

    public Long getQuantiteTransfere() {
        return quantiteTransfere;
    }

    public void setQuantiteTransfere(Long quantiteTransfere) {
        this.quantiteTransfere = quantiteTransfere;
    }

    public Long getQuantiteRecuparTransfert() {
        return quantiteRecuparTransfert;
    }

    public void setQuantiteRecuparTransfert(Long quantiteRecuparTransfert) {
        this.quantiteRecuparTransfert = quantiteRecuparTransfert;
    }

    public Long getStockreel() {
        return stockreel;
    }

    public void setStockreel(Long stockreel) {
        this.stockreel = stockreel;
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
    
    
    
}
