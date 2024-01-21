package com.webstocker.reports.newfeature;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.webstocker.domain.BonDeSortie;
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
public class RecuPdf {

    private static final String TITRE_RECU = "RECU FACTURE N° ";

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

    public void infoRecu(Document doc, BonDeSortie bonDeSortie) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String currentDateTime = dateFormat.format(new Date());
        Facture facture = factureRepository.findByBonDeSortie(bonDeSortie);

        String info = "Date reglement :\n" +
            "Client :\n" +
            "Commercial :\n" +
            "Numero Facture :";
        String repInfo = currentDateTime + "\n" +
            facture.getClient().getNomClient() + "\n" +
            bonDeSortie.getDemandeur().getLastName() + " " + bonDeSortie.getDemandeur().getFirstName() + "\n" +
            bonDeSortie.getNumeroFactureNormalise();

        Table table = new Table(UnitValue.createPercentArray(new float[]{15, 15, 25f})).useAllAvailableWidth();
        table.addCell(createCellInfoRecu(info, 15));
        table.addCell(createCellInfoRecu("", 15));
        table.addCell(createCellInfoRecu(repInfo, 20));

        doc.add(table);
    }

    public void addTableRecu(Document doc, BonDeSortie bonDeSortie) {
        final Facture facture = factureRepository.findByBonDeSortie(bonDeSortie);
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


    private class RoundedCornersCellRenderer extends CellRenderer {

        public RoundedCornersCellRenderer(Cell modelElement) {
            super(modelElement);
        }

        @Override
        public void drawBorder(DrawContext drawContext) {
            float x = getOccupiedAreaBBox().getX();
            float y = getOccupiedAreaBBox().getY();
            float width = getOccupiedAreaBBox().getWidth();
            float height = getOccupiedAreaBBox().getHeight();
            float radius = 10f; // Ajustez cette valeur selon vos préférences pour le rayon des coins arrondis

            PdfCanvas canvas = drawContext.getCanvas();

            // Dessiner les bords arrondis
            canvas.setStrokeColor(ColorConstants.BLACK);
            canvas.roundRectangle(x, y, width, height, radius);
            canvas.stroke();
        }
    }


}
