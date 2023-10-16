package com.webstocker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Lignecommande.
 */
@Entity
@Table(name = "lignecommande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignecommande")
public class Lignecommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_fabrication", nullable = false)
    private LocalDate dateFabrication;

    @NotNull
    @Column(name = "quantite_ligne_commande", nullable = false)
    private Integer quantiteLigneCommande;

    @ManyToOne
    private Commande commande;

    @ManyToOne
    private Produit produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(LocalDate dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public Integer getQuantiteLigneCommande() {
        return quantiteLigneCommande;
    }

    public void setQuantiteLigneCommande(Integer quantiteLigneCommande) {
        this.quantiteLigneCommande = quantiteLigneCommande;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lignecommande lignecommande = (Lignecommande) o;
        if(lignecommande.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lignecommande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lignecommande{" +
            "id=" + id +
            ", dateFabrication='" + dateFabrication + "'" +
            ", quantiteLigneCommande='" + quantiteLigneCommande + "'" +
            '}';
    }
}
