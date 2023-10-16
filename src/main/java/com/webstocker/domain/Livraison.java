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
 * A Livraison.
 */
@Entity
@Table(name = "livraison")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "livraison")
public class Livraison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_livraison", nullable = false)
    private LocalDate dateLivraison;

    @Column(name = "description_livraison")
    private String descriptionLivraison;

    @NotNull
    @Column(name = "numero_livraison", nullable = false)
    private String numeroLivraison;

    @NotNull
    @Column(name = "valeur_livraison", nullable = false)
    private Long valeurLivraison=0L;

    @NotNull
    @Column(name = "frais_test", nullable = false)
    private Long fraisTest=0L;

    @NotNull
    @Column(name = "frais_transit", nullable = false)
    private Long fraisTransit= 0L;

    @NotNull
    @Column(name = "frais_assurance_locale", nullable = false)
    private Long fraisAssuranceLocale= 0L;

    @NotNull
    @Column(name = "frais_manutention", nullable = false)
    private Long fraisManutention= 0L;

    @Column(name = "valeur_livraison_devise")
    private Long valeurLivraisonDevise;

    @Column(name = "devise")
    private String devise;

    @ManyToOne
    private Magasin magasin;

    @ManyToOne
    private Commande commande;

    @OneToMany(mappedBy = "livraison",cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Lignelivraison> lignelivraisons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getDescriptionLivraison() {
        return descriptionLivraison;
    }

    public void setDescriptionLivraison(String descriptionLivraison) {
        this.descriptionLivraison = descriptionLivraison;
    }

    public String getNumeroLivraison() {
        return numeroLivraison;
    }

    public void setNumeroLivraison(String numeroLivraison) {
        this.numeroLivraison = numeroLivraison;
    }

    public Long getValeurLivraison() {
        return valeurLivraison;
    }

    public void setValeurLivraison(Long valeurLivraison) {
        this.valeurLivraison = valeurLivraison;
    }

    public Long getFraisTest() {
        return fraisTest;
    }

    public void setFraisTest(Long fraisTest) {
        this.fraisTest = fraisTest;
    }

    public Long getFraisTransit() {
        return fraisTransit;
    }

    public void setFraisTransit(Long fraisTransit) {
        this.fraisTransit = fraisTransit;
    }

    public Long getFraisAssuranceLocale() {
        return fraisAssuranceLocale;
    }

    public void setFraisAssuranceLocale(Long fraisAssuranceLocale) {
        this.fraisAssuranceLocale = fraisAssuranceLocale;
    }

    public Long getFraisManutention() {
        return fraisManutention;
    }

    public void setFraisManutention(Long fraisManutention) {
        this.fraisManutention = fraisManutention;
    }

    public Long getValeurLivraisonDevise() {
        return valeurLivraisonDevise;
    }

    public void setValeurLivraisonDevise(Long valeurLivraisonDevise) {
        this.valeurLivraisonDevise = valeurLivraisonDevise;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Set<Lignelivraison> getLignelivraisons() {
        return lignelivraisons;
    }

    public void setLignelivraisons(Set<Lignelivraison> lignelivraisons) {
        this.lignelivraisons = lignelivraisons;
    }

    public void addLigneLivraison(Lignelivraison lignelivraison){
        lignelivraisons.add(lignelivraison);
        lignelivraison.setLivraison(this);
        //lignelivraison.setProduit(this.getFabricant().getProduit());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Livraison livraison = (Livraison) o;
        if(livraison.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, livraison.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Livraison{" +
            "id=" + id +
            ", dateLivraison='" + dateLivraison + "'" +
            ", descriptionLivraison='" + descriptionLivraison + "'" +
            ", numeroLivraison='" + numeroLivraison + "'" +
            ", valeurLivraison='" + valeurLivraison + "'" +
            ", fraisTest='" + fraisTest + "'" +
            ", fraisTransit='" + fraisTransit + "'" +
            ", fraisAssuranceLocale='" + fraisAssuranceLocale + "'" +
            ", fraisManutention='" + fraisManutention + "'" +
            ", valeurLivraisonDevise='" + valeurLivraisonDevise + "'" +
            ", devise='" + devise + "'" +
            '}';
    }
}
