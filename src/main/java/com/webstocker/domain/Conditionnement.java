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
 * A Conditionnement.
 */
@Entity
@Table(name = "conditionnement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "conditionnement")
public class Conditionnement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "description_cond")
    private String descriptionCond;

    @NotNull
    @Column(name = "capacite_carton", nullable = false)
    private Long capaciteCarton;

    @NotNull
    @Column(name = "capacite_cartouche", nullable = false)
    private Long capaciteCartouche;

    @NotNull
    @Column(name = "capacite_etui", nullable = false)
    private Long capaciteEtui;

    @OneToMany(mappedBy = "conditionnement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Produit> produits = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescriptionCond() {
        return descriptionCond;
    }

    public void setDescriptionCond(String descriptionCond) {
        this.descriptionCond = descriptionCond;
    }

    public Long getCapaciteCarton() {
        return capaciteCarton;
    }

    public void setCapaciteCarton(Long capaciteCarton) {
        this.capaciteCarton = capaciteCarton;
    }

    public Long getCapaciteCartouche() {
        return capaciteCartouche;
    }

    public void setCapaciteCartouche(Long capaciteCartouche) {
        this.capaciteCartouche = capaciteCartouche;
    }

    public Long getCapaciteEtui() {
        return capaciteEtui;
    }

    public void setCapaciteEtui(Long capaciteEtui) {
        this.capaciteEtui = capaciteEtui;
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
        Conditionnement conditionnement = (Conditionnement) o;
        if(conditionnement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, conditionnement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Conditionnement{" +
            "id=" + id +
            ", libelle='" + libelle + "'" +
            ", descriptionCond='" + descriptionCond + "'" +
            ", capaciteCarton='" + capaciteCarton + "'" +
            ", capaciteCartouche='" + capaciteCartouche + "'" +
            ", capaciteEtui='" + capaciteEtui + "'" +
            '}';
    }
}
