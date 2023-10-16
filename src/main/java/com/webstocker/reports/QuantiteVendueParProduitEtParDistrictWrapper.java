package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.VenteParDistrictWrapper;

/**
 *
 * @author Athanase
 */
public class QuantiteVendueParProduitEtParDistrictWrapper {
    String nomProduit;
    String ville;
    Long quantite;
    String dateVente;
    
    public QuantiteVendueParProduitEtParDistrictWrapper(VenteParDistrictWrapper venteParDistrict){
        this.nomProduit = venteParDistrict.getProduit();
        this.ville = venteParDistrict.getVille();
        this.quantite = venteParDistrict.getQuantiteVendue();
//        this.dateVente = venteParDistrict.getBonDeSortie().getDaateCreation().toString();
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public String getDateVente() {
        return dateVente;
    }

    public void setDateVente(String dateVente) {
        this.dateVente = dateVente;
    }
    
    
}
