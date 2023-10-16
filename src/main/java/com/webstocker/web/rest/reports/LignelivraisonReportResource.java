package com.webstocker.web.rest.reports;

import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;
import com.webstocker.reports.BordereauLivraisonsReport;
import com.webstocker.reports.BordereauReceptionsReport;
import com.webstocker.service.LignelivraisonService;
import com.webstocker.service.LivraisonService;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * REST controller for managing Lignelivraison.
 */
@RestController
@RequestMapping("/api")
public class LignelivraisonReportResource {

    private final Logger log = LoggerFactory.getLogger(LignelivraisonReportResource.class);
        
    @Inject
    private LignelivraisonService lignelivraisonService;
    
    @Inject
    private LivraisonService livraisonService;
    
    
//    @RequestMapping("/report/lignelivraisons/bordereaulivraison")
//    public void bordereauLivraisonReports(HttpServletRequest request,
//            HttpServletResponse response) {
//        response.setContentType("application/pdf");
//        List<Lignelivraison> all = lignelivraisonService.findAll();
//        OutputStream out = null;
//        try {
//            BordereauLivraisonsReport blReport = new BordereauLivraisonsReport(all);
//            out = response.getOutputStream();
//            blReport.build().toPdf(out);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    
    @RequestMapping("/report/lignelivraisons/bordereauReception")
    public void bordereauReceptionReports(HttpServletRequest request,
            HttpServletResponse response) {
        response.setContentType("application/pdf");
        List<Lignelivraison> all = lignelivraisonService.findAll();
        OutputStream out = null;
        try {
            BordereauReceptionsReport bordereauReceptionsReport = new BordereauReceptionsReport(all);
            out = response.getOutputStream();
            bordereauReceptionsReport.build().toPdf(out);
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
    
    @RequestMapping(value ="/report/lignelivraisons/bordereauReception/{id}", method = RequestMethod.GET)
    public void bordereauParApprovisionnement(HttpServletRequest request,
            HttpServletResponse response,@PathVariable Long id) {
        response.setContentType("application/pdf");
        
        Livraison livraison = livraisonService.findOne(id);
        
        List<Lignelivraison> all = lignelivraisonService.recupererLignesLivraison(livraison);
        OutputStream out = null;
        try {
            BordereauReceptionsReport bordereauReceptionsReport = new BordereauReceptionsReport(all);
            out = response.getOutputStream();
            bordereauReceptionsReport.build().toPdf(out);
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
