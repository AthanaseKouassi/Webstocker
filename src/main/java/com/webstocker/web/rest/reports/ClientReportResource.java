/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.web.rest.reports;

import com.webstocker.domain.Client;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;
import com.webstocker.reports.ChiffreAffaireParCategorieClientReport;
import com.webstocker.reports.ChiffreAffaireUnClientReport;
import com.webstocker.reports.ClientsReport;
import com.webstocker.reports.FrequenceAchatsReport;
import com.webstocker.service.ChiffreAffaireService;
import com.webstocker.service.ClientService;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.service.newfeature.ClientPdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Athanase
 */
@RestController
@RequestMapping("/api")
public class ClientReportResource {

    private final Logger log = LoggerFactory.getLogger(ClientReportResource.class);

    @Inject
    private ClientPdfService clientPdfService;

    @Inject
    private ChiffreAffaireService chiffreAffaireService;
    @Inject
    private ClientService clientService;
    @Inject
    private LigneBonDeSortieService ligneBonDeSortieService;

    @RequestMapping("/report/clients/liste")
    public void listeDesClients(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        List<Client> all = clientService.findAll();
        OutputStream out = null;
        try {
            ClientsReport clientReports = new ClientsReport(all);
            out = response.getOutputStream();
            clientReports.build().toPdf(out);
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


    @RequestMapping(value = "/report/clients/frequenceAchat/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public void frequenceAchat(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable String dateDebut, @PathVariable String dateFin) {
        response.setContentType("application/pdf");
        List<LigneBonDeSortie> all = ligneBonDeSortieService.getFrequenceAchatByClientPeriode(dateDebut, dateFin);
        OutputStream out = null;
        try {
            FrequenceAchatsReport frequenceReport = new FrequenceAchatsReport(all, dateDebut, dateFin);
            out = response.getOutputStream();
            frequenceReport.build().toPdf(out);
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


    @RequestMapping(value = "/report/clients/chiffreaffairetypeclient/{libelleCategorie}/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public void chiffreAffaireTypeClient(HttpServletRequest request, HttpServletResponse response, @PathVariable String libelleCategorie,
                                         @PathVariable String dateDebut, @PathVariable String dateFin) {
        response.setContentType("application/pdf");
        List<ChiffreAffaireWrapper> all = chiffreAffaireService.chiffreAffaireUneCategorieClient(libelleCategorie, dateDebut, dateFin);
        OutputStream out = null;
        try {
            ChiffreAffaireParCategorieClientReport chiffreReport = new ChiffreAffaireParCategorieClientReport(all, dateDebut, dateFin);
            out = response.getOutputStream();
            chiffreReport.build().toPdf(out);
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

    @RequestMapping(value = "/report/client/chiffreaffaireunclient/{nomClient}/{dateDebutPeriode}/{dateFinPeriode}", method = RequestMethod.GET)
    public void chiffreAffaireUnclient(HttpServletRequest request,
                                       HttpServletResponse response, @PathVariable String nomClient, @PathVariable String dateDebutPeriode, @PathVariable String dateFinPeriode) {
        response.setContentType("application/pdf");

        List<ChiffreAffaireWrapper> all = chiffreAffaireService.chiffreAffaireParClient(nomClient, dateDebutPeriode, dateFinPeriode);

        OutputStream out = null;

        try {
            ChiffreAffaireUnClientReport bsReport = new ChiffreAffaireUnClientReport(all, dateDebutPeriode, dateFinPeriode);
            out = response.getOutputStream();
            bsReport.build().toPdf(out);
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


    @RequestMapping(value = "/report/client/commercial/{idUser}/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportClientParCommercialPdf(@PathVariable Long idUser,
                                                               @PathVariable String dateDebut,
                                                               @PathVariable String dateFin) throws Exception {
        ByteArrayOutputStream pdfStream = clientPdfService.generatePdf(idUser, dateDebut, dateFin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=recu.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
}
