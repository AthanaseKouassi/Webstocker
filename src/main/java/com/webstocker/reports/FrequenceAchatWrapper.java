package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

/**
 *
 * @author Athanase
 */
public class FrequenceAchatWrapper {
    String nomClient;
    String localiteClient;
    Long quantiteAchetee;
    Long nombreFacture;
    String telephone;
    
    public FrequenceAchatWrapper(LigneBonDeSortie ligne){
        this.nomClient = ligne.getBonDeSortie().getFacture().getClient().getNomClient();
        this.telephone = ligne.getBonDeSortie().getFacture().getClient().getTelephoneClient();
        this.quantiteAchetee = ligne.getQuantite();
        this.localiteClient = ligne.getBonDeSortie().getClient().getLocalite() != null? ligne.getBonDeSortie().getClient().getLocalite().getDisplayName():" ";
        this.nombreFacture = ligne.getBonDeSortie().getFacture().getId();
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getLocaliteClient() {
        return localiteClient;
    }

    public void setLocaliteClient(String localiteClient) {
        this.localiteClient = localiteClient;
    }

    public Long getQuantiteAchetee() {
        return quantiteAchetee;
    }

    public void setQuantiteAchetee(Long quantiteAchetee) {
        this.quantiteAchetee = quantiteAchetee;
    }

    public Long getNombreFacture() {
        return nombreFacture;
    }

    public void setNombreFacture(Long nombreFacture) {
        this.nombreFacture = nombreFacture;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
}
