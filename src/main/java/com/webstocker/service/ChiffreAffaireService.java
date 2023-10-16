package com.webstocker.service;

import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface ChiffreAffaireService {
    
    List<ChiffreAffaireWrapper> chiffreAffaireParProduit(String dateDebut, String DateFin);
    
    List<ChiffreAffaireWrapper> chiffreAffaireParClient(String client,String dateDebut, String DateFin);
    
    List<ChiffreAffaireWrapper> chiffreAffaireMagasin(String dateDebut, String DateFin);
    
    List<ChiffreAffaireWrapper> chiffreAffaireParMagasin(String magasin,String dateDebut, String DateFin);
    
    List<ChiffreAffaireWrapper> chiffreAffaireParCategorieClient(String dateDebut, String DateFin);
    
    List<ChiffreAffaireWrapper> chiffreAffaireUneCategorieClient(String libelleCategorie,String dateDebut, String DateFin);
    
}
