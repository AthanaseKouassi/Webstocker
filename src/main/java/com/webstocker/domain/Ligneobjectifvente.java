package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Ligneobjectifvente.
 */
@Entity
@Table(name = "ligneobjectifvente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ligneobjectifvente")
public class Ligneobjectifvente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "quantite_objectif")
    private Integer quantiteObjectif;

    @ManyToOne
    private Objectifs objectifs;

    @ManyToOne
    private Produit produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantiteObjectif() {
        return quantiteObjectif;
    }

    public void setQuantiteObjectif(Integer quantiteObjectif) {
        this.quantiteObjectif = quantiteObjectif;
    }

    public Objectifs getObjectifs() {
        return objectifs;
    }

    public void setObjectifs(Objectifs objectifs) {
        this.objectifs = objectifs;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ligneobjectifvente ligneobjectifvente = (Ligneobjectifvente) o;
        if(ligneobjectifvente.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ligneobjectifvente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ligneobjectifvente{" +
            "id=" + id +
            ", quantiteObjectif='" + quantiteObjectif + "'" +
            '}';
    }
}
