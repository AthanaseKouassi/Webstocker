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
 * A Cellule.
 */
@Entity
@Table(name = "cellule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cellule")
public class Cellule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @ManyToOne
    private User responsable;

    @OneToMany(mappedBy = "cellule")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Mission> missions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public User getResponsable() {
        return responsable;
    }

    public void setResponsable(User user) {
        this.responsable = user;
    }

    public Set<Mission> getMissions() {
        return missions;
    }

    public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cellule cellule = (Cellule) o;
        if(cellule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cellule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cellule{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            '}';
    }
}
