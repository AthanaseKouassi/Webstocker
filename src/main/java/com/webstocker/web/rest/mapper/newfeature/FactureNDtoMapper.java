package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.ClientRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ReglementRepository;
import com.webstocker.web.rest.dto.newfeature.FactureNDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FactureNDtoMapper {
    @Autowired
    ReglementRepository reglementRepository;
    @Autowired
    BonDeSortieRepository bonDeSortieRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LigneBonDeSortieRepository ligneBonDeSortieRepository;


    FactureNDto toFactureDTO(Facture facture) {
        FactureNDto fact = new FactureNDto();
        fact.setId(facture.getId());
        fact.setDateFacture(facture.getDateFacture());
        fact.setStatutFacture(facture.getStatutFacture());
        fact.setIdBonDeSortie(facture.getBonDeSortie().getId());
        fact.setIdClient(facture.getClient().getId());
        fact.setNumero(facture.getNumero());
        fact.setNomClient(facture.getClient().getNomClient());
        fact.setMontantTotal(ligneBonDeSortieRepository.findAllByBonDeSortie(facture.getBonDeSortie())
            .stream().mapToLong(LigneBonDeSortie::getPrixDeVente).sum());
        fact.setResteApayer(fact.getMontantTotal() - reglementRepository.findByFacture(facture)
            .stream().mapToLong(Reglement::getMontantReglement).sum());

        return fact;
    }


    Facture factureDtoToFacture(FactureNDto nDto) {
        Facture fact = new Facture();
        fact.setId(nDto.getId());
        fact.setDateFacture(nDto.getDateFacture());
        fact.setStatutFacture(nDto.getStatutFacture());
        fact.setClient(clientRepository.findOne(nDto.getIdClient()));
        fact.setNumero(nDto.getNumero());
        fact.setBonDeSortie(bonDeSortieRepository.findOne(nDto.getIdBonDeSortie()));
        return fact;
    }

    public List<FactureNDto> toFactureDTOs(List<Facture> factures) {
        List<FactureNDto> list = new ArrayList<>();
        for (Facture facture : factures) {
            FactureNDto fact = new FactureNDto();
            fact.setId(facture.getId());
            fact.setDateFacture(facture.getDateFacture());
            fact.setStatutFacture(facture.getStatutFacture());
            fact.setIdBonDeSortie(facture.getBonDeSortie().getId());
            fact.setIdClient(facture.getClient().getId());
            fact.setNumero(facture.getNumero());
            fact.setNomClient(facture.getClient().getNomClient());
            fact.setMontantTotal(ligneBonDeSortieRepository.findAllByBonDeSortie(facture.getBonDeSortie())
                .stream().mapToLong(LigneBonDeSortie::getPrixDeVente).sum());
            fact.setResteApayer(fact.getMontantTotal() - reglementRepository.findByFacture(facture)
                .stream().mapToLong(Reglement::getMontantReglement).sum());

            list.add(fact);
        }
        return list;
    }

}
