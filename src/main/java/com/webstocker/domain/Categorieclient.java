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
 * A Categorieclient.
 */
@Entity
@Table(name = "categorieclient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "categorieclient")
public class Categorieclient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "libelle_categorie_client", nullable = false)
    private String libelleCategorieClient;

    @OneToMany(mappedBy = "categorieclient")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "categorieclient")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prix> prixs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleCategorieclient() {
        return libelleCategorieClient;
    }

    public void setLibelleCategorieclient(String libelleCategorieClient) {
        this.libelleCategorieClient = libelleCategorieClient;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
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
        Categorieclient categorieclient = (Categorieclient) o;
        if(categorieclient.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, categorieclient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Categorieclient{" +
            "id=" + id +
            ", libelleCategorieclient='" + libelleCategorieClient + "'" +
            '}';
    }
}
