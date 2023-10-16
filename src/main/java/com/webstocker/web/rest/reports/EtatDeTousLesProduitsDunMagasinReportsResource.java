package com.webstocker.web.rest.reports;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import com.webstocker.reports.EtatDeTousLesProduitsMagasinReport;
import com.webstocker.service.EtatDeTousLesProduitsDunMagasinService;
import com.webstocker.service.MagasinService;
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
public class EtatDeTousLesProduitsDunMagasinReportsResource {
    
    private final Logger log = LoggerFactory.getLogger(EtatDeTousLesProduitsDunMagasinReportsResource.class);
    
    @Inject
    EtatDeTousLesProduitsDunMagasinService etatDeTousLesProduitsDunMagasinService;
    
    @Inject
    MagasinService magasinService;
    
    @RequestMapping(value = "/report/etatdetouslesproduitsdunmagasin/{nommagasin}/{dateDebut}/{dateFin}",
            method = RequestMethod.GET)
    public void etatDeTousLesProduits(HttpServletRequest request, HttpServletResponse response, @PathVariable String nommagasin,
            @PathVariable String dateDebut, @PathVariable String dateFin) {

        response.setContentType("application/pdf");
        
        Magasin magasin = magasinService.findByNomMagasin(nommagasin);
    
         List<EtatDeTousLesProduitsDunMagasinWrapper> all = etatDeTousLesProduitsDunMagasinService.etatdeTousLesProduitDunMagasin(magasin, dateDebut, dateFin);

        OutputStream out = null;
        
        try {
            EtatDeTousLesProduitsMagasinReport report = new EtatDeTousLesProduitsMagasinReport(all, dateDebut, dateFin);
            out = response.getOutputStream();
            report.build().toPdf(out);
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
