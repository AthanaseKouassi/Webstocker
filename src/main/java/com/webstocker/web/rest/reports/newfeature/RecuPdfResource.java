package com.webstocker.web.rest.reports.newfeature;

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
public class RecuPdfResource {
    private final Logger log = LoggerFactory.getLogger(RecuPdfResource.class);

    @Inject
    private RecuPdfService recuPdfService;

    @RequestMapping(value = "/recu/{idBonDeSortie}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long idBonDeSortie) throws Exception {
        ByteArrayOutputStream pdfStream = recuPdfService.generateRecuPdf(idBonDeSortie);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=recu.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/credit/recu/{idFacture}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportCreditPdf(@PathVariable Long idFacture) throws Exception {
        ByteArrayOutputStream pdfStream = recuPdfService.generateCreditRecuPdf(idFacture);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=recu.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


}
