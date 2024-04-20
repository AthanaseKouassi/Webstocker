package com.webstocker.service;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.reports.newfeature.AllReportPdf;
import com.webstocker.reports.newfeature.CreanceParCommercialReportPdf;
import com.webstocker.reports.newfeature.FactureSoldeesNonSoldeesPdf;
import com.webstocker.repository.FactureRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@Service
public class FacturePdfService {

    private final Logger log = LoggerFactory.getLogger(FacturePdfService.class);

    @Inject
    FactureSoldeesNonSoldeesPdf FactureSoldeesNonSoldeesPdf;
    @Inject
    AllReportPdf allReportPdf;
    @Inject
    CreanceParCommercialReportPdf creanceParCommercialReportPdf;
    @Inject
    private FactureRepository factureRepository;

    public ByteArrayOutputStream generateFactureSoldeesNonSoldeesPdf(String typeFacture, String dateDebut, String dateFin) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateD = LocalDate.parse(dateDebut, formatter);
        LocalDate dateF = LocalDate.parse(dateFin, formatter);

        List<Facture> factureRepos = factureRepository
            .findByDateFactureBetweenOrderByDateFactureDesc(dateD, dateF);

        List<Facture> factures = new ArrayList<>();

        if (typeFacture.equalsIgnoreCase("NON_SOLDE")) { // NON_SOLDE
            FactureSoldeesNonSoldeesPdf.TITRE_RECU = "FACTURES NON SOLDÉES";
            factures = factureRepos.stream().filter(f -> f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum()
                >
                f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()).collect(Collectors.toList());
        } else { // SOLDE
            FactureSoldeesNonSoldeesPdf.TITRE_RECU = "FACTURES SOLDÉES";
            factures = factureRepos.stream().filter(f -> f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum()
                <=
                f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()).collect(Collectors.toList());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        FactureSoldeesNonSoldeesPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        p.add(FactureSoldeesNonSoldeesPdf.createBorderedText(dateD, dateF));
        document.add(p);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        FactureSoldeesNonSoldeesPdf.addTableFacture(document, factures, typeFacture);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }


    public ByteArrayOutputStream generateFacturePaiementsPdf(Long factureId) throws Exception {

        Facture facture = factureRepository.findOne(factureId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        AllReportPdf.TITRE_RECU = "DETAIL PAIEMENT FACTURE";

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        allReportPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        document.add(p);
        allReportPdf.infoRecu(document, facture);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        allReportPdf.addTableFacture(document, facture);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }

    public ByteArrayOutputStream generateFacturePaiementsPdf(String factureNumero) throws Exception {

        Facture facture = factureRepository.findByNumero(factureNumero);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        AllReportPdf.TITRE_RECU = "DETAIL PAIEMENT FACTURE";

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        allReportPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        document.add(p);
        allReportPdf.infoRecu(document, facture);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        allReportPdf.addTableFacture(document, facture);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }


    public ByteArrayOutputStream generatecreanceCommercialPdf(Long commercialId, String dateDebut, String dateFin) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateD = LocalDate.parse(dateDebut, formatter);
        LocalDate dateF = LocalDate.parse(dateFin, formatter);

        List<Facture> factureRepos = factureRepository.findByBonDeSortieDemandeurIdAndDateFactureBetweenOrderByDateFacture(commercialId, dateD, dateF);

        List<Facture> factures = factureRepos.stream().filter(f -> f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum()
            >
            f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()).collect(Collectors.toList());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        creanceParCommercialReportPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        p.add(creanceParCommercialReportPdf.createBorderedText(factures, dateD, dateF));
        document.add(p);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        creanceParCommercialReportPdf.addTableFactures(document, factures);

        Table table2 = new Table(UnitValue.createPercentArray(new float[]{30f, 30, 30, 30})).useAllAvailableWidth();
        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        creanceParCommercialReportPdf.addCellTotalHT(table2);
        document.add(new Paragraph(" "));
        document.add(table2);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }


}
