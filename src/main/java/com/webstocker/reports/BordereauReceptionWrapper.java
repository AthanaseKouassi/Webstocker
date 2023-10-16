package com.webstocker.reports;

import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;
import com.webstocker.domain.Produit;
import static java.time.Clock.system;

/**
 *
 * @author Athanase
 */
public class BordereauReceptionWrapper {
    /**
     * Les numeros de lots sont connus à la livraison des produits et donc pas avant
     */
    String dateLivraison;
    String fabricant;    
    String nomProduit;
    String magasin;
    String numeroCommandelivrée;
    String numeroLivraison;
    
    String numeroLot;
    long valeurLivraison;
    long fraisTransit;
    long fraisTest;
    long fraisAssuranceLocale;
    long fraisManutention;
    
    int quantiteRecu;
    // int prixUnitaire;
    int quantiteTotaleProduit = 0;
   

    public BordereauReceptionWrapper(Lignelivraison lignelivraison) {
        
        this.dateLivraison = lignelivraison.getLivraison().getDateLivraison().toString();
        this.fabricant = lignelivraison.getLivraison().getCommande().getFabricant().getNomFabricant();
        this.numeroLot = lignelivraison.getLot().getNumeroLot().toString();
        this.magasin = lignelivraison.getLivraison().getMagasin().getNomMagasin();
        this.numeroCommandelivrée = lignelivraison.getLivraison().getCommande().getNumeroCommande();
        //TODO la relation entre commande et produit a changé
        this.nomProduit = lignelivraison.getLot().getProduit().getNomProduit();
        this.quantiteRecu = lignelivraison.getQuantiteLotLivre();
        // this.prixUnitaire = prixUnitaire;
        this.quantiteTotaleProduit = getQuantiteTotaleProduit();
        this.numeroLivraison = lignelivraison.getLivraison().getNumeroLivraison();
        this.valeurLivraison = lignelivraison.getLivraison().getValeurLivraison();
        this.fraisTransit = lignelivraison.getLivraison().getFraisTransit();
        this.fraisTest = lignelivraison.getLivraison().getFraisTest();
        this.fraisManutention = lignelivraison.getLivraison().getFraisManutention();
        this.fraisAssuranceLocale = lignelivraison.getLivraison().getFraisAssuranceLocale();
        
    }

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
    }

    public String getNumeroCommandelivrée() {
        return numeroCommandelivrée;
    }

    public void setNumeroCommandelivrée(String numeroCommandelivrée) {
        this.numeroCommandelivrée = numeroCommandelivrée;
    }

    public String getNumeroLivraison() {
        return numeroLivraison;
    }

    public void setNumeroLivraison(String numeroLivraison) {
        this.numeroLivraison = numeroLivraison;
    }

    public long getValeurLivraison() {
        return valeurLivraison;
    }

    public void setValeurLivraison(long valeurLivraison) {
        this.valeurLivraison = valeurLivraison;
    }

    public long getFraisTransit() {
        return fraisTransit;
    }

    public void setFraisTransit(long fraisTransit) {
        this.fraisTransit = fraisTransit;
    }

    public long getFraisTest() {
        return fraisTest;
    }

    public void setFraisTest(long fraisTest) {
        this.fraisTest = fraisTest;
    }

    public long getFraisAssuranceLocale() {
        return fraisAssuranceLocale;
    }

    public void setFraisAssuranceLocale(long fraisAssuranceLocale) {
        this.fraisAssuranceLocale = fraisAssuranceLocale;
    }

    public long getFraisManutention() {
        return fraisManutention;
    }

    public void setFraisManutention(long fraisManutention) {
        this.fraisManutention = fraisManutention;
    }

    public String getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(String dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getFabricant() {
        return fabricant;
    }

    public void setFabricant(String fabricant) {
        this.fabricant = fabricant;
    }

    public String getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public int getQuantiteRecu() {
        return quantiteRecu;
    }

    public void setQuantiteRecu(int quantiteRecu) {
        this.quantiteRecu = quantiteRecu;
    }

//    public int getPrixUnitaire() {
//        return prixUnitaire;
//    }
//
//    public void setPrixUnitaire(int prixUnitaire) {
//        this.prixUnitaire = prixUnitaire;
//    }

    public int getQuantiteTotaleProduit() {
        return quantiteTotaleProduit = + quantiteRecu;
    }

    public void setQuantiteTotaleProduit(int quantiteTotaleProduit) {
        this.quantiteTotaleProduit = quantiteTotaleProduit;
    }

    
}
