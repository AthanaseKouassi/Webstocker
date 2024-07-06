package com.webstocker.domain.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Athanase
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventaireWrapper {

    private String nomMagasin;
    private String nomProduit;
    private String dateInventaire;
    private Long quantiteTheorique;
    private String localiteMagasin;
    private Long stockInitial;
    private Long arrivage;
    private Long vente;
    private Long promo;
    private Long perteAbime;

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

    public Long getArrivage() {
        return arrivage;
    }

    public void setArrivage(Long arrivage) {
        this.arrivage = arrivage;
    }

    public Long getVente() {
        return vente;
    }

    public void setVente(Long vente) {
        this.vente = vente;
    }

    public Long getPromo() {
        return promo;
    }

    public void setPromo(Long promo) {
        this.promo = promo;
    }

    public Long getPerteAbime() {
        return perteAbime;
    }

    public void setPerteAbime(Long perteAbime) {
        this.perteAbime = perteAbime;
    }
}
