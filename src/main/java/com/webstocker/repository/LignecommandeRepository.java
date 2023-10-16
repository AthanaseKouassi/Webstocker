package com.webstocker.repository;

import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignecommande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lignecommande entity.
 */
public interface LignecommandeRepository extends JpaRepository<Lignecommande,Long> {

    List<Lignecommande> findByCommande(Commande commande);
}
