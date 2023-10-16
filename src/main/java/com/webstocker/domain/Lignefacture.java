package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lignefacture.
 */
@Entity
@Table(name = "lignefacture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignefacture")
public class Lignefacture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "quantite_facture", nullable = false)
    private Integer quantiteFacture;

    @ManyToOne
    private Lot lot;

    @ManyToOne
    private Facture facture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantiteFacture() {
        return quantiteFacture;
    }

    public void setQuantiteFacture(Integer quantiteFacture) {
        this.quantiteFacture = quantiteFacture;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lignefacture lignefacture = (Lignefacture) o;
        if(lignefacture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lignefacture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lignefacture{" +
            "id=" + id +
            ", quantiteFacture='" + quantiteFacture + "'" +
            '}';
    }
}
