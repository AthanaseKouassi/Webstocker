/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.service;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface EtatDeTousLesProduitsDunMagasinService {
    
    List<EtatDeTousLesProduitsDunMagasinWrapper> etatdeTousLesProduitDunMagasin(Magasin magasin,String dateDebut, String dateFin);
    
    List<EtatDeTousLesProduitsDunMagasinWrapper> etatSituationStockMagasin(Magasin magasin, String dateDumois);
}
