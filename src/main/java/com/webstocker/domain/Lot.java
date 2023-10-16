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
 * A Lot.
 */
@Entity
@Table(name = "lot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lot")
public class Lot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "numero_lot", nullable = false)
    private Long numeroLot;

    @NotNull
    @Column(name = "date_fabrication", nullable = false)
    private LocalDate dateFabrication;

    @NotNull
    @Column(name = "date_peremption", nullable = false)
    private LocalDate datePeremption;

    @Column(name = "description_lot")
    private String descriptionLot;

    @NotNull
    @Column(name = "quantite_lot", nullable = false)
    private Long quantiteLot;

    @Column(name = "quantite_carton_lot")
    private Long quantiteCartonLot;

    @Column(name = "quantite_sortie")
    private Long quantiteSortie=0L;

    @ManyToOne
    private Produit produit;

    @OneToMany(mappedBy = "lot")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lignelivraison> lignelivraisons = new HashSet<>();

    @Transient
    private boolean isComplete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(Long numeroLot) {
        this.numeroLot = numeroLot;
    }

    public LocalDate getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(LocalDate dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public String getDescriptionLot() {
        return descriptionLot;
    }

    public void setDescriptionLot(String descriptionLot) {
        this.descriptionLot = descriptionLot;
    }

    public Long getQuantiteLot() {
        return quantiteLot;
    }

    public void setQuantiteLot(Long quantiteLot) {
        this.quantiteLot = quantiteLot;
    }

    public Long getQuantiteCartonLot() {
        return quantiteCartonLot;
    }

    public void setQuantiteCartonLot(Long quantiteCartonLot) {
        this.quantiteCartonLot = quantiteCartonLot;
    }

    public Long getQuantiteSortie() {
        return quantiteSortie;
    }

    public void setQuantiteSortie(Long quantiteSortie) {
        this.quantiteSortie += quantiteSortie;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Set<Lignelivraison> getLignelivraisons() {
        return lignelivraisons;
    }

    public void setLignelivraisons(Set<Lignelivraison> lignelivraisons) {
        this.lignelivraisons = lignelivraisons;
    }

    public boolean isComplete() {
        return quantiteLot==quantiteSortie;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public Long quantiteRestante(){
        return quantiteLot-quantiteSortie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lot lot = (Lot) o;
        if(lot.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lot{" +
            "id=" + id +
            ", numeroLot='" + numeroLot + "'" +
            ", dateFabrication='" + dateFabrication + "'" +
            ", datePeremption='" + datePeremption + "'" +
            ", descriptionLot='" + descriptionLot + "'" +
            ", quantiteLot='" + quantiteLot + "'" +
            ", quantiteCartonLot='" + quantiteCartonLot + "'" +
            ", quantiteSortie='" + quantiteSortie + "'" +
            '}';
    }
}
