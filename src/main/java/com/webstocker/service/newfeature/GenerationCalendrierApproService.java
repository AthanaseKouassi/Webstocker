package com.webstocker.service.newfeature;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Produit;
import com.webstocker.service.ProduitService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class GenerationCalendrierApproService {

    @Inject
    private InventaireNewService inventaireNewService;

    @Inject
    private ProduitService produitService;


    public void genereFichierExcel(String dateInventaire) {
        final List<Produit> listProduits = produitService.findAll();

        final List<Inventaire> listInventaires = inventaireNewService.getInventaireByMonth(dateInventaire);


    }


}
