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
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Produit;
import com.webstocker.domain.Reglement;
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
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class CreanceParCommercialReportPdf {

    public static String TITRE_RECU = "";
    private static final String PATTERN_DATE = "dd MMMM yyyy";

    @Autowired
    private ReglementRepository reglementRepository;
    @Autowired
    private FactureRepository factureRepository;

    private BigDecimal totalCAs = BigDecimal.ZERO;

    private BigDecimal totalSolde = BigDecimal.ZERO;
    private BigDecimal totalResteSolde = BigDecimal.ZERO;

    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{200f})).useAllAvailableWidth();
        table.addCell(createCellTitre().setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        doc.add(table);
    }

    public void infoRecu(Document doc, Facture facture) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String currentDateTime = dateFormat.format(new Date());

        String info = "Date reglement :\n" +
            "Client :\n" +
            "Numero Facture :\n" +
            "Commercial :";
        String repInfo = currentDateTime + "\n" +
            facture.getClient().getNomClient() + "\n" +
            facture.getBonDeSortie().getNumeroFactureNormalise() + "\n" +
            facture.getBonDeSortie().getDemandeur().getLastName() + " " + facture.getBonDeSortie().getDemandeur().getFirstName();

        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 12,})).useAllAvailableWidth();
        table.addCell(createCellInfoRecu(info, 10));
        table.addCell(createCellInfoRecu(repInfo, 12));

        Table table2 = new Table(UnitValue.createPercentArray(new float[]{15, 15, 25f})).useAllAvailableWidth();

        table2.setBorder(Border.NO_BORDER);

        Div div = new Div();
        div.add(table);
        div.add(table2);

        doc.add(div);

    }

    public Paragraph createBorderedText(LocalDate dateDebut, LocalDate dateFin) {
        Paragraph container = new Paragraph();

        String info = "Date début :   " + (DateTimeFormatter.ofPattern(PATTERN_DATE).format(dateDebut)) + "\n" +
            "Date fin :   " + (DateTimeFormatter.ofPattern(PATTERN_DATE).format(dateFin)) + "\n";

        Text one = new Text(info);
        container.add(one);
        container.setBorder(new SolidBorder(ColorConstants.BLACK, 0f));
        container.setBorderRight(Border.NO_BORDER);
        container.setBorderLeft(Border.NO_BORDER);
        container.setBorderTop(Border.NO_BORDER);
        container.setBorderBottom(Border.NO_BORDER);
        return container;
    }


    public Paragraph createBorderedText(List<Facture> factures, LocalDate dateDebut, LocalDate dateFin) {
        Paragraph container = new Paragraph();

        String info = "Commercial :   " + (!factures.isEmpty() ? (factures.get(0).getBonDeSortie().getDemandeur().getFirstName().toUpperCase() + " " + factures.get(0).getBonDeSortie().getDemandeur().getLastName().toUpperCase()) : "") + "\n" +
        "Date début :   " + (DateTimeFormatter.ofPattern(PATTERN_DATE).format(dateDebut)) + "\n" +
            "Date fin :   " + (DateTimeFormatter.ofPattern(PATTERN_DATE).format(dateFin)) + "\n";
        ;

        Text one = new Text(info);
        container.add(one);
        container.setBorder(new SolidBorder(ColorConstants.BLACK, 0f));
        container.setBorderRight(Border.NO_BORDER);
        container.setBorderLeft(Border.NO_BORDER);
        container.setBorderTop(Border.NO_BORDER);
        container.setBorderBottom(Border.NO_BORDER);
        return container;
    }

//    public void addTableFacture(Document doc, Facture facture) {
//        Table table = new Table(UnitValue.createPercentArray(new float[]{20f, 20, 20})).useAllAvailableWidth();
//        addHeadTable(table);
//        addTableRowFacture(facture, table);
//        doc.add(table);
//        Table table2 = new Table(UnitValue.createPercentArray(new float[]{30f, 30, 30})).useAllAvailableWidth();
//        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
//        addCellTotalHT(table2);
//        doc.add(table2);
//    }

    public void addTableFactures(Document doc, List<Facture> factures) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20f})).useAllAvailableWidth();
        addHeadTable(table);
        addTableRow(factures, table);
        doc.add(table);
        Table table2 = new Table(UnitValue.createPercentArray(new float[]{30f, 30f, 30f})).useAllAvailableWidth();
        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
