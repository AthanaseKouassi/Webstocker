package com.webstocker.repository;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;

import com.webstocker.domain.Produit;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.*;

import java.util.List;
  
/**
 * Spring Data JPA repository for the Lignelivraison entity.
 */
public interface LignelivraisonRepository extends JpaRepository<Lignelivraison,Long> {
    List<Lignelivraison> findAllByLivraison(Livraison livraison);
    
    @Query(value = " SELECT lignelivraison.id, lignelivraison.quantite_carton_lot, lignelivraison.lot_id, lignelivraison.livraison_id "
            + "produit.nom_produit, SUM(quantite_lot_livre) AS quantite_lot_livre "
            + "FROM lignelivraison , livraison, produit, lot "
            + "WHERE lignelivraison.livraison_id = livraison.id "
            + "AND lignelivraison.lot_id = lot.id "
            + "AND lot.produit_id = produit.id "
            + "AND livraison.date_livraison BETWEEN ?1 AND ?2 "
            + "GROUP BY produit.nom_produit" , nativeQuery = true)
    List<Lignelivraison> quantiteProduitLivreParMois(String dateDebut, String dateFin);

    List<Lignelivraison> findAllByLivraisonCommandeProduit(Produit produit);
    
    List<Lignelivraison> findByLivraisonDateLivraisonBetween(LocalDate date1, LocalDate date2);
    
    List<Lignelivraison> findByLivraisonCommandeBailleurNomBailleurAndLivraisonDateLivraisonBetween(String nomBailleur, LocalDate dateDebut, LocalDate dateFin);
    
    List<Lignelivraison> findByLivraisonCommandeBailleurAndLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(Bailleur bailleur,Produit produit, LocalDate dateDebut, LocalDate dateFin);
   
    List<Lignelivraison> findByLivraisonCommandeBailleurAndLivraisonDateLivraisonBetween(Bailleur bailleur, LocalDate dateDebut, LocalDate dateFin);

    List<Lignelivraison> findAllByLivraisonCommandeBailleur(Bailleur bailleur);
    
    List<Lignelivraison> findByLivraisonCommandeProduitAndLivraisonDateLivraisonBetween(Produit produit, LocalDate dateDebut, LocalDate dateFin);
}
