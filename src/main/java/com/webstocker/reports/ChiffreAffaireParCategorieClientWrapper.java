package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ChiffreAffaireParCategorieClientWrapper {

    String nomCategorie;
    String nomProduit;
    Long quantiteProduit;
//    BigDecimal montant;
    Long montant;

    public ChiffreAffaireParCategorieClientWrapper(ChiffreAffaireWrapper ligne) {
        this.nomCategorie = ligne.getLibellecategorie();
        this.nomProduit = ligne.getNomProduit();
        this.montant = ligne.getChiffreAffaire();
        this.quantiteProduit = ligne.getQuantiteVendue();
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Long getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(Long quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

}
