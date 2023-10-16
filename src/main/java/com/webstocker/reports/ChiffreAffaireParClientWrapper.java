package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ChiffreAffaireParClientWrapper {

    String numeroFacture;
    String nomClient;
    String telephoneClient;
    String localiteClient;
    String bpClient;
    String nomProduit;
    String numeroLot;
    Long quantiteFacture;
    BigDecimal prixvente;
//    Long prixvente;
    String typeVente;
//    BigDecimal montantVente;
    Long montantVente;


    public ChiffreAffaireParClientWrapper(LigneBonDeSortie ligneBonDeSortie) {

        this.nomClient = ligneBonDeSortie.getBonDeSortie().getClient().getNomClient();
        this.telephoneClient = ligneBonDeSortie.getBonDeSortie().getClient().getTelephoneClient();
        this.localiteClient = ligneBonDeSortie.getBonDeSortie().getClient().getLocalite()!= null?ligneBonDeSortie.getBonDeSortie().getClient().getLocalite().getDisplayName():"";
        this.bpClient = ligneBonDeSortie.getBonDeSortie().getClient().getBoitepostale();
        this.nomProduit = ligneBonDeSortie.getProduit().getNomProduit();
       this.montantVente = ligneBonDeSortie.getPrixDeVente();
        this.quantiteFacture = ligneBonDeSortie.getQuantite();
//        this.prixvente = ligneBonDeSortie.getPrixVente();

        this.typeVente = ligneBonDeSortie.getBonDeSortie().getTypeVente().toString();

    }

    public Long getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(Long montantVente) {
        this.montantVente = montantVente;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getLocaliteClient() {
        return localiteClient;
    }

    public void setLocaliteClient(String localiteClient) {
        this.localiteClient = localiteClient;
    }

    public String getBpClient() {
        return bpClient;
    }

    public void setBpClient(String bpClient) {
        this.bpClient = bpClient;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    public Long getQuantiteFacture() {
        return quantiteFacture;
    }

    public void setQuantiteFacture(Long quantiteFacture) {
        this.quantiteFacture = quantiteFacture;
    }

    public BigDecimal getPrixvente() {
        return prixvente;
    }

    public void setPrixvente(BigDecimal prixvente) {
        this.prixvente = prixvente;
    }

    public String getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(String typeVente) {
        this.typeVente = typeVente;
    }



}
