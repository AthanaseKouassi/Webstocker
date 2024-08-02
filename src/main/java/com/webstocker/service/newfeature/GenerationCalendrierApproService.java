package com.webstocker.service.newfeature;

import com.webstocker.domain.Inventaire;
import com.webstocker.domain.Produit;
import com.webstocker.service.InventaireService;
import com.webstocker.service.ProduitService;
import com.webstocker.utilitaires.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class GenerationCalendrierApproService {

    private static final String PATTERN_MOIS = "MMMM";

    @Inject
    private Utils utils;
    @Inject
    private InventaireNewService inventaireNewService;
    @Inject
    private InventaireService inventaireService;
    @Inject
    private ProduitService produitService;


    private void addInventaireToSheet(Sheet sheet, Inventaire inventaire, Long moyenne, CellStyle borderedCellStyle) {

        final long stockReelTotalFinal = inventaire.getStockMagasinCentral() + inventaire.getStockAntenne()
            + inventaire.getStockAgent();
        final long ajustement = (inventaire.getStockTheoDebut() + inventaire.getArrivage())
            - inventaire.getVente() - inventaire.getPromo() - inventaire.getPerteAbime() - stockReelTotalFinal;

        final String mois = inventaire.getDateInventaire().format(DateTimeFormatter.ofPattern(PATTERN_MOIS, Locale.FRENCH));

        final int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);
        row.setHeightInPoints(16); // Définir la hauteur de la ligne

        Cell cell = row.createCell(1);
        cell.setCellValue(mois);
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(2);
        cell.setCellValue(inventaire.getStockTheoDebut());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(3);
        cell.setCellValue(inventaire.getArrivage());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(4);
        cell.setCellValue(inventaire.getVente());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(5);
        cell.setCellValue(inventaire.getPromo());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(6);
        cell.setCellValue(inventaire.getPerteAbime());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(7);
        cell.setCellValue(ajustement);
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(8);
        cell.setCellValue(inventaire.getStockMagasinCentral());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(9);
        cell.setCellValue(inventaire.getStockAntenne());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(10);
        cell.setCellValue(inventaire.getStockAgent());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(11);
        cell.setCellValue(stockReelTotalFinal);
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(12);
        cell.setCellValue("");
        if (utils.lastMonthQuarter(inventaire.getDateInventaire())) {
            cell.setCellValue(moyenne);
        }
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(13);
        cell.setCellValue("");
        if (utils.lastMonthQuarter(inventaire.getDateInventaire()) && moyenne != 0) {
            cell.setCellValue(utils.arrondir(((double) stockReelTotalFinal / moyenne), 2));
        }
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(14);
        cell.setCellValue(inventaire.getBailleur().getNomBailleur());
        cell.setCellStyle(borderedCellStyle);
        cell = row.createCell(15);
        cell.setCellValue(inventaire.getCommentaire());
        cell.setCellStyle(borderedCellStyle);

    }

    // Ajouter la ligne total à la fin du tableau
    private void addRowTotal(Sheet sheet, CellStyle borderedCellStyle, List<Inventaire> lstInventaire) {
        final long sommeQteTheorique = lstInventaire.stream().mapToLong(Inventaire::getStockTheoDebut).sum();
        final long sommeArrivage = lstInventaire.stream().mapToLong(Inventaire::getArrivage).sum();
        final long sommeVente = lstInventaire.stream().mapToLong(Inventaire::getVente).sum();
        final long sommePromo = lstInventaire.stream().mapToLong(Inventaire::getPromo).sum();
        final long sommePerte = lstInventaire.stream().mapToLong(Inventaire::getPerteAbime).sum();
        final long sommeStockReelMagasin = lstInventaire.stream().mapToLong(Inventaire::getStockMagasinCentral).sum();
        final long sommeAntenne = lstInventaire.stream().mapToLong(Inventaire::getStockAntenne).sum();
        final long sommeAgent = lstInventaire.stream().mapToLong(Inventaire::getStockAgent).sum();

        final long sommeAjustement = (sommeQteTheorique + sommeArrivage) - (sommeVente - sommePromo - sommePerte)
            - (sommeStockReelMagasin + sommeAntenne + sommeAgent);
        final long sommeStockTotalreelFinal = sommeStockReelMagasin + sommeAntenne + sommeAgent;

        final int row = 4 + lstInventaire.size();

        Row totalRow = sheet.createRow(row);

        Cell cell = totalRow.createCell(1);
        cell.setCellValue("Total Année");
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(2);
        cell.setCellValue(sommeQteTheorique);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(3);
        cell.setCellValue(sommeArrivage);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(4);
        cell.setCellValue(sommeVente);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(5);
        cell.setCellValue(sommePromo);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(6);
        cell.setCellValue(sommePerte);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(7);
        cell.setCellValue(sommeAjustement);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(8);
        cell.setCellValue(sommeStockReelMagasin);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(9);
        cell.setCellValue(sommeAntenne);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(10);
        cell.setCellValue(sommeAgent);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(11);
        cell.setCellValue(sommeStockTotalreelFinal);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(12);
        cell.setCellValue(0);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(13);
        cell.setCellValue(0);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(14);
        cell.setCellValue(0);
        cell.setCellStyle(borderedCellStyle);
        cell = totalRow.createCell(15);
        cell.setCellValue("");
        cell.setCellStyle(borderedCellStyle);

    }

    private void createHeaderRow(Sheet sheet, CellStyle borderedCellStyle, String nomProduit, Inventaire inventaire) {

        final CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        final Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);

        Row titleRow = sheet.createRow(1);
        Cell celltitle = titleRow.createCell(1);
        celltitle.setCellValue("Annexe 2 :Calendrier d'approvisionnement et inventaire de " + nomProduit.toUpperCase());
        celltitle.setCellStyle(cellStyle);

        Row headerRow = sheet.createRow(3);
        headerRow.setHeightInPoints(30); // Définir la hauteur de la ligne
        final int year = inventaire.getDateInventaire().getYear();

        Cell cell = headerRow.createCell(1);
        cell.setCellValue("Année " + year);
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(2);
        cell.setCellValue("Stock début Mois");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(3);
        cell.setCellValue("Entrée en stock");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(4);
        cell.setCellValue("Ventes");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(5);
        cell.setCellValue("Promo");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(6);
        cell.setCellValue("Perte/Abimé");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(7);
        cell.setCellValue("Ajustement");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(8);
        cell.setCellValue("Stock Magasin central");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(9);
        cell.setCellValue("Stock Antenne");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(10);
        cell.setCellValue("Stock Délégués et commerciaux");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(11);
        cell.setCellValue("Stock total final");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(12);
        cell.setCellValue("Distribution moyenne mensuelle");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(13);
        cell.setCellValue("Couverture stock");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(14);
        cell.setCellValue("Bailleur");
        cell.setCellStyle(borderedCellStyle);
        cell = headerRow.createCell(15);
        cell.setCellValue("Observations");
        cell.setCellStyle(borderedCellStyle);

    }

    private void setColumnWidths(Sheet sheet) {
        sheet.setColumnWidth(1, 3000); // Mois
        sheet.setColumnWidth(2, 5000); // Stock début Mois
        sheet.setColumnWidth(3, 4000); // Entrée en stock
        sheet.setColumnWidth(4, 4000); // Ventes
        sheet.setColumnWidth(5, 3000); // Promo
        sheet.setColumnWidth(6, 3000); // Perte/Abimé
        sheet.setColumnWidth(7, 3000); // Ajustement
        sheet.setColumnWidth(8, 4000); // Stock Magasin central
        sheet.setColumnWidth(9, 3000); // Stock Antenne
        sheet.setColumnWidth(10, 4000); // Stock Délégués & commerciaux
        sheet.setColumnWidth(11, 4000);  // Stock total final
        sheet.setColumnWidth(12, 5000);  // Distribution moyenne mensuelle
        sheet.setColumnWidth(13, 3000); // Couverture stock
        sheet.setColumnWidth(14, 3000); // Bailleur
        sheet.setColumnWidth(15, 6000);  // Observations

    }

    private CellStyle getCellStyle(Workbook workbook) {
        final CellStyle borderedCellStyle = workbook.createCellStyle();
        borderedCellStyle.setBorderBottom(BorderStyle.THIN);
        borderedCellStyle.setBorderTop(BorderStyle.THIN);
        borderedCellStyle.setBorderLeft(BorderStyle.THIN);
        borderedCellStyle.setBorderRight(BorderStyle.THIN);
        borderedCellStyle.setWrapText(true);

        return borderedCellStyle;
    }

    private CellStyle getFrontCellStyle(Workbook workbook) {
        final CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        return cellStyle;
    }


    public void generateExcelFile(String dateInventaire, HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ServletOutputStream outputStream = response.getOutputStream()) {

            final Map<Produit, List<Inventaire>> mapInventaireProduit = inventaireNewService.getAllInventaireParProduit(dateInventaire);
            final CellStyle borderedCellStyle = getCellStyle(workbook);

            for (Map.Entry<Produit, List<Inventaire>> entry : mapInventaireProduit.entrySet()) {
                final Produit produit = entry.getKey();
                final List<Inventaire> inventaires = entry.getValue();

                final Map<Integer, Long> mapMoyennetrimestre = utils.calculerMoyennesTrimestrielles(inventaires);
                mapMoyennetrimestre.forEach((key, value) -> log.info("Trimestre ::{} -- LA Moyenne ::{}", key, value));
                Long moyenne = 0L;

                for (Inventaire inventaire : inventaires) {

                    if (utils.lastMonthQuarter(inventaire.getDateInventaire())) {
                        // Trouver la moyenne trimestrielle pour le trimestre de l'inventaire
                        moyenne = mapMoyennetrimestre.entrySet().stream()
                            .filter(e -> e.getKey().equals(utils.getQuarter(inventaire.getDateInventaire())))
                            .map(Map.Entry::getValue)
                            .findFirst()
                            .orElse(0L);
                    }

                    Sheet sheet = workbook.getSheet(produit.getNomProduit());
                    if (sheet == null) {
                        sheet = workbook.createSheet(produit.getNomProduit());
                        createHeaderRow(sheet, borderedCellStyle, produit.getNomProduit(), inventaire);
                        setColumnWidths(sheet); // Définir la largeur des colonnes
                    }
                    sheet.autoSizeColumn(0);
                    addInventaireToSheet(sheet, inventaire, moyenne, borderedCellStyle);
                    if (inventaire.getDateInventaire().getMonthValue() == 12) {
                        addRowTotal(sheet, borderedCellStyle, inventaires);
                    }
                }

            }
            workbook.write(outputStream);
        }
    }


}
