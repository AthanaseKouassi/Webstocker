package com.webstocker.repository;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;
import com.webstocker.domain.enumeration.newfeature.StatutFacture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Facture entity.
 */
public interface FactureRepository extends JpaRepository<Facture, Long> {

    @Query("SELECT f.id,f.dateFacture,b.numero,SUM(l.prixDeVente)"
        + "FROM Facture f "
        + " JOIN  f.client c "
        + " JOIN  f.bonDeSortie b "
        + "JOIN b.ligneBonDeSorties l "
        + "WHERE (c.id=?1 AND f.reglements IS EMPTY)"
        + " GROUP BY f.id ")
    List<Facture> findFactureNonReglees(Long id);

    @Query(" SELECT c.nomClient,f.dateFacture,l.prixDeVente,f.id "
        + " FROM Facture f "
        + " JOIN  f.client c "
        + " JOIN f.bonDeSortie b "
        + " JOIN b.ligneBonDeSorties l "
        + " WHERE  "
        + " ((?1 is null) OR (f.dateFacture >= ?1)) AND"
        + " ((?2 is null) OR (f.dateFacture <= ?2)) ")
    List<Facture> findFactureByDate(LocalDate dateDebut, LocalDate dateFin);

    //    @Query(" SELECT f.id,f.dateFacture, b.numero,SUM(l.quantite * l.prixVente) "
    @Query(" SELECT f.id,f.dateFacture, b.numero,SUM(l.prixDeVente) "
        + "FROM Facture f "
        + "JOIN  f.client c "
        + "JOIN f.bonDeSortie b "
        + "JOIN b.ligneBonDeSorties l "
        + "WHERE c.id=:id  "
        + " GROUP BY f.id "
        + "ORDER BY f.dateFacture ASC")
    List<Facture> findFisrtFacture(@Param("id") Long id);

    List<Facture> findByDateLimitePaiementBetween(LocalDate dateDebut, LocalDate dateFin);

    @Query(value = " SELECT facture.id, facture.client_id , client.nom_client, facture.date_facture, ligne_bon_de_sortie.prix_de_vente, facture.date_limite_paiement, "
        + " facture.delai_paiement, facture.valeur_remise, facture.bon_de_sortie_id, ligne_bon_de_sortie.quantite, ligne_bon_de_sortie.prix_vente "
        + " FROM facture , client , bon_de_sortie , ligne_bon_de_sortie "
        + " WHERE  ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
        + "AND bon_de_sortie.id = facture.bon_de_sortie_id "
        + "AND facture.client_id = client.id "
        + "AND facture.date_facture BETWEEN ?1 AND ?2 "
        + "ORDER BY facture.date_facture DESC ", nativeQuery = true)
    List<Facture> findAllFactureByPeriode(String dateDebut, String dateFin);

    Facture findByBonDeSortie(BonDeSortie bonDesortie);

    List<Facture> findByStatutFacture(String statutfacture);

    List<Facture> findByStatutFactureAndDateFactureBetween(StatutFacture statutfacture, LocalDate debut, LocalDate fin);

    Page<Facture> findByStatutFactureAndDateFactureBetween(StatutFacture statutfacture, LocalDate debut, LocalDate fin, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE facture  SET statut = ?1 WHERE id = ?2", nativeQuery = true)
    void updateStatutFacture(String statutFacture, Long idFacture);

    List<Facture> findByStatutFactureAndNumero(StatutFacture statutfacture, String numero);

    Page<Facture> findByStatutFactureAndNumero(StatutFacture statutfacture, String numero, Pageable pageable);

    @Query(value = "SELECT f.* FROM facture f  " +
        "WHERE datediff(current_date(),f.date_facture) >= 30  " +
        "AND datediff(current_date(),f.date_facture) < 45  " +
        "AND f.statut = 'NON_SOLDE' " +
        "ORDER BY datediff(current_date(),f.date_facture) DESC ", nativeQuery = true)
    List<Facture> getFactureEntreTrenteEtQuaranteCinqJours();

    @Query(value = "SELECT f.* FROM facture f  " +
        "WHERE datediff(current_date(),f.date_facture) < 30 AND f.statut = 'NON_SOLDE'  " +
        "ORDER BY datediff(current_date(),f.date_facture) DESC ", nativeQuery = true)
    List<Facture> getFactureMoinsdetrentejour();

    @Query(value = "SELECT f.* FROM facture f  " +
        "WHERE datediff(current_date(),f.date_facture) >= 45 and f.statut = 'NON_SOLDE'  " +
        "ORDER BY datediff(current_date(),f.date_facture) DESC ", nativeQuery = true)
    List<Facture> getFacturePlusDeQuaranteCinqJours();

    List<Facture> findByDateFactureBetweenOrderByDateFactureDesc(LocalDate dateDebut, LocalDate dateFin);

    @Query(value = " SELECT f.* FROM facture f " +
        "INNER JOIN bon_de_sortie bds ON bds.id = f.bon_de_sortie_id " +
        "INNER JOIN ligne_bon_de_sortie lbds ON lbds.bon_de_sortie_id =bds.id " +
        "INNER JOIN reglement r ON r.facture_id = f.id " +
        "WHERE f.statut = 'NON_SOLDE' AND bds.client_id = ?1 " +
        "AND f.date_facture BETWEEN ?2 AND ?3 ", nativeQuery = true)
    List<Facture> findByCommercialParPeriode(Long idClient, LocalDate dateDebut, LocalDate dateFin);
}
