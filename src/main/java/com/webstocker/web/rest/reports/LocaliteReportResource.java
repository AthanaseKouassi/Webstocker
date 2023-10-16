package com.webstocker.web.rest.reports;

import com.webstocker.domain.Localite;
import com.webstocker.reports.LocalitesReport;
import com.webstocker.service.LocaliteService;
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
 * REST controller for managing Localite.
 */
@RestController
@RequestMapping("/api")
public class LocaliteReportResource {

    private final Logger log = LoggerFactory.getLogger(LocaliteReportResource.class);
        
    @Inject
    private LocaliteService localiteService;    
        
    @RequestMapping("/report/localites/liste")
    public void listeLocalitesReports(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/pdf");
        List<Localite> all = localiteService.findAll();
        OutputStream out = null;
        try {
            LocalitesReport localitesReport = new LocalitesReport(all);
            out = response.getOutputStream();
            localitesReport.build().toPdf(out);

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
