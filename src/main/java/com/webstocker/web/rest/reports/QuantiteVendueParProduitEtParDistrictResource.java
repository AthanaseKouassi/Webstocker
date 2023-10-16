package com.webstocker.web.rest.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.QuantiteVendueParAgentWrapper;
import com.webstocker.domain.wrapper.VenteParDistrictWrapper;
import com.webstocker.reports.QuantiteProduitVenduePourUneVilleReport;
import com.webstocker.reports.QuantiteVendueParAgentReport;
import com.webstocker.reports.QuantiteVendueParProduitEtParDistrictReport;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.service.QuantiteVendueParAgentWrapperService;
import com.webstocker.service.VenteParDistrictService;
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
public class QuantiteVendueParProduitEtParDistrictResource {
    
    private final Logger log = LoggerFactory.getLogger(QuantiteVendueParProduitEtParDistrictResource.class);
    
    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;
    
    @Inject
    private VenteParDistrictService venteParDistrictService;
    
    @Inject
    private QuantiteVendueParAgentWrapperService quantiteVendueParAgentWrapperService;
    
    @RequestMapping(value = "/report/quantite/quantite-vendue-par-commercial/{id}/{datedebut}/{datefin}", method = RequestMethod.GET)
    public void quantiteVendueParAgent(HttpServletRequest request, HttpServletResponse response,
            @PathVariable Long id, @PathVariable String datedebut, @PathVariable String datefin){
        
        response.setContentType("application/pdf");
        List<QuantiteVendueParAgentWrapper> all = quantiteVendueParAgentWrapperService.quantiteVendueParAgentCommercial(id,datedebut, datefin);
        OutputStream out = null;
        try{
            QuantiteVendueParAgentReport bonreport = new QuantiteVendueParAgentReport(all,datedebut,datefin);
            out = response.getOutputStream();
            bonreport.build().toPdf(out);
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
    
    
    @RequestMapping(value = "/report/bondesortie/quantiteproduitvenduedansuneville/{ville}/{datedebut}/{datefin}", method = RequestMethod.GET)
    public void quantiteProduitVendueDansUneVille(HttpServletRequest request,
            HttpServletResponse response,@PathVariable String ville, @PathVariable String datedebut, @PathVariable String datefin){
        
        response.setContentType("application/pdf");
        
//        List<LigneBonDeSortie> all = ligneBonDeSortieService.getQuantiteProduitVendueDansUneVille(ville, datedebut, datefin);
        List<VenteParDistrictWrapper> all = venteParDistrictService.quantiteVendueParDistrict(ville, datedebut, datefin);
        OutputStream out = null;
        try{
            QuantiteProduitVenduePourUneVilleReport bonreport = new QuantiteProduitVenduePourUneVilleReport(all,datedebut,datefin);
            out = response.getOutputStream();
            bonreport.build().toPdf(out);
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
