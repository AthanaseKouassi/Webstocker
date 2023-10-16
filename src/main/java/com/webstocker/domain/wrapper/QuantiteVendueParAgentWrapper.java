/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.domain.wrapper;

import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class QuantiteVendueParAgentWrapper {
    
    private String nomAgent;
    private String prenomAgent;
    private String nomproduit;
    private String nomMagasin;
    private String dateVente;
    private Long quantiteVendueAgent;
//    private BigDecimal montantVente;
    private Long montantVente;
    private String antenne;

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    public String getPrenomAgent() {
        return prenomAgent;
    }

    public void setPrenomAgent(String prenomAgent) {
        this.prenomAgent = prenomAgent;
    }

    public String getNomproduit() {
        return nomproduit;
    }

    public void setNomproduit(String nomproduit) {
        this.nomproduit = nomproduit;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public String getDateVente() {
        return dateVente;
    }

    public void setDateVente(String dateVente) {
        this.dateVente = dateVente;
    }

    public Long getQuantiteVendueAgent() {
        return quantiteVendueAgent;
    }

    public void setQuantiteVendueAgent(Long quantiteVendueAgent) {
        this.quantiteVendueAgent = quantiteVendueAgent;
    }

    public Long getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(Long montantVente) {
        this.montantVente = montantVente;
    }

    public String getAntenne() {
        return antenne;
    }

    public void setAntenne(String antenne) {
        this.antenne = antenne;
    }
    
    
    
    
}
