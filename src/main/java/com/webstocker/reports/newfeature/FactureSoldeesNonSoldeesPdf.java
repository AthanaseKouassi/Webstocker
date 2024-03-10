package com.webstocker.reports.newfeature;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.webstocker.domain.BonDeSortie;
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.domain.enumeration.newfeature.StatutFacture;
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
import java.util.Objects;


@Slf4j
@Component
public class FactureSoldeesNonSoldeesPdf {

    private static final String PATTERN_DATE = "dd MMMM yyyy";
    public static String TITRE_RECU = "";
    @Autowired
    private ReglementRepository reglementRepository;
    @Autowired
    private FactureRepository factureRepository;

    private BigDecimal totalCAs = BigDecimal.ZERO;
    private BigDecimal totalMoontant = BigDecimal.ZERO;

    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{200f})).useAllAvailableWidth();
        table.addCell(createCellTitre().setHorizontalAlignment(HorizontalAlignment.CENTER));
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

    public void addTableFacture(Document doc, List<Facture> factures, String typeFacture) {
        Table table;
        if (typeFacture.equals(StatutFacture.NON_SOLDE.toString())) {
            table = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20f, 20, 20})).useAllAvailableWidth();

        } else {
            table = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20f, 20})).useAllAvailableWidth();
        }

        addHeadTable(table, typeFacture);
        addTableRow(factures, table, typeFacture);
        doc.add(table);
        Table table2;

        if (typeFacture.equals(StatutFacture.NON_SOLDE.toString())) {
            table2 = new Table(UnitValue.createPercentArray(new float[]{30f, 30f, 30f, 30, 30})).useAllAvailableWidth();

        } else {
            table2 = new Table(UnitValue.createPercentArray(new float[]{30f, 30f, 30f, 30})).useAllAvailableWidth();
        }

        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        addCellTotalHT(table2, typeFacture);
        doc.add(table2);
    }

    private void addHeadTable(Table table, String typeFacture) {
        table.addHeaderCell(createHeaderCell("Facture", 60));
        table.addHeaderCell(createHeaderCell("Date", 60));
        table.addHeaderCell(createHeaderCell("Montant total", 15));
        table.addHeaderCell(createHeaderCell("Montant encaissé", 10));
        if (typeFacture.equalsIgnoreCase(StatutFacture.NON_SOLDE.toString())) {
            table.addHeaderCell(createHeaderCell("Reste à payer", 10));
        }
    }

    private void addTableRow(List<Facture> factures, Table table, String typeFacture) {
        BigDecimal totalCA = BigDecimal.ZERO;
        BigDecimal montant = BigDecimal.ZERO;

        for (Facture f : factures) {
            if (Objects.nonNull(f.getNumero())) {
                table.addCell(createCellReglements(f.getNumero(), 60));
                table.addCell(createCellReglements(DateTimeFormatter.ofPattern(PATTERN_DATE).format(f.getDateFacture()), 60));
                table.addCell(createCellReglements(NumberFormat.getInstance().format(f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum()), 40).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(createCellReglements(NumberFormat.getInstance().format(f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()), 40).setTextAlignment(TextAlignment.RIGHT));
                if (typeFacture.equalsIgnoreCase(StatutFacture.NON_SOLDE.toString())) {
                    table.addCell(createCellReglements(NumberFormat.getInstance().format(f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum() - f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()), 40)
                        .setTextAlignment(TextAlignment.RIGHT));
                }
                montant = montant.add(BigDecimal.valueOf(f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()));
                totalCA = totalCA.add(BigDecimal.valueOf(f.getBonDeSortie().getLigneBonDeSorties().stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum() - f.getReglements().stream().mapToLong(Reglement::getMontantReglement).sum()));
            }
        }
        totalCAs = totalCA;
        totalMoontant = montant;

    }

    private void addCellTotalHT(Table table, String typeFacture) {
        table.addCell(createTotauxCell("TOTAL").setPaddingTop(6));
        table.addCell(createTotauxCell("").setPaddingTop(6));
        table.addCell(createTotauxCell("").setPaddingTop(6));
        table.addCell(createTotauxCell(NumberFormat.getInstance().format(totalMoontant))
            .setTextAlignment(TextAlignment.RIGHT).setPaddingTop(6));
        if (typeFacture.equalsIgnoreCase(StatutFacture.NON_SOLDE.toString())) {
            table.addCell(createTotauxCell(NumberFormat.getInstance().format(totalCAs))
                .setTextAlignment(TextAlignment.RIGHT).setPaddingTop(6));
        }


    }

    private Cell createTotauxCell(String content) {
        Cell cell = new Cell()
            .add(new Paragraph(content))
            .setWidth(30)
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
        Cell cell = new Cell().add(new Paragraph(FactureSoldeesNonSoldeesPdf.TITRE_RECU)).setWidth(900).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
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
