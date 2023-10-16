package com.webstocker.web.rest.dto;

import com.webstocker.domain.Client;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by centonni on 27/01/17.
 */
public class FactureDTO {
    LocalDate date;
    Long factureID;
//    BigDecimal montantTotal;
    Long montantTotal;
    Long montantPaye;
//    BigDecimal montantPaye;
    Long reste;
//    BigDecimal reste;
    Client client;
    String normalise;

    public FactureDTO(LocalDate date, Long factureID, Client client) {
        this.date = date;
        this.factureID = factureID;
        this.client = client;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getFactureID() {
        return factureID;
    }

    public void setFactureID(Long factureID) {
        this.factureID = factureID;
    }

    public Long getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Long montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Long getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(Long montantPaye) {
        this.montantPaye = montantPaye;
    }

    public Long getReste() {

//        return this.montantTotal.subtract(this.montantPaye);
        return this.montantTotal-this.montantPaye;
    }

    public void setReste(Long reste) {
        this.reste = reste;
    }

    public String getNormalise() {
        return normalise;
    }

    public void setNormalise(String normalise) {
        this.normalise = normalise;
    }
    
}
