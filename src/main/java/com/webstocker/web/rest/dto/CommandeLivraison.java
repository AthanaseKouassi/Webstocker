package com.webstocker.web.rest.dto;

import com.webstocker.domain.Lignecommande;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;

import java.util.List;

/**
 * Created by komi on 23/06/16.
 */
public class CommandeLivraison {

    Livraison livraison;
    List<Lignelivraison> lignelivraisons;

    public CommandeLivraison() {
    }

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public List<Lignelivraison> getLignelivraisons() {
        return lignelivraisons;
    }

    public void setLignelivraisons(List<Lignelivraison> lignelivraisons) {
        this.lignelivraisons = lignelivraisons;
    }
}
