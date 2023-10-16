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
 * A Prix.
 */
@Entity
@Table(name = "prix")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "prix")
public class Prix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_fixation", nullable = false)
    private LocalDate dateFixation;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @NotNull
    @Column(name = "prix_unitaire", precision=10, scale=2, nullable = false)
    private BigDecimal prixUnitaire;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    private Categorieclient categorieclient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFixation() {
        return dateFixation;
    }

    public void setDateFixation(LocalDate dateFixation) {
        this.dateFixation = dateFixation;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Categorieclient getCategorieclient() {
        return categorieclient;
    }

    public void setCategorieclient(Categorieclient categorieclient) {
        this.categorieclient = categorieclient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prix prix = (Prix) o;
        if (prix.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, prix.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prix{" +
            "id=" + id +
            ", dateFixation='" + dateFixation + "'" +
            ", actif='" + actif + "'" +
            ", prixUnitaire='" + prixUnitaire + "'" +
            '}';
    }
}
