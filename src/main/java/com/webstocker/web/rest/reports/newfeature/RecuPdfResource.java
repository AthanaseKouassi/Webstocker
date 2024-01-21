package com.webstocker.web.rest.reports.newfeature;

import com.webstocker.domain.BonDeSortie;
import com.webstocker.service.RecuPdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/pdf")
public class RecuPdfResource {
    private final Logger log = LoggerFactory.getLogger(RecuPdfResource.class);

    @Inject
    private RecuPdfService recuPdfService;

    @RequestMapping(value = "/recu", method = RequestMethod.POST)
    public ResponseEntity<byte[]> exportPdf(@RequestBody BonDeSortie bonDeSortie) throws Exception {
        ByteArrayOutputStream pdfStream = recuPdfService.generateRecuPdf(bonDeSortie);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recu.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/credit/recu", method = RequestMethod.POST)
    public ResponseEntity<byte[]> exportCreditPdf(@RequestBody BonDeSortie bonDeSortie) throws Exception {
        ByteArrayOutputStream pdfStream = recuPdfService.generateCreditRecuPdf(bonDeSortie);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recu.pdf");
        headers.setContentLength(pdfStream.size());
        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }


}
