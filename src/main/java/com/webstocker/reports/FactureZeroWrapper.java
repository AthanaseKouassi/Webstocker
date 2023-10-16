package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class FactureZeroWrapper {

    String numeroFacture;
    String nomClient;
    String telephoneClient;
//    String localiteClient;
//    String bpClient;
    String nomProduit;
    String dateFacture;
    String numeroLot;
    Long quantiteFacture;
    BigDecimal prixvente;
    String typeSortie;
    String magasin;
    String numeroBondeSortie;
    String numeroFactureNormalise;
    Long prixUnitaire = 0L;

    public FactureZeroWrapper(LigneBonDeSortie lignebs) {
        this.nomClient = lignebs.getBonDeSortie().getClient() != null ? lignebs.getBonDeSortie().getClient().getNomClient() : " ";
        this.telephoneClient = lignebs.getBonDeSortie().getClient() != null ? lignebs.getBonDeSortie().getClient().getTelephoneClient() : " ";
//        this.localiteClient = lignebs.getBonDeSortie().getClient().getLocalite().getDisplayName();
//        this.bpClient = lignebs.getBonDeSortie().getClient().getBoitepostale();
        this.nomProduit = lignebs.getProduit().getNomProduit();
        this.numeroLot = lignebs.getLot().getNumeroLot().toString();
        this.magasin = lignebs.getBonDeSortie().getMagasin() != null ? lignebs.getBonDeSortie().getMagasin().getNomMagasin() : " ";

        this.quantiteFacture = lignebs.getQuantite();
        this.prixvente = lignebs.getPrixVente() != null ? lignebs.getPrixVente() : BigDecimal.ZERO;

        this.typeSortie = lignebs.getBonDeSortie().getTypeSortie().toString();
        this.dateFacture = lignebs.getBonDeSortie().getFacture()!= null ? lignebs.getBonDeSortie().getFacture().getDateFacture().toString(): lignebs.getBonDeSortie().getDaateCreation().toString();
        this.numeroBondeSortie = lignebs.getBonDeSortie().getNumero();
        this.numeroFactureNormalise = lignebs.getBonDeSortie().getNumeroFactureNormalise() != null ? lignebs.getBonDeSortie().getNumeroFactureNormalise() : " ";

    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getNumeroFactureNormalise() {
        return numeroFactureNormalise;
    }

    public void setNumeroFactureNormalise(String numeroFactureNormalise) {
        this.numeroFactureNormalise = numeroFactureNormalise;
    }

    public Long getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Long prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
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

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
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

    public String getTypeSortie() {
        return typeSortie;
    }

    public void setTypeSortie(String typeSortie) {
        this.typeSortie = typeSortie;
    }

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
    }

    public String getNumeroBondeSortie() {
        return numeroBondeSortie;
    }

    public void setNumeroBondeSortie(String numeroBondeSortie) {
        this.numeroBondeSortie = numeroBondeSortie;
    }

}
