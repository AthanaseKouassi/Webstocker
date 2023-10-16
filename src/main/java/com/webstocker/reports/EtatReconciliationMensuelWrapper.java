package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatDeReconciliationWrapper;
import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class EtatReconciliationMensuelWrapper {

    String produit;

    Long quantiteVente;
    Long quantitePromo;
    Long quantiteTotal;
    Long quantiteVendueCredit;
    Long quantiteVendueCash;
    BigDecimal sommeVenteCash;
    BigDecimal sommeVenteCredit;
    Long quantiteLivree;

    Long balanceOuvertureCreance;
    Long balanceClotureCreance;
    Long quantiteFinMois;
    BigDecimal sommeRecouvre;
    BigDecimal sommeCreance30;
    BigDecimal sommeCreance30_90;
    BigDecimal sommeCreance90;
    BigDecimal prixUnitaire;
    String userNom;
    String userPrenom;
    Long quantiteTransfert;
    Long quantitePerte;
    Long sommeEnAvoir;
    Long autreQuantiteSortie;
    Long sommeIrrecouvrable;
    Long quantiteInitiale;

    public EtatReconciliationMensuelWrapper(EtatDeReconciliationWrapper etatRecon) {

        this.produit = etatRecon.getProduit().getNomProduit();
        this.quantiteVente = etatRecon.getQuantiteVendue();
        this.quantitePromo = etatRecon.getQuantitePromotion();
        this.quantiteTotal = etatRecon.getQuantiteTotal();
        this.quantiteVendueCredit = etatRecon.getQuantiteVenteCredit();
        this.quantiteVendueCash = etatRecon.getQuantiteVenteCash();
        this.sommeVenteCash = etatRecon.getSommeVenteCash();
        this.sommeVenteCredit = etatRecon.getSommeVenteCredit();
        this.quantiteLivree = etatRecon.getQuantiteLivree();

        this.balanceOuvertureCreance = etatRecon.getBalanceOuvertureCreance();
        this.balanceClotureCreance = etatRecon.getBalanceClotureCreance();
        this.prixUnitaire = etatRecon.getPrix() != null ? etatRecon.getPrix().getPrixUnitaire() : BigDecimal.ZERO;
        this.quantiteTransfert = etatRecon.getQuantiteTransfert();
        this.quantitePerte = etatRecon.getQuantitePerte();
        this.sommeRecouvre = etatRecon.getSommeRecouvree();
        this.userPrenom = etatRecon.getUser() != null ? etatRecon.getUser().getFirstName() : " ";
        this.userNom = etatRecon.getUser() != null ? etatRecon.getUser().getLastName() : " ";
        this.sommeCreance30 = etatRecon.getSommeCreance30();
        this.sommeCreance30_90 = etatRecon.getSommeCreance30_90();
        this.sommeCreance90 = etatRecon.getSommeCreance90();
        this.quantiteFinMois = getQuantiteFinMois();
        this.autreQuantiteSortie = etatRecon.getAutreQuantiteSortie();
        this.sommeIrrecouvrable = etatRecon.getAvoir();
        this.quantiteInitiale = etatRecon.getQuantiteInitial();

    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
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

    public Long getQuantiteTotal() {
        return quantiteTotal;
    }

    public void setQuantiteTotal(Long quantiteTotal) {
        this.quantiteTotal = quantiteTotal;
    }

    public Long getQuantiteVendueCredit() {
        return quantiteVendueCredit;
    }

    public void setQuantiteVendueCredit(Long quantiteVendueCredit) {
        this.quantiteVendueCredit = quantiteVendueCredit;
    }

    public Long getQuantiteVendueCash() {
        return quantiteVendueCash;
    }

    public void setQuantiteVendueCash(Long quantiteVendueCash) {
        this.quantiteVendueCash = quantiteVendueCash;
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

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getUserNom() {
        return userNom;
    }

    public void setUserNom(String userNom) {
        this.userNom = userNom;
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

    public final Long getQuantiteFinMois() {
        return quantiteFinMois = (quantiteTotal + quantiteLivree) - (quantiteVente + quantitePromo + quantitePerte);
    }

    public void setQuantiteFinMois(Long quantiteFinMois) {
        this.quantiteFinMois = quantiteFinMois;
    }

    public BigDecimal getSommeRecouvre() {
        return sommeRecouvre;
    }

    public void setSommeRecouvre(BigDecimal sommeRecouvre) {
        this.sommeRecouvre = sommeRecouvre;
    }

    public Long getBalanceOuvertureCreance() {
        return balanceOuvertureCreance;
    }

    public void setBalanceOuvertureCreance(Long balanceOuvertureCreance) {
        this.balanceOuvertureCreance = balanceOuvertureCreance;
    }

    public final Long getBalanceClotureCreance() {
        
        return balanceClotureCreance;
    }

    public void setBalanceClotureCreance(Long balanceClotureCreance) {
        this.balanceClotureCreance = balanceClotureCreance;
    }

    public String getUserPrenom() {
        return userPrenom;
    }

    public void setUserPrenom(String userPrenom) {
        this.userPrenom = userPrenom;
    }

    public Long getSommeEnAvoir() {
        return sommeEnAvoir;
    }

    public void setSommeEnAvoir(Long sommeEnAvoir) {
        this.sommeEnAvoir = sommeEnAvoir;
    }

    public Long getAutreQuantiteSortie() {
        return autreQuantiteSortie;
    }

    public void setAutreQuantiteSortie(Long autreQuantiteSortie) {
        this.autreQuantiteSortie = autreQuantiteSortie;
    }

    public Long getSommeIrrecouvrable() {
        return sommeIrrecouvrable;
    }

    public void setSommeIrrecouvrable(Long sommeIrrecouvrable) {
        this.sommeIrrecouvrable = sommeIrrecouvrable;
    }

    public Long getQuantiteInitiale() {
        return quantiteInitiale;
    }

    public void setQuantiteInitiale(Long quantiteInitiale) {
        this.quantiteInitiale = quantiteInitiale;
    }

    
}
