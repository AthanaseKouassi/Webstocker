package com.webstocker.repository;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import org.springframework.stereotype.Repository;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.domain.enumeration.TypeVente;
import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the LigneBonDeSortie entity.
 */
@Repository
public interface LigneBonDeSortieRepository extends JpaRepository<LigneBonDeSortie, Long> {

    List<LigneBonDeSortie> findAllByProduitAndBonDeSortieMagasinAndBonDeSortieTypeSortieAndBonDeSortieDaateCreationBetween(Produit produit, Magasin magasin, TypeSortie typeSortie, LocalDate debut, LocalDate fin);

    List<LigneBonDeSortie> findAllByProduit(Produit produit);

    List<LigneBonDeSortie> findAllByProduitAndBonDeSortieDestination(Produit produit, Magasin destination);

    List<LigneBonDeSortie> findAllByProduitAndBonDeSortieMagasinAndBonDeSortieTypeSortie(Produit produit, Magasin magasin, TypeSortie typeSortie);

    List<LigneBonDeSortie> findByBonDeSortieDaateCreationBetween(LocalDate dateDebut, LocalDate DateFin);

    List<LigneBonDeSortie> findAllByBonDeSortie(BonDeSortie bonDeSortie);



    List<LigneBonDeSortie> findByBonDeSortieFactureDateLimitePaiementBetween(LocalDate dateDebut, LocalDate dateFin);

    List<LigneBonDeSortie> findByBonDeSortieFactureDateFactureBetween(LocalDate dateDebut, LocalDate dateFin);

    Page<LigneBonDeSortie> findByBonDeSortieTypeSortieAndBonDeSortieFactureDateFactureBetween(TypeSortie typeSortie, LocalDate dateDebut, LocalDate dateFin, Pageable pageable);

    List<LigneBonDeSortie> findByBonDeSortieTypeSortieAndBonDeSortieDaateCreationBetween(TypeSortie typeSortie, LocalDate dateDebut, LocalDate dateFin);

    Page<LigneBonDeSortie> findByBonDeSortieTypeSortieAndBonDeSortieNumeroFactureNormaliseContaining(TypeSortie typeSortie, String numeroFactureNormalise, Pageable pageable);

    List<LigneBonDeSortie> findByBonDeSortieTypeSortieAndBonDeSortieNumeroFactureNormaliseContaining(TypeSortie typeSortie, String numeroFactureNormalise);

    List<LigneBonDeSortie> findByProduitAndBonDeSortieFactureDateLimitePaiementBetween(Produit produit, LocalDate dateDebut, LocalDate dateFin);

    List<LigneBonDeSortie> findByBonDeSortieDaateCreationBefore(LocalDate maDate);

    List<LigneBonDeSortie> findByBonDeSortieDestination(Magasin magasin);

    List<LigneBonDeSortie> findByBonDeSortieMagasinAndBonDeSortieTypeSortieAndBonDeSortieDaateCreationBetween(Magasin magasin, TypeSortie typeSortie, LocalDate dateDebut, LocalDate dateFin);

