package com.webstocker.service;

import com.webstocker.domain.wrapper.SortieParProduitMagasinWrapper;
import java.util.List;

/**
 * 
 * Service Interface for managing SortieParProduitMagasinWrapper.
 * 
 * @author Athanase
 */
public interface SortieParProduitMagasinService {
    
    List<SortieParProduitMagasinWrapper> venteParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin);
   
    List<SortieParProduitMagasinWrapper> promotionParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin);
    
    List<SortieParProduitMagasinWrapper> transfertParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin);
   
    List<SortieParProduitMagasinWrapper> perteParProduitparMagasin(String nomProduit, String nomMagasin, String dateDebut, String dateFin);
}
