 package com.webstocker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "produit")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom_produit", nullable = false)
    private String nomProduit;

    @Column(name = "description_produit")
    private String descriptionProduit;

    @ManyToOne
    private Categorie categorie;

    @OneToMany(mappedBy = "produit")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Objectifs> objectifss = new HashSet<>();

    @OneToMany(mappedBy = "produit")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lignecommande> lignecommandes = new HashSet<>();

    @ManyToOne
    private Conditionnement conditionnement;

    @OneToMany(mappedBy = "produit")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lot> lots = new HashSet<>();

    @ManyToMany(mappedBy = "produits")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fabricant> fabricants = new HashSet<>();

    @OneToMany(mappedBy = "produit")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prix> prixs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDescriptionProduit() {
        return descriptionProduit;
    }

    public void setDescriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Set<Objectifs> getObjectifss() {
        return objectifss;
    }

    public void setObjectifss(Set<Objectifs> objectifss) {
        this.objectifss = objectifss;
    }

    public Set<Lignecommande> getLignecommandes() {
        return lignecommandes;
    }

    public void setLignecommandes(Set<Lignecommande> lignecommandes) {
        this.lignecommandes = lignecommandes;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public Set<Lot> getLots() {
        return lots;
    }

    public void setLots(Set<Lot> lots) {
        this.lots = lots;
    }

    public Set<Fabricant> getFabricants() {
        return fabricants;
    }

    public void setFabricants(Set<Fabricant> fabricants) {
        this.fabricants = fabricants;
    }

    public Set<Prix> getPrixs() {
        return prixs;
    }

    public void setPrixs(Set<Prix> prixs) {
        this.prixs = prixs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Produit produit = (Produit) o;
        if(produit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, produit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Produit{" +
            "id=" + id +
            ", nomProduit='" + nomProduit + "'" +
            ", descriptionProduit='" + descriptionProduit + "'" +
            '}';
    }
}
