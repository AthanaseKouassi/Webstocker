package com.webstocker.web.rest.reports;

import com.webstocker.domain.Lignelivraison;
import com.webstocker.domain.Livraison;
import com.webstocker.reports.BordereauLivraisonsReport;
import com.webstocker.reports.BordereauReceptionsReport;
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
 * REST controller for managing Livraison.
 */
@RestController
@RequestMapping("/api")
public class LivraisonReportResource {

    private final Logger log = LoggerFactory.getLogger(LivraisonReportResource.class);
        
    @Inject
    private LivraisonService livraisonService;
//    
//    @RequestMapping("/report/livraisons/bordereauReception")
//    public void bordereauReceptionReports(HttpServletRequest request,
//            HttpServletResponse response) {
//        response.setContentType("application/pdf");
//        List<Livraison> all = livraisonService.findAll();
//        OutputStream out = null;
//        try {
//            BordereauReceptionsReport bordereauReceptionsReport = new BordereauReceptionsReport(all);
//            out = response.getOutputStream();
//            bordereauReceptionsReport.build().toPdf(out);
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
//    
    
}
