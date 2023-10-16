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
 * A Magasin.
 */
@Entity
@Table(name = "magasin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "magasin")
public class Magasin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom_magasin", nullable = false)
    private String nomMagasin;

    @ManyToOne
    private Localite localite;

    @OneToMany(mappedBy = "magasin")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Livraison> livraisons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public Localite getLocalite() {
        return localite;
    }

    public void setLocalite(Localite localite) {
        this.localite = localite;
    }

    public Set<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(Set<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Magasin magasin = (Magasin) o;
        if(magasin.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, magasin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Magasin{" +
            "id=" + id +
            ", nomMagasin='" + nomMagasin + "'" +
            '}';
    }
}
