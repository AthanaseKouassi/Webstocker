/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lignelivraison;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.RectangleBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class BordereauLivraisonsReport {

    List<BordereauLivraisonWrapper> reportData;
    private AggregationSubtotalBuilder<Long> totalQuantiteProduit;

    public BordereauLivraisonsReport(List<LigneBonDeSortie> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
                .setDataSource(new SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("\nBORDEREAU DE LIVRAISON"),
//                    .title(Templates.createTitleComponent("\nBORDEREAU DE LIVRAISON N° : 00" + reportData.get(0).getNumeroOrdre()),
                            bordereauInfoComponent(), bordereauInfoClientDemandeurComponent())
                    
                    .detail(
                            subreport,
                            cmp.verticalGap(20))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource());
//                .toPdf(Exporters.pdfExporter("/home/komi/reportaimas.pdf"));
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

            int masterRowNumber = reportParameters.getReportRowNumber();
            JasperReportBuilder report = report();
            report.setTemplate(Templates.reportTemplate);

            long unit = 996;

//            report.addColumn(col.column("Produit", "nomProduit", type.stringType()).setWidth(170));
//            report.addColumn(col.column("Numero de lot", "numeroLot", type.stringType()));
//            report.addColumn(col.column("Quantité", "quantiteLivre", type.longType()));
            
            TextColumnBuilder<String> designationColumn = col.column("Designation", "nomProduit", type.stringType()).setFixedWidth(180);
            TextColumnBuilder<String> numeroLotColumn = col.column("Numéro lot", "numeroLot", type.stringType());
            TextColumnBuilder<Long> quantiteColumn = col.column("Quantite", "quantiteLivre", type.longType());
            totalQuantiteProduit = sbt.sum(quantiteColumn);
            
           report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));
           
            report.columns(designationColumn, numeroLotColumn, quantiteColumn)
                    .columnGrid(designationColumn, numeroLotColumn, quantiteColumn)
                    .subtotalsAtSummary(totalQuantiteProduit);
//            
            report.summary(cmp.horizontalList().add(cmp.hListCell(cmp.text("Magasinier (nom et visa): ")
                    .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                    .setStyle(stl.style().setBorder(stl.pen1Point())))).setFixedHeight(80)
                    .add(cmp.hListCell(cmp.text("Chauffeur(nom et visa): \n" + " \n" + " \n" + " \n"
                            + "CNI / PC / :...............................................\n"
                            + "N°vehicule :.............................................")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                            .setStyle(stl.style().setBorder(stl.pen1Point()))))
                    .add(cmp.hListCell(cmp.text("Reçu par (nom et visa): ")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                            .setStyle(stl.style().setBorder(stl.pen1Point())))));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<BordereauLivraisonWrapper> reportData;

        public SubreportDataSourceExpression(List<BordereauLivraisonWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<BordereauLivraisonWrapper> generateReportData(List<LigneBonDeSortie> lignebonDesortie) {

        List<BordereauLivraisonWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie l : lignebonDesortie) {
            wrapperList.add(new BordereauLivraisonWrapper(l));
        }
        return wrapperList;
    }

//    public static void main(String[] args) {
//        new ProduitsReport(null);
//    }
    private class Column {

        private String title;
        private String field;
        private String dataType;

        private Column(String title, String field, String dataType) {
            this.title = title;
            this.field = field;
            this.dataType = dataType;
        }
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(15).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> bordereauInfoComponent() {
        RectangleBuilder rect = cmp.rectangle();
        HorizontalListBuilder dateBl = cmp.horizontalList().add(cmp.text("Date  : " + reportData.get(0).dateBordereauLivraison)).setBackgroundComponent(rect);
        HorizontalListBuilder numeroBonSortie = cmp.horizontalList().add(cmp.text("N° bon de sortie : " + reportData.get(0).getNumeroBonsortie())).setBackgroundComponent(rect);
        //HorizontalListBuilder numeroBonEnlevement = cmp.horizontalList().add(cmp.text("N° bon enlèvement : "+ numBe)).setBackgroundComponent(rect);
        HorizontalListBuilder containtDate = cmp.horizontalList()
                .add(dateBl, numeroBonSortie) //, cmp.horizontalGap(20), title2
                .setStyle(stl.style(4));

        return containtDate;
    }

    private ComponentBuilder<?, ?> bordereauInfoClientDemandeurComponent() {

        RectangleBuilder rect = cmp.rectangle();
        HorizontalListBuilder cli = cmp.horizontalList().add(cmp.text("Client  : " + reportData.get(0).getNomClient())).setBackgroundComponent(rect).setFixedWidth(200);
        HorizontalListBuilder dem = cmp.horizontalList().add(cmp.text("Demandeur  : " + reportData.get(0).getNomDemandeur())).setBackgroundComponent(rect);
        HorizontalListBuilder containtInfoClientDemandeur = cmp.horizontalList()
                .add(cli, dem) //, cmp.horizontalGap(20), title2
                .setStyle(stl.style(4));
        return containtInfoClientDemandeur;
    }

//    private ComponentBuilder<?, ?> infoClientDemandeur() {
//        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
//        //addCustomerAttribute(list, "La période ", " ");
//        addCustomerAttribute(list, "Client  ", reportData.get(0).getNomClient());
//        addCustomerAttribute(list, "Demandeur ", reportData.get(0).getNomDemandeur());
//
//        return cmp.verticalList(list);
//
//    }

}
