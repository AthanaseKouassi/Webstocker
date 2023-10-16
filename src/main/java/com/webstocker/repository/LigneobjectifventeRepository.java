package com.webstocker.repository;

import com.webstocker.domain.Ligneobjectifvente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ligneobjectifvente entity.
 */
public interface LigneobjectifventeRepository extends JpaRepository<Ligneobjectifvente,Long> {

}
