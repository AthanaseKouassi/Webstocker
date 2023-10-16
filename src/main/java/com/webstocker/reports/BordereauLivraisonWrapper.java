package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;

/**
 *
 * @author Athanase
 */
public class BordereauLivraisonWrapper {

    long numeroOrdre;
    String numeroBonsortie;
    long numeroBonEnlevement;
    String nomDemandeur;
    String dateBordereauLivraison;
    String nomClient;
    String nomProduit;
    String numeroLot;
    long quantiteLivre;
    long quantiteCartonLivre;

    public BordereauLivraisonWrapper(LigneBonDeSortie lignebondesortie) {
        this.nomProduit = lignebondesortie.getLot().getProduit().getNomProduit();
        this.numeroLot = lignebondesortie.getLot().getNumeroLot().toString();
        this.quantiteLivre = lignebondesortie.getQuantite();
        this.dateBordereauLivraison = lignebondesortie.getBonDeSortie().getDaateCreation().toString();
        this.numeroOrdre = lignebondesortie.getBonDeSortie().getId();
        this.nomDemandeur = lignebondesortie.getBonDeSortie().getDemandeur().getLastName();
        this.numeroBonsortie = lignebondesortie.getBonDeSortie().getNumero();

    }

    public long getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(long numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getNumeroBonsortie() {
        return numeroBonsortie;
    }

    public void setNumeroBonsortie(String numeroBonsortie) {
        this.numeroBonsortie = numeroBonsortie;
    }

    public long getNumeroBonEnlevement() {
        return numeroBonEnlevement;
    }

    public void setNumeroBonEnlevement(long numeroBonEnlevement) {
        this.numeroBonEnlevement = numeroBonEnlevement;
    }

    public String getNomDemandeur() {
        return nomDemandeur;
    }

    public void setNomDemandeur(String nomDemandeur) {
        this.nomDemandeur = nomDemandeur;
    }

    public String getDateBordereauLivraison() {
        return dateBordereauLivraison;
    }

    public void setDateBordereauLivraison(String dateBordereauLivraison) {
        this.dateBordereauLivraison = dateBordereauLivraison;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    public long getQuantiteLivre() {
        return quantiteLivre;
    }

    public void setQuantiteLivre(long quantiteLivre) {
        this.quantiteLivre = quantiteLivre;
    }

    public long getQuantiteCartonLivre() {
        return quantiteCartonLivre;
    }

    public void setQuantiteCartonLivre(long quantiteLivre) {

        this.quantiteCartonLivre = quantiteCartonLivre / 996;
    }

}
