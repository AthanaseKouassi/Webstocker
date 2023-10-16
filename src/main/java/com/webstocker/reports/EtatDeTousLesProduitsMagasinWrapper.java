package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;

/**
 *
 * @author Athanase
 */
public class EtatDeTousLesProduitsMagasinWrapper {
    
    String magasin;
    String nomProduit;
    Long quantiteVente;
    Long quantitePromo;
    Long quantitePerte;
    Long quantiteTotalEnStock;
    Long quantiteTransfere;
    Long quantiteRecuparTransfert;
    Long quantiteArrivee;
    Long stockInitial;
    
    public EtatDeTousLesProduitsMagasinWrapper(EtatDeTousLesProduitsDunMagasinWrapper etlprod){
        this.magasin = etlprod.getMagasin().getNomMagasin();
        this.nomProduit = etlprod.getProduit()!= null? etlprod.getProduit().getNomProduit():" ";
        this.quantitePerte = etlprod.getQuantitePerte();
        this.quantitePromo = etlprod.getQuantitePromo();
        this.quantiteRecuparTransfert = etlprod.getQuantiteTransfertRecuMagasin();
        this.quantiteTransfere = etlprod.getQuantiteTransfert();
        this.quantiteVente = etlprod.getQuantiteVendue();
        this.quantiteTotalEnStock = etlprod.getQuantiteTotalEnStock();
        this.quantiteArrivee = etlprod.getArrivage();
        this.stockInitial = etlprod.getStockInitial();
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

    public Long getQuantiteArrivee() {
        return quantiteArrivee;
    }

    public void setQuantiteArrivee(Long quantiteArrivee) {
        this.quantiteArrivee = quantiteArrivee;
    }

    public Long getStockInitial() {
        return stockInitial;
    }

    public void setStockInitial(Long stockInitial) {
        this.stockInitial = stockInitial;
    }
    
    
}
