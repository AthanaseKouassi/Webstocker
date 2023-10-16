package com.webstocker.web.rest.reports;

import com.webstocker.domain.Produit;
import com.webstocker.domain.wrapper.EtatProduitParMagasinWrapper;
import com.webstocker.reports.EtatDesProduitsMagasinsReport;
import com.webstocker.service.EtatProduitParMagasinService;
import com.webstocker.service.ProduitService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class EtatDesProduitsParMagasinReportResource {

    private final Logger log = LoggerFactory.getLogger(EtatDesProduitsParMagasinReportResource.class);

    @Inject
    EtatProduitParMagasinService etatProduitParMagasinService;
    
    @Inject
    ProduitService produitService;

    @RequestMapping(value = "/report/etatdeproduitsparmagasin/{nomproduit}/{dateDebut}/{dateFin}",
            method = RequestMethod.GET)
    public void etatDeProduitsParMagasin(HttpServletRequest request, HttpServletResponse response, @PathVariable String nomproduit,
            @PathVariable String dateDebut, @PathVariable String dateFin) {

        response.setContentType("application/pdf");
        
        Produit produit = produitService.findByNomProduit(nomproduit);
        System.out.println(" LE PRODUIT CHOISI "+produit.getNomProduit());
         List<EtatProduitParMagasinWrapper> all = etatProduitParMagasinService.etatProduitParMagasin(produit, dateDebut, dateFin);

        OutputStream out = null;

        try {
            EtatDesProduitsMagasinsReport reportEtatDesProduitparMagasin = new EtatDesProduitsMagasinsReport(all, dateDebut, dateFin);
            out = response.getOutputStream();
            reportEtatDesProduitparMagasin.build().toPdf(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
