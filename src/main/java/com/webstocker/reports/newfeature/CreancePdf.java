package com.webstocker.reports.newfeature;


import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.webstocker.web.rest.dto.newfeature.CreanceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CreancePdf {

    private static final String TITRE_RECU = "CREANCES CLIENT ";
    private BigDecimal totalAsolde = BigDecimal.ZERO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50f, 25f})).useAllAvailableWidth();

        table.addCell(createCellTitre(" ", 160).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(TITRE_RECU, 200).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(" ", 150));
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        doc.add(table);
    }

    public Paragraph createBorderedText(int numcategorie, String nomProduit) {
        Paragraph container = new Paragraph();
        String infoCategorie = " catégorie " + numcategorie;
        String info = "";
        if (numcategorie == 1) {
            info = infoCategorie + " : créances inférieures à 30 jours";
        } else if (numcategorie == 2) {
            info = infoCategorie + " : créances entre 30 et 45 jours";
        } else if (numcategorie == 3) {
            info = infoCategorie + " : créances supérieures à 45 jours";
        }
        String info2 = "Créance " + info;
        String prduitInfo = "";

        Text one = new Text(info2);
        container.add(one);

        if (nomProduit != null && !nomProduit.isEmpty()) {
            prduitInfo = "\nProduit: " + nomProduit.toUpperCase();
            Text two = new Text(prduitInfo);
            container.add(two);
        }

        return container;
    }

    public void addTableRecu(Document doc, List<CreanceDto> creanceDtos) {

        Table table = new Table(UnitValue.createPercentArray(new float[]{15, 12, 15, 15, 11, 11, 12, 12f})).useAllAvailableWidth();
        addHeadTable(table);
        addTableRow(creanceDtos, table);
        doc.add(table);
        Table table2 = new Table(UnitValue.createPercentArray(new float[]{25, 20f})).useAllAvailableWidth();
//        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        addCellTotalHT(table2);
        doc.add(table2);
//        doc.add(new Paragraph("Montant réglé en lettre : " + StringUtils.capitalize(NombreEnChiffre.getLettre(totalSolde.intValue())) + " Francs CFA"));
    }

    private void addHeadTable(Table table) {
        table.addHeaderCell(createHeaderCell("Nom client", 15));
        table.addHeaderCell(createHeaderCell("N°Téléphone", 11));
        table.addHeaderCell(createHeaderCell("Commercial", 15));
        table.addHeaderCell(createHeaderCell("Produit", 15));
        table.addHeaderCell(createHeaderCell("Date Facture", 11));
        table.addHeaderCell(createHeaderCell("N°Facture", 11));
        table.addHeaderCell(createHeaderCell("Montant produit", 12));
        table.addHeaderCell(createHeaderCell("Reste à payer", 12));
    }

    private void addTableRow(List<CreanceDto> creanceDtos, Table table) {
        BigDecimal restApaye = BigDecimal.ZERO;
        for (CreanceDto ligne : creanceDtos) {

            table.addCell(createCellCreance(ligne.getNomClient(), 15));
            table.addCell(createCellCreance(ligne.getTelClient(), 11));
            table.addCell(createCellCreance(ligne.getNomUser().toUpperCase(), 15));
            table.addCell(createCellCreance(ligne.getNomProduit(), 15));
            table.addCell(createCellCreance(formatter.format(ligne.getDateFacture()), 11));
            table.addCell(createCellCreance(ligne.getNumero(), 11));
            table.addCell(createCellCreance(NumberFormat.getInstance().format(BigDecimal.valueOf(ligne.getMontantRegle())), 12).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCellCreance(NumberFormat.getInstance().format(BigDecimal.valueOf(ligne.getResteApayer())), 12).setTextAlignment(TextAlignment.RIGHT));
            restApaye = restApaye.add(BigDecimal.valueOf(ligne.getResteApayer()));

        }
        totalAsolde = restApaye;

    }

    private Cell createCellTitre(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content)).setWidth(width).setBorder(Border.NO_BORDER);
        Style style = new Style().setFontSize(16).setBold().setFontColor(ColorConstants.BLACK);
        cell.addStyle(style);
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

    private Cell createCellCreance(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content != null ? content : "")).setWidth(width);
        Style style = new Style().setFontSize(9);
        cell.addStyle(style);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(0f);
        return cell;
    }

    private void addCellTotalHT(Table table) {
        table.addCell(createTotauxCell("TOTAL", 60)).setHeight(20);
        table.addCell(createTotauxCell(NumberFormat.getInstance(new Locale("fr", "CI")).format(totalAsolde), 40)
            .setTextAlignment(TextAlignment.RIGHT));

    }

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

}
