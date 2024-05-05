package com.webstocker.web.rest.dto.newfeature;

import java.math.BigDecimal;
import java.util.List;

public class ReglementFactureDto {
    private Long idFacture;
    private List<ReglementDto> reglementDtos;
    private BigDecimal totalFacture;

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public List<ReglementDto> getReglementDtos() {
        return reglementDtos;
    }

    public void setReglementDtos(List<ReglementDto> reglementDtos) {
        this.reglementDtos = reglementDtos;
    }

    public BigDecimal getTotalFacture() {
        return totalFacture;
    }

    public void setTotalFacture(BigDecimal totalFacture) {
        this.totalFacture = totalFacture;
    }
}
