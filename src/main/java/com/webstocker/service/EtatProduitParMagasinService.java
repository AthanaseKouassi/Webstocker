package com.webstocker.service;

import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.EtatProduitParMagasinWrapper;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Athanase
 */
public interface EtatProduitParMagasinService {
    
    List<EtatProduitParMagasinWrapper> etatProduitParMagasin(Produit produit,String dateDebut, String dateFin);
}
