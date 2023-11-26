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
import java.util.Objects;
import java.util.Set;

import com.webstocker.domain.enumeration.TypeSortie;

import com.webstocker.domain.enumeration.TypeVente;

import com.webstocker.domain.enumeration.StatusTransfert;

/**
 * A BonDeSortie.
 */
@Entity
@Table(name = "bon_de_sortie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bondesortie")
public class BonDeSortie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "daate_creation", nullable = false)
    private LocalDate daateCreation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_sortie", nullable = false)
    private TypeSortie typeSortie;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_vente")
    private TypeVente typeVente;

    @Column(name = "print_status")
    private Boolean printStatus;

    @Column(name = "numero_facture_normalise")
    private String numeroFactureNormalise;

    @Column(name = "date_reception")
    private LocalDate dateReceptionTransfert;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_tranfert")
    private StatusTransfert statusTranfert;

    @ManyToOne
    private Magasin magasin;

    @ManyToOne
    private Magasin destination;

    @ManyToOne
    private User demandeur;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "bonDeSortie", cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LigneBonDeSortie> ligneBonDeSorties = new HashSet<>();

    @OneToOne(mappedBy = "bonDeSortie", cascade = CascadeType.REMOVE)
    @JsonIgnore
    Facture facture;

    @ManyToOne
    private User emetteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDaateCreation() {
        return daateCreation;
    }

    public void setDaateCreation(LocalDate daateCreation) {
        this.daateCreation = daateCreation;
    }

    public TypeSortie getTypeSortie() {
        return typeSortie;
    }

    public void setTypeSortie(TypeSortie typeSortie) {
        this.typeSortie = typeSortie;
    }

    public TypeVente getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(TypeVente typeVente) {
        this.typeVente = typeVente;
    }

    public Boolean isPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Boolean printStatus) {
        this.printStatus = printStatus;
    }

    public String getNumeroFactureNormalise() {
        return numeroFactureNormalise;
    }

    public void setNumeroFactureNormalise(String numeroFactureNormalise) {
        this.numeroFactureNormalise = numeroFactureNormalise;
    }

    public StatusTransfert getStatusTranfert() {
        return statusTranfert;
    }

    public void setStatusTranfert(StatusTransfert statusTranfert) {
        this.statusTranfert = statusTranfert;
    }

    public LocalDate getDateReceptionTransfert() {
        return dateReceptionTransfert;
    }

    public void setDateReceptionTransfert(LocalDate dateReceptionTransfert) {
        this.dateReceptionTransfert = dateReceptionTransfert;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Magasin getDestination() {
        return destination;
    }

    public void setDestination(Magasin magasin) {
        this.destination = magasin;
    }

    public User getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(User user) {
        this.demandeur = user;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<LigneBonDeSortie> getLigneBonDeSorties() {
        return ligneBonDeSorties;
    }

    public void addLigneBonDeSortie(LigneBonDeSortie ligneBonDeSortie) {
        ligneBonDeSorties.add(ligneBonDeSortie);
        ligneBonDeSortie.setBonDeSortie(this);
        // TODO recuperer le produit dès le forulaire de ommande et l'associer a la ligne de commande
        //lignecommande.setProduit(this.getFabricant().getProduit());
    }

    public User getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(User user) {
        this.emetteur = user;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BonDeSortie bonDeSortie = (BonDeSortie) o;
        if (bonDeSortie.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bonDeSortie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BonDeSortie{"
                + "id=" + id
                + ", numero='" + numero + "'"
                + ", daateCreation='" + daateCreation + "'"
                + ", typeSortie='" + typeSortie + "'"
                + ", typeVente='" + typeVente + "'"
                + ", printStatus='" + printStatus + "'"
                + ", numeroFactureNormalise='" + numeroFactureNormalise + "'"
                + ", statusTranfert='" + statusTranfert + "'"
                +", dateReceptionTransfert='" + dateReceptionTransfert + "'"
                + '}';

    }

}
