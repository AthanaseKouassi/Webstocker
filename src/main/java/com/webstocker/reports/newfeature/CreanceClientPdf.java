package com.webstocker.reports.newfeature;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
public class CreanceClientPdf {

    private static final String TITRE_RECU = "CREANCES CLIENT PAR PERIODE ";
    private BigDecimal totalAsolde = BigDecimal.ZERO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRENCH);

    public void titreRecu(Document doc) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{23, 55f, 23f})).useAllAvailableWidth();

        table.addCell(createCellTitre(" ", 60).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(TITRE_RECU, 250).setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(createCellTitre(" ", 60));
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        doc.add(table);
    }


    private Cell createCellTitre(String content, float width) {
        Cell cell = new Cell().add(new Paragraph(content)).setWidth(width).setBorder(Border.NO_BORDER);
        Style style = new Style().setFontSize(16).setBold().setFontColor(ColorConstants.BLACK);
        cell.addStyle(style);
        return cell;
    }

}
