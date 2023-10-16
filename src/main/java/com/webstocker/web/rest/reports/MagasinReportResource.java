package com.webstocker.web.rest.reports;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Magasin;
import com.webstocker.domain.enumeration.TypeSortie;
import com.webstocker.reports.ListeDesFactureWrapper;
import com.webstocker.reports.ListeTransfertParMagasinReport;
import com.webstocker.reports.MagasinsReport;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.service.MagasinService;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing Magasin.
 */
@RestController
@RequestMapping("/api")
public class MagasinReportResource {

    private final Logger log = LoggerFactory.getLogger(MagasinReportResource.class);

    @Inject
    private MagasinService magasinService;

    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;

    @RequestMapping("/report/magasins/liste")
    public void listeMagasinsReports(HttpServletRequest request,
            HttpServletResponse response) {
        response.setContentType("application/pdf");
        List<Magasin> all = magasinService.findAll();
        OutputStream out = null;
        try {
            MagasinsReport magasinsReport = new MagasinsReport(all);
            out = response.getOutputStream();
            magasinsReport.build().toPdf(out);

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

    /**
     * Etat des transfert d'un magasin
     *
     * @param request
     * @param response
     * @param nomMagasin
     * @param dateDebut
     * @param dateFin
     */
    @RequestMapping(value = "/report/listedestransfertsparmagasin/{nomMagasin}/{dateDebut}/{dateFin}",
            method = RequestMethod.GET)
    public void listeTransfertParmagasin(HttpServletRequest request, HttpServletResponse response, @PathVariable String nomMagasin,
            @PathVariable String dateDebut, @PathVariable String dateFin) {

        response.setContentType("application/pdf");

        List<LigneBonDeSortie> lbs = ligneBonDeSortieService.transfert(nomMagasin, dateDebut, dateFin);
        OutputStream out = null;

        try {
            ListeTransfertParMagasinReport reportList = new ListeTransfertParMagasinReport(lbs, nomMagasin, dateDebut, dateFin);
            out = response.getOutputStream();
            reportList.build().toPdf(out);
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
}
