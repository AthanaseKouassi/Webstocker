package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Inventaire;
import com.webstocker.web.rest.dto.newfeature.InventaireDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventaireMapper {

    InventaireDto inventaireToInventaireDto(Inventaire inventaire);

    Inventaire inventaireDtoToInventaire(InventaireDto inventaireDto);
}
