
package com.webstocker.domain.wrapper;

/**
 *
 * @author Athanase
 */
public class InventaireWrapper {
    
    private String nomMagasin;
    private String nomProduit;
    private String dateInventaire;
    private Long quantiteTheorique;
    private String localiteMagasin;
    private Long stockInitial;

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(String dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public Long getQuantiteTheorique() {
        return quantiteTheorique;
    }

    public void setQuantiteTheorique(Long quantiteTheorique) {
        this.quantiteTheorique = quantiteTheorique;
    }

    public String getLocaliteMagasin() {
        return localiteMagasin;
    }

    public void setLocaliteMagasin(String localiteMagasin) {
        this.localiteMagasin = localiteMagasin;
    }

    public Long getStockInitial() {
        return stockInitial;
    }

    public void setStockInitial(Long stockInitial) {
        this.stockInitial = stockInitial;
    }
    
    
}
