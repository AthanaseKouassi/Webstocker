package com.webstocker.reports;

import com.webstocker.domain.Lignecommande;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Athanase
 */
public class CommandeWrapper {

    String dateCommande;
    String bailleur;
    String fabricant;
    String paysFabricant;
    String dateFabrication;
    int quantite;
    String produit;
    String numeroCommande;
    String quantiteTotalCommande;

    public CommandeWrapper(Lignecommande lignecommande) {
        this.dateCommande = lignecommande.getCommande().getDateCommande().toString();
        this.bailleur = lignecommande.getCommande().getBailleur().getNomBailleur();
        this.fabricant = lignecommande.getCommande().getFabricant().getNomFabricant();
        this.paysFabricant = lignecommande.getCommande().getFabricant().getPaysFabricant();
        this.dateFabrication = lignecommande.getDateFabrication().toString();
        this.quantite = lignecommande.getQuantiteLigneCommande();
        this.produit = lignecommande.getProduit().getNomProduit();
        this.numeroCommande = lignecommande.getCommande().getNumeroCommande();
        this.quantiteTotalCommande = lignecommande.getCommande().getQuantiteCommande().toString();
    }

    public String getPaysFabricant() {
        return paysFabricant;
    }

    public void setPaysFabricant(String paysFabricant) {
        this.paysFabricant = paysFabricant;
    }

    public String getQuantiteTotalCommande() {
        return quantiteTotalCommande;
    }

    public void setQuantiteTotalCommande(String quantiteTotalCommande) {
        this.quantiteTotalCommande = quantiteTotalCommande;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(String dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getBailleur() {
        return bailleur;
    }

    public void setBailleur(String bailleur) {
        this.bailleur = bailleur;
    }

    public String getFabricant() {
        return fabricant;
    }

    public void setFabricant(String fabricant) {
        this.fabricant = fabricant;
    }

    public String getDateFabrication() {
        return convertirDate(dateFabrication);
    }

    public void setDateFabrication(String dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    private String convertirDate(String dateAConvertir) {
        String dateConvertie = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = sdf.parse(dateAConvertir);
            dateConvertie = sdf2.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        // System.out.println("La date de la commande "+dateConvertie);
        return dateConvertie;
    }

}
