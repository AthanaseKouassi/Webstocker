package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.domain.User;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ReglementRepository;
import com.webstocker.web.rest.dto.newfeature.CreanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreanceDtoMapper {

    @Autowired
    ReglementRepository reglementRepository;
    @Autowired
    LigneBonDeSortieRepository ligneBonDeSortieRepository;

    public CreanceDto mapToCreanceDto(Facture facture, LigneBonDeSortie ligneBonDeSortie, User user, int categorie) {
        CreanceDto dto = new CreanceDto();
        dto.setDateFacture(facture.getDateFacture());
        dto.setNumero(facture.getNumero());
        dto.setIdClient(facture.getClient().getId());
        dto.setNomClient(facture.getClient().getNomClient());
        dto.setIdUser(user.getId());
        dto.setIdProduit(ligneBonDeSortie.getProduit().getId());
        dto.setPrixDeVente(ligneBonDeSortie.getPrixDeVente());
        dto.setTelClient(facture.getClient().getTelephoneClient());
        dto.setIdFacture(facture.getId());
        dto.setLibelleCategorie("Categorie " + categorie);
        dto.setNomUser(user.getFirstName() + " " + user.getLastName());
        dto.setNomProduit(ligneBonDeSortie.getProduit().getNomProduit());
        dto.setMontantRegle(facture.getReglements().stream()
            .filter(r -> r.getProduit().equals(ligneBonDeSortie.getProduit()))
            .mapToLong(Reglement::getMontantReglement).sum());
        dto.setResteApayer(ligneBonDeSortie.getPrixDeVente() - dto.getMontantRegle());

        return dto;
    }

    public CreanceDto mapToCreanceDtoClient(Facture facture, LigneBonDeSortie ligneBonDeSortie) {
        CreanceDto dto = new CreanceDto();
        dto.setDateFacture(facture.getDateFacture());
        dto.setNumero(facture.getNumero());
        dto.setIdClient(facture.getClient().getId());
        dto.setNomClient(facture.getClient().getNomClient());
        dto.setTelClient(facture.getClient().getTelephoneClient());
        dto.setIdProduit(ligneBonDeSortie.getProduit().getId());
        dto.setPrixDeVente(ligneBonDeSortie.getPrixDeVente());
        dto.setTelClient(facture.getClient().getTelephoneClient());
        dto.setIdFacture(facture.getId());
        dto.setNomUser(facture.getBonDeSortie().getDemandeur().getFirstName() + " " + facture.getBonDeSortie().getDemandeur().getLastName());
        dto.setNomProduit(ligneBonDeSortie.getProduit().getNomProduit());
        dto.setMontantRegle(facture.getReglements().stream()
            .filter(r -> r.getProduit().equals(ligneBonDeSortie.getProduit()))
            .mapToLong(Reglement::getMontantReglement).sum());
        dto.setResteApayer(ligneBonDeSortie.getPrixDeVente() - dto.getMontantRegle());

        return dto;
    }


}
