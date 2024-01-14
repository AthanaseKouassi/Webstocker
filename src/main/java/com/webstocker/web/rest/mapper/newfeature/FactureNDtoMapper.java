package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Facture;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.ClientRepository;
import com.webstocker.repository.FactureRepository;
import com.webstocker.web.rest.dto.newfeature.FactureNDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FactureNDtoMapper {
    @Autowired
    FactureRepository factureRepository;
    @Autowired
    BonDeSortieRepository bonDeSortieRepository;
    @Autowired
    ClientRepository clientRepository;


    FactureNDto ToFactureDTO(Facture facture) {
        FactureNDto fact = new FactureNDto();
        fact.setId(facture.getId());
        fact.setDateFacture(facture.getDateFacture());
        fact.setStatutFacture(facture.getStatutFacture());
        fact.setIdClient(facture.getClient().getId());
        fact.setNumero(facture.getNumero());
        fact.setDelaiPaiement(facture.getDelaiPaiement());
        //  fact.setReglements(new LinkedList<>(facture.getReglements()));
        fact.setIdBonDeSortie(facture.getBonDeSortie().getId());

        return fact;
    }


    Facture factureDtoToFacture(FactureNDto nDto) {
        Facture fact = new Facture();
        fact.setId(nDto.getId());
        fact.setDateFacture(nDto.getDateFacture());
        fact.setStatutFacture(nDto.getStatutFacture());
        fact.setClient(clientRepository.findOne(nDto.getIdClient()));
        fact.setNumero(nDto.getNumero());
        fact.setDelaiPaiement(nDto.getDelaiPaiement());
        // fact.setReglements(new HashSet<>(nDto.getReglements()));
        fact.setBonDeSortie(bonDeSortieRepository.findOne(nDto.getIdBonDeSortie()));
        return fact;
    }

    public List<FactureNDto> toFactureDTOs(List<Facture> factures) {
        List<FactureNDto> list = new ArrayList<>();
        for (Facture f : factures) {
            FactureNDto fact = new FactureNDto();
            fact.setId(f.getId());
            fact.setDateFacture(f.getDateFacture());
            fact.setStatutFacture(f.getStatutFacture());
            fact.setIdClient(f.getClient().getId());
            fact.setNumero(f.getNumero());
            fact.setDelaiPaiement(f.getDelaiPaiement());
            //fact.setReglements(new LinkedList<>(f.getReglements()));
            fact.setIdBonDeSortie(f.getBonDeSortie().getId());

            list.add(fact);
        }
        return list;
    }

}
