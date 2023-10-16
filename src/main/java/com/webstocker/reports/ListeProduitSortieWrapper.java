package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ListeProduitSortieWrapper {
    String nomProduit;
    String dateBonDeSortie;
//    BigDecimal prixVente;
    Long quantiteProduitsVendus;
    String typeDeSortie;
    String numeroLot;
    String nomMagasin;
//    BigDecimal montantvente;
    Long montantvente;

    public ListeProduitSortieWrapper(LigneBonDeSortie ligneBonDesortie){
        this.nomProduit = ligneBonDesortie.getLot().getProduit()!=null? ligneBonDesortie.getLot().getProduit().getNomProduit(): "";
        this.nomMagasin = ligneBonDesortie.getBonDeSortie().getMagasin()!= null?ligneBonDesortie.getBonDeSortie().getMagasin().getNomMagasin():" ";
        this.dateBonDeSortie = ligneBonDesortie.getBonDeSortie().getDaateCreation().toString();
        this.typeDeSortie = ligneBonDesortie.getBonDeSortie().getTypeSortie().toString();
//        this.prixVente = ligneBonDesortie.getPrixVente();
        this.quantiteProduitsVendus = ligneBonDesortie.getQuantite();
        this.numeroLot = ligneBonDesortie.getLot().getNumeroLot().toString();
        this.montantvente = ligneBonDesortie.getPrixDeVente();
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

    public Long getMontantvente() {
        return montantvente;
    }

    public void setMontantvente(Long montantvente) {
        this.montantvente = montantvente;
    }

    public Long getQuantiteProduitsVendus() {
        return quantiteProduitsVendus;
    }

    public void setQuantiteProduitsVendus(Long quantiteProduitsVendus) {
        this.quantiteProduitsVendus = quantiteProduitsVendus;
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

}
