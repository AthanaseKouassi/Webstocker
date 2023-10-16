package com.webstocker.repository;

import com.webstocker.domain.Categorieclient;
import com.webstocker.domain.Client;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Client entity.
 */
public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query(" SELECT NEW com.webstocker.domain.ClientFacture(c.id,c.nomClient,c.telephoneClient,c.boitepostale, COUNT(distinct f.id))"
            + " FROM Client c JOIN c.factures f "
            + "WHERE ((?3 is null) OR (c.nomClient LIKE concat(concat('%',?3),'%'))) AND "
            + " ((?1 is null) OR (f.dateFacture >= ?1)) AND"
            + " ((?2 is null) OR (f.dateFacture <= ?2)) ")
    List<Client> findStatsClientByFacture(LocalDate localDate,LocalDate dateFin,String nomClient);
    
    Client findByNomClientAndTelephoneClient(String nom, String telephone);
    
    Page<Client> findByNomClient(String nomClient,Pageable pageable);
    
    Page<Client> findByNomClientContaining(String nomClient,Pageable pageable);
    
    Client findByNomClient(String nomClient);
    
    @Query(value="SELECT * FROM client ORDER BY nom_client asc",nativeQuery = true)
    List<Client> findAllOrderByNomClientAsc();
    
    List<Client> findByCategorieclientOrderByNomClientAsc(Categorieclient categorieclient);
    
    
}
