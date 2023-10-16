package com.webstocker.service;

import com.webstocker.domain.Bailleur;
import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface EtatStockBailleurService {
    List<EtatStockGlobalAimasWrapper> etatStockParBailleur(String nomBailleur, String dateDebut, String dateFin);
    
   // List<EtatStockGlobalAimasWrapper> etatStockProduitBailleur(Bailleur bailleur,Produit produit, String dateDebut, String dateFin);
    
    List<EtatStockGlobalAimasWrapper> etatStockProduitLivreParBailleur(Bailleur bailleur, String dateDebut, String dateFin);
}
