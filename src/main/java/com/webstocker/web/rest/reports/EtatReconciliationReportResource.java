package com.webstocker.web.rest.reports;

import com.webstocker.domain.wrapper.EtatDeReconciliationWrapper;
import com.webstocker.reports.EtatReconciliationMensuelReport;
import com.webstocker.service.EtatReconciliationService;
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
public class EtatReconciliationReportResource {
    
    private final Logger log = LoggerFactory.getLogger(EtatReconciliationReportResource.class);
   
    @Inject
    EtatReconciliationService etatReconciliationService;
        
    
//    @RequestMapping(value = "/report/etatdereconciliationmensuel/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    @RequestMapping(value = "/report/etatdereconciliationmensuel/{dateFin}", method = RequestMethod.GET)
     public void rapportEtatReconciliation(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String dateFin) {
        response.setContentType("application/pdf");
//        response.setContentType("application/vnd.ms-excel");
        
        List<EtatDeReconciliationWrapper> all = etatReconciliationService.etatGlobalReconciliation(dateFin);        
       
        OutputStream out = null;
        
        try{
            EtatReconciliationMensuelReport bsReport = new EtatReconciliationMensuelReport(all,dateFin);
            out = response.getOutputStream();
            bsReport.build().toPdf(out);
//            bsReport.build().toXlsx(out);
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
