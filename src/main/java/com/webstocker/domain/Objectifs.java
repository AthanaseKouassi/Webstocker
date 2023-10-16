package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Objectifs.
 */
@Entity
@Table(name = "objectifs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "objectifs")
public class Objectifs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "periode")
    private LocalDate periode;

    @Column(name = "quantite_attendue")
    private Long quantiteAttendue;

    @Column(name = "quantite_obtenu")
    private Long quantiteObtenu;

    @Column(name = "taux")
    private String taux;

    @ManyToOne
    private Produit produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPeriode() {
        return periode;
    }

    public void setPeriode(LocalDate periode) {
        this.periode = periode;
    }

    public Long getQuantiteAttendue() {
        return quantiteAttendue;
    }

    public void setQuantiteAttendue(Long quantiteAttendue) {
        this.quantiteAttendue = quantiteAttendue;
    }

    public Long getQuantiteObtenu() {
        return quantiteObtenu;
    }

    public void setQuantiteObtenu(Long quantiteObtenu) {
        this.quantiteObtenu = quantiteObtenu;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
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
        Objectifs objectifs = (Objectifs) o;
        if(objectifs.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, objectifs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Objectifs{" +
            "id=" + id +
            ", periode='" + periode + "'" +
            ", quantiteAttendue='" + quantiteAttendue + "'" +
            ", quantiteObtenu='" + quantiteObtenu + "'" +
            ", taux='" + taux + "'" +
            '}';
    }
}
