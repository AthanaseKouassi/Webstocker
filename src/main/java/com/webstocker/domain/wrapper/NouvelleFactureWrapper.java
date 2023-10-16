
package com.webstocker.domain.wrapper;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Produit;

/**
 *
 * @author Athanase
 */
public class NouvelleFactureWrapper {
 
    private Long prixDeVente;
    private Long quantiteProduit;
    private String nomProduit;
    private BonDeSortie bonDeSortie;
    
    

    public Long getPrixDeVente() {
        return prixDeVente;
    }

    public void setPrixDeVente(Long prixDeVente) {
        this.prixDeVente = prixDeVente;
    }

    public Long getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(Long quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public BonDeSortie getBonDeSortie() {
        return bonDeSortie;
    }

    public void setBonDeSortie(BonDeSortie bonDeSortie) {
        this.bonDeSortie = bonDeSortie;
    }
    
    
    
    
    
}
