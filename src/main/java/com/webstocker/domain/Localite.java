package com.webstocker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * A Localite.
 */
@Entity
@Table(name = "localite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "localite")
public class Localite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @OneToMany(mappedBy = "localite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Magasin> magasins = new HashSet<>();

    @OneToMany(mappedBy = "localite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "localite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Mission> missions = new HashSet<>();

    @ManyToOne
    private Commune communes;

    @Transient
    private String displayName;

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

    public Set<Magasin> getMagasins() {
        return magasins;
    }

    public void setMagasins(Set<Magasin> magasins) {
        this.magasins = magasins;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Set<Mission> getMissions() {
        return missions;
    }

    public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }

    @JsonProperty
    public String getDisplayName() {
        displayName= getCommunes().getLibelle()+ " -- "+ getNom();
        return displayName;
    }

    public Commune getCommunes() {
        return communes;
    }

    public void setCommunes(Commune commune) {
        this.communes = commune;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Localite localite = (Localite) o;
        if(localite.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, localite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Localite{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            '}';
    }
}
