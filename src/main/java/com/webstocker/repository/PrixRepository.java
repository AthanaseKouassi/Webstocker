package com.webstocker.repository;

import com.webstocker.domain.Categorieclient;
import com.webstocker.domain.Prix;

import com.webstocker.domain.Produit;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prix entity.
 */
public interface PrixRepository extends JpaRepository<Prix,Long> {

    Prix findByProduitAndCategorieclient(Produit produit, Categorieclient categorieclient);
    Prix findByProduitAndCategorieclientAndActif(Produit produit, Categorieclient categorieclient,boolean valide);
}
