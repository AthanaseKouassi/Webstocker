package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.ProduitRepository;
import com.webstocker.web.rest.dto.newfeature.DetailFactureDto;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Objects;

@Component
public class DetailFactureMapper {

    @Inject
    private FactureRepository factureRepository;
    @Inject
    private ProduitRepository produitRepository;

    public DetailFactureDto mapToDto(LigneBonDeSortie ligneBonDeSortie, Facture facture) {
        DetailFactureDto detailFactureDto = new DetailFactureDto();
        detailFactureDto.setId(ligneBonDeSortie.getId());
        detailFactureDto.setIdFacture(facture.getId());

        detailFactureDto.setMontantRegle(!facture.getReglements().isEmpty() ? facture.getReglements().stream()
            .filter(r -> Objects.equals(r.getProduit().getId(), ligneBonDeSortie.getProduit().getId()))
            .mapToLong(Reglement::getMontantReglement).sum() : 0L);

        detailFactureDto.setIdProduit(ligneBonDeSortie.getProduit().getId());
        detailFactureDto.setQuantite(ligneBonDeSortie.getQuantite());
        detailFactureDto.setNomProduit(ligneBonDeSortie.getProduit().getNomProduit());
        detailFactureDto.setPrixDeVente(BigDecimal.valueOf(ligneBonDeSortie.getPrixDeVente()));
        detailFactureDto.setResteApaye(BigDecimal.valueOf(ligneBonDeSortie.getPrixDeVente() - detailFactureDto.getMontantRegle()));

        return detailFactureDto;
    }

    public DetailFactureDto mapNoReglementToDto(LigneBonDeSortie ligneBonDeSortie, Facture facture) {
        DetailFactureDto detailFactureDto = new DetailFactureDto();
        detailFactureDto.setIdFacture(facture.getId());
        detailFactureDto.setPrixDeVente(BigDecimal.valueOf(ligneBonDeSortie.getPrixDeVente()));
        detailFactureDto.setIdProduit(ligneBonDeSortie.getProduit().getId());
        detailFactureDto.setResteApaye(ligneBonDeSortie.getPrixVente());
        detailFactureDto.setNomProduit(ligneBonDeSortie.getProduit().getNomProduit());
        detailFactureDto.setResteApaye(BigDecimal.valueOf(ligneBonDeSortie.getPrixDeVente()));
        detailFactureDto.setQuantite(ligneBonDeSortie.getQuantite());

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
