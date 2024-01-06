package com.webstocker.repository;

import com.webstocker.domain.Facture;
import com.webstocker.domain.Produit;
import com.webstocker.domain.Reglement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Reglement entity.
 */
@SuppressWarnings("unused")
public interface ReglementRepository extends JpaRepository<Reglement, Long> {

    List<Reglement> findByDateReglementBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Reglement> findByFacture(Facture facture);

    Reglement findByFactureAndProduit(Facture facture, Produit produit);


}
