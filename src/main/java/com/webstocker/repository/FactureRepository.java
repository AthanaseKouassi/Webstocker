package com.webstocker.repository;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Facture entity.
 */
public interface FactureRepository extends JpaRepository<Facture, Long> {
//
//    @Query("SELECT f.valeurRemise, f.client FROM Facture f WHERE f.reglements IS EMPTY AND f.dateFacture=?1")
//     List<Facture> findAllCreancesThirthyDaysAgo(LocalDate localDate);

    /**
     * @param id
     *
     * @return
     */
//    @Query("SELECT f.id,f.dateFacture,b.numero,SUM(l.quantite * l.prixVente)"
    @Query("SELECT f.id,f.dateFacture,b.numero,SUM(l.prixDeVente)"
            + "FROM Facture f "
            + " JOIN  f.client c "
            + " JOIN  f.bonDeSortie b "
            + "JOIN b.ligneBonDeSorties l "
            + "WHERE (c.id=?1 AND f.reglements IS EMPTY)"
            + " GROUP BY f.id ")
    List<Facture> findFactureNonReglees(Long id);
    
    
//      @Query(" SELECT c.nomClient,f.dateFacture,SUM(l.quantite * l.prixVente),f.id "      
//      @Query(" SELECT c.nomClient,f.dateFacture,(l.quantite * l.prixVente),f.id "      
      @Query(" SELECT c.nomClient,f.dateFacture,l.prixDeVente,f.id "      
            + " FROM Facture f "
            + " JOIN  f.client c "
            + " JOIN f.bonDeSortie b "
            + " JOIN b.ligneBonDeSorties l "
            + " WHERE  "                        
            + " ((?1 is null) OR (f.dateFacture >= ?1)) AND"
            + " ((?2 is null) OR (f.dateFacture <= ?2)) ")
      List<Facture> findFactureByDate(LocalDate dateDebut,LocalDate dateFin);

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

    List<Facture> findByDateLimitePaiementBetween(LocalDate dateDebut,  LocalDate dateFin);
    
          
//      @Query( value = " SELECT facture.id, facture.client_id , client.nom_client, facture.date_facture,(ligne_bon_de_sortie.quantite * ligne_bon_de_sortie.prix_vente) AS montant,facture.date_limite_paiement, "
      @Query( value = " SELECT facture.id, facture.client_id , client.nom_client, facture.date_facture, ligne_bon_de_sortie.prix_de_vente, facture.date_limite_paiement, "
              + " facture.delai_paiement, facture.valeur_remise, facture.bon_de_sortie_id, ligne_bon_de_sortie.quantite, ligne_bon_de_sortie.prix_vente "
            + " FROM facture , client , bon_de_sortie , ligne_bon_de_sortie "            
            + " WHERE  ligne_bon_de_sortie.bon_de_sortie_id = bon_de_sortie.id "
              + "AND bon_de_sortie.id = facture.bon_de_sortie_id "
              + "AND facture.client_id = client.id "
              + "AND facture.date_facture BETWEEN ?1 AND ?2 "
              + "ORDER BY facture.date_facture DESC " , nativeQuery = true )
      List<Facture> findAllFactureByPeriode(String dateDebut,String dateFin);
      
     Facture findByBonDeSortie(BonDeSortie bonDesortie);

}
