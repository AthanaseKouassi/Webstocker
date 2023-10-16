package com.webstocker.service;

import com.webstocker.domain.wrapper.VenteParDistrictWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface VenteParDistrictService {
    
    List<VenteParDistrictWrapper> quantiteVendueParDistrict(String ville ,String dateDebut, String dateFin);
    
}
