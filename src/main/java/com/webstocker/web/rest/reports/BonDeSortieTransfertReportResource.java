package com.webstocker.web.rest.reports;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.reports.BonDeSortieTransfertReport;
import com.webstocker.reports.BonDeSortiesReport;
import com.webstocker.reports.BordereauLivraisonsReport;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.service.LigneBonDeSortieService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
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
public class BonDeSortieTransfertReportResource {
    private final Logger log = LoggerFactory.getLogger(BonDeSortieTransfertReportResource.class);
    
    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;
    
    @Inject
    private BonDeSortieService bonDeSortieService;
    
    
    @RequestMapping(value = "/report/lignebondesorties/bondesortietransfert/{id}", method = RequestMethod.GET)
    public void bondesortieTransfert(HttpServletRequest request,
            HttpServletResponse response,Pageable pageable, @PathVariable Long id) {
        response.setContentType("application/pdf");
        
         BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.recupererLignesBonDeSortieTransfert(bonDeSortie);
        //Page<LigneBonDeSortie> all = ligneBonDeSortieService.findAll(pageable);
        OutputStream out = null;
        
        try{
            BonDeSortieTransfertReport bsReport = new BonDeSortieTransfertReport(ligneBonDeSorties);
            out = response.getOutputStream();
            bsReport.build().toPdf(out);
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
