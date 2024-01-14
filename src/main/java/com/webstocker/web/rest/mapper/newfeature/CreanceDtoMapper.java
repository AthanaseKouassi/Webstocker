package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.User;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.web.rest.dto.newfeature.CreanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreanceDtoMapper {

    @Autowired
    FactureRepository factureRepository;
    @Autowired
    ProduitRepository produitRepository;
    @Autowired
    BonDeSortieRepository bonDeSortieRepository;
    @Autowired
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    public CreanceDto mapToCreanceDto(Facture facture, LigneBonDeSortie ligneBonDeSortie, User user) {
        CreanceDto dto = new CreanceDto();
        dto.setDateFacture(facture.getDateFacture());
        dto.setIdClient(facture.getClient().getId());
        dto.setNomClient(facture.getClient().getNomClient());
        dto.setIdProduit(ligneBonDeSortie.getProduit().getId());
        

        return dto;
    }
}
