package com.webstocker.reports;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;

/**
 *
 * @author Athanase
 */
public class EtatStockGlobalWrapper {

    String produit;
    Long quantiteVendue;
    Long quantitePromotion;
    Long quantitePerte;
    Long quantiteTransfert;
    Long quantiteInitial;
    Long quantiteFinale;
    Long valeurVente;
    Long quantiteLivree;
    Long quantiteTotaleTransfert;
    Bailleur bailleur;
    Long quantiteGlobalEnStock;

    public EtatStockGlobalWrapper(EtatStockGlobalAimasWrapper etatstock) {
        this.quantiteGlobalEnStock = etatstock.getQuantiteTotalEnStock();
        this.quantiteFinale = etatstock.getQuantiteFinale();
        this.produit = etatstock.getProduit().getNomProduit();
        this.quantitePerte = etatstock.getQuantitePerte();
        this.quantiteTransfert = etatstock.getQuantiteTransfert();
        this.valeurVente = etatstock.getValeurVente();
        this.quantiteVendue = etatstock.getQuantiteVendue();
        this.quantitePromotion = etatstock.getQuantitePromotion();
        this.quantiteInitial = etatstock.getQuantiteInitial();
        this.quantiteLivree = etatstock.getQuantiteLivre();
        this.quantiteTotaleTransfert = etatstock.getQuantiteTotalTransfert();
    }

    public Long getQuantiteLivree() {
        return quantiteLivree;
    }

    public void setQuantiteLivree(Long quantiteLivree) {
        this.quantiteLivree = quantiteLivree;
    }

    public Long getQuantiteTotaleTransfert() {
        return quantiteTotaleTransfert;
    }

    public void setQuantiteTotaleTransfert(Long quantiteTotaleTransfert) {
        this.quantiteTotaleTransfert = quantiteTotaleTransfert;
    }

    public Long getQuantiteGlobalEnStock() {
        return quantiteGlobalEnStock;
    }

    public void setQuantiteGlobalEnStock(Long quantiteGlobalEnStock) {
        this.quantiteGlobalEnStock = quantiteGlobalEnStock;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
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

    public Long getQuantiteTransfert() {
        return quantiteTransfert;
    }

    public void setQuantiteTransfert(Long quantiteTransfert) {
        this.quantiteTransfert = quantiteTransfert;
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

    public Bailleur getBailleur() {
        return bailleur;
    }

    public void setBailleur(Bailleur bailleur) {
        this.bailleur = bailleur;
    }

}
