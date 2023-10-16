/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.wrapper.QuantiteVendueParAgentWrapper;
import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class QuantiteVendueParAgentReportWrapper {
    
    private String nomAgent;
    private String nomProduit;
    private Long qtevendue;
//    private BigDecimal montant;
    private Long montant;
    private String prenomAgent;

    public QuantiteVendueParAgentReportWrapper(QuantiteVendueParAgentWrapper qteParAgent) {
        this.nomAgent = qteParAgent.getNomAgent();
        this.nomProduit = qteParAgent.getNomproduit();
        this.qtevendue = qteParAgent.getQuantiteVendueAgent();
//        this.montant = qteParAgent.getMontantVente();
        this.montant = qteParAgent.getMontantVente();
        this.prenomAgent = qteParAgent.getPrenomAgent();
    }

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getQtevendue() {
        return qtevendue;
    }

    public void setQtevendue(Long qtevendue) {
        this.qtevendue = qtevendue;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public String getPrenomAgent() {
        return prenomAgent;
    }

    public void setPrenomAgent(String prenomAgent) {
        this.prenomAgent = prenomAgent;
    }

    
    
}
