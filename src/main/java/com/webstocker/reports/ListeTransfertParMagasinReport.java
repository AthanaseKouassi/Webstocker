/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.enumeration.TypeSortie;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class ListeTransfertParMagasinReport {

    List<ListeTransfertParMagasinWrapper> reportData;
    String nomMagasin;
    String dateDebutPeriode;
    String dateFinPeriode;

    public ListeTransfertParMagasinReport(List<LigneBonDeSortie> reportData, String nomMagasin, String dateDebutPeriode, String dateFinPeriode) {
        this.reportData = generateReportData(reportData);
        this.nomMagasin = nomMagasin;
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new ListeTransfertParMagasinReport.SubreportExpression())
                .setDataSource(new ListeTransfertParMagasinReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Liste des transferts " + nomMagasin.toLowerCase()),
                            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                            cmp.hListCell(informationRapport())),
                            cmp.verticalGap(10)
                    )
                    .detail(
                            subreport,
                            cmp.verticalGap(20))
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

            TextColumnBuilder<String> dateColumn = col.column("Date ", "dateTransfert", type.stringType());
            TextColumnBuilder<String> numeroBsColumn = col.column("Numéro bon de sortie", "numeroBondeSortie", type.stringType());
            TextColumnBuilder<String> numeroLotColumn = col.column("Numéro Lot", "numeroLot", type.stringType());
            TextColumnBuilder<String> nomProduitcolumn = col.column("Produit", "nomProduit", type.stringType());
            TextColumnBuilder<String> destinationcolumn = col.column("Magasin Destination", "magasindestination", type.stringType());
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité transferé", "quantite", type.longType());
            report
                    .columns(dateColumn, numeroBsColumn,numeroLotColumn,nomProduitcolumn, destinationcolumn, quantityColumn)
                    .subtotalsAtSummary(sbt.sum(quantityColumn).setLabel("Total"))
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<ListeTransfertParMagasinWrapper> reportData;

        public SubreportDataSourceExpression(List<ListeTransfertParMagasinWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<ListeTransfertParMagasinWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        long sommeQte = 0;

        List<ListeTransfertParMagasinWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lignebs : ligneBonDeSortie) {
            wrapperList.add(new ListeTransfertParMagasinWrapper(lignebs));

        }

        return wrapperList;
    }

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
            list.add(cmp.text(label + " :").setFixedColumns(12).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

//    private ComponentBuilder<?, ?> informationRapport(List<ListeTransfertParMagasinWrapper> listeWrapper) {
    private ComponentBuilder<?, ?> informationRapport() {
        // RectangleBuilder rectangle = cmp.roundRectangle();
        //rectangle.setDimension(200, 80);//.setStyle(stl.style().setBorder(stl.pen1Point()));
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        // addCustomerAttribute(list, " Période des transferts","");
        addCustomerAttribute(list, " Date début", dateDebutPeriode);
        addCustomerAttribute(list, " Date fin", dateFinPeriode);

        return cmp.verticalList(list);//.setBackgroundComponent(rectangle);
    }

}
