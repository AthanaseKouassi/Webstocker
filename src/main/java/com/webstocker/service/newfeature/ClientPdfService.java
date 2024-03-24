package com.webstocker.service.newfeature;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.webstocker.domain.Client;
import com.webstocker.reports.newfeature.ListeClientParCommercialPdf;
import com.webstocker.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Transactional
@Service
public class ClientPdfService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ListeClientParCommercialPdf listeClientParCommercialPdf;


    public ByteArrayOutputStream generatePdf(Long idUser, String dateDebut, String dateFin) throws Exception {

        LocalDate debut = LocalDate.parse(dateDebut);
        LocalDate fin = LocalDate.parse(dateFin);
        List<Client> listClient = clientRepository.getClientParCommercialAndPeriode(idUser, dateDebut, dateFin);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        listeClientParCommercialPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        p.add(listeClientParCommercialPdf.createBorderedText(idUser, debut, fin));
        document.add(p);
        document.add(new Paragraph(" "));

        listeClientParCommercialPdf.addTableRecu(document, listClient, dateDebut, dateFin);

        document.close();
        return outputStream;
    }


}
