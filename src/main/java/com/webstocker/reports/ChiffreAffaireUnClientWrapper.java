package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ChiffreAffaireUnClientWrapper {

    String nomClient;
    String adresseClient;
    String telephone;
    String libellecategorie;
    String nomProduit;
    Long quantiteProduit;
//    BigDecimal montantVendu;
    Long montantVendu;

    public ChiffreAffaireUnClientWrapper(ChiffreAffaireWrapper chiffreaffClient) {
        this.nomClient = chiffreaffClient.getNomClient();
//        this.adresseClient = caw.getBonDeSortie().getFacture().getClient().getLocalite().getDisplayName();
        this.telephone = chiffreaffClient.getTelephoneClient();
        this.nomProduit = chiffreaffClient.getNomProduit();
        this.quantiteProduit = chiffreaffClient.getQuantiteVendue();
        this.montantVendu = chiffreaffClient.getChiffreAffaire();
        this.libellecategorie = chiffreaffClient.getLibellecategorie();
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getAdresseClient() {
        return adresseClient;
    }

    public void setAdresseClient(String adresseClient) {
        this.adresseClient = adresseClient;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(Long quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public Long getMontantVendu() {
        return montantVendu;
    }

    public void setMontantVendu(Long montantVendu) {
        this.montantVendu = montantVendu;
    }

    public String getLibellecategorie() {
        return libellecategorie;
    }

    public void setLibellecategorie(String libellecategorie) {
        this.libellecategorie = libellecategorie;
    }
    
}