//        addCellTotalHT(table2);
        doc.add(table2);
    }

    private void addHeadTable(Table table) {
//        table.addHeaderCell(createHeaderCell("Produit", 60));
//        table.addHeaderCell(createHeaderCell("Date facture", 10));
//        table.addHeaderCell(createHeaderCell("N° facture", 10));
//        table.addHeaderCell(createHeaderCell("Client", 10));
        table.addHeaderCell(createHeaderCell("Produit", 10));
        table.addHeaderCell(createHeaderCell("Montant encaissé", 15));
        table.addHeaderCell(createHeaderCell("Reste à payer", 10));
    }


    private Cell addCellCollapse(String content, float width) {
        Cell cell = new Cell(1, 3).add(new Paragraph(content)).setWidth(width);
        Style style = new Style().setFontSize(9);
        cell.addStyle(style);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(0f);
        return cell;
    }

    private void addTableRowFacture(Facture facture, Table table) {
        BigDecimal totalCA = BigDecimal.ZERO;

        BigDecimal totalRegle = BigDecimal.ZERO;
        BigDecimal totalAsolde = BigDecimal.ZERO;

        Map<Produit, List<Reglement>> reglementsParProduit = facture.getReglements().stream()
            .collect(Collectors.groupingBy(Reglement::getProduit));
        double montantSolde = 0;

        for (Map.Entry<Produit, List<Reglement>> entry : reglementsParProduit.entrySet()) {

            Produit produit = entry.getKey();
            List<Reglement> reglementsDuProduit = entry.getValue();

            table.addCell(addCellCollapse(produit.getNomProduit(), 60));
            reglementsDuProduit.sort(Comparator.comparing(Reglement::getDateReglement));

            double montantTotal = 0;
            int k = 0;

            Long sumReglement = 0L;
            for (Reglement g : reglementsDuProduit) {
                if (k == 0) {
                    montantTotal = facture.getBonDeSortie().getLigneBonDeSorties().stream()
                        .filter(ligne -> ligne.getProduit().equals(produit))
                        .mapToDouble(LigneBonDeSortie::getPrixDeVente)
                        .sum();
                }
                montantSolde = montantTotal - g.getMontantReglement();

                sumReglement += g.getMontantReglement();
//            table.addCell(createCellReglements(facture.getNumero(), 60));
//            table.addCell(createCellReglements(DateTimeFormatter.ofPattern(PATTERN_DATE).format(facture.getDateFacture()), 60));
//                table.addCell(createCellReglements(g.getProduit().getNomProduit(), 60));

//                table.addCell(createCellReglements(DateTimeFormatter.ofPattern(PATTERN_DATE).format(g.getDateReglement()), 60).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(createCellReglements(DateTimeFormatter.ofPattern(PATTERN_DATE).format(g.getDateReglement()), 60).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(createCellReglements(NumberFormat.getCurrencyInstance(new Locale("fr", "CI")).format(g.getMontantReglement()), 40).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(createCellReglements(NumberFormat.getCurrencyInstance(new Locale("fr", "CI")).format(facture.getBonDeSortie().getLigneBonDeSorties().stream().filter((l) -> l.getProduit().getId().equals(g.getProduit().getId())).findFirst().get().getPrixDeVente() - sumReglement), 40).setTextAlignment(TextAlignment.RIGHT));
//            totalCA = totalCA.add(BigDecimal.valueOf(f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum() - f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()));
                totalRegle = totalRegle.add(new BigDecimal(g.getMontantReglement()));

                if (k == reglementsDuProduit.size() - 1) {
                    totalAsolde = totalAsolde.add(BigDecimal.valueOf(montantSolde));
                }
                montantTotal = montantSolde;
                k++;
            }
//        totalCAs = totalCA;


        }

        totalSolde = totalRegle;
        totalResteSolde = totalAsolde;

    }

    private void addTableRow(List<Facture> factures, Table table) {
        BigDecimal totalCA = BigDecimal.ZERO;

        totalSolde = BigDecimal.ZERO;
        totalResteSolde = BigDecimal.ZERO;


        for (Facture f : factures) {
            Map<Produit, List<LigneBonDeSortie>> ligneBonDeSortiesParProduit =  f.getBonDeSortie().getLigneBonDeSorties().stream().collect(Collectors.groupingBy(LigneBonDeSortie::getProduit));

            table.addCell(addCellCollapse(DateTimeFormatter.ofPattern(PATTERN_DATE).format(f.getDateFacture()) + " | " + f.getNumero() + "\n" + f.getClient().getNomClient(), 60));


            Long sumReglement = 0L;
            for (Map.Entry<Produit, List<LigneBonDeSortie>> entry : ligneBonDeSortiesParProduit.entrySet()) {
                table.addCell(createCellReglements(entry.getKey().getNomProduit(), 60));
                table.addCell(createCellReglements(NumberFormat.getCurrencyInstance(new Locale("fr", "CI")).format(f.getReglements().stream().filter(reg -> reg.getProduit().getId().equals(entry.getKey().getId())).mapToLong(Reglement::getMontantReglement).sum()), 40).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(createCellReglements(NumberFormat.getCurrencyInstance(new Locale("fr", "CI")).format(f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum() - f.getReglements().stream().filter(reg -> reg.getProduit().getId().equals(entry.getKey().getId())).mapToLong(Reglement::getMontantReglement).sum()), 40).setTextAlignment(TextAlignment.RIGHT));
                totalCA = totalCA.add(BigDecimal.valueOf(f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum() - f.getReglements().stream().filter(reg -> reg.getProduit().getId().equals(entry.getKey().getId())).mapToLong(Reglement::getMontantReglement).sum()));
                totalSolde = totalSolde.add(BigDecimal.valueOf(f.getReglements().stream().filter(reg -> reg.getProduit().getId().equals(entry.getKey().getId())).mapToLong(Reglement::getMontantReglement).sum()));
                totalResteSolde = totalResteSolde.add(BigDecimal.valueOf(f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum() - f.getReglements().stream().filter(reg -> reg.getProduit().getId().equals(entry.getKey().getId())).mapToLong(Reglement::getMontantReglement).sum()));
            }
       }

    }

    public void addCellTotalHT(Table table) {
        table.addCell(createTotauxCell("TOTAL", 60)).setHeight(20);
        table.addCell(createTotauxCell(NumberFormat.getInstance().format(totalSolde), 40)
            .setTextAlignment(TextAlignment.RIGHT));
        table.addCell(createTotauxCell(NumberFormat.getInstance().format(totalResteSolde), 40)
            .setTextAlignment(TextAlignment.RIGHT));
    }


//    private void addCellTotalHT(Table table) {
//        table.addCell(createTotauxCell("TOTAL").setPaddingTop(6));
//        table.addCell(createTotauxCell("").setPaddingTop(6));
//        table.addCell(createTotauxCell("").setPaddingTop(6));
//        table.addCell(createTotauxCell("").setPaddingTop(6));
//        table.addCell(createTotauxCell(NumberFormat.getCurrencyInstance(new Locale("fr", "CI")).format(totalCAs))
//            .setTextAlignment(TextAlignment.RIGHT).setPaddingTop(6));
//
//    }

//    private Cell createTotauxCell(String content) {
//        Cell cell = new Cell()
//            .add(new Paragraph(content))
//            .setWidth(30)
//            .setBorderRight(Border.NO_BORDER)
//            .setBorderLeft(Border.NO_BORDER)
//            .setBorderTop(Border.NO_BORDER);
//
//        Style style = new Style().setFontSize(10);
//        cell.addStyle(style);
//        return cell;
//    }


    private Cell createTotauxCell(String content, float with) {
        Cell cell = new Cell()
            .add(new Paragraph(content)).setPaddingTop(4f)
            .setWidth(with)
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

    private Cell createCellTitre() {
        Cell cell = new Cell().add(new Paragraph(CreanceParCommercialReportPdf.TITRE_RECU)).setWidth(900).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        Style style = new Style().setFontSize(16).setBold().setFontColor(ColorConstants.BLACK);
        cell.addStyle(style).setHorizontalAlignment(HorizontalAlignment.CENTER);
        return cell;
    }

    private Cell createCellInfoRecu(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content)).setWidth(width);
        Style style = new Style().setFontSize(12).setBold().setFontColor(ColorConstants.BLACK);
        cell.addStyle(style);
        return cell;
    }


}
