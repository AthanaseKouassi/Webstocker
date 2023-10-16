package com.webstocker.repository;

import com.webstocker.domain.Commande;

import com.webstocker.domain.enumeration.StatutCommande;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Commande entity.
 */
public interface CommandeRepository extends JpaRepository<Commande,Long> {

    List<Commande> findAllByStatutNotIn(StatutCommande statutCommande);
}
