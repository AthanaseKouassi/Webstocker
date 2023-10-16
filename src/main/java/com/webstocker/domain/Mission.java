package com.webstocker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Mission.
 */
@Entity
@Table(name = "mission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mission")
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @NotNull
    @Column(name = "objectif_general", nullable = false)
    private String objectifGeneral;

    @Column(name = "objectif_specifique")
    private String objectifSpecifique;

    @OneToMany(mappedBy = "mission",cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LigneMissionActivite> ligneMissionActivites = new HashSet<>();

    @OneToMany(mappedBy = "mission",cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LigneBudget> ligneBudgets = new HashSet<>();

    @ManyToOne
    private Localite localite;

    @ManyToOne
    private Cellule cellule;

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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getObjectifGeneral() {
        return objectifGeneral;
    }

    public void setObjectifGeneral(String objectifGeneral) {
        this.objectifGeneral = objectifGeneral;
    }

    public String getObjectifSpecifique() {
        return objectifSpecifique;
    }

    public void setObjectifSpecifique(String objectifSpecifique) {
        this.objectifSpecifique = objectifSpecifique;
    }

    public Set<LigneMissionActivite> getLigneMissionActivites() {
        return ligneMissionActivites;
    }

    public void setLigneMissionActivites(Set<LigneMissionActivite> ligneMissionActivites) {
        this.ligneMissionActivites = ligneMissionActivites;
    }

    public Set<LigneBudget> getLigneBudgets() {
        return ligneBudgets;
    }

    public void setLigneBudgets(Set<LigneBudget> ligneBudgets) {
        this.ligneBudgets = ligneBudgets;
    }
    
    public void addLigneBudgets(LigneBudget ligneBudget) {
        if(this.ligneBudgets==null){
            this.ligneBudgets = new HashSet<>();
        }
        this.ligneBudgets.add(ligneBudget);
        ligneBudget.setMission(this);
    }
    
    public void addLigneActivite(LigneMissionActivite ligneMissionActivite) {
        if(this.ligneMissionActivites==null){
            this.ligneMissionActivites = new HashSet<>();
        }
        this.ligneMissionActivites.add(ligneMissionActivite);
        ligneMissionActivite.setMission(this);
    }

    public Localite getLocalite() {
        return localite;
    }

    public void setLocalite(Localite localite) {
        this.localite = localite;
    }

    public Cellule getCellule() {
        return cellule;
    }

    public void setCellule(Cellule cellule) {
        this.cellule = cellule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mission mission = (Mission) o;
        if(mission.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mission.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mission{" +
            "id=" + id +
            ", libelle='" + libelle + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", objectifGeneral='" + objectifGeneral + "'" +
            ", objectifSpecifique='" + objectifSpecifique + "'" +
            '}';
    }
}
