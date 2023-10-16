/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.service;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.wrapper.NouvelleFactureWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface NouvelleFactureWrapperService {
    
    List<NouvelleFactureWrapper> nouvelleFactureSansLot (BonDeSortie bonDeSortie);
    
}
