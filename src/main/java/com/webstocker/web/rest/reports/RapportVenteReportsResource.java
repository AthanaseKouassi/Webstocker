package com.webstocker.web.rest.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;
import com.webstocker.reports.ChiffreAffaireParClientsReport;
import com.webstocker.reports.ChiffreAffaireParMagasinReport;
import com.webstocker.reports.ChiffreAffaireParProduitsReports;
import com.webstocker.reports.ChiffreAffaireUnClientReport;
import com.webstocker.reports.ListeDesFacturesReport;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.service.ChiffreAffaireService;
import com.webstocker.service.LigneBonDeSortieService;
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
public class RapportVenteReportsResource {
    
    private final Logger log = LoggerFactory.getLogger(RapportVenteReportsResource.class);
    
    @Inject
    LigneBonDeSortieService ligneBonDeSortieService;

    @Inject
    BonDeSortieService bonDeSortieService;
    
    @Inject
    ChiffreAffaireService chiffreAffaireService;
    
    @RequestMapping(value ="/report/lignebondesorties/chiffreaffaireparproduit/{dateDebutPeriode}/{dateFinPeriode}" , method = RequestMethod.GET)
    public void chiffreAffaireParProduit(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String dateDebutPeriode, @PathVariable String dateFinPeriode) {
        response.setContentType("application/pdf");

//        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.chiffreAffaireParProduit(dateDebutPeriode,dateFinPeriode);
        List<ChiffreAffaireWrapper> chiffreAffaireParProduit = chiffreAffaireService.chiffreAffaireParProduit(dateDebutPeriode,dateFinPeriode);
     
        OutputStream out = null;

        try{
            ChiffreAffaireParProduitsReports bsReport = new ChiffreAffaireParProduitsReports(chiffreAffaireParProduit,dateDebutPeriode,dateFinPeriode);
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
    
    @RequestMapping(value ="/report/lignebondesorties/chiffreaffaireparclient/{dateDebutPeriode}/{dateFinPeriode}" , method = RequestMethod.GET)
    public void chiffreAffaireClient(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String dateDebutPeriode, @PathVariable String dateFinPeriode) {
        response.setContentType("application/pdf");

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.chiffreAffaireParClient(dateDebutPeriode,dateFinPeriode);
     
        OutputStream out = null;

        try{
            ChiffreAffaireParClientsReport bsReport = new ChiffreAffaireParClientsReport(ligneBonDeSorties,dateDebutPeriode,dateFinPeriode);
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
   
    
    @RequestMapping(value ="/report/lignebondesorties/chiffreaffaireparmagasin/{dateDebutPeriode}/{dateFinPeriode}" , method = RequestMethod.GET)
    public void chiffreAffaireMagasin(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String dateDebutPeriode, @PathVariable String dateFinPeriode) {
        response.setContentType("application/pdf");

//        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.getChiffreAffaireParMagasin(dateDebutPeriode,dateFinPeriode);
        List<ChiffreAffaireWrapper> listeChiffreAffaire = chiffreAffaireService.chiffreAffaireMagasin(dateDebutPeriode, dateFinPeriode);
     
        OutputStream out = null;

        try{
            ChiffreAffaireParMagasinReport bsReport = new ChiffreAffaireParMagasinReport(listeChiffreAffaire,dateDebutPeriode,dateFinPeriode);
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
    
    @RequestMapping(value ="/report/rapportdesventes/listedesfacturesparperiode/{dateDebutPeriode}/{dateFinPeriode}" , method = RequestMethod.GET)
    public void listeDesFacturesParPeriode(HttpServletRequest request,
            HttpServletResponse response, @PathVariable String dateDebutPeriode, @PathVariable String dateFinPeriode) {
        response.setContentType("application/pdf");

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.getListeDesFactureParPeriode(dateDebutPeriode,dateFinPeriode);
     
        OutputStream out = null;

        try{
            ListeDesFacturesReport bsReport = new ListeDesFacturesReport(ligneBonDeSorties,dateDebutPeriode,dateFinPeriode);
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
