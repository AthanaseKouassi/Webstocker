package com.webstocker.domain.wrapper;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Categorieclient;
import java.math.BigDecimal;

/**
 *
 * @author Athanase
 */
public class ChiffreAffaireWrapper {
    private String nomClient;
    private String telephoneClient;
    private String localiteClient;
    private Categorieclient categorieclient;
    private String nomProduit;
    private String nommagasin;
    private Long quantiteVendue;
//    private BigDecimal chiffreAffaire;
    private Long chiffreAffaire;
    private BonDeSortie bonDeSortie;
    private String libellecategorie;
    

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getNommagasin() {
        return nommagasin;
    }

    public void setNommagasin(String nommagasin) {
        this.nommagasin = nommagasin;
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

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public Categorieclient getCategorieclient() {
        return categorieclient;
    }

    public void setCategorieclient(Categorieclient categorieclient) {
        this.categorieclient = categorieclient;
    }

    public BonDeSortie getBonDeSortie() {
        return bonDeSortie;
    }

    public void setBonDeSortie(BonDeSortie bonDeSortie) {
        this.bonDeSortie = bonDeSortie;
    }

    public String getTelephoneClient() {
        return telephoneClient;
    }

    public void setTelephoneClient(String telephoneClient) {
        this.telephoneClient = telephoneClient;
    }

    public String getLocaliteClient() {
        return localiteClient;
    }

    public void setLocaliteClient(String localiteClient) {
        this.localiteClient = localiteClient;
    }

    public String getLibellecategorie() {
        return libellecategorie;
    }

    public void setLibellecategorie(String libellecategorie) {
        this.libellecategorie = libellecategorie;
    }
    
    
    
}
