package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A LigneMissionActivite.
 */
@Entity
@Table(name = "ligne_mission_activite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignemissionactivite")
public class LigneMissionActivite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "resultat_obtenu")
    private String resultatObtenu;

    @Column(name = "date_resultat")
    private LocalDate dateResultat;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Activite activite;

    @ManyToOne
    private Mission mission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResultatObtenu() {
        return resultatObtenu;
    }

    public void setResultatObtenu(String resultatObtenu) {
        this.resultatObtenu = resultatObtenu;
    }

    public LocalDate getDateResultat() {
        return dateResultat;
    }

    public void setDateResultat(LocalDate dateResultat) {
        this.dateResultat = dateResultat;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
        activite.addLigneMissionActivites(this);
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LigneMissionActivite ligneMissionActivite = (LigneMissionActivite) o;
        if(ligneMissionActivite.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ligneMissionActivite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LigneMissionActivite{" +
            "id=" + id +
            ", resultatObtenu='" + resultatObtenu + "'" +
            ", dateResultat='" + dateResultat + "'" +
            '}';
    }
}
