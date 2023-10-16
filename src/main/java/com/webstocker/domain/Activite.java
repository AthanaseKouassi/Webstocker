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
 * A Activite.
 */
@Entity
@Table(name = "activite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "activite")
public class Activite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nom_activite", nullable = false)
    private String nomActivite;

    @Column(name = "description_activite")
    private String descriptionActivite;

    @NotNull
    @Column(name = "resultat_attendu", nullable = false)
    private String resultatAttendu;

    @OneToMany(mappedBy = "activite",cascade=CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LigneMissionActivite> ligneMissionActivites = new HashSet<>();

    @ManyToOne
    private TypeActivite typeActivite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomActivite() {
        return nomActivite;
    }

    public void setNomActivite(String nomActivite) {
        this.nomActivite = nomActivite;
    }

    public String getDescriptionActivite() {
        return descriptionActivite;
    }

    public void setDescriptionActivite(String descriptionActivite) {
        this.descriptionActivite = descriptionActivite;
    }

    public String getResultatAttendu() {
        return resultatAttendu;
    }

    public void setResultatAttendu(String resultatAttendu) {
        this.resultatAttendu = resultatAttendu;
    }

    public Set<LigneMissionActivite> getLigneMissionActivites() {
        return ligneMissionActivites;
    }
    
    
    public void addLigneMissionActivites(LigneMissionActivite ligneMissionActivites) {
        if(this.ligneMissionActivites == null)
            this.ligneMissionActivites = new HashSet<>();
        this.ligneMissionActivites.add(ligneMissionActivites);
    }
    

    public void setLigneMissionActivites(Set<LigneMissionActivite> ligneMissionActivites) {
        this.ligneMissionActivites = ligneMissionActivites;
    }

    public TypeActivite getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(TypeActivite typeActivite) {
        this.typeActivite = typeActivite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Activite activite = (Activite) o;
        if(activite.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, activite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Activite{" +
            "id=" + id +
            ", nomActivite='" + nomActivite + "'" +
            ", descriptionActivite='" + descriptionActivite + "'" +
            ", resultatAttendu='" + resultatAttendu + "'" +
            '}';
    }
}
