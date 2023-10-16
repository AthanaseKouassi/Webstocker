package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LigneBudget.
 */
@Entity
@Table(name = "ligne_budget")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignebudget")
public class LigneBudget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "libelle_ligne_budget", nullable = false)
    private String libelleLigneBudget;

    @NotNull
    @Column(name = "montant_ligne_budget", nullable = false)
    private Long montantLigneBudget;

    @ManyToOne
    private Budget budget;

    @ManyToOne
    private Mission mission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleLigneBudget() {
        return libelleLigneBudget;
    }

    public void setLibelleLigneBudget(String libelleLigneBudget) {
        this.libelleLigneBudget = libelleLigneBudget;
    }

    public Long getMontantLigneBudget() {
        return montantLigneBudget;
    }

    public void setMontantLigneBudget(Long montantLigneBudget) {
        this.montantLigneBudget = montantLigneBudget;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
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
        LigneBudget ligneBudget = (LigneBudget) o;
        if(ligneBudget.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ligneBudget.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LigneBudget{" +
            "id=" + id +
            ", libelleLigneBudget='" + libelleLigneBudget + "'" +
            ", montantLigneBudget='" + montantLigneBudget + "'" +
            '}';
    }
}
