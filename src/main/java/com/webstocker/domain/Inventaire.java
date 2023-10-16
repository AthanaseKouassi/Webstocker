package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Inventaire.
 */
@Entity
@Table(name = "inventaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "inventaire")
public class Inventaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_inventaire")
    private LocalDate dateInventaire;

    @Column(name = "stock_final_theorique")
    private Long stockFinalTheorique;

    @Column(name = "stock_reel")
    private Long stockReel;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    private Magasin magasin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInventaire() {
        return dateInventaire;
    }

    public Inventaire dateInventaire(LocalDate dateInventaire) {
        this.dateInventaire = dateInventaire;
        return this;
    }

    public void setDateInventaire(LocalDate dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public Long getStockFinalTheorique() {
        return stockFinalTheorique;
    }

    public Inventaire stockFinalTheorique(Long stockFinalTheorique) {
        this.stockFinalTheorique = stockFinalTheorique;
        return this;
    }

    public void setStockFinalTheorique(Long stockFinalTheorique) {
        this.stockFinalTheorique = stockFinalTheorique;
    }

    public Long getStockReel() {
        return stockReel;
    }

    public Inventaire stockReel(Long stockReel) {
        this.stockReel = stockReel;
        return this;
    }

    public void setStockReel(Long stockReel) {
        this.stockReel = stockReel;
    }

    public Produit getProduit() {
        return produit;
    }

    public Inventaire produit(Produit produit) {
        this.produit = produit;
        return this;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public Inventaire magasin(Magasin magasin) {
        this.magasin = magasin;
        return this;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Inventaire inventaire = (Inventaire) o;
        if (inventaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, inventaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Inventaire{" +
            "id=" + id +
            ", dateInventaire='" + dateInventaire + "'" +
            ", stockFinalTheorique='" + stockFinalTheorique + "'" +
            ", stockReel='" + stockReel + "'" +
            '}';
    }
}