    List<LigneBonDeSortie> findByBonDeSortieMagasinNomMagasinAndBonDeSortieDaateCreationBetween(String nomMagasin, LocalDate dateDebut, LocalDate dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie , ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id ,ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente,  "
            + "daate_creation, nom_magasin, nom_produit, SUM(quantite) AS quantite, SUM(prix_de_vente) AS prix_de_vente "
//            + "daate_creation, nom_magasin, nom_produit, SUM(quantite) AS quantite, SUM(prix_vente) AS prix_vente "
            + "FROM magasin,bon_de_sortie, produit, ligne_bon_de_sortie, lot "
            + "WHERE magasin.id = bon_de_sortie.magasin_id AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND bon_de_sortie.type_sortie = ?1 "
            + "AND magasin.nom_magasin = ?2 AND bon_de_sortie.daate_creation BETWEEN ?3 AND ?4 "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> findallByProduit(String typeSortie, String nomMagasin, String dateDebutPeriode, String dateFinPeriode);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id ,ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente,  "
//            + " bon_de_sortie.daate_creation,produit.nom_produit, SUM(ligne_bon_de_sortie.quantite) AS quantite, SUM(ligne_bon_de_sortie.prix_vente * ligne_bon_de_sortie.quantite) AS prix_vente "
            + " bon_de_sortie.daate_creation,produit.nom_produit, SUM(ligne_bon_de_sortie.quantite) AS quantite, SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente "
            + "FROM bon_de_sortie, ligne_bon_de_sortie, produit,lot "
            + "WHERE ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND bon_de_sortie.type_sortie= 'VENTE' "
            + "AND bon_de_sortie.daate_creation BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> findChiffreAffaireByProduit(String dateDebutPeriode, String dateFinPeriode);

    @Query(value = " SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente, ligne_bon_de_sortie.quantite, "
            + "quantite, client.nom_client, produit.nom_produit, SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente "
//            + "quantite, client.nom_client, produit.nom_produit, SUM(ligne_bon_de_sortie.prix_vente) AS prix_vente "
            //            + "quantite, client.nom_client, produit.nom_produit, SUM(ligne_bon_de_sortie.prix_vente * ligne_bon_de_sortie.quantite) AS prix_vente "
            + "FROM bon_de_sortie, client, ligne_bon_de_sortie, produit, lot "
            + "WHERE bon_de_sortie.type_sortie = 'VENTE' "
            + "AND bon_de_sortie.client_id = client.id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.daate_creation BETWEEN ?1 AND ?2 "
            + "GROUP BY client.nom_client ASC , produit.nom_produit", nativeQuery = true)
    List<LigneBonDeSortie> findChiffreAffaireByClient(String dateDebutPeriode, String dateFinPeriode);

    @Query(value = " SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente, "
            + "magasin.nom_magasin, SUM(ligne_bon_de_sortie.quantite) AS quantite, SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente "
//            + "magasin.nom_magasin, SUM(ligne_bon_de_sortie.quantite) AS quantite, SUM(ligne_bon_de_sortie.prix_vente) AS prix_vente "
            + "FROM bon_de_sortie, magasin, ligne_bon_de_sortie, produit, lot "
            + "WHERE bon_de_sortie.type_sortie = 'VENTE' "
            + "AND bon_de_sortie.magasin_id = magasin.id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.daate_creation BETWEEN ?1 AND ?2 "
            + "GROUP BY magasin.nom_magasin, produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> findChiffreAffaireByMagasin(String dateDebutPeriode, String dateFinPeriode);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente,  "
//            + "nom_magasin, lot.numero_lot, SUM(quantite) AS quantite, SUM(prix_vente) AS prix_vente "
            + "nom_magasin, lot.numero_lot, SUM(quantite) AS quantite, SUM(prix_de_vente) AS prix_de_vente "
            + "FROM magasin,bon_de_sortie, produit, ligne_bon_de_sortie ,lot "
            + "WHERE magasin.id = bon_de_sortie.magasin_id "
            + "AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND produit.id = lot.produit_id AND bon_de_sortie.type_sortie = ?1 "
            + "AND magasin.nom_magasin = ?2 "
            + "AND bon_de_sortie.daate_creation BETWEEN ?3 AND ?4 "
            + "AND produit.nom_produit = ?5 "
            + "GROUP BY lot.numero_lot ", nativeQuery = true)
    List<LigneBonDeSortie> findallLotProduitSortie(String typeSortie, String nomMagasin, String dateDebutPeriode, String dateFinPeriode, String produit);

    @Query(value = "SELECT  SUM(DISTINCT(ligne_bon_de_sortie.quantite)) AS quantite "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot "
            + "WHERE ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.type_sortie ='VENTE' "
            + "AND bon_de_sortie.daate_creation BETWEEN ?1 AND ?2 "
            + "AND produit.nom_produit= ?3 ", nativeQuery = true)
    Long quantiteVenduByProduit(String dateDebut, String dateFin, String nomProduit);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , "
            + "produit.nom_produit, SUM(DISTINCT(ligne_bon_de_sortie.quantite)) AS quantite "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot "
            + "WHERE ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = facture.bon_de_sortie_id "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit", nativeQuery = true)
    List<LigneBonDeSortie> quantiteProduitVendueDuMois(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , "
            + " produit.nom_produit, SUM(DISTINCT(quantite)) AS quantite "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot "
            + "WHERE ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.type_sortie = 'PROMOTION' "
            + "AND bon_de_sortie.daate_creation BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> quantiteProduitPromotionDuMois(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , "
            + "produit.nom_produit, SUM(DISTINCT(ligne_bon_de_sortie.quantite)) AS quantite "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot "
            + "WHERE produit.id = lot.produit_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.bon_de_sortie_id =  bon_de_sortie.id  "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "AND facture.date_limite_paiement > ?2 "
            + "GROUP BY produit.nom_produit", nativeQuery = true)
    List<LigneBonDeSortie> quantiteProduitVendueCREDITDuMois(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente , ligne_bon_de_sortie.prix_de_vente, "
            + "produit.nom_produit, SUM(DISTINCT(ligne_bon_de_sortie.quantite)) AS quantite "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot, reglement "
            + "WHERE ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = facture.bon_de_sortie_id "
            + "AND AND facture.id = reglement.facture_id"
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "AND facture.date_facture = reglement.date_reglement "
            + "GROUP BY produit.nom_produit", nativeQuery = true)
    List<LigneBonDeSortie> quantiteProduitVendueCASHDuMois(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente , ligne_bon_de_sortie.quantite , "
            + "produit.nom_produit, SUM(DISTINCT(ligne_bon_de_sortie.prix_de_vente )) AS prix_de_vente  "
//            + "produit.nom_produit, SUM(DISTINCT(ligne_bon_de_sortie.prix_vente * ligne_bon_de_sortie.quantite)) AS prix_vente  "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot , reglement"
            + "WHERE lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = facture.bon_de_sortie_id "
            + "AND facture.id = reglement.facture_id "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "AND facture.date_facture = reglement.date_reglement "
            + "GROUP BY produit.nom_produit", nativeQuery = true)
    List<LigneBonDeSortie> valeurVenteCASHDuMois(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente , ligne_bon_de_sortie.quantite , "
//            + "produit.nom_produit, SUM(ligne_bon_de_sortie.prix_vente* ligne_bon_de_sortie.quantite) AS prix_vente  "
            + "produit.nom_produit, SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente  "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot "
            + "WHERE produit.id = lot.produit_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.bon_de_sortie_id =  bon_de_sortie.id  "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "AND facture.date_limite_paiement > ?2 "
            + "GROUP BY produit.nom_produit", nativeQuery = true)
    List<LigneBonDeSortie> valeurVenteCREDITDuMois(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id ,ligne_bon_de_sortie.prix_vente, ligne_bon_de_sortie.quantite ,  "
            + "produit.nom_produit, SUM(DISTINCT (ligne_bon_de_sortie.prix_de_vente)) AS montant_regle "
//            + "produit.nom_produit, SUM(DISTINCT (ligne_bon_de_sortie.prix_vente)) AS montant_regle "
            + "FROM reglement , facture, bon_de_sortie, ligne_bon_de_sortie, produit, lot "
            + "WHERE reglement.facture_id = facture.id "
            + "AND reglement.date_reglement BETWEEN ?1 AND ?2 "
            + "AND reglement.montant_reglement > 0 "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> valeurRecouvrementParProduitParMois(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente , "
            + "produit.nom_produit, SUM(prix_de_vente) AS prix_de_vente "
//            + "produit.nom_produit, SUM(prix_vente) AS prix_vente "
            + "FROM `facture`, produit, bon_de_sortie, ligne_bon_de_sortie, lot, reglement "
            + "WHERE reglement.facture_id = facture.id "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND facture.delai_paiement  < 30 "
            + "AND reglement.montant_reglement = 0 "
            + "AND reglement.date_reglement BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> creanceMoinsdeTrenteJourParProduit(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente , "
            + "produit.nom_produit, SUM(DISTINCT(prix_de_vente)) AS prix_de_vente "
//            + "produit.nom_produit, SUM(DISTINCT(prix_vente)) AS prix_vente "
            + "FROM facture, produit, bon_de_sortie, ligne_bon_de_sortie, lot, reglement "
            + "WHERE reglement.facture_id = facture.id "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND facture.delai_paiement BETWEEN 30 AND 90 "
            + "AND reglement.montant_reglement = 0 "
            + "AND reglement.date_reglement BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> creanceEntreTrenteEtQuatreVingtDixJour(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente ,  "
            + "produit.nom_produit, SUM(DISTINCT(prix_de_vente)) AS prix_de_vente "
//            + "produit.nom_produit, SUM(DISTINCT(prix_vente)) AS prix_vente "
            + "FROM facture, produit, bon_de_sortie, ligne_bon_de_sortie, lot, reglement "
            + "WHERE reglement.facture_id = facture.id "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = ligne_bon_de_sortie.bon_de_sortie_id "
            + "AND ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND facture.delai_paiement > 90 "
            + "AND reglement.montant_reglement = 0 "
            + "AND reglement.date_reglement BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit", nativeQuery = true)
    List<LigneBonDeSortie> creancePlusDeQuatreVingtDixJour(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente , ligne_bon_de_sortie.prix_de_vente , "
            + "(ville.libelle) AS ville, produit.nom_produit, SUM(ligne_bon_de_sortie.quantite) AS quantite "
            + "FROM produit, client, bon_de_sortie, ligne_bon_de_sortie, localite, commune, ville, lot "
            + "WHERE produit.id = lot.produit_id "
            + "AND lot.id = ligne_bon_de_sortie.lot_id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.client_id = client.id "
            + "AND client.localite_id = localite.id "
            + "AND  localite.communes_id = commune.id "
            + "AND commune.ville_id = ville.id "
            + "AND bon_de_sortie.type_sortie = 'VENTE' "
            + "AND bon_de_sortie.daate_creation BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit , ville.libelle ", nativeQuery = true)
    List<LigneBonDeSortie> quantiteVendueParProduitEtParVille(String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente, ligne_bon_de_sortie.prix_de_vente ,  "
            + "(ville.libelle) AS ville, produit.nom_produit, SUM(ligne_bon_de_sortie.quantite) AS quantite "
            + "FROM produit, client, bon_de_sortie, ligne_bon_de_sortie, localite, commune, ville, lot "
            + "WHERE produit.id = lot.produit_id "
            + "AND lot.id = ligne_bon_de_sortie.lot_id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.client_id = client.id "
            + "AND client.localite_id = localite.id "
            + "AND  localite.communes_id = commune.id "
            + "AND commune.ville_id = ville.id "
            + "AND bon_de_sortie.type_sortie = 'VENTE' "
            + "AND ville.libelle = ?1 "
            + "AND bon_de_sortie.daate_creation BETWEEN ?2 AND ?3 "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> quantiteVenduePourUneVille(String ville, String dateDebut, String dateFin);

    @Query(value = "SELECT bon_de_sortie.type_sortie, ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.prix_vente ,  "
//            + "client.nom_client,produit.nom_produit, SUM(ligne_bon_de_sortie.quantite) AS quantite, SUM(ligne_bon_de_sortie.prix_vente) AS prix_vente "
            + "client.nom_client,produit.nom_produit, SUM(ligne_bon_de_sortie.quantite) AS quantite, SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente "
            + "FROM bon_de_sortie, client, ligne_bon_de_sortie, produit, lot,facture "
            + "WHERE ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.client_id = client.id "
            + "AND client.nom_client = ?1 "
            + "AND bon_de_sortie.daate_creation BETWEEN ?2 AND ?3 "
            + "GROUP BY produit.nom_produit  ", nativeQuery = true)
    List<LigneBonDeSortie> chiffreAffaireUnClient(String nomClient, String dateDebut, String dateFin);

    @Query(value = " SELECT DISTINCT(ligne_bon_de_sortie.id), ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.quantite , ligne_bon_de_sortie.prix_vente , "
//            + "client.nom_client,facture.date_facture,facture.id,  SUM(ligne_bon_de_sortie.quantite * ligne_bon_de_sortie.prix_vente) AS prix_vente "
            + "client.nom_client,facture.date_facture,facture.id,  SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente "
            + "FROM  facture,client, ligne_bon_de_sortie, bon_de_sortie "
            + "WHERE ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id  "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id  "
            + "AND facture.client_id = client.id "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "GROUP BY facture.id ", nativeQuery = true)
    List<LigneBonDeSortie> listeFactureParPeriode(String dateDebut, String dateFin);

    @Query(value = "SELECT ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id, ligne_bon_de_sortie.prix_vente, ligne_bon_de_sortie.prix_de_vente , "
            + " produit.nom_produit, SUM(quantite) AS quantite "
            + "FROM ligne_bon_de_sortie , produit , bon_de_sortie, facture ,lot "
            + "WHERE ligne_bon_de_sortie.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND bon_de_sortie.id = facture.bon_de_sortie_id "
            + "AND bon_de_sortie.daate_creation BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> quantiteDeProduitVendusParMois(String dateDebut, String dateFin);

    @Query(value = " SELECT ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id, ligne_bon_de_sortie.prix_vente, ligne_bon_de_sortie.prix_de_vente ,  "
            + "client.nom_client,client.telephone_client, COUNT(facture.id)AS nombre_facture, SUM( ligne_bon_de_sortie.quantite) AS quantite "
            + "FROM client, ligne_bon_de_sortie, facture, bon_de_sortie WHERE ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.client_id = client.id "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "GROUP BY client.nom_client "
            + "ORDER BY nombre_facture DESC", nativeQuery = true)
    List<LigneBonDeSortie> frequenceAchatByClientPeriode(String dateDebut, String DateFin);

    @Query(value = "SELECT ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id, ligne_bon_de_sortie.quantite, ligne_bon_de_sortie.prix_vente  , "
//            + "categorieclient.libelle_categorie_client,produit.nom_produit, SUM(ligne_bon_de_sortie.prix_vente * ligne_bon_de_sortie.quantite) AS prix_vente "
            + "categorieclient.libelle_categorie_client,produit.nom_produit, SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente "
            + "FROM ligne_bon_de_sortie,lot, produit, bon_de_sortie, facture, client, categorieclient "
            + "WHERE produit.id = lot.produit_id "
            + "AND lot.id = ligne_bon_de_sortie.lot_id "
            + "AND ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id "
            + "AND facture.client_id = client.id "
            + "AND client.categorieclient_id = categorieclient.id "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "GROUP BY categorieclient.libelle_categorie_client, produit.nom_produit ", nativeQuery = true)
    List<LigneBonDeSortie> chiffreAffaireCategorieClient(String dateDebut, String DateFin);

    @Query(value = "SELECT ligne_bon_de_sortie.id, ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id, ligne_bon_de_sortie.quantite, ligne_bon_de_sortie.prix_vente, "
            + " (ligne_bon_de_sortie.prix_de_vente) AS montant, client.nom_client, facture.id, facture.date_facture "
//            + " (ligne_bon_de_sortie.prix_vente * ligne_bon_de_sortie.quantite) AS montant, client.nom_client, facture.id, facture.date_facture "
            + " FROM facture , client , bon_de_sortie , ligne_bon_de_sortie "
            + " WHERE  ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
            + " AND bon_de_sortie.id = facture.bon_de_sortie_id AND facture.client_id = client.id "
            + " AND facture.date_facture BETWEEN ?1 AND ?2 "
            + " ORDER BY facture.date_facture DESC ", nativeQuery = true)
    List<LigneBonDeSortie> findAllFactureParPeriode(String dateDebut, String dateFin);

    //obtenir liste des facture par periode
    @Query(value = " SELECT DISTINCT(ligne_bon_de_sortie.id), ligne_bon_de_sortie.bon_de_sortie_id , ligne_bon_de_sortie.lot_id ,ligne_bon_de_sortie.produit_id , ligne_bon_de_sortie.quantite , facture.id , ligne_bon_de_sortie.prix_vente , "
//            + "bon_de_sortie.numero_facture_normalise,bon_de_sortie.numero,client.nom_client,facture.date_facture,  SUM(ligne_bon_de_sortie.quantite * ligne_bon_de_sortie.prix_vente) AS prix_vente "
            + "bon_de_sortie.numero_facture_normalise,bon_de_sortie.numero,client.nom_client,facture.date_facture,  SUM(ligne_bon_de_sortie.prix_de_vente) AS prix_de_vente "
            + "FROM  facture,client, ligne_bon_de_sortie, bon_de_sortie "
            + "WHERE ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id  "
            + "AND facture.bon_de_sortie_id = bon_de_sortie.id  "
            + "AND facture.client_id = client.id "
            + "AND bon_de_sortie.type_sortie = 'VENTE' "
            + "AND facture.date_facture BETWEEN ?1 AND ?2 "
            + "GROUP BY facture.id ", nativeQuery = true)
    List<LigneBonDeSortie> findFactureParPeriode(String dateDebut, String dateFin);


    List<LigneBonDeSortie> findByBonDeSortieTypeVenteAndBonDeSortieDaateCreationBetweenOrderByProduit(TypeVente typeVente, LocalDate dateDebut, LocalDate dateFin);


}
