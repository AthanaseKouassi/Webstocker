package com.webstocker.web.rest.dto.newfeature;

import java.time.LocalDate;

public class ReglementDto {
    private Long id;
    private LocalDate dateReglement;
    private Long montantReglement;
    private Long idFacture;
    private Long idProduit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(LocalDate dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Long getMontantReglement() {
        return montantReglement;
    }

    public void setMontantReglement(Long montantReglement) {
        this.montantReglement = montantReglement;
    }

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }
}
