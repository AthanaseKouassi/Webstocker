package com.webstocker.web.rest.dto;

import com.webstocker.domain.Produit;

/**
 *
 * @author Athanase
 */
public class EtatReconciliationDTO {
    Long quantiteVente;
    Long quantiteTotal;
    Long quantitePromotion;
    Long quantiteCredit;
    Long quantiteCash;
    Long sommeCredit;
    Long sommeCash;
    Produit produit;

    public EtatReconciliationDTO() {
    }

    public Long getQuantiteVente() {
        return quantiteVente;
    }

    public void setQuantiteVente(Long quantiteVente) {
        this.quantiteVente = quantiteVente;
    }

    public Long getQuantiteTotal() {
        return quantiteTotal;
    }

    public void setQuantiteTotal(Long quantiteTotal) {
        this.quantiteTotal = quantiteTotal;
    }

    public Long getQuantitePromotion() {
        return quantitePromotion;
    }

    public void setQuantitePromotion(Long quantitePromotion) {
        this.quantitePromotion = quantitePromotion;
    }

    public Long getQuantiteCredit() {
        return quantiteCredit;
    }

    public void setQuantiteCredit(Long quantiteCredit) {
        this.quantiteCredit = quantiteCredit;
    }

    public Long getQuantiteCash() {
        return quantiteCash;
    }

    public void setQuantiteCash(Long quantiteCash) {
        this.quantiteCash = quantiteCash;
    }

    public Long getSommeCredit() {
        return sommeCredit;
    }

    public void setSommeCredit(Long sommeCredit) {
        this.sommeCredit = sommeCredit;
    }

    public Long getSommeCash() {
        return sommeCash;
    }

    public void setSommeCash(Long sommeCash) {
        this.sommeCash = sommeCash;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    
}
