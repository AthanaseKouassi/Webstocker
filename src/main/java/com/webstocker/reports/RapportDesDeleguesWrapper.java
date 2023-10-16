package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatAgentWrapper;

/**
 *
 * @author Athanase
 */
public class RapportDesDeleguesWrapper {
    String nomProduit;
    Long quantiteSortie;    
    Long quantiteDetenue;   
    Long quantiteInitiale;
    Long quantiteRecue;
    String nomAgent;
          
    
    public RapportDesDeleguesWrapper(EtatAgentWrapper etatAgent){
        this.nomProduit = etatAgent.getProduit().getNomProduit();
        this.nomAgent = etatAgent.getMagasin().getNomMagasin();
        this.quantiteDetenue = etatAgent.getQuantiteDetenue();
        this.quantiteSortie = etatAgent.getQuantiteSortie();
        this.quantiteInitiale = etatAgent.getQuantiteInitiale();
        this.quantiteRecue = etatAgent.getQuantiteRecue();
    }

    public Long getQuantiteInitiale() {
        return quantiteInitiale;
    }

    public void setQuantiteInitiale(Long quantiteInitiale) {
        this.quantiteInitiale = quantiteInitiale;
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
   
    public Long getQuantiteSortie() {
        return quantiteSortie;
    }

    public void setQuantiteSortie(Long quantiteSortie) {
        this.quantiteSortie = quantiteSortie;
    }

    public Long getQuantiteDetenue() {
        return quantiteDetenue;
    }

    public void setQuantiteDetenue(Long quantiteDetenue) {
        this.quantiteDetenue = quantiteDetenue;
    }

    public Long getQuantiteRecue() {
        return quantiteRecue;
    }

    public void setQuantiteRecue(Long quantiteRecue) {
        this.quantiteRecue = quantiteRecue;
    }
        
}

