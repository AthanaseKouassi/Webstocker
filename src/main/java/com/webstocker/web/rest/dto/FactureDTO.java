package com.webstocker.web.rest.dto;

import com.webstocker.domain.Client;
import com.webstocker.domain.Reglement;

import java.time.LocalDate;
import java.util.Set;

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

    private Long id;
    private LocalDate dateFacture;
    private Integer valeurRemise;
    private Integer delaiPaiement;
    private LocalDate dateLimitePaiement;
    private Set<Reglement> reglements;

    private Long idBonDeSortie;
    private Long idclient;
    private String statutFacture;
    private String numero;

    public FactureDTO(LocalDate date, Long factureID, Client client) {
        this.date = date;
        this.factureID = factureID;
        this.client = client;
    }

    public FactureDTO() {

    }

    public String getStatutFacture() {
        return statutFacture;
    }

    public void setStatutFacture(String statutFacture) {
        this.statutFacture = statutFacture;
    }

    public Long getIdBonDeSortie() {
        return idBonDeSortie;
    }

    public void setIdBonDeSortie(Long idBonDeSortie) {
        this.idBonDeSortie = idBonDeSortie;
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
        return this.montantTotal - this.montantPaye;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Integer getValeurRemise() {
        return valeurRemise;
    }

    public void setValeurRemise(Integer valeurRemise) {
        this.valeurRemise = valeurRemise;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public LocalDate getDateLimitePaiement() {
        return dateLimitePaiement;
    }

    public void setDateLimitePaiement(LocalDate dateLimitePaiement) {
        this.dateLimitePaiement = dateLimitePaiement;
    }

    public Set<Reglement> getReglements() {
        return reglements;
    }

    public void setReglements(Set<Reglement> reglements) {
        this.reglements = reglements;
    }

    public Long getIdclient() {
        return idclient;
    }

    public void setIdclient(Long idclient) {
        this.idclient = idclient;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
