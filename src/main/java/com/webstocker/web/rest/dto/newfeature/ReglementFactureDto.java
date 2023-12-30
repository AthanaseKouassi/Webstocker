package com.webstocker.web.rest.dto.newfeature;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ReglementFactureDto {
    private Long idFacture;
    private List<ReglementDto> reglementDtos;
    private BigDecimal totalFacture;
}
