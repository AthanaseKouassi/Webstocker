package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ListeLotProduitSortieParMagasinWrapper {
    String nomProduit;
    String dateBonDeSortie;
//    BigDecimal prixVente;
    Long quantite;
    String typeDeSortie;
    String numeroLot;
    String nomMagasin;
    Long montantVente;

    public ListeLotProduitSortieParMagasinWrapper(LigneBonDeSortie lbs) {
        this.nomProduit = lbs.getLot().getProduit()!=null? lbs.getLot().getProduit().getNomProduit():" ";
        this.dateBonDeSortie = lbs.getBonDeSortie().getDaateCreation().toString();
//        this.prixVente = lbs.getPrixVente();
        this.quantite = lbs.getQuantite();
        this.typeDeSortie = lbs.getBonDeSortie().getTypeSortie().toString();
        this.numeroLot = lbs.getLot().getNumeroLot().toString();
        this.nomMagasin = lbs.getBonDeSortie().getMagasin()!=null?lbs.getBonDeSortie().getMagasin().getNomMagasin():" ";
        this.montantVente = lbs.getPrixDeVente();
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDateBonDeSortie() {
        return dateBonDeSortie;
    }

    public void setDateBonDeSortie(String dateBonDeSortie) {
        this.dateBonDeSortie = dateBonDeSortie;
    }

//    public BigDecimal getPrixVente() {
//        return prixVente;
//    }
//
//    public void setPrixVente(BigDecimal prixVente) {
//        this.prixVente = prixVente;
//    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public String getTypeDeSortie() {
        return typeDeSortie;
    }

    public void setTypeDeSortie(String typeDeSortie) {
        this.typeDeSortie = typeDeSortie;
    }

    public String getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public Long getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(Long montantVente) {
        this.montantVente = montantVente;
    }



}
