package com.webstocker.web.rest.reports.newfeature;

import com.webstocker.service.FacturePdfService;
import com.webstocker.service.newfeature.CreancePdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/report")
public class FactureNonSoldePdfResource {
    private final Logger log = LoggerFactory.getLogger(FactureNonSoldePdfResource.class);

    @Inject
    private FacturePdfService facturePdfService;
    @Inject
    private CreancePdfService creancePdfService;

    /* NO USE */
    @RequestMapping(value = "/facture/liste-factures-non-soldees/{dateDebut}/{dateFin}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportPdf(@PathVariable String dateDebut, @PathVariable String dateFin) throws Exception {
        ByteArrayOutputStream pdfStream = facturePdfService.generateFactureNonSoldeesPdf(dateDebut, dateFin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=factures-non-soldees.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/facture/creance-client-period", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportPdfCreanceClient(@RequestParam Long idClient,
                                                         @RequestParam String dateDebut,
                                                         @RequestParam String dateFin) throws Exception {
        ByteArrayOutputStream pdfStream = creancePdfService.generatePdfCreanceClient(idClient, dateDebut, dateFin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Creance-client-periode.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }

}
