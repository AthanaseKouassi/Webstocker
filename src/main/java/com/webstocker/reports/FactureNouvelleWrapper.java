package com.webstocker.reports;

import com.webstocker.domain.wrapper.NouvelleFactureWrapper;

/**
 *
 * @author Athanase
 */
public class FactureNouvelleWrapper {

    private String nomProduit;
    private Long prixDeVente;
    private Long quantiteProduitFacture;
    private String nomClient;
    private String telephoneClient;
    private String localiteClient;
    private String bpClient;
    private String dateFacture;
    private String numeroLot;
    private String typeVente;
    private String magasin;
    private String numeroBondeSortie;
    private String dateLimite;
    private String numeroFactureNormalise;

    public FactureNouvelleWrapper(NouvelleFactureWrapper nFactWrapper) {
        this.nomProduit = nFactWrapper.getNomProduit();
        this.bpClient = nFactWrapper.getBonDeSortie().getClient() != null ? nFactWrapper.getBonDeSortie().getClient().getBoitepostale():"";
        this.prixDeVente = nFactWrapper.getPrixDeVente();
        this.quantiteProduitFacture = nFactWrapper.getQuantiteProduit();
        this.nomClient = nFactWrapper.getBonDeSortie().getClient() != null ? nFactWrapper.getBonDeSortie().getClient().getNomClient(): "";
        this.numeroBondeSortie = nFactWrapper.getBonDeSortie().getNumero();
        this.telephoneClient = nFactWrapper.getBonDeSortie().getClient() != null ? nFactWrapper.getBonDeSortie().getClient().getTelephoneClient() : "";
        this.localiteClient = nFactWrapper.getBonDeSortie().getClient().getLocalite() != null ? nFactWrapper.getBonDeSortie().getClient().getLocalite().getDisplayName() : " ";
        this.dateFacture = nFactWrapper.getBonDeSortie().getFacture() != null ? nFactWrapper.getBonDeSortie().getFacture().getDateFacture().toString() : "";
        this.numeroFactureNormalise = nFactWrapper.getBonDeSortie().getNumeroFactureNormalise() != null ? nFactWrapper.getBonDeSortie().getNumeroFactureNormalise() : " ";
        this.magasin = nFactWrapper.getBonDeSortie().getMagasin() != null ? nFactWrapper.getBonDeSortie().getMagasin().getNomMagasin() : " ";
        this.typeVente = nFactWrapper.getBonDeSortie().getTypeVente().toString();
        this.dateLimite = nFactWrapper.getBonDeSortie().getFacture().getDateLimitePaiement() != null ? nFactWrapper.getBonDeSortie().getFacture().getDateLimitePaiement().toString() : " ";
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getPrixDeVente() {
        return prixDeVente;
    }

    public void setPrixDeVente(Long prixDeVente) {
        this.prixDeVente = prixDeVente;
    }

    public Long getQuantiteProduitFacture() {
        return quantiteProduitFacture;
    }

    public void setQuantiteProduitFacture(Long quantiteProduitFacture) {
        this.quantiteProduitFacture = quantiteProduitFacture;
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

    public String getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(String typeVente) {
        this.typeVente = typeVente;
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

    public String getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(String dateLimite) {
        this.dateLimite = dateLimite;
    }

    public String getNumeroFactureNormalise() {
        return numeroFactureNormalise;
    }

    public void setNumeroFactureNormalise(String numeroFactureNormalise) {
        this.numeroFactureNormalise = numeroFactureNormalise;
    }

}
