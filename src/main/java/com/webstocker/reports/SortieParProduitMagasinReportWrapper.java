
package com.webstocker.reports;

import com.webstocker.domain.wrapper.SortieParProduitMagasinWrapper;

/**
 *
 * @author Athanase
 */
public class SortieParProduitMagasinReportWrapper {

       String numeroBs;
    String Datebs;
    String NumeroFacture;
    String DateFacture;
    String nomClient;
    
     public SortieParProduitMagasinReportWrapper(SortieParProduitMagasinWrapper sppm ) {
        this.numeroBs = sppm.getBonDeSortie().getNumero();
        this.Datebs = sppm.getBonDeSortie().getDaateCreation().toString();
        this.NumeroFacture = sppm.getBonDeSortie().getNumeroFactureNormalise();
        this.DateFacture = sppm.getBonDeSortie().getFacture().getDateFacture().toString();
        this.nomClient = sppm.getBonDeSortie().getClient().getNomClient();
    }

    public String getNumeroBs() {
        return numeroBs;
    }

    public void setNumeroBs(String numeroBs) {
        this.numeroBs = numeroBs;
    }

    public String getDatebs() {
        return Datebs;
    }

    public void setDatebs(String Datebs) {
        this.Datebs = Datebs;
    }

    public String getNumeroFacture() {
        return NumeroFacture;
    }

    public void setNumeroFacture(String NumeroFacture) {
        this.NumeroFacture = NumeroFacture;
    }

    public String getDateFacture() {
        return DateFacture;
    }

    public void setDateFacture(String DateFacture) {
        this.DateFacture = DateFacture;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }
     
    
}
