package com.webstocker.reports;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.Prix;
import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;

/**
 *
 * @author Athanase
 */
public class EtatStockGlobalParBailleurWrapper {
    String produit;
    Long quantiteVendue;
    Long quantitePromotion;
    Long quantitePerte;
    Long quantiteTransfert;
    Long quantiteInitial ;
    Long quantiteFinale ;
    Long valeurVente;
    Long quantiteLivre;
    String bailleur;
   // Prix prix;
    
    public EtatStockGlobalParBailleurWrapper(EtatStockGlobalAimasWrapper etatStockDuBailleur){
        
        this.produit = etatStockDuBailleur.getProduit().getNomProduit();
        this.quantiteLivre = etatStockDuBailleur.getQuantiteLivre();
        this.quantitePerte = etatStockDuBailleur.getQuantitePerte();
        this.quantitePromotion = etatStockDuBailleur.getQuantitePromotion();
        this.quantiteVendue = etatStockDuBailleur.getQuantiteVendue();
        this.quantiteTransfert = etatStockDuBailleur.getQuantiteTransfert();
        this.quantiteInitial = etatStockDuBailleur.getQuantiteInitial();
        this.valeurVente = etatStockDuBailleur.getValeurVente();
        this.quantiteFinale = etatStockDuBailleur.getQuantiteFinale();
        this.bailleur = etatStockDuBailleur.getBailleur() != null? etatStockDuBailleur.getBailleur().getNomBailleur(): "xxxxxx";
        this.quantiteLivre = etatStockDuBailleur.getQuantiteLivre();
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
//        (quantiteInitial + quantiteLivree + quantiteTotaleTransfert ) 
        return quantiteFinale = (quantiteInitial + quantiteLivre)- (quantiteVendue + quantitePromotion + quantitePerte);
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

    public Long getQuantiteLivre() {
        return quantiteLivre;
    }

    public void setQuantiteLivre(Long quantiteLivre) {
        this.quantiteLivre = quantiteLivre;
    }

    public String getBailleur() {
        return bailleur;
    }

    public void setBailleur(String bailleur) {
        this.bailleur = bailleur;
    }
    
    
    
}
