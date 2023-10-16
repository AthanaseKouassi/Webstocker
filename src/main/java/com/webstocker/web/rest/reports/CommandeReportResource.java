package com.webstocker.web.rest.reports;

import com.webstocker.domain.Commande;
import com.webstocker.domain.Lignecommande;
import com.webstocker.reports.CommandesReport;
import com.webstocker.service.CommandeService;
import com.webstocker.service.LignecommandeService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class CommandeReportResource {
     private final Logger log = LoggerFactory.getLogger(FactureReportResource.class);
     
     @Inject
     LignecommandeService lignecommandeService;
     
     @Inject
     CommandeService commandeService;
     
     @RequestMapping("/report/lignecommande/commande/{id}")
     public void commande(HttpServletRequest request, HttpServletResponse response,@PathVariable Long id){
       response.setContentType("application/pdf");
       
       Commande commande = commandeService.findOne(id);
       List <Lignecommande> all = lignecommandeService.findByCommande(commande);
       OutputStream out = null;

       try{
           CommandesReport commandeReport = new CommandesReport(all);
           out = response.getOutputStream();
           commandeReport.build().toPdf(out);
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
