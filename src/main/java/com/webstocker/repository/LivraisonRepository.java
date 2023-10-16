package com.webstocker.repository;

import com.webstocker.domain.Livraison;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Livraison entity.
 */
public interface LivraisonRepository extends JpaRepository<Livraison,Long> {

//    @Query(" SELECT c.nomClient,f.dateFacture,(l.quantite * l.prixVente),f.id "      
//            + " FROM Facture f "
//            + " JOIN  f.client c "
//            + " JOIN f.bonDeSortie b "
//            + " JOIN b.ligneBonDeSorties l "
//            + " WHERE  "                        
//            + " ((?1 is null) OR (f.dateFacture >= ?1)) AND"
//            + " ((?2 is null) OR (f.dateFacture <= ?2)) ")
      List<Livraison> findByDateLivraisonBetween(LocalDate dateDebut,LocalDate dateFin);
}
