package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public final class FactureWrapper {

    String numeroFacture;
    String nomClient;
    String telephoneClient;
    String localiteClient;
    String bpClient;
    String nomProduit;
    String dateFacture;
    String numeroLot;
    Long quantiteFacture;
    BigDecimal prixvente;
    String typeVente;
    String magasin;
    String numeroBondeSortie;
    String dateLimite;
//    BigDecimal montantVente;
    Long montantVente;
    String numeroFactureNormalise;
    int remise  ;

    public FactureWrapper(LigneBonDeSortie lignebs) {

        this.nomClient = lignebs.getBonDeSortie().getClient().getNomClient();
        this.telephoneClient = lignebs.getBonDeSortie().getClient()!=null?lignebs.getBonDeSortie().getClient().getTelephoneClient():"";
        this.localiteClient = lignebs.getBonDeSortie().getClient().getLocalite()!=null? lignebs.getBonDeSortie().getClient().getLocalite().getDisplayName():" ";
        this.bpClient = lignebs.getBonDeSortie().getClient().getBoitepostale();
        this.nomProduit = lignebs.getProduit().getNomProduit();
        this.numeroLot = lignebs.getLot().getNumeroLot().toString();
        this.dateLimite = lignebs.getBonDeSortie().getFacture().getDateLimitePaiement() != null ? lignebs.getBonDeSortie().getFacture().getDateLimitePaiement().toString() : " ";
//        this.dateLimite = lignebs.getBonDeSortie().getFacture() != null ? lignebs.getBonDeSortie().getFacture().getDateLimitePaiement().toString() : " ";
//        this.dateLimite = lignebs.getBonDeSortie().getFacture().getDateLimitePaiement().toString() ;
        this.magasin = lignebs.getBonDeSortie().getMagasin()!= null ? lignebs.getBonDeSortie().getMagasin().getNomMagasin() : " ";
//         this.magasin = lignebs.getBonDeSortie().getMagasin().getNomMagasin();
        this.quantiteFacture = lignebs.getQuantite();
        this.prixvente = lignebs.getPrixVente();
        this.montantVente = lignebs.getPrixDeVente();
        this.dateFacture = lignebs.getBonDeSortie().getFacture() != null ? lignebs.getBonDeSortie().getFacture().getDateFacture().toString():"";
        this.typeVente = lignebs.getBonDeSortie().getTypeVente().toString();
        this.remise = lignebs.getBonDeSortie().getFacture()!= null? lignebs.getBonDeSortie().getFacture().getValeurRemise():0;
//        this.remise = lignebs.getBonDeSortie().getFacture().getValeurRemise()!= null? lignebs.getBonDeSortie().getFacture().getValeurRemise():0;
        this.numeroBondeSortie = lignebs.getBonDeSortie().getNumero();
        this.numeroFactureNormalise = lignebs.getBonDeSortie().getNumeroFactureNormalise() != null ? lignebs.getBonDeSortie().getNumeroFactureNormalise() : " ";

    }

    public int getRemise() {
        return remise;
    }

    public void setRemise(int remise) {
        this.remise = remise;
    }

    public String getNumeroFactureNormalise() {
        return numeroFactureNormalise;
    }

    public void setNumeroFactureNormalise(String numeroFactureNormalise) {
        this.numeroFactureNormalise = numeroFactureNormalise;
    }

    public BigDecimal getPrixvente() {
        return prixvente;
    }

    public void setPrixvente(BigDecimal prixvente) {
        this.prixvente = prixvente;
    }

    public Long getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(Long montantVente) {
        this.montantVente = montantVente;
    }

    public String getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(String dateLimite) {
        this.dateLimite = dateLimite;
    }

    public String getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(String typeVente) {
        this.typeVente = typeVente;
    }

    public String getNumeroBondeSortie() {
        return numeroBondeSortie;
    }

    public void setNumeroBondeSortie(String numeroBondeSortie) {
        this.numeroBondeSortie = numeroBondeSortie;
    }

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
    }

    public String getBpClient() {
        return bpClient;
    }

    public void setBpClient(String bpClient) {
        this.bpClient = bpClient;
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
//        Long numProd= lbs.getProduit().getId();
//        Set<Lot> numProdLot = lbs.getProduit().getLots();
//        if(numProdLot.contains(lbs)){
//             numeroLot = numProdLot.add(e) ;
//        }

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

}
