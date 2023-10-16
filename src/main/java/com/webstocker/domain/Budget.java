package com.webstocker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Budget.
 */
@Entity
@Table(name = "budget")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "budget")
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "montant_budget")
    private Long montantBudget;

    @OneToMany(mappedBy = "budget")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LigneBudget> ligneBudgets = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMontantBudget() {
        return montantBudget;
    }

    public void setMontantBudget(Long montantBudget) {
        this.montantBudget = montantBudget;
    }

    public Set<LigneBudget> getLigneBudgets() {
        return ligneBudgets;
    }

    public void setLigneBudgets(Set<LigneBudget> ligneBudgets) {
        this.ligneBudgets = ligneBudgets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Budget budget = (Budget) o;
        if(budget.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, budget.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Budget{" +
            "id=" + id +
            ", montantBudget='" + montantBudget + "'" +
            '}';
    }
}
