package com.webstocker.web.rest.reports;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.reports.FactureZeroReport;
import com.webstocker.reports.FacturesReport;
import com.webstocker.service.BonDeSortieService;
import com.webstocker.service.LigneBonDeSortieService;
import com.webstocker.service.NouvelleFactureWrapperService;
import com.webstocker.service.newfeature.CreancePdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class FactureReportResource {

    private final Logger log = LoggerFactory.getLogger(FactureReportResource.class);

    @Inject
    LigneBonDeSortieService ligneBonDeSortieService;

    @Inject
    BonDeSortieService bonDeSortieService;

    @Inject
    NouvelleFactureWrapperService nouvelleFactureWrapperService;
    @Inject
    private CreancePdfService creancePdfService;


    @RequestMapping("/report/lignesfactures/facture")
    public void facture(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        response.setContentType("application/pdf");
        Page<LigneBonDeSortie> all = ligneBonDeSortieService.findAll(pageable);
        OutputStream out = null;

        try {
            FacturesReport factureReport = new FacturesReport(all.getContent());
            out = response.getOutputStream();
            factureReport.build().toPdf(out);
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

    @RequestMapping(value = "/report/lignesfactures/facture/{id}", method = RequestMethod.GET)
    public void factureParBonSortie(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        response.setContentType("application/pdf");

        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.recupererLignesFacture(bonDeSortie);

//        List<NouvelleFactureWrapper> listeFact = nouvelleFactureWrapperService.nouvelleFactureSansLot(bonDeSortie);

        OutputStream out = null;

        try {
            FacturesReport factureReport = new FacturesReport(ligneBonDeSorties);
//           FactureNouvelleReport factureReport = new FactureNouvelleReport(listeFact);
            out = response.getOutputStream();
            factureReport.build().toPdf(out);
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

    @RequestMapping(value = "/report/lignesfactures/facturezero/{id}", method = RequestMethod.GET)
    public void factureZeroParBonSortie(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        response.setContentType("application/pdf");

        BonDeSortie bonDeSortie = bonDeSortieService.findOne(id);

        List<LigneBonDeSortie> ligneBonDeSorties = ligneBonDeSortieService.recupererLignesBonDeSortiePromotion(bonDeSortie);

        OutputStream out = null;

        try {
            FactureZeroReport factureReport = new FactureZeroReport(ligneBonDeSorties);
            out = response.getOutputStream();
            factureReport.build().toPdf(out);
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


    @RequestMapping(value = "/report/categorie-creance/{categorie}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportCategoriePdf(@PathVariable int categorie,
                                                     @RequestParam(name = "idProduit", defaultValue = "0") Long idProduit) throws Exception {
        ByteArrayOutputStream pdfStream = creancePdfService.generatePdf(categorie, idProduit);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=recu.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


}
