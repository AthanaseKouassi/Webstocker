/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.web.rest.reports;

import com.webstocker.domain.Magasin;
import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import com.webstocker.reports.InventaireSituationDeStockReport;
import com.webstocker.service.EtatDeTousLesProduitsDunMagasinService;
import com.webstocker.service.InventaireService;
import com.webstocker.service.MagasinService;
import com.webstocker.service.newfeature.GenerationCalendrierApproService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class InventaireSituationDeStockReportResource {

    private final Logger log = LoggerFactory.getLogger(EtatDeTousLesProduitsDunMagasinReportsResource.class);

    @Inject
    EtatDeTousLesProduitsDunMagasinService etatDeTousLesProduitsDunMagasinService;

    @Inject
    MagasinService magasinService;
    @Inject
    InventaireService inventaireService;
    @Inject
    private GenerationCalendrierApproService generationCalendrierApproService;


    @RequestMapping(value = "/report/inventaire/{nommagasin}/{dateMois}",
        method = RequestMethod.GET)
    public void etatSituationDeStockMagasin(HttpServletRequest request, HttpServletResponse response, @PathVariable String nommagasin,
                                            @PathVariable String dateMois) {

        response.setContentType("application/pdf");
        final Magasin magasin = magasinService.findByNomMagasin(nommagasin);
        final List<EtatDeTousLesProduitsDunMagasinWrapper> all = etatDeTousLesProduitsDunMagasinService.etatSituationStockMagasin(magasin, dateMois);
//         List<EtatDeTousLesProduitsDunMagasinWrapper> all = etatDeTousLesProduitsDunMagasinService.etatdeTousLesProduitDunMagasin(magasin, dateDebut, dateFin);

        OutputStream out = null;

        try {
            InventaireSituationDeStockReport report = new InventaireSituationDeStockReport(all, dateMois);
            out = response.getOutputStream();
            report.build().toPdf(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/report/inventaires/calendrier-approvisionnement/{dateInventaire}",
        method = RequestMethod.GET)
    public void getExcelFile(@PathVariable String dateInventaire, HttpServletResponse response) throws IOException {

        final String fileName = "Calendrier_approvisionnement_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        generationCalendrierApproService.generateExcelFile(dateInventaire, response);
    }

}
