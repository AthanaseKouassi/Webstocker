package com.webstocker.repository;

import com.webstocker.domain.Objectifs;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Objectifs entity.
 */
public interface ObjectifsRepository extends JpaRepository<Objectifs,Long> {  

 List<Objectifs> findByQuantiteObtenuIsNull();
 List<Objectifs> findByPeriodeBetween(LocalDate date1 ,LocalDate date2);

 
 @Query(value = " SELECT objectifs.id, objectifs.quantite_attendue, objectifs.quantite_obtenu, objectifs.periode ,objectifs.taux, objectifs.produit_id, "
         + "produit.nom_produit, "
         + "IF(MONTH(periode) = 1, taux ,'-') AS Janvier, "
         + "IF(MONTH(periode) = 2, taux ,'-') AS Février, "
         + "IF(MONTH(periode) = 3, taux ,'-') AS Mars, "
         + "IF(MONTH(periode) = 4, taux ,'-') AS AvrilL, "
         + "IF(MONTH(periode) = 5, taux ,'-') AS Mai, "
         + "IF(MONTH(periode) = 6, taux ,'-') AS Juin, "
         + "IF(MONTH(periode) = 7, taux ,'-') AS Juillet, "
         + "IF(MONTH(periode) = 8, taux ,'-') AS Août, "
         + "IF(MONTH(periode) = 9, taux ,'-') AS Septembre, "
         + "IF(MONTH(periode) = 10, taux ,'-') AS Octobre, "
         + "IF(MONTH(periode) = 11, taux ,'-') AS Novembre, "
         + "IF(MONTH(periode) = 12, taux ,'-') AS Décembre "
         + "FROM objectifs , produit "
         + "WHERE objectifs.produit_id = produit.id "
         + "AND periode BETWEEN ?1 AND ?2 "
         + "GROUP BY produit.nom_produit ", nativeQuery = true)
 List<Objectifs> findAllTauxParProduit( String dateDebutAnnee, String dateFinAnnee);
 
 @Query(value = "SELECT objectifs.id, objectifs.quantite_attendue, objectifs.quantite_obtenu, objectifs.produit_id, "
         + "produit.nom_produit, objectifs.periode ,objectifs.taux "
         + "FROM objectifs , produit "
         + "WHERE objectifs.produit_id = produit.id "
         + "AND periode BETWEEN ?1 AND ?2 "
         + "GROUP BY objectifs.periode, produit.nom_produit", nativeQuery = true)
 List<Objectifs> findAllTauxParMoisetParProduit(String dateDebutAnnee, String dateFinAnnee);
 
 @Query(value = "SELECT objectifs.id, objectifs.quantite_attendue, objectifs.quantite_obtenu, objectifs.produit_id, "
         + "produit.nom_produit, objectifs.periode ,objectifs.taux "
         + "FROM objectifs , produit "
         + "WHERE objectifs.produit_id = produit.id "
         + "AND periode BETWEEN ?1 AND ?2 "
         + "AND MONTH(periode)= ?3 "
         + "GROUP BY objectifs.periode, produit.nom_produit", nativeQuery = true)
 List<Objectifs> findAllTauxParProduitPourUnMois(String dateDebut, String dateFin, int numeroMois);
 
// List<Objectifs> findAllQuantiteObtenueParProduit( String dateDebutAnnee, String dateFinAnnee);
// 
// List<Objectifs> findAllQuantiteAttendueParProduit(String dateDebutAnnee, String dateFinAnnee);
}
 