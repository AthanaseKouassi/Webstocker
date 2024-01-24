package com.webstocker.reports.newfeature;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.webstocker.domain.Facture;
import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Reglement;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.ReglementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Slf4j
@Component
public class RecuCreditPdf {

    private static final String TITRE_RECU = "RECU FACTURE ";

    @Autowired
    private ReglementRepository reglementRepository;
    @Autowired
    private FactureRepository factureRepository;

    private double totalrecu = 0;

    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 25f})).useAllAvailableWidth();
        table.setMargins(10f, 10f, 0f, 10f);
        table.addCell(createCellTitre(TITRE_RECU, 200));
        table.addCell(createCellTitre(" ", 90));
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
            facture.getNumero() + "\n" +
            facture.getBonDeSortie().getDemandeur().getLastName() + " " + facture.getBonDeSortie().getDemandeur().getFirstName();


        Table table = new Table(UnitValue.createPercentArray(new float[]{15, 15,})).useAllAvailableWidth();
        table.addCell(createCellInfoRecu(info, 15));
        table.addCell(createCellInfoRecu(repInfo, 20));

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

    public void addTableRecu(Document doc, Facture facture) {
        List<Reglement> reglements = reglementRepository.findByFacture(facture);
        Table table = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20f})).useAllAvailableWidth();
        addHeadTable(table);
        addTableRow(reglements, table);
        addCellTotalHT(table);
        doc.add(table);
    }

    private void addHeadTable(Table table) {
        table.addHeaderCell(createHeaderCell("Produit", 60));
        table.addHeaderCell(createHeaderCell("Montant reglé", 15));
        table.addHeaderCell(createHeaderCell("Reste à payer", 10));
    }

    private void addTableRow(List<Reglement> reglements, Table table) {
        for (Reglement reg : reglements) {
            double montant = 0;
            montant = reg.getFacture().getBonDeSortie().getLigneBonDeSorties()
                .stream().mapToDouble(LigneBonDeSortie::getPrixDeVente).sum();
            table.addCell(createCellReglements(reg.getProduit().getNomProduit(), 60));
            table.addCell(createCellReglements(String.valueOf(reg.getMontantReglement()), 40));
            table.addCell(createCellReglements(String.valueOf(montant - reg.getMontantReglement()), 40));
            totalrecu += reg.getMontantReglement();
        }

    }

    private void addCellTotalHT(Table table) {
        table.addCell(createTotauxCell("total", 30).setPaddingTop(6));
        table.addCell(createTotauxCell(NumberFormat.getCurrencyInstance(new Locale("fr", "FR")).format(totalrecu), 30)
            .setTextAlignment(TextAlignment.RIGHT).setPaddingTop(6));
    }

    private Cell createTotauxCell(String content, float width) {
        Cell cell = new Cell()
            .add(new Paragraph(content))
            .setWidth(width)
            .setBorderRight(Border.NO_BORDER)
            .setBorderLeft(Border.NO_BORDER)
            .setBorderTop(Border.NO_BORDER);

        Style style = new Style().setFontSize(9);
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
