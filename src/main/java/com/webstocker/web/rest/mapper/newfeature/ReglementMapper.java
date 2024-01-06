package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Reglement;
import com.webstocker.web.rest.dto.newfeature.ReglementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReglementMapper {

    ReglementMapper INSTANCE = Mappers.getMapper(ReglementMapper.class);

    @Mapping(target = "idFacture", source = "reglement.facture.id")
    @Mapping(target = "idProduit", source = "reglement.produit.id")
    ReglementDto reglementToReglementDto(Reglement reglement);

    @Mapping(target = "facture.id", source = "idFacture")
    @Mapping(target = "produit.id", source = "idProduit")
    Reglement reglementDtoToReglement(ReglementDto reglementDto);

    List<Reglement> listDtoTo(List<ReglementDto> reglementDtos);

}
