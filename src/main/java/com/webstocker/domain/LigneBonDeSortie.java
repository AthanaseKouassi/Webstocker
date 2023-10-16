package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A LigneBonDeSortie.
 */
@Entity
@Table(name = "ligne_bon_de_sortie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignebondesortie")
public class LigneBonDeSortie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Long quantite;

    @NotNull
    @Column(name = "prix_vente", precision=10, scale=2, nullable = false)
    private BigDecimal prixVente;

    @NotNull
    @Column(name = "prix_de_vente", nullable = false)
    private Long prixDeVente;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    private BonDeSortie bonDeSortie;

    @ManyToOne
    private Lot lot;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public Long getPrixDeVente() {
        return prixDeVente;
    }

    public void setPrixDeVente(Long prixDeVente) {
        this.prixDeVente = prixDeVente;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public BonDeSortie getBonDeSortie() {
        return bonDeSortie;
    }

    public void setBonDeSortie(BonDeSortie bonDeSortie) {
        this.bonDeSortie = bonDeSortie;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
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
        LigneBonDeSortie ligneBonDeSortie = (LigneBonDeSortie) o;
        if (ligneBonDeSortie.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneBonDeSortie.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneBonDeSortie{" +
            "id=" + getId() +
            ", quantite='" + getQuantite() + "'" +
            ", prixVente='" + getPrixVente() + "'" +
            ", prixDeVente='" + getPrixDeVente() + "'" +
            "}";
    }
}
