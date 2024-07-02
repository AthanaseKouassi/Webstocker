package com.webstocker.service.newfeature;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.LignelivraisonRepository;
import com.webstocker.repository.ProduitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class CalendrierApprovisionnementService {

    @Inject
    LigneBonDeSortieRepository ligneBonDeSortieRepository;
    @Inject
    ProduitRepository produitRepository;
    @Inject
    LignelivraisonRepository lignelivraisonRepository;

    public List<EtatStockGlobalAimasWrapper> etatRecapitulatifVenteEtPromo(String dateDebut, String dateFin, Produit produit,
                                                                           Long stockReelMagasin, Long stockReelAntenne, Long stockReelCommerciaux) {
        final List<EtatStockGlobalAimasWrapper> listRecapitulatif = new ArrayList<>();
        Long stockDebutMois = 0L;
        Long stockTheoriqueFinal = 0L;
        Long stockReelFinal = 0L;
        Long stockReelMagasinCentral = 0L;
        Long distributionMoyenMensuelle = 0L;
        Long quantiteGlobalLivre = 0L;
        Long quantiteArrivageDateFin = 0L;

        Long couverture = 0L;
        Long quantiteVendu = 0L;
        Long quantitePomo = 0L;
        Long quantitePerte = 0L;

        List<EtatStockGlobalAimasWrapper> calendrierList = new ArrayList<>();

        final String dateCommenecement = "2016-12-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate debut = LocalDate.parse(dateDebut, formatter);
        final LocalDate fin = LocalDate.parse(dateFin, formatter);
        final LocalDate dateDebutStock = LocalDate.parse(dateCommenecement, formatter);


        final List<LigneBonDeSortie> lignebons = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(debut, fin);
        final List<Lignelivraison> ligneLivre = lignelivraisonRepository.findByLivraisonDateLivraisonBetween(debut, fin);

        //Tous les transferts de produit avant la date de la fin du mois
        final List<LigneBonDeSortie> lignebsTransfertAvantdate = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBefore(fin);

        //Liste des produits sortie de date dateDebutStock à date fin
        final List<LigneBonDeSortie> sorteDebutaFin = ligneBonDeSortieRepository.findByBonDeSortieDaateCreationBetween(dateDebutStock, fin);

        final List<LigneBonDeSortie> allSortie = ligneBonDeSortieRepository.findAllByProduit(produit);
        final List<Lignelivraison> allLivraison = lignelivraisonRepository.findAllByLivraisonCommandeProduit(produit);

        //Toutes les livraisons au magasin central du produit dans la periode de DateDebutStock a datefin
        final List<Lignelivraison> listLivraisonProduitDateDebutStockDateFin = lignelivraisonRepository.findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(produit, dateDebutStock, fin);


        quantiteGlobalLivre = allLivraison.stream()
            .mapToLong(Lignelivraison::getQuantiteLotLivre)
            .sum();

        //Recuperer les arrivages d'un produit de la periode dateDebutStock et date fin
        quantiteArrivageDateFin = listLivraisonProduitDateDebutStockDateFin.stream()
            .mapToLong(Lignelivraison::getQuantiteLotLivre)
            .sum();


        return null;
    }

}
