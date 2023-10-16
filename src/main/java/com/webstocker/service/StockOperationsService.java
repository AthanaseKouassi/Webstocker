package com.webstocker.service;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.StockSortieWrapper;

import java.util.List;

/**
 * Service pour recuperer les informations utiles sur le stock
 * Created by komi on 09/09/16.
 */
public interface StockOperationsService {

    Long getAvailableProducts(Produit produit);

    Long getAvailableProductsByMagasin(Produit produit, Magasin magasin);

    List<StockSortieWrapper> faireUneSortie(Produit produit, Long quantite);
    
}
