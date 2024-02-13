package com.webstocker.service;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Produit;
import com.webstocker.domain.Reglement;
import com.webstocker.domain.enumeration.TypeVente;
import com.webstocker.reports.newfeature.ChiffreAffaireParModePaiementPdf;
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

}
