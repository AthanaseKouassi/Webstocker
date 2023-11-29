package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Reglement;
import com.webstocker.web.rest.dto.newfeature.ReglementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReglementMapper {

    @Mapping(target = "idFacture", source = "facture.id")
    @Mapping(target = "idProduit", source = "produit.id")
    ReglementDto reglementToReglementDto(Reglement reglement);

    @Mapping(target = "facture.id", source = "idFacture")
    @Mapping(target = "produit.id", source = "idProduit")
    Reglement reglementDtoToReglement(ReglementDto reglementDto);
}
