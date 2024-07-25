package com.webstocker.web.rest.dto.newfeature;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;

import java.time.LocalDate;

public class InventaireDto {
    private Long id;
    private LocalDate dateInventaire;
    private Long stockFinalTheorique;
    private Long stockReel;
    private Produit produit;
    private Magasin magasin;
    private Long stockMagasinCentral;
    private Long stockAntenne;
    private Long stockAgent;
    private Long arrivage;
    private Long stockTheoDebut;
    private Long vente;
    private Long promo;
    private Long perteAbime;
    private Bailleur bailleur;
    private String commentaire;
    private Long ajustement;

    public InventaireDto() {
    }

    public InventaireDto(Long id, LocalDate dateInventaire, Long stockFinalTheorique, Long stockReel,
                         Produit produit, Magasin magasin, Long stockMagasinCentral, Long stockAntenne,
                         Long stockAgent, Long arrivage, Long stockTheoDebut, Long vente, Long promo,
                         Long perteAbime, Bailleur bailleur, String commentaire, Long ajustement) {
        this.id = id;
        this.dateInventaire = dateInventaire;
        this.stockFinalTheorique = stockFinalTheorique;
        this.stockReel = stockReel;
        this.produit = produit;
        this.magasin = magasin;
        this.stockMagasinCentral = stockMagasinCentral;
        this.stockAntenne = stockAntenne;
        this.stockAgent = stockAgent;
        this.arrivage = arrivage;
        this.stockTheoDebut = stockTheoDebut;
        this.vente = vente;
        this.promo = promo;
        this.perteAbime = perteAbime;
        this.bailleur = bailleur;
        this.commentaire = commentaire;
        this.ajustement = ajustement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(LocalDate dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public Long getStockFinalTheorique() {
        return stockFinalTheorique;
    }

    public void setStockFinalTheorique(Long stockFinalTheorique) {
        this.stockFinalTheorique = stockFinalTheorique;
    }

    public Long getStockReel() {
        return stockReel;
    }

    public void setStockReel(Long stockReel) {
        this.stockReel = stockReel;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Long getStockMagasinCentral() {
        return stockMagasinCentral;
    }

    public void setStockMagasinCentral(Long stockMagasinCentral) {
        this.stockMagasinCentral = stockMagasinCentral;
    }

    public Long getStockAntenne() {
        return stockAntenne;
    }

    public void setStockAntenne(Long stockAntenne) {
        this.stockAntenne = stockAntenne;
    }

    public Long getStockAgent() {
        return stockAgent;
    }

    public void setStockAgent(Long stockAgent) {
        this.stockAgent = stockAgent;
    }

    public Long getArrivage() {
        return arrivage;
    }

    public void setArrivage(Long arrivage) {
        this.arrivage = arrivage;
    }

    public Long getStockTheoDebut() {
        return stockTheoDebut;
    }

    public void setStockTheoDebut(Long stockTheoDebut) {
        this.stockTheoDebut = stockTheoDebut;
    }

    public Long getVente() {
        return vente;
    }

    public void setVente(Long vente) {
        this.vente = vente;
    }

    public Long getPromo() {
        return promo;
    }

    public void setPromo(Long promo) {
        this.promo = promo;
    }

    public Long getPerteAbime() {
        return perteAbime;
    }

    public void setPerteAbime(Long perteAbime) {
        this.perteAbime = perteAbime;
    }

    public Bailleur getBailleur() {
        return bailleur;
    }

    public void setBailleur(Bailleur bailleur) {
        this.bailleur = bailleur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getAjustement() {
        return ajustement;
    }

    public void setAjustement(Long ajustement) {
        this.ajustement = ajustement;
    }
}
