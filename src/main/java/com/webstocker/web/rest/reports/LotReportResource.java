package com.webstocker.web.rest.reports;

import com.webstocker.domain.Lot;
import com.webstocker.reports.BordereauLivraisonsReport;
import com.webstocker.reports.LotsReport;
import com.webstocker.service.LotService;
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
 * REST controller for managing Lot.
 */
@RestController
@RequestMapping("/api")
public class LotReportResource {

    private final Logger log = LoggerFactory.getLogger(LotReportResource.class);
        
    @Inject
    private LotService lotService;
    
       
    @RequestMapping("/report/lots/liste")
    public void listeLotsReports(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/pdf");
        List<Lot> all = lotService.findAll();
        OutputStream out = null;
        try {
            LotsReport lotsReport = new LotsReport(all);
            out = response.getOutputStream();
            lotsReport.build().toPdf(out);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        
   

}
