package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    @Column(name = "stock_magasin_central")
    private Long stockMagasinCentral;

    @Column(name = "stock_antenne")
    private Long stockAntenne;

    @Column(name = "stock_agent")
    private Long stockAgent;

    @Column(name = "arrivage")
    private Long arrivage;

    @Column(name = "stock_theo_debut")
    private Long stockTheoDebut;

    @Column(name = "vente")
    private Long vente;

    @Column(name = "promo")
    private Long promo;

    @Column(name = "perte_abime")
    private Long perteAbime;

    @ManyToOne
    private Bailleur bailleur;

    @Column(name = "commentaire")
    private String commentaire;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(LocalDate dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public Inventaire dateInventaire(LocalDate dateInventaire) {
        this.dateInventaire = dateInventaire;
        return this;
    }

    public Long getStockFinalTheorique() {
        return stockFinalTheorique;
    }

    public void setStockFinalTheorique(Long stockFinalTheorique) {
        this.stockFinalTheorique = stockFinalTheorique;
    }

    public Inventaire stockFinalTheorique(Long stockFinalTheorique) {
        this.stockFinalTheorique = stockFinalTheorique;
        return this;
    }

    public Long getStockReel() {
        return stockReel;
    }

    public void setStockReel(Long stockReel) {
        this.stockReel = stockReel;
    }

    public Inventaire stockReel(Long stockReel) {
        this.stockReel = stockReel;
        return this;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Inventaire produit(Produit produit) {
        this.produit = produit;
        return this;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Inventaire magasin(Magasin magasin) {
        this.magasin = magasin;
        return this;
    }

    public Long getStockMagasinCentral() {
        return stockMagasinCentral;
    }

    public void setStockMagasinCentral(Long stockMagasinCentral) {
        this.stockMagasinCentral = stockMagasinCentral;
    }

    public Long getStockAntenne() {
        return stockAntenne;
    }

    public void setStockAntenne(Long stockAntenne) {
        this.stockAntenne = stockAntenne;
    }

    public Long getStockAgent() {
        return stockAgent;
    }

    public void setStockAgent(Long stockAgent) {
        this.stockAgent = stockAgent;
    }

    public Long getArrivage() {
        return arrivage;
    }

    public void setArrivage(Long arrivage) {
        this.arrivage = arrivage;
    }

    public Long getStockTheoDebut() {
        return stockTheoDebut;
    }

    public void setStockTheoDebut(Long stockTheoDebut) {
        this.stockTheoDebut = stockTheoDebut;
    }

    public Long getVente() {
        return vente;
    }

    public void setVente(Long vente) {
        this.vente = vente;
    }

    public Long getPromo() {
        return promo;
    }

    public void setPromo(Long promo) {
        this.promo = promo;
    }

    public Long getPerteAbime() {
        return perteAbime;
    }

    public void setPerteAbime(Long perteAbime) {
        this.perteAbime = perteAbime;
    }

    public Bailleur getBailleur() {
        return bailleur;
    }

    public void setBailleur(Bailleur bailleur) {
        this.bailleur = bailleur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
            ", dateInventaire=" + dateInventaire +
            ", stockFinalTheorique=" + stockFinalTheorique +
            ", stockReel=" + stockReel +
            ", produit=" + produit +
            ", magasin=" + magasin +
            ", stockMagasinCentral=" + stockMagasinCentral +
            ", stockAntenne=" + stockAntenne +
            ", stockAgent=" + stockAgent +
            ", arrivage=" + arrivage +
            ", stockTheoDebut=" + stockTheoDebut +
            ", vente=" + vente +
            ", promo=" + promo +
            ", perteAbime=" + perteAbime +
            ", bailleur=" + bailleur +
            ", commentaire='" + commentaire + '\'' +
            '}';
    }
}
