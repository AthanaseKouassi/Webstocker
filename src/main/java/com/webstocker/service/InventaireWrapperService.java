package com.webstocker.service;

import com.webstocker.domain.wrapper.InventaireWrapper;

import java.util.List;

/**
 * @author Athanase
 */
public interface InventaireWrapperService {

    List<InventaireWrapper> situationStockMagasin(String nomProduit, String nomMagasin, String dateInventaire);

    InventaireWrapper situationDunProduitMagasin(String nomProduit, String nomMagasin, String dateInventaire);

    InventaireWrapper getEtatProduit(String nomProduit, String dateInventaire);


}
