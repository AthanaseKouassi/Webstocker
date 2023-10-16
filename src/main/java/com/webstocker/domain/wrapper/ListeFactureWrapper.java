package com.webstocker.domain.wrapper;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Client;
import com.webstocker.domain.LigneBonDeSortie;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Athanase
 */
public class ListeFactureWrapper {

    private BonDeSortie bonDeSortie;
//    private BigDecimal resteApayer;
    private Long resteApayer;
//    private BigDecimal MontantTotal;
    private Long MontantTotal;
    private String dateFacture;
    private String nomClient;
    
    
    
    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

           
    public String getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }
      
    public BonDeSortie getBonDeSortie() {
        return bonDeSortie;
    }

    public void setBonDeSortie(BonDeSortie bonDeSortie) {
        this.bonDeSortie = bonDeSortie;
    }

    public Long getResteApayer() {
        return resteApayer;
    }

    public void setResteApayer(Long resteApayer) {
        this.resteApayer = resteApayer;
    }

    public Long getMontantTotal() {
        return MontantTotal;
    }

    public void setMontantTotal(Long MontantTotal) {
        this.MontantTotal = MontantTotal;
    }

    

}
