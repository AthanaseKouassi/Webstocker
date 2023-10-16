package com.webstocker.domain.wrapper;

import com.webstocker.domain.Prix;
import com.webstocker.domain.Produit;
import com.webstocker.domain.User;
import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class EtatDeReconciliationWrapper {
    
    Long quantiteTotal;
    Long quantiteVendue;
    Long quantitePromotion;
    Long quantiteVenteCash;
    Long quantiteVenteCredit;
    BigDecimal sommeVenteCash;
    BigDecimal sommeVenteCredit;
    Produit produit;
    
    Long quantiteLivree;
    BigDecimal sommeRecouvree;
    BigDecimal sommeCreance30;
    BigDecimal sommeCreance30_90;
    BigDecimal sommeCreance90;
    Prix prix;
    User user;
    Long quantiteTransfert;
    Long quantitePerte;
    Long balanceOuvertureCreance;
    Long balanceClotureCreance;
    Long quantiteInitial;
    Long autreQuantiteSortie;
    Long avoir;
    BigDecimal sommeIrrecouvrable;
    
        
    
    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    public Long getQuantiteTotal() {
        return quantiteTotal;
    }

    public void setQuantiteTotal(Long quantiteTotal) {
        this.quantiteTotal = quantiteTotal;
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

    public Long getQuantiteVenteCash() {
        return quantiteVenteCash;
    }

    public void setQuantiteVenteCash(Long quantiteVenteCash) {
        this.quantiteVenteCash = quantiteVenteCash;
    }

    public Long getQuantiteVenteCredit() {
        return quantiteVenteCredit;
    }

    public void setQuantiteVenteCredit(Long quantiteVenteCredit) {
        this.quantiteVenteCredit = quantiteVenteCredit;
    }

    public BigDecimal getSommeVenteCash() {
        return sommeVenteCash;
    }

    public void setSommeVenteCash(BigDecimal sommeVenteCash) {
        this.sommeVenteCash = sommeVenteCash;
    }

    public BigDecimal getSommeVenteCredit() {
        return sommeVenteCredit;
    }

    public void setSommeVenteCredit(BigDecimal sommeVenteCredit) {
        this.sommeVenteCredit = sommeVenteCredit;
    }

    public Long getQuantiteLivree() {
        return quantiteLivree;
    }

    public void setQuantiteLivree(Long quantiteLivree) {
        this.quantiteLivree = quantiteLivree;
    }
    
    public BigDecimal getSommeCreance30() {
        return sommeCreance30;
    }

    public void setSommeCreance30(BigDecimal sommeCreance30) {
        this.sommeCreance30 = sommeCreance30;
    }

    public BigDecimal getSommeCreance30_90() {
        return sommeCreance30_90;
    }

    public void setSommeCreance30_90(BigDecimal sommeCreance30_90) {
        this.sommeCreance30_90 = sommeCreance30_90;
    }

    public BigDecimal getSommeCreance90() {
        return sommeCreance90;
    }

    public void setSommeCreance90(BigDecimal sommeCreance90) {
        this.sommeCreance90 = sommeCreance90;
    }

    public Prix getPrix() {
        return prix;
    }

    public void setPrix(Prix prix) {
        this.prix = prix;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getQuantiteTransfert() {
        return quantiteTransfert;
    }

    public void setQuantiteTransfert(Long quantiteTransfert) {
        this.quantiteTransfert = quantiteTransfert;
    }

    public Long getQuantitePerte() {
        return quantitePerte;
    }

    public void setQuantitePerte(Long quantitePerte) {
        this.quantitePerte = quantitePerte;
    }

    public BigDecimal getSommeRecouvree() {
        return sommeRecouvree;
    }

    public void setSommeRecouvree(BigDecimal sommeRecouvree) {
        this.sommeRecouvree = sommeRecouvree;
    }

    public Long getBalanceOuvertureCreance() {
        return balanceOuvertureCreance;
    }

    public void setBalanceOuvertureCreance(Long balanceOuvertureCreance) {
        this.balanceOuvertureCreance = balanceOuvertureCreance;
    }

    public Long getBalanceClotureCreance() {
        return balanceClotureCreance;
    }

    public void setBalanceClotureCreance(Long balanceClotureCreance) {
        this.balanceClotureCreance = balanceClotureCreance;
    }

    public Long getQuantiteInitial() {
        return quantiteInitial;
    }

    public void setQuantiteInitial(Long quantiteInitial) {
        this.quantiteInitial = quantiteInitial;
    }

    public Long getAutreQuantiteSortie() {
        return autreQuantiteSortie;
    }

    public void setAutreQuantiteSortie(Long autreQuantiteSortie) {
        this.autreQuantiteSortie = autreQuantiteSortie;
    }

    public Long getAvoir() {
        return avoir;
    }

    public void setAvoir(Long avoir) {
        this.avoir = avoir;
    }

    public BigDecimal getSommeIrrecouvrable() {
        return sommeIrrecouvrable;
    }

    public void setSommeIrrecouvrable(BigDecimal sommeIrrecouvrable) {
        this.sommeIrrecouvrable = sommeIrrecouvrable;
    }
    
    
    
}