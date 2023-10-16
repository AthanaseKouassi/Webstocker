package com.webstocker.reports;

import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;
import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ChiffreDesAffairesWrapper {

    private String nomClient;
    private Long quantiteVendue;
//    private BigDecimal chiffreAffaire;
    private Long chiffreAffaire;
    private String telephone;
    private String nomProduit;
    private String localiteClient;
    private String nomCategorie;
    private String nomMagasin;

    public ChiffreDesAffairesWrapper(ChiffreAffaireWrapper caw) {
        this.nomClient = caw.getNomClient();
        this.nomProduit = caw.getNomProduit();
        this.chiffreAffaire = caw.getChiffreAffaire();
        this.quantiteVendue = caw.getQuantiteVendue();
        this.nomMagasin = caw.getNommagasin();
//        this.localiteClient = caw.getBonDeSortie().getClient().getLocalite().getCommunes().getVille() != null ? caw.getBonDeSortie().getClient().getLocalite().getCommunes().getVille().getLibelle() : "";
//        this.telephone = caw.getBonDeSortie().getFacture().getClient()!= null ? caw.getBonDeSortie().getFacture().getClient().getTelephoneClient() : " ";
//        this.nomCategorie = caw.getBonDeSortie().getClient().getCategorieclient().getLibelleCategorieclient();
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public Long getQuantiteVendue() {
        return quantiteVendue;
    }

    public void setQuantiteVendue(Long quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }

    public Long getChiffreAffaire() {
        return chiffreAffaire;
    }

    public void setChiffreAffaire(Long chiffreAffaire) {
        this.chiffreAffaire = chiffreAffaire;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getLocaliteClient() {
        return localiteClient;
    }

    public void setLocaliteClient(String localiteClient) {
        this.localiteClient = localiteClient;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

}
