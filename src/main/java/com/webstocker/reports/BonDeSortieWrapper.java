package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.enumeration.TypeSortie;

/**
 *
 * @author Athanase
 */
public class BonDeSortieWrapper {

    String numeroBondeSortie;
    String dateBonDeSortie;
    String demandeur;
    String magasinDestinataire;
    String nomProduit;
    String nomClient;
    String magasinOrigine;
    String typeSortie;
    Long quantite;
    String numeroLot;

    public BonDeSortieWrapper(LigneBonDeSortie lignebondesortie) {
        this.dateBonDeSortie = lignebondesortie.getBonDeSortie() != null? lignebondesortie.getBonDeSortie().getDaateCreation().toString() :"";
        this.demandeur = lignebondesortie.getBonDeSortie().getDemandeur().getLastName();
        this.magasinDestinataire = lignebondesortie.getBonDeSortie().getDestination() != null ? lignebondesortie.getBonDeSortie().getDestination().getNomMagasin() : "pas de destinataire";
        this.nomProduit = lignebondesortie.getLot().getProduit().getNomProduit();
        this.magasinOrigine = lignebondesortie.getBonDeSortie().getMagasin()!= null? lignebondesortie.getBonDeSortie().getMagasin().getNomMagasin():" ";
        this.nomClient = lignebondesortie.getBonDeSortie().getClient() != null ? lignebondesortie.getBonDeSortie().getClient().getNomClient() : " ";
        this.quantite = lignebondesortie.getQuantite();
        this.numeroLot = lignebondesortie.getLot().getNumeroLot().toString();
        this.numeroBondeSortie = lignebondesortie.getBonDeSortie().getNumero();
        this.typeSortie = lignebondesortie.getBonDeSortie().getTypeSortie().toString();

    }

    public String getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    public String getDateBonDeSortie() {
        return dateBonDeSortie;
    }

    public void setDateBonDeSortie(String dateBonDeSortie) {
        this.dateBonDeSortie = dateBonDeSortie;
    }

    public String getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(String demandeur) {
        this.demandeur = demandeur;
    }

    public String getMagasinDestinataire() {
        return magasinDestinataire;
    }

    public void setMagasinDestinataire(String magasinDestinataire) {
        this.magasinDestinataire = magasinDestinataire;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getMagasinOrigine() {
        return magasinOrigine;
    }

    public void setMagasinOrigine(String magasinOrigine) {
        this.magasinOrigine = magasinOrigine;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public String getNumeroBondeSortie() {
        return numeroBondeSortie;
    }

    public void setNumeroBondeSortie(String numeroBondeSortie) {
        this.numeroBondeSortie = numeroBondeSortie;
    }

    public String getTypeSortie() {
        return typeSortie;
    }

    public void setTypeSortie(String typeSortie) {
        this.typeSortie = typeSortie;
    }

}
