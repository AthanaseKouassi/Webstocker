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
import com.webstocker.domain.Produit;
import com.webstocker.domain.Reglement;
import com.webstocker.domain.enumeration.TypeVente;
import com.webstocker.reports.newfeature.AllReportPdf;
import com.webstocker.reports.newfeature.ChiffreAffaireParModePaiementPdf;
import com.webstocker.reports.newfeature.CreanceParCommercialReportPdf;
import com.webstocker.reports.newfeature.FactureNonSoldeesPdf;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ProduitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@Service
public class FacturePdfService {


    @Inject
    private FactureRepository factureRepository;

    @Inject
    FactureNonSoldeesPdf factureNonSoldeesPdf;
    @Inject
    AllReportPdf allReportPdf;
    @Inject
    CreanceParCommercialReportPdf creanceParCommercialReportPdf;


    public ByteArrayOutputStream generateFactureNonSoldeesPdf(String dateDebut, String dateFin) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateD = LocalDate.parse(dateDebut, formatter);
        LocalDate dateF = LocalDate.parse(dateFin, formatter);

        List<Facture> factureRepos = factureRepository.findByDateFactureBetweenOrderByDateFactureDesc(
            dateD,
            dateF
        );

        List<Facture> factures = factureRepos.stream().filter(f -> {
            return
                f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(bs -> bs.getQuantite() * bs.getPrixDeVente()).sum()
                >
                f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum();
        }).collect(Collectors.toList());


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        factureNonSoldeesPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        p.add(factureNonSoldeesPdf.createBorderedText(dateD, dateF));
//        p.add(chiffreAffaireParModePaiementPdf.createBorderedText2(ligneBonDeSorties));
        document.add(p);
        // recuPdf.infoRecu(document, bonDeSortie);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        factureNonSoldeesPdf.addTableFacture(document, factures);
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
//        p.add(allReportPdf.createBorderedText(dateD, dateF));
//        p.add(chiffreAffaireParModePaiementPdf.createBorderedText2(ligneBonDeSorties));
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
//        p.add(allReportPdf.createBorderedText(dateD, dateF));
//        p.add(chiffreAffaireParModePaiementPdf.createBorderedText2(ligneBonDeSorties));
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

        List<Facture> factureRepos = factureRepository.findByBonDeSortieDemandeur_IdAndDateFactureBetweenOrderByDateFacture(commercialId, dateD, dateF);

        List<Facture> factures = factureRepos.stream().filter(f -> {
            return
                f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum()
                    >
                    f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum();
        }).collect(Collectors.toList());


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        CreanceParCommercialReportPdf.TITRE_RECU = "ETAT DES CREANCES PAR COMMERCIAL";

        creanceParCommercialReportPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        p.add(creanceParCommercialReportPdf.createBorderedText(factures, dateD, dateF));
//        p.add(chiffreAffaireParModePaiementPdf.createBorderedText2(ligneBonDeSorties));
        document.add(p);
//        creanceParCommercialReportPdf.infoRecu(document, bonDeSortie);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        creanceParCommercialReportPdf.addTableFactures(document, factures);

        Table table2 = new Table(UnitValue.createPercentArray(new float[]{30f, 30, 30})).useAllAvailableWidth();
        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        creanceParCommercialReportPdf.addCellTotalHT(table2);
        document.add(new Paragraph(" "));
        document.add(table2);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }




}
