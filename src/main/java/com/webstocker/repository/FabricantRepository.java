package com.webstocker.repository;

import com.webstocker.domain.Fabricant;

import com.webstocker.domain.Produit;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Fabricant entity.
 */
public interface FabricantRepository extends JpaRepository<Fabricant,Long> {

    @Query("select distinct fabricant from Fabricant fabricant left join fetch fabricant.produits")
    List<Fabricant> findAllWithEagerRelationships();

    @Query("select fabricant from Fabricant fabricant left join fetch fabricant.produits where fabricant.id =:id")
    Fabricant findOneWithEagerRelationships(@Param("id") Long id);

}
