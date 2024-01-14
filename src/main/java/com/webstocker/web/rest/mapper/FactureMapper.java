package com.webstocker.web.rest.mapper;

import com.webstocker.domain.Facture;
import com.webstocker.web.rest.dto.FactureDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FactureMapper {
    FactureDTO factureToFactureDTO(Facture facture);

    Facture factureDtoToFacture(FactureDTO factureDTO);

    List<FactureDTO> factureToFactureDTOs(List<Facture> facture);
}
