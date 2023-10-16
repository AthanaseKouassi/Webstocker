package com.webstocker.domain.wrapper;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.Prix;
import com.webstocker.domain.Produit;

/**
 *
 * @author Athanase
 */
public class EtatStockGlobalAimasWrapper {
    Produit produit;
    Long quantiteVendue;
    Long quantitePromotion;
    Long quantitePerte;
    Long quantiteTransfert;
    Long quantiteInitial;
    Long quantiteFinale;
    Long valeurVente;
    Long quantiteLivre;
    Long quantiteTotalTransfert;
    Long quantiteTotalEnStock;
    Bailleur bailleur;
    Prix prix;

    public Long getQuantiteTotalEnStock() {
        return quantiteTotalEnStock;
    }

    public void setQuantiteTotalEnStock(Long quantiteTotalEnStock) {
        this.quantiteTotalEnStock = quantiteTotalEnStock;
    }

    public Long getQuantiteTotalTransfert() {
        return quantiteTotalTransfert;
    }

    public void setQuantiteTotalTransfert(Long quantiteTotalTransfert) {
        this.quantiteTotalTransfert = quantiteTotalTransfert;
    }    
    
    public Long getQuantiteLivre() {
        return quantiteLivre;
    }

    public void setQuantiteLivre(Long quantiteLivre) {
        this.quantiteLivre = quantiteLivre;
    }
   
    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Long getQuantiteVendue() {
        return quantiteVendue;
    }

    public void setQuantiteVendue(Long quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }

    public Long getQuantitePromotion() {
        return quantitePromotion;
    }

    public void setQuantitePromotion(Long quantitePromotion) {
        this.quantitePromotion = quantitePromotion;
    }

    public Long getQuantitePerte() {
        return quantitePerte;
    }

    public void setQuantitePerte(Long quantitePerte) {
        this.quantitePerte = quantitePerte;
    }

    public Long getQuantiteInitial() {
        return quantiteInitial;
    }

    public void setQuantiteInitial(Long quantiteInitial) {
        this.quantiteInitial = quantiteInitial;
    }

    public Long getQuantiteFinale() {
        return quantiteFinale;
    }

    public void setQuantiteFinale(Long quantiteFinale) {
        this.quantiteFinale = quantiteFinale;
    }

    public Long getValeurVente() {
        return valeurVente;
    }

    public void setValeurVente(Long valeurVente) {
        this.valeurVente = valeurVente;
    }

    public Long getQuantiteTransfert() {
        return quantiteTransfert;
    }

    public void setQuantiteTransfert(Long quantiteTransfert) {
        this.quantiteTransfert = quantiteTransfert;
    }

    public Bailleur getBailleur() {
        return bailleur;
    }

    public void setBailleur(Bailleur bailleur) {
        this.bailleur = bailleur;
    }

    public Prix getPrix() {
        return prix;
    }

    public void setPrix(Prix prix) {
        this.prix = prix;
    }
     
        
    
}
