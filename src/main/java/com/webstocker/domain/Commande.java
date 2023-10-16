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

import com.webstocker.domain.enumeration.StatutCommande;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_commande", nullable = false)
    private LocalDate dateCommande;

    @NotNull
    @Column(name = "numero_commande", nullable = false)
    private String numeroCommande;

    @Column(name = "quantite_commande")
    private Long quantiteCommande;

    @NotNull
    @Column(name = "valeur_commande", nullable = false)
    private Long valeurCommande;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutCommande statut=StatutCommande.EN_COURS;

    @OneToMany(mappedBy = "commande")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Livraison> livraisons = new HashSet<>();

    @OneToMany(mappedBy = "commande",cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lignecommande> lignecommandes = new HashSet<>();

    @ManyToOne
    private Bailleur bailleur;

    @ManyToOne
    private Fabricant fabricant;

    @ManyToOne
    private Produit produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public Long getQuantiteCommande() {
        return quantiteCommande;
    }

    public void setQuantiteCommande(Long quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }

    public Long getValeurCommande() {
        return valeurCommande;
    }

    public void setValeurCommande(Long valeurCommande) {
        this.valeurCommande = valeurCommande;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public Set<Livraison> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(Set<Livraison> livraisons) {
        this.livraisons = livraisons;
    }

    public Set<Lignecommande> getLignecommandes() {
        return lignecommandes;
    }

    public void setLignecommandes(Set<Lignecommande> lignecommandes) {
        this.lignecommandes = lignecommandes;
    }

    public Bailleur getBailleur() {
        return bailleur;
    }

    public void setBailleur(Bailleur bailleur) {
        this.bailleur = bailleur;
    }

    public Fabricant getFabricant() {
        return fabricant;
    }

    public void setFabricant(Fabricant fabricant) {
        this.fabricant = fabricant;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public void addLigneCommande(Lignecommande lignecommande){
        lignecommandes.add(lignecommande);
        lignecommande.setCommande(this);
        // TODO recuperer le produit dès le forulaire de ommande et l'associer a la ligne de commande
        //lignecommande.setProduit(this.getFabricant().getProduit());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commande commande = (Commande) o;
        if(commande.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, commande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Commande{" +
            "id=" + id +
            ", dateCommande='" + dateCommande + "'" +
            ", numeroCommande='" + numeroCommande + "'" +
            ", quantiteCommande='" + quantiteCommande + "'" +
            ", valeurCommande='" + valeurCommande + "'" +
            ", statut='" + statut + "'" +
            '}';
    }
}
