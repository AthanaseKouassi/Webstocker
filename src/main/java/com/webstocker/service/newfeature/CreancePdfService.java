package com.webstocker.service.newfeature;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.webstocker.reports.newfeature.CreanceClientPdf;
import com.webstocker.reports.newfeature.CreancePdf;
import com.webstocker.service.FactureService;
import com.webstocker.web.rest.dto.newfeature.CreanceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Transactional
@Service
public class CreancePdfService {

    @Autowired
    private CreanceClientPdf creanceClientPdf;
    @Autowired
    private CreancePdf creancePdf;
    @Inject
    private FactureService factureService;

    public ByteArrayOutputStream generatePdf(int numeroCategorie) throws Exception {

        List<CreanceDto> creanceDtos = factureService.getFactureCreance(numeroCategorie);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.add(new Paragraph(" ").setPadding(15f));

        creancePdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        p.add(creancePdf.createBorderedText(numeroCategorie));
        document.add(p);
        document.add(new Paragraph(" "));
        creancePdf.addTableRecu(document, creanceDtos);

        document.close();
        return outputStream;
    }

    public ByteArrayOutputStream generatePdfCreanceClient(Long idClient, String dateDebut, String dateFin) throws Exception {
        List<CreanceDto> creanceDtos = factureService.getCreanceParClientAndPeriode(idClient, dateDebut, dateFin);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(15f));

        creanceClientPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        //   p.add(creancePdf.createBorderedText(numeroCategorie));
        document.add(p);
        document.add(new Paragraph(" "));
        //   creancePdf.addTableRecu(document, creanceDtos);

        document.close();

        return outputStream;
    }


}
