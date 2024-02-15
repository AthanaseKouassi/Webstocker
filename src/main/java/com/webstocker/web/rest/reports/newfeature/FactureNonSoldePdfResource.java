package com.webstocker.web.rest.reports.newfeature;

import com.webstocker.service.ChiffreAffairePdfService;
import com.webstocker.service.FacturePdfService;
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
public class FactureNonSoldePdfResource {
    private final Logger log = LoggerFactory.getLogger(FactureNonSoldePdfResource.class);

    @Inject
    private FacturePdfService facturePdfService;

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

}
