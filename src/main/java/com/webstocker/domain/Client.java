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

import com.webstocker.domain.enumeration.TypeClient;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom_client", nullable = false)
    private String nomClient;

    @Column(name = "telephone_client")
    private String telephoneClient;

    @Column(name = "boitepostale")
    private String boitepostale;

//    @Column(name = "localite_client")
//    private String localiteClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_client")
    private TypeClient typeClient;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Facture> factures = new HashSet<>();

    @ManyToOne
    private Localite localite;

    @ManyToOne
    private Categorieclient categorieclient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getBoitepostale() {
        return boitepostale;
    }

    public void setBoitepostale(String boitepostale) {
        this.boitepostale = boitepostale;
    }

//    public String getLocaliteClient() {
//        return localiteClient;
//    }
//
//    public void setLocaliteClient(String localiteClient) {
//        this.localiteClient = localiteClient;
//    }

    public TypeClient getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

    public Set<Facture> getFactures() {
        return factures;
    }

    public void setFactures(Set<Facture> factures) {
        this.factures = factures;
    }

    public Localite getLocalite() {
        return localite;
    }

    public void setLocalite(Localite localite) {
        this.localite = localite;
    }

    public Categorieclient getCategorieclient() {
        return categorieclient;
    }

    public void setCategorieclient(Categorieclient categorieclient) {
        this.categorieclient = categorieclient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        if (client.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", nomClient='" + nomClient + '\'' +
            ", telephoneClient='" + telephoneClient + '\'' +
            ", boitepostale='" + boitepostale + '\'' +
            ", typeClient=" + typeClient +
            ", factures=" + factures +
            ", localite=" + localite +
            ", categorieclient=" + categorieclient +
            '}';
    }


}
