package com.webstocker.web.rest.dto.newfeature;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DetailFactureDto {
    private BigDecimal resteApaye;
    private String nomProduit;
    private BigDecimal prixDeVente;
    private Long montantRegle;
    private Long quantite;
    private Long idProduit;
    private Long idFacture;
    private LocalDate dateReglement;
    private Long id;

    public BigDecimal getResteApaye() {
        return resteApaye;
    }

    public void setResteApaye(BigDecimal resteApaye) {
        this.resteApaye = resteApaye;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public BigDecimal getPrixDeVente() {
        return prixDeVente;
    }

    public void setPrixDeVente(BigDecimal prixDeVente) {
        this.prixDeVente = prixDeVente;
    }

    public Long getMontantRegle() {
        return montantRegle;
    }

    public void setMontantRegle(Long montantRegle) {
        this.montantRegle = montantRegle;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public LocalDate getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(LocalDate dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
