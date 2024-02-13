package com.webstocker.repository;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.enumeration.StatusTransfert;
import com.webstocker.domain.enumeration.TypeSortie;
import java.time.LocalDate;

import com.webstocker.domain.enumeration.TypeVente;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the BonDeSortie entity.
 */
public interface BonDeSortieRepository extends JpaRepository<BonDeSortie, Long> {

    @Query("select bonDeSortie from BonDeSortie bonDeSortie where bonDeSortie.demandeur.login = ?#{principal.username}")
    List<BonDeSortie> findByDemandeurIsCurrentUser();

    @Query("select bonDeSortie from BonDeSortie bonDeSortie where bonDeSortie.printStatus = 1")
    List<BonDeSortie> findAllNonPrint();

    @Query("select bonDeSortie from BonDeSortie bonDeSortie where (?1 is null) OR bonDeSortie.numero  LIKE concat(concat('%',?1),'%')")
    Page<BonDeSortie> findBonByNumber(String numero,Pageable pageable);

    List<BonDeSortie> findByMagasinNomMagasinAndDaateCreationBetween(String magasin, LocalDate dateDebut, LocalDate dateFin);

    List<BonDeSortie> findByDaateCreationBetween( LocalDate dateDebut, LocalDate dateFin);

    List<BonDeSortie> findByMagasinNomMagasinAndTypeSortieAndDaateCreationBetween(String magasin,TypeSortie typeSortie, LocalDate dateDebut, LocalDate dateFin);

    Page<BonDeSortie> findByTypeSortie(TypeSortie typeSortie,Pageable pageable);

    List<BonDeSortie> findByTypeSortie(TypeSortie typeSortie);

    Page<BonDeSortie> findByTypeSortieAndNumeroContaining(TypeSortie typeSortie,String numero,Pageable pageable);

    List<BonDeSortie> findByTypeSortieAndNumeroFactureNormaliseContaining(TypeSortie typeSortie,String numero);

    Page<BonDeSortie> findByNumeroContaining(String numero,Pageable pageable);

    Page<BonDeSortie> findByStatusTranfertOrderByDaateCreation(StatusTransfert statusTransfert,Pageable pageable);


}
