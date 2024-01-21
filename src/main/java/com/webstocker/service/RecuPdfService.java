package com.webstocker.service;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;
import com.webstocker.reports.newfeature.RecuCreditPdf;
import com.webstocker.reports.newfeature.RecuPdf;
import com.webstocker.repository.BonDeSortieRepository;
import com.webstocker.repository.FactureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;


@Slf4j
@Transactional
@Service
public class RecuPdfService {

    @Autowired
    private RecuPdf recuPdf;
    @Autowired
    private RecuCreditPdf recuCreditPdf;

    @Inject
    private BonDeSortieRepository bonDeSortieRepository;
    @Inject
    private FactureRepository factureRepository;


    public ByteArrayOutputStream generateRecuPdf(BonDeSortie bonDeSortie) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(50f));

        recuPdf.titreRecu(document);
        recuPdf.infoRecu(document, bonDeSortie);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        recuPdf.addTableRecu(document, bonDeSortie);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }


    public ByteArrayOutputStream generateCreditRecuPdf(Facture facture) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(50f));

        recuCreditPdf.titreRecu(document);
        recuCreditPdf.infoRecu(document, facture);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        recuCreditPdf.addTableRecu(document, facture);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }
}
