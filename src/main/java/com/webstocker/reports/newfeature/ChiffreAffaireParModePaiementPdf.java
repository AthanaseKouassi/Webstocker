package com.webstocker.reports.newfeature;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.ReglementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Slf4j
@Component
public class ChiffreAffaireParModePaiementPdf {

    private static final String TITRE_RECU = "CHIFFRE D'AFFAIRE PAR MODE DE PAIEMENT ";

    @Autowired
    private ReglementRepository reglementRepository;
    @Autowired
    private FactureRepository factureRepository;

    private BigDecimal totalCAs = BigDecimal.ZERO;

    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{25, 200f, 25f})).useAllAvailableWidth();
        //   table.setMargins(10f, 10f, 0f, 10f);
        table.addCell(createCellTitre(" ", 90).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(TITRE_RECU, 90).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(" ", 90));
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        doc.add(table);
    }

    public void infoRecu(Document doc, BonDeSortie bonDeSortie) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String currentDateTime = dateFormat.format(new Date());
        Facture facture = factureRepository.findByBonDeSortie(bonDeSortie);

        String info = "Date reglement :\n" +
            "Client :\n" +
            "Numero Facture :\n" +
            "Commercial :";
        String repInfo = currentDateTime + "\n" +
            facture.getClient().getNomClient() + "\n" +
            bonDeSortie.getNumeroFactureNormalise() + "\n" +
            bonDeSortie.getDemandeur().getLastName() + " " + bonDeSortie.getDemandeur().getFirstName();

        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 12,})).useAllAvailableWidth();
        table.addCell(createCellInfoRecu(info, 10));
        table.addCell(createCellInfoRecu(repInfo, 12));

        Table table2 = new Table(UnitValue.createPercentArray(new float[]{15, 15, 25f})).useAllAvailableWidth();
        table2.addCell(createCellInfoRecu("", 15));
        table2.addCell(createCellInfoRecu("", 15));
        table2.addCell(createCellInfoRecu("", 20));
        table2.setBorder(Border.NO_BORDER);

        Div div = new Div();
        div.add(table);
        div.add(table2);

        doc.add(div);

    }

    public Paragraph createBorderedText(String modePAiement, LocalDate dateDebut, LocalDate dateFin) {
        Paragraph container = new Paragraph();

        String info =
            "Mode paiement :   " + modePAiement + "\n" +
                "Date début :   " + (DateTimeFormatter.ofPattern("dd MMMM yyyy").format(dateDebut)) + "\n" +
                "Date fin :   " + (DateTimeFormatter.ofPattern("dd MMMM yyyy").format(dateFin)) + "\n";

        Text one = new Text(info);
        container.add(one);
        container.setBorder(new SolidBorder(ColorConstants.BLACK, 0f));
        container.setBorderRight(Border.NO_BORDER);
        container.setBorderLeft(Border.NO_BORDER);
        container.setBorderTop(Border.NO_BORDER);
        container.setBorderBottom(Border.NO_BORDER);
        return container;
    }

    public Paragraph createBorderedText2(BonDeSortie bonDeSortie) {
        Paragraph container = new Paragraph();
        Facture facture = factureRepository.findByBonDeSortie(bonDeSortie);

        String repInfo = facture.getDateFacture() + "\n" +
            facture.getClient().getNomClient() + "\n" +
            bonDeSortie.getNumeroFactureNormalise() + "\n" +
            bonDeSortie.getDemandeur().getLastName() + " " + bonDeSortie.getDemandeur().getFirstName();

        Text two = new Text(repInfo);
        container.setBorder(new SolidBorder(ColorConstants.BLACK, 0f));
        container.setBorderLeft(Border.NO_BORDER);
        container.setBorderRight(Border.NO_BORDER);
        container.add(two);

        return container;
    }

    public void addTableCA(Document doc, List<LigneBonDeSortie> ligneBonDeSortieRepository) {
//        final Facture facture = factureRepository.findByBonDeSortie(ligneBonDeSortieRepository);
//        List<Reglement> reglements = reglementRepository.findByFacture(facture);
        Table table = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20f})).useAllAvailableWidth();
        addHeadTable(table);
        addTableRow(ligneBonDeSortieRepository, table);
        doc.add(table);
        Table table2 = new Table(UnitValue.createPercentArray(new float[]{30f, 30f, 30f})).useAllAvailableWidth();
        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        addCellTotalHT(table2);
        doc.add(table2);
    }

    private void addHeadTable(Table table) {
        table.addHeaderCell(createHeaderCell("Produit", 60));
        table.addHeaderCell(createHeaderCell("Quantité", 15));
        table.addHeaderCell(createHeaderCell("Chiffre affaire", 10));
    }

    private void addTableRow(List<LigneBonDeSortie> lbs, Table table) {
        BigDecimal totalCA = BigDecimal.ZERO;

        for (LigneBonDeSortie l : lbs) {

            table.addCell(createCellReglements(l.getProduit().getNomProduit(), 60));
            table.addCell(createCellReglements(String.valueOf(l.getQuantite()), 40).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCellReglements(NumberFormat.getCurrencyInstance(new Locale("fr", "CI")).format(new BigDecimal(l.getPrixDeVente())), 40).setTextAlignment(TextAlignment.RIGHT));
            totalCA = totalCA.add(new BigDecimal(l.getPrixDeVente()));
        }
        totalCAs = totalCA;

    }

    private void addCellTotalHT(Table table) {
        table.addCell(createTotauxCell("TOTAL", 30).setPaddingTop(6));
        table.addCell(createTotauxCell("", 30).setPaddingTop(6));
        table.addCell(createTotauxCell(NumberFormat.getCurrencyInstance(new Locale("fr", "CI")).format(totalCAs), 30)
            .setTextAlignment(TextAlignment.RIGHT).setPaddingTop(6));

//        table.addCell(new Cell(1, 3)
//            .add(new Paragraph(NombreEnChiffre.getLettre(totalCAs.longValue())))
//            .setTextAlignment(TextAlignment.RIGHT)
//            .setBorderLeft(Border.NO_BORDER)
//            .setBorderRight(Border.NO_BORDER)
//            .setBorderBottom(Border.NO_BORDER));

    }

    private Cell createTotauxCell(String content, float width) {
        Cell cell = new Cell()
            .add(new Paragraph(content))
            .setWidth(width)
            .setBorderRight(Border.NO_BORDER)
            .setBorderLeft(Border.NO_BORDER)
            .setBorderTop(Border.NO_BORDER);

        Style style = new Style().setFontSize(10);
        cell.addStyle(style);
        return cell;
    }

    private Cell createCellReglements(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content)).setWidth(width);
        Style style = new Style().setFontSize(9);
        cell.addStyle(style);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(0f);
        return cell;
    }


    private Cell createHeaderCell(String header1, float width) {
        Cell cell = new Cell()
            .add(new Paragraph(header1).setTextAlignment(TextAlignment.CENTER))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            .setWidth(width)
            .setBold();

        Style style = new Style().setFontSize(10);
        cell.addStyle(style);
        return cell;
    }

    private Cell createCellTitre(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content)).setWidth(width).setBorder(Border.NO_BORDER);
        Style style = new Style().setFontSize(16).setBold().setFontColor(ColorConstants.BLACK);
        cell.addStyle(style);
        return cell;
    }

    private Cell createCellInfoRecu(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content)).setWidth(width);
        Style style = new Style().setFontSize(12).setBold().setFontColor(ColorConstants.BLACK);
        cell.addStyle(style);
        return cell;
    }


}
