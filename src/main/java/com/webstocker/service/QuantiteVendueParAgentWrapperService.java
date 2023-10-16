/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.service;

import com.webstocker.domain.wrapper.QuantiteVendueParAgentWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface QuantiteVendueParAgentWrapperService {
    
//    List<QuantiteVendueParAgentWrapper> quantiteVendueParAgentCommercial(String nomCommercial, String dateDebut, String dateFin);
    List<QuantiteVendueParAgentWrapper> quantiteVendueParAgentCommercial(Long IDCommercial, String dateDebut, String dateFin);
    List<QuantiteVendueParAgentWrapper> quantiteVendueParAgentCommercialParProduit(String nomCommercial,String nomProduit, String dateDebut, String dateFin);
    
}
