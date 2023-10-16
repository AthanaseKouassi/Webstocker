package com.webstocker.web.rest.dto;

import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignecommande;

import java.util.List;

/**
 * Created by komi on 23/06/16.
 */
public class CommandeFournisseur {

    Commande commande;
    List<Lignecommande> lignecommandes;

    public CommandeFournisseur() {
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public List<Lignecommande> getLignecommandes() {
        return lignecommandes;
    }

    public void setLignecommandes(List<Lignecommande> lignecommandes) {
        this.lignecommandes = lignecommandes;
    }
}
