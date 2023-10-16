package com.webstocker.web.rest.reports;

import com.webstocker.domain.Bailleur;
import com.webstocker.reports.BailleursReport;
import com.webstocker.service.BailleurService;
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
 * REST controller for managing Bailleur.
 */
@RestController
@RequestMapping("/api")
public class BailleurReportResource {

    private final Logger log = LoggerFactory.getLogger(BailleurReportResource.class);
        
    @Inject
    private BailleurService bailleurService;
    
   
    @RequestMapping("/report/bailleurs/liste")
    public void listeBailleursReports(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/pdf");
        List<Bailleur> all = bailleurService.findAll();
        OutputStream out = null;
        try {
            BailleursReport bailleursReport = new BailleursReport(all);
            out = response.getOutputStream();
            bailleursReport.build().toPdf(out);


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
