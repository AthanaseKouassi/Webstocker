package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Reglement.
 */
@Entity
@Table(name = "reglement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reglement")
public class Reglement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_reglement", nullable = false)
    private LocalDate dateReglement;

    @NotNull
    @Column(name = "montant_reglement", nullable = false)
    private Long montantReglement;

    @ManyToOne
    private Facture facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reglement reglement = (Reglement) o;
        if (reglement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reglement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reglement{" +
            "id=" + getId() +
            ", dateReglement='" + getDateReglement() + "'" +
            ", montantReglement='" + getMontantReglement() + "'" +
            "}";
    }

   
}
