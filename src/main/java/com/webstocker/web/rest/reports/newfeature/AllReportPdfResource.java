package com.webstocker.web.rest.reports.newfeature;

import com.webstocker.service.FacturePdfService;
import com.webstocker.service.RecuPdfService;
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
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/report")
public class AllReportPdfResource {
    private final Logger log = LoggerFactory.getLogger(AllReportPdfResource.class);

    @Inject
    private FacturePdfService facturePdfService;


    @RequestMapping(value = "/facture/paiement/{factureId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long factureId) throws Exception {
        ByteArrayOutputStream pdfStream = facturePdfService.generateFacturePaiementsPdf(factureId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=paiement_facture.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/facture/paiement/numero/{factureNumero}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportPdf(@PathVariable String factureNumero) throws Exception {
        ByteArrayOutputStream pdfStream = facturePdfService.generateFacturePaiementsPdf(factureNumero);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=paiement_facture.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/creance/commercial/{commercialId}/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long commercialId, @PathVariable String dateDebut, @PathVariable String dateFin) throws Exception {
        ByteArrayOutputStream pdfStream = facturePdfService.generatecreanceCommercialPdf(commercialId, dateDebut, dateFin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=creance_client_commercial.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


}
