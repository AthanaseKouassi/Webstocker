package com.webstocker.web.rest.mapper.newfeature;

import com.webstocker.domain.Reglement;
import com.webstocker.web.rest.dto.newfeature.ReglementFactureDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReglementFactureMapper {
    ReglementFactureDto toDto(Reglement reglement);
}
