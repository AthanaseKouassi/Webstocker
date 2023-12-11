package com.webstocker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webstocker.domain.enumeration.newfeature.StatutFacture;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "facture")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_facture", nullable = false)
    private LocalDate dateFacture;

    @Column(name = "valeur_remise")
    private Integer valeurRemise;

    @Column(name = "delai_paiement")
    private Integer delaiPaiement;

    @Column(name = "date_limite_paiement")
    private LocalDate dateLimitePaiement;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "facture")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reglement> reglements = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private BonDeSortie bonDeSortie;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutFacture statutFacture;

    @Column(name = "numero")
    private String numero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Integer getValeurRemise() {
        return valeurRemise;
    }

    public void setValeurRemise(Integer valeurRemise) {
        this.valeurRemise = valeurRemise;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public LocalDate getDateLimitePaiement() {
        return dateLimitePaiement;
    }

    public void setDateLimitePaiement(LocalDate dateLimitePaiement) {
        this.dateLimitePaiement = dateLimitePaiement;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Reglement> getReglements() {
        return reglements;
    }

    public void setReglements(Set<Reglement> reglements) {
        this.reglements = reglements;
    }

    public BonDeSortie getBonDeSortie() {
        return bonDeSortie;
    }

    public void setBonDeSortie(BonDeSortie bonDeSortie) {
        this.bonDeSortie = bonDeSortie;
    }

    public StatutFacture getStatutFacture() {
        return statutFacture;
    }

    public void setStatutFacture(StatutFacture statutFacture) {
        this.statutFacture = statutFacture;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facture facture = (Facture) o;
        if (facture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, facture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
