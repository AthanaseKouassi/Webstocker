package com.webstocker.web.rest.dto;

import com.webstocker.domain.*;
import com.webstocker.domain.enumeration.StatusTransfert;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.enumeration.TypeVente;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by komi on 21/07/16.
 */
public class BonDeSortieDTO {

    private Long id;

    private Long remise;

    private String numero;

    private String numeroFactureNormalise;

    private LocalDate daateCreation;

    private TypeSortie typeSortie;

    private TypeVente typeVente;

    private User demandeur;

    private Client client;

    private Integer delaiPaiement;

    private Magasin magasin;

    private Magasin destination;
   
    private LocalDate dateFacture;
   
    private LocalDate dateReceptionTransfert;
    
    private StatusTransfert statusTranfert;
    
    private User emetteur;

    List<LigneBonDeSortie> ligneBonDeSorties;

    public BonDeSortieDTO() {
    }

    public BonDeSortie createBonDeSortieVente(){
        BonDeSortie bonDeSortie = new BonDeSortie();
        bonDeSortie.setId(getId());
        bonDeSortie.setClient(getClient());
        bonDeSortie.setDemandeur(getDemandeur());
        bonDeSortie.setNumero(getNumero());
        bonDeSortie.setNumeroFactureNormalise(getNumeroFactureNormalise());
        bonDeSortie.setTypeSortie(getTypeSortie());
        bonDeSortie.setDaateCreation(getDaateCreation());
        bonDeSortie.setTypeVente(getTypeVente());
        bonDeSortie.setMagasin(getMagasin());

        return bonDeSortie;
    }

    public BonDeSortie createBonDeSortie(){
        BonDeSortie bonDeSortie = createBonDeSortieVente();

        bonDeSortie.setMagasin(getMagasin());
        bonDeSortie.setDestination(getDestination());
        bonDeSortie.setStatusTranfert(getStatusTranfert());
        bonDeSortie.setDateReceptionTransfert(getDateReceptionTransfert());
        bonDeSortie.setEmetteur(getEmetteur());
        return bonDeSortie;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }
    
    public List<LigneBonDeSortie> getLigneBonDeSorties() {
        return ligneBonDeSorties;
    }

    public void setLigneBonDeSorties(List<LigneBonDeSortie> ligneBonDeSorties) {
        this.ligneBonDeSorties = ligneBonDeSorties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRemise() {
        return remise;
    }

    public void setRemise(Long remise) {
        this.remise = remise;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroFactureNormalise() {
        return numeroFactureNormalise;
    }

    public void setNumeroFactureNormalise(String numeroFactureNormalise) {
        this.numeroFactureNormalise = numeroFactureNormalise;
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

    public User getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(User demandeur) {
        this.demandeur = demandeur;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public void setDestination(Magasin destination) {
        this.destination = destination;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public LocalDate getDateReceptionTransfert() {
        return dateReceptionTransfert;
    }

    public void setDateReceptionTransfert(LocalDate dateReceptionTransfert) {
        this.dateReceptionTransfert = dateReceptionTransfert;
    }

    public StatusTransfert getStatusTranfert() {
        return statusTranfert;
    }

    public void setStatusTranfert(StatusTransfert statusTranfert) {
        this.statusTranfert = statusTranfert;
    }

    public User getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(User emetteur) {
        this.emetteur = emetteur;
    }
    
    
}
