package com.webstocker.repository;

import com.webstocker.domain.Lot;

import com.webstocker.domain.Produit;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Lot entity.
 */
public interface LotRepository extends JpaRepository<Lot,Long> {

    List<Lot> findAllByProduitOrderByDatePeremptionAsc(Produit produit);
    
    Page<Lot>findByNumeroLotContaining(Long numeroLot,Pageable pageable);
}
