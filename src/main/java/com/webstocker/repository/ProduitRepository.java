package com.webstocker.repository;

import com.webstocker.domain.Produit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Produit entity.
 */
public interface ProduitRepository extends JpaRepository<Produit,Long> {

    Produit findByNomProduit(String nomproduit);
    
}
