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
import com.webstocker.domain.User;
import com.webstocker.repository.FactureRepository;
import com.webstocker.repository.ReglementRepository;
import com.webstocker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class ListeClientParCommercialPdf {

    private static final String TITRE = "LISTE DES CLIENTS D'UN COMMERCIAL ";

    @Autowired
    private ReglementRepository reglementRepository;
    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private UserRepository userRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{20, 60f, 20f})).useAllAvailableWidth();

        table.addCell(createCellTitre(" ", 70).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(TITRE, 200).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(" ", 60));
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        doc.add(table);
    }


    public Paragraph createBorderedText(Long idUser, LocalDate dateDebut, LocalDate dateFin) {
        Paragraph container = new Paragraph();
        User user = userRepository.findOne(idUser);
        String info = "Commercial : " + user.getLastName().toUpperCase() + " " + user.getFirstName().toUpperCase() + "\n" +
            "Période du : " + dateDebut.format(formatter) + " au " + dateFin.format(formatter);

        Text one = new Text(info);
        container.add(one);
        container.setBorderRight(Border.NO_BORDER);
        container.setBorderLeft(Border.NO_BORDER);
        return container;
    }


    public void addTableRecu(Document doc, List<Client> lstClients, String dateDebut, String dateFin) {

        Table table = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20f, 20f})).useAllAvailableWidth();
        addHeadTable(table);
        addTableRow(lstClients, table, dateDebut, dateFin);
        doc.add(table);
    }

    private void addHeadTable(Table table) {
        table.addHeaderCell(createHeaderCell("Nom ", 60));
        table.addHeaderCell(createHeaderCell("Contact", 15));
        table.addHeaderCell(createHeaderCell("Adresse postale", 10));
        table.addHeaderCell(createHeaderCell("Nombre Transaction", 10));
    }

    private void addTableRow(List<Client> lstClients, Table table, String dateDebut, String dateFin) {
        Set<Client> uniqueClients = new HashSet<>(lstClients);
        for (Client clt : uniqueClients) {
            long nombreTransaction = factureRepository.findByClientParPeriode(clt.getId(), dateDebut, dateFin).size();
            log.info("OUHH nombre transaction :::{}", nombreTransaction);
            table.addCell(createCellTableau(clt.getNomClient(), 60));
            table.addCell(createCellTableau(clt.getTelephoneClient() != null ? clt.getTelephoneClient() : "", 40).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCellTableau(clt.getBoitepostale() != null ? clt.getBoitepostale() : "", 40).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCellTableau(String.valueOf(nombreTransaction), 40).setTextAlignment(TextAlignment.RIGHT));
        }
    }

    private Cell createCellTableau(String content, float width) {
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

    private String dateDuJour() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");

        return dateTime.format(formatter);
    }

}
