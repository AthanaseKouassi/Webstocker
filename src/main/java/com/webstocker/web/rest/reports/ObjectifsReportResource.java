package com.webstocker.web.rest.reports;

import com.webstocker.domain.Objectifs;
import com.webstocker.reports.TauxAtteinteObjectifsReport;
import com.webstocker.reports.TauxObjectifsParMoisParProduitReport;
import com.webstocker.service.ObjectifsService;
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
public class ObjectifsReportResource {

    private final Logger log = LoggerFactory.getLogger(ObjectifsReportResource.class);

    @Inject
    ObjectifsService objectifsService;
    
    @RequestMapping(value = "/report/objectifs/tauxatteinteobjectifs", method = RequestMethod.GET )
    public void tauxAtteinteObjectif(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/pdf");
       // List<Objectifs> all = objectifsService.lesTauxAtteinteObjectifs();
        List<Objectifs> all = objectifsService.tauxAtteinteObjectifsParMoisEtParProduit();
        OutputStream out = null;
        try{
            TauxAtteinteObjectifsReport tauxReport = new TauxAtteinteObjectifsReport(all);
            out = response.getOutputStream();
            tauxReport.build().toPdf(out);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
       }    
    }
    
        @RequestMapping(value = "/report/objectifs/tauxatteinteobjectifsparmois/{maDate}", method = RequestMethod.GET )
    public void tauxAtteinteObjectifsParMois(HttpServletRequest request, HttpServletResponse response,
    @PathVariable String maDate){
        response.setContentType("application/pdf");
       // List<Objectifs> all = objectifsService.lesTauxAtteinteObjectifs();
        List<Objectifs> all = objectifsService.tauxAtteinteObjectifsParProduitPourUnMois(maDate.trim());
        OutputStream out = null;
        try{
            TauxObjectifsParMoisParProduitReport tauxReport = new TauxObjectifsParMoisParProduitReport(all);
            out = response.getOutputStream();
            tauxReport.build().toPdf(out);
        }catch(Exception e){
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
