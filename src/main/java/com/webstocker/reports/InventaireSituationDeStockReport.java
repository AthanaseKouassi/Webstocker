/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class InventaireSituationDeStockReport {

    List<InventaireSituationDeStockWrapper> reportData;
    String dateDuMois;
    String dateFin;

    public InventaireSituationDeStockReport(List<EtatDeTousLesProduitsDunMagasinWrapper> reportData, String dateDuMois) {
        this.reportData = generateReportData(reportData);
        this.dateDuMois = dateDuMois;
        
        build();
    }

     public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new InventaireSituationDeStockReport.SubreportExpression())
                .setDataSource(new InventaireSituationDeStockReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Situation de Stock de produits"),
                            afficherPeriode(reportData),
                            cmp.verticalGap(10))
                    .detail(subreport, cmp.verticalGap(20))
                    .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportBuilder;
    }

    private JRDataSource createDataSource() {
        return new JREmptyDataSource(1);
    }
    
     private class SubreportExpression extends AbstractSimpleExpression<JasperReportBuilder> {

        private static final long serialVersionUID = 1L;

        @Override
        public JasperReportBuilder evaluate(ReportParameters reportParameters) {
            Object object = new Object();
            int masterRowNumber = reportParameters.getReportRowNumber();
            JasperReportBuilder report = report();
            report.setTemplate(Templates.reportTemplate);

            TextColumnBuilder<String> magasinColumn = col.column("Produit", "nomProduit", type.stringType()).setFixedWidth(160);;//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> stockInitColumn = col.column("Stock Initial Théorique", "stockInitial", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> quantiteVendueColumn = col.column("Quantité Vendue", "quantiteVente", type.longType());//.setPrintRepeatedDetailValues(false);

            TextColumnBuilder<Long> qtePromoColumn = col.column("Quantité sortie (promotion)", "quantitePromo", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qteTransfertColumn = col.column("Quantité transferée", "quantiteTransfere", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qtePerteColumn = col.column("Quantité perte", "quantitePerte", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> stocksursiteColumn = col.column("Quantité reçue magasins antenne", "quantiteRecuparTransfert", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> stockMagasinCentralColumn = col.column("Stock Final Théorique", "quantiteTotalEnStock", type.longType());//.setFixedWidth(200);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> stockReelColumn = col.column("Stock Final Réel", "stockreel", type.longType());//.setFixedWidth(200);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> ecartColumn = col.column("Ecart", "ecart", type.longType());//.setFixedWidth(200);//.setPrintRepeatedDetailValues(false);

            report
                    .columns(magasinColumn, stockInitColumn, quantiteVendueColumn, qtePromoColumn, qteTransfertColumn, qtePerteColumn, stocksursiteColumn, stockMagasinCentralColumn,stockReelColumn, ecartColumn)
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));
      
            return report;
        }
    }
     
     private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<InventaireSituationDeStockWrapper> reportData;

        public SubreportDataSourceExpression(List<InventaireSituationDeStockWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<InventaireSituationDeStockWrapper> generateReportData(List<EtatDeTousLesProduitsDunMagasinWrapper> etatSituation) {

        List<InventaireSituationDeStockWrapper> wrapperList = new LinkedList<>();
        for (EtatDeTousLesProduitsDunMagasinWrapper etlprod : etatSituation) {
            wrapperList.add(new InventaireSituationDeStockWrapper(etlprod));
        }
        return wrapperList;
    }
    
     private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(10).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> afficherPeriode(List<InventaireSituationDeStockWrapper> etlprod) {       
        
        LocalDate localdate1 = LocalDate.parse(dateDuMois);        ;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM yyyy");
        
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, "Magasin", etlprod.get(0).getMagasin());
        addCustomerAttribute(list, "Période", dateFormat.format(localdate1));
//        addCustomerAttribute(list, "Période du", dateDebut + " au " + dateFin);

        return cmp.verticalList(list);
    }
    
}
