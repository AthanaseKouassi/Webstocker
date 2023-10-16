package com.webstocker.web.rest.reports;

import com.webstocker.domain.Fabricant;
import com.webstocker.reports.FabricantsReport;
import com.webstocker.service.FabricantService;
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
 * REST controller for managing Fabricant.
 */
@RestController
@RequestMapping("/api")
public class FabricantReportResource {

    private final Logger log = LoggerFactory.getLogger(FabricantReportResource.class);
        
    @Inject
    private FabricantService fabricantService;
    
       
    @RequestMapping("/report/fabricants/liste")
    public void listeFabricantsReports(HttpServletRequest request,
                                     HttpServletResponse response){
        response.setContentType("application/pdf");
        List<Fabricant> all = fabricantService.findAll();
        OutputStream out = null;
        try {
            FabricantsReport fabricantsReport = new FabricantsReport(all);
            out = response.getOutputStream();
            fabricantsReport.build().toPdf(out);


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
