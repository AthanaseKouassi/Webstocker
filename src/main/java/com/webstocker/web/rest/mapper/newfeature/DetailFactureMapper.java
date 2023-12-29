package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.web.rest.dto.newfeature.DetailFactureDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DetailFactureMapper {

    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private ProduitRepository produitRepository;

    public DetailFactureDto mapToDto(Reglement reglement, LigneBonDeSortie ligneBonDeSortie, Facture facture) {
        DetailFactureDto detailFactureDto = new DetailFactureDto();
        detailFactureDto.setIdFacture(facture.getId());
        detailFactureDto.setMontantRegle(reglement.getMontantReglement());
        detailFactureDto.setDateReglement(reglement.getDateReglement());
        detailFactureDto.setIdProduit(ligneBonDeSortie.getProduit().getId());
        detailFactureDto.setResteApaye(ligneBonDeSortie.getPrixVente()
            .subtract(BigDecimal.valueOf(reglement.getMontantReglement())));

        return detailFactureDto;
    }

    public DetailFactureDto mapNoReglementToDto(LigneBonDeSortie ligneBonDeSortie, Facture facture) {
        DetailFactureDto detailFactureDto = new DetailFactureDto();
        detailFactureDto.setIdFacture(facture.getId());
        detailFactureDto.setPrixDeVente(BigDecimal.valueOf(ligneBonDeSortie.getPrixDeVente()));
        detailFactureDto.setIdProduit(ligneBonDeSortie.getProduit().getId());
        detailFactureDto.setResteApaye(ligneBonDeSortie.getPrixVente());

        return detailFactureDto;
    }

    public Reglement createReglement(DetailFactureDto detailFactureDto) {
        Reglement reglement = new Reglement();
        reglement.setMontantReglement(detailFactureDto.getMontantRegle());
        reglement.setFacture(factureRepository.findOne(detailFactureDto.getIdFacture()));
        reglement.setProduit(produitRepository.findOne(detailFactureDto.getIdProduit()));
        reglement.setDateReglement(detailFactureDto.getDateReglement());
        return reglement;
    }

}
