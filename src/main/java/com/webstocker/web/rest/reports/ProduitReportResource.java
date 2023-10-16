package com.webstocker.web.rest.reports;

import com.webstocker.domain.Produit;
import com.webstocker.reports.BordereauLivraisonsReport;
import com.webstocker.reports.ProduitsReport;
import com.webstocker.service.ProduitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * REST controller for managing Produit.
 */
@RestController
@RequestMapping("/api")
public class ProduitReportResource {

    private final Logger log = LoggerFactory.getLogger(ProduitReportResource.class);

    @Inject
    private ProduitService produitService;


    @RequestMapping("/report/produits/liste")
    public void listeProduitsReports(HttpServletRequest request,
            HttpServletResponse response) {
        response.setContentType("application/pdf");
        List<Produit> all = produitService.findAll();
        OutputStream out = null;
        try {
            ProduitsReport produitsReport = new ProduitsReport(all);
            out = response.getOutputStream();
            produitsReport.build().toPdf(out);

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
