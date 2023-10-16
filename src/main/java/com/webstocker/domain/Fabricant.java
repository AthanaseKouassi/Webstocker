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
 * A Fabricant.
 */
@Entity
@Table(name = "fabricant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fabricant")
public class Fabricant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom_fabricant", nullable = false)
    private String nomFabricant;

    @Column(name = "pays_fabricant")
    private String paysFabricant;

    @OneToMany(mappedBy = "fabricant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Commande> commandes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "fabricant_produit",
               joinColumns = @JoinColumn(name="fabricants_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="produits_id", referencedColumnName="ID"))
    private Set<Produit> produits = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFabricant() {
        return nomFabricant;
    }

    public void setNomFabricant(String nomFabricant) {
        this.nomFabricant = nomFabricant;
    }

    public String getPaysFabricant() {
        return paysFabricant;
    }

    public void setPaysFabricant(String paysFabricant) {
        this.paysFabricant = paysFabricant;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fabricant fabricant = (Fabricant) o;
        if(fabricant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fabricant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fabricant{" +
            "id=" + id +
            ", nomFabricant='" + nomFabricant + "'" +
            ", paysFabricant='" + paysFabricant + "'" +
            '}';
    }
}
