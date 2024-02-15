package com.webstocker.service;


import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Produit;
import com.webstocker.domain.enumeration.TypeVente;
import com.webstocker.reports.newfeature.ChiffreAffaireParModePaiementPdf;
import com.webstocker.repository.LigneBonDeSortieRepository;
import com.webstocker.repository.ProduitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ChiffreAffairePdfService {

    @Inject
    private ChiffreAffaireParModePaiementPdf chiffreAffaireParModePaiementPdf;
    @Inject
    private LigneBonDeSortieRepository ligneBonDeSortieRepository;

    @Inject
    private ProduitRepository produitRepository;

    public ByteArrayOutputStream generateChiffreAffaireParModePaiementPdf(String modePAiement, String dateDebut, String dateFin) throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateD = LocalDate.parse(dateDebut, formatter);
        LocalDate dateF = LocalDate.parse(dateFin, formatter);

        List<Produit> produits = produitRepository.findAll();

        List<LigneBonDeSortie> ligneBonDeSortieRepos = ligneBonDeSortieRepository.findByBonDeSortieTypeVenteAndBonDeSortieDaateCreationBetweenOrderByProduit(
            modePAiement.equals(TypeVente.CASH.toString()) ? TypeVente.CASH : TypeVente.CREDIT,
            dateD,
            dateF
        );

        List<LigneBonDeSortie> ligneBonDeSorties = produits.stream().map(p -> {
            LigneBonDeSortie l = new LigneBonDeSortie();
            l.setProduit(p);
            l.setQuantite(ligneBonDeSortieRepos.stream().filter(lbs -> lbs.getProduit().getId().equals(p.getId())).mapToLong(LigneBonDeSortie::getQuantite).sum());
            l.setPrixDeVente(ligneBonDeSortieRepos.stream().filter(lbs -> lbs.getProduit().getId().equals(p.getId())).mapToLong(LigneBonDeSortie::getPrixDeVente).sum());
            return l;
        }).filter(l -> l.getQuantite() != 0).collect(Collectors.toList());


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.add(new Paragraph(" ").setPadding(10f));

        chiffreAffaireParModePaiementPdf.titreRecu(document);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        Paragraph p = new Paragraph();
        p.add(chiffreAffaireParModePaiementPdf.createBorderedText(modePAiement, dateD, dateF));
//        p.add(chiffreAffaireParModePaiementPdf.createBorderedText2(ligneBonDeSorties));
        document.add(p);
        // recuPdf.infoRecu(document, bonDeSortie);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        chiffreAffaireParModePaiementPdf.addTableCA(document, ligneBonDeSorties);
        document.add(new Paragraph(" "));

        document.close();
        return outputStream;
    }


//    public ByteArrayOutputStream generateCreditRecuPdf(Long idFacture) throws Exception {
//        Facture facture = factureRepository.findOne(idFacture);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        PdfWriter writer = new PdfWriter(outputStream);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document document = new Document(pdf, PageSize.A4);
//        document.add(new Paragraph(" ").setPadding(40f));
//
//        recuPdf.titreRecu(document);
//        document.add(new Paragraph(" "));
//        document.add(new Paragraph(" "));
//        recuCreditPdf.infoRecu(document, facture);
//        document.add(new Paragraph(" "));
//        document.add(new Paragraph(" "));
//        document.add(new Paragraph(" "));
//        recuCreditPdf.addTableRecu(document, facture);
//        document.add(new Paragraph(" "));
//
//        document.close();
//        return outputStream;
//    }

}
