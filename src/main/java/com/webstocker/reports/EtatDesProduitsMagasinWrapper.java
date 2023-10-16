package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatProduitParMagasinWrapper;

/**
 *
 * @author Athanase
 */
public class EtatDesProduitsMagasinWrapper {
    
    String magasin;
    String nomProduit;
    Long quantiteVente;
    Long quantitePromo;
    Long quantitePerte;
    Long quantiteTotalEnStock;
    Long quantiteTransfere;
    Long quantiteRecuparTransfert;
    Long quantiteStockInitial ;
    Long quantiteArrivage;
    
    public EtatDesProduitsMagasinWrapper(EtatProduitParMagasinWrapper etatProduitMgasin){
//        this.magasin = etatProduitMgasin.getMagasin().getNomMagasin().equals("MAGASIN CENTRAL")?etatProduitMgasin.getMagasin().getNomMagasin()+"*":etatProduitMgasin.getMagasin().getNomMagasin();
        this.magasin = etatProduitMgasin.getMagasin().getNomMagasin();
        this.nomProduit = etatProduitMgasin.getProduit()!= null? etatProduitMgasin.getProduit().getNomProduit():" ";
        this.quantitePerte = etatProduitMgasin.getQuantitePerte();
        this.quantitePromo = etatProduitMgasin.getQuantitePromo();
        this.quantiteRecuparTransfert = etatProduitMgasin.getQuantiteTransfertRecuMagasin();
        this.quantiteTransfere = etatProduitMgasin.getQuantiteTransfert();
        this.quantiteVente = etatProduitMgasin.getQuantiteVendue();
        this.quantiteTotalEnStock = etatProduitMgasin.getQuantiteTotalEnStock();
        this.quantiteStockInitial = etatProduitMgasin.getStockInitial();
        this.quantiteArrivage = etatProduitMgasin.getArrivage();
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
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

    public Long getQuantiteStockInitial() {
        return quantiteStockInitial;
    }

    public void setQuantiteStockInitial(Long quantiteStockInitial) {
        this.quantiteStockInitial = quantiteStockInitial;
    }

    public Long getQuantiteArrivage() {
        return quantiteArrivage;
    }

    public void setQuantiteArrivage(Long quantiteArrivage) {
        this.quantiteArrivage = quantiteArrivage;
    }
    
    
    
}
