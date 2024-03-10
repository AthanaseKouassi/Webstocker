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
import com.webstocker.domain.Client;
import com.webstocker.repository.ClientRepository;
import com.webstocker.utilitaires.NombreEnChiffre;
import com.webstocker.web.rest.dto.newfeature.CreanceDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CreanceClientPdf {
    private static final String TITRE_RECU = "CREANCES CLIENT PAR PERIODE ";
    @Autowired
    private ClientRepository clientRepository;

    private BigDecimal totalCreance = BigDecimal.ZERO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);


    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{28, 55f, 23f})).useAllAvailableWidth();

        table.addCell(createCellTitre(" ", 85).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(TITRE_RECU, 250).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(" ", 60));
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        doc.add(table);
    }

    public Paragraph createBorderedText(Long idClient, LocalDate dateDebut, LocalDate dateFin) {
        Paragraph container = new Paragraph();
        Client client = clientRepository.findOne(idClient);

        String repInfo = "Client : " + client.getNomClient() + "\n" +
            "Date début : " + DateTimeFormatter.ofPattern("dd MMMM yyyy").format(dateDebut) + "\n" +
            "Date fin : " + DateTimeFormatter.ofPattern("dd MMMM yyyy").format(dateFin);

        Text two = new Text(repInfo);
        container.add(two);
        return container;
    }

    public void addTableRecu(Document doc, List<CreanceDto> listCreances) {

        Table table = new Table(UnitValue.createPercentArray(new float[]{10, 10, 25, 20, 15, 15, 15})).useAllAvailableWidth();
        addHeadTable(table);
        addTableRow(listCreances, table);
        doc.add(table);
        Table table2 = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20f})).useAllAvailableWidth();
        table2.setHorizontalAlignment(HorizontalAlignment.LEFT);
        addCellTotalHT(table2);
        doc.add(table2);
        doc.add(new Paragraph("Montant réglé en lettre : " + StringUtils.capitalize(NombreEnChiffre.getLettre(totalCreance.intValue())) + " Francs CFA"));
    }


    private void addHeadTable(Table table) {
        table.addHeaderCell(createHeaderCell("N°Facture", 10));
        table.addHeaderCell(createHeaderCell("Date", 10));
        table.addHeaderCell(createHeaderCell("Commercial", 20));
        table.addHeaderCell(createHeaderCell("Produit", 15));
        table.addHeaderCell(createHeaderCell("Montant produit", 10));
        table.addHeaderCell(createHeaderCell("Montant encaissé", 10));
        table.addHeaderCell(createHeaderCell("Solde produit", 10));
    }

    private void addTableRow(List<CreanceDto> listCreances, Table table) {
        BigDecimal restApaye = BigDecimal.ZERO;

        for (CreanceDto cr : listCreances) {
            table.addCell(createCellCreance(cr.getNumero(), 10));
            table.addCell(createCellCreance(formatter.format(cr.getDateFacture()), 10));
            table.addCell(createCellCreance(cr.getNomUser().toUpperCase(), 20));
            table.addCell(createCellCreance(cr.getNomProduit(), 15));

            table.addCell(createCellCreance(NumberFormat.getInstance().format(BigDecimal.valueOf(cr.getPrixDeVente())), 11).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCellCreance(NumberFormat.getInstance().format(BigDecimal.valueOf(cr.getMontantRegle())), 11).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCellCreance(NumberFormat.getInstance().format(BigDecimal.valueOf(cr.getResteApayer())), 11).setTextAlignment(TextAlignment.RIGHT));
            restApaye = restApaye.add(BigDecimal.valueOf(cr.getResteApayer()));

        }
        totalCreance = restApaye;
    }


    private void addCellTotalHT(Table table) {
        table.addCell(createTotauxCell("TOTAL").setPaddingTop(6));
        table.addCell(createTotauxCell("").setPaddingTop(6));
        table.addCell(createTotauxCell(NumberFormat.getInstance().format(totalCreance))
            .setTextAlignment(TextAlignment.RIGHT).setPaddingTop(6));

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


    private Cell createCellCreance(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content)).setWidth(width);
        Style style = new Style().setFontSize(10);
        cell.addStyle(style);
        cell.setPaddingTop(10f);
        cell.setPaddingBottom(0f);
        return cell;
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
}
