package com.webstocker.repository;

import com.webstocker.domain.Cellule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cellule entity.
 */
public interface CelluleRepository extends JpaRepository<Cellule,Long> {

    @Query("select cellule from Cellule cellule where cellule.responsable.login = ?#{principal.username}")
    List<Cellule> findByResponsableIsCurrentUser();

}
