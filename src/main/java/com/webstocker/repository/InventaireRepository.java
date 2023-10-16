package com.webstocker.repository;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Inventaire entity.
 */
@SuppressWarnings("unused")
public interface InventaireRepository extends JpaRepository<Inventaire,Long> {

    
    List<Inventaire> findByMagasinAndDateInventaireBetween(Magasin magasin, LocalDate dateDebut, LocalDate dateFin);
    
    Page<Inventaire> findByMagasinAndDateInventaireBetween(Magasin magasin, LocalDate dateDebut, LocalDate dateFin,Pageable pageable);
    
    Inventaire findByProduitAndMagasin(Produit produit, Magasin magasin);

}
