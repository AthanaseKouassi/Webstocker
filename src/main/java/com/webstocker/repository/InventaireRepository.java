package com.webstocker.repository;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Inventaire entity.
 */
@SuppressWarnings("unused")
public interface InventaireRepository extends JpaRepository<Inventaire, Long> {


    List<Inventaire> findByMagasinAndDateInventaireBetween(Magasin magasin, LocalDate dateDebut, LocalDate dateFin);

    Page<Inventaire> findByMagasinAndDateInventaireBetween(Magasin magasin, LocalDate dateDebut, LocalDate dateFin, Pageable pageable);

    Inventaire findByProduitAndMagasin(Produit produit, Magasin magasin);

    Page<Inventaire> findByDateInventaireBetween(LocalDate dateDebut, LocalDate dateFin, Pageable pageable);

    List<Inventaire> findByDateInventaireBetween(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT i FROM Inventaire i WHERE YEAR(i.dateInventaire) = :year " +
        "AND MONTH(i.dateInventaire) = :month AND i.produit = :produit")
    Optional<Inventaire> findByMonth(@Param("year") int year, @Param("month") int month, @Param("produit") Produit produit);

    @Query("SELECT i FROM Inventaire i WHERE YEAR(i.dateInventaire) = :year ")
    List<Inventaire> findByInventaireByYear(@Param("year") int year);

    @Query("SELECT i FROM Inventaire i WHERE YEAR(i.dateInventaire) = :year AND i.produit = :produit ORDER BY i.dateInventaire ASC")
    List<Inventaire> findByInventaireByYearAndProduit(@Param("year") int year, @Param("produit") Produit produit);
}
