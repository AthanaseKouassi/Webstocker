package com.webstocker.reports;

import com.webstocker.domain.Lignelivraison;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.RectangleBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class BordereauReceptionsReport {

    List<BordereauReceptionWrapper> reportData;
    private AggregationSubtotalBuilder<Integer> totalQuantiteProduit;

    public BordereauReceptionsReport(List<Lignelivraison> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }

    //Athanase un teste de la reprise
    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
                .setDataSource(new SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();
        try {
            reportBuilder
                    .title(Templates.createTitleComponent("BORDEREAU DE RECEPTION"),
                            createMultiRowHorizontalList(reportData))
                    //bordereauInfoComponent( ))
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
            report
                    .setTemplate(Templates.reportTemplate);

            TextColumnBuilder<String> descriptionColumn = col.column("Designation", "nomProduit", type.stringType());
            TextColumnBuilder<String> numeroLotColumn = col.column("Numero de lot", "numeroLot", type.stringType()).setFixedWidth(180);
            TextColumnBuilder<Integer> quantiteProduitColumn = col.column("Quantité", "quantiteRecu", type.integerType()).setFixedWidth(180);
            totalQuantiteProduit = sbt.sum(quantiteProduitColumn);

            report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));

            report.columns(
                    descriptionColumn, numeroLotColumn, quantiteProduitColumn)
                    .columnGrid(descriptionColumn, numeroLotColumn, quantiteProduitColumn)
                    .subtotalsAtSummary(totalQuantiteProduit);

            report.addColumnFooter(cmp.horizontalList().add(cmp.hListCell(cmp.text("Livreur (nom et visa) ")
                    .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                    .setStyle(stl.style().setBorder(stl.pen1Point())))).setFixedHeight(80)
                    .add(cmp.hListCell(cmp.text("Magasinier (nom et visa) ")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                            .setStyle(stl.style().setBorder(stl.pen1Point())))));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<BordereauReceptionWrapper> reportData;

        public SubreportDataSourceExpression(List<BordereauReceptionWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<BordereauReceptionWrapper> generateReportData(List<Lignelivraison> lignelivraisons) {

        List<BordereauReceptionWrapper> wrapperList = new LinkedList<>();
        for (Lignelivraison l : lignelivraisons) {
            wrapperList.add(new BordereauReceptionWrapper(l));
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

    private ComponentBuilder<?, ?> bordereauInfoComponent() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String datebon = null;
        datebon = sdf.format(date);
        String fabrcant = "zzzz-ets";

        RectangleBuilder rect = cmp.rectangle();
        HorizontalListBuilder dateBl = cmp.horizontalList().add(cmp.text(" Date  : " + reportData.get(0).dateLivraison)).setBackgroundComponent(rect).setFixedWidth(150);
        HorizontalListBuilder nomFabricant = cmp.horizontalList().add(cmp.text(" Origine : " + reportData.get(0).fabricant)).setBackgroundComponent(rect);
        HorizontalListBuilder containtDate = cmp.horizontalList()
                .add(dateBl, nomFabricant) //, cmp.horizontalGap(20), title2
                .setStyle(stl.style(10));
        return containtDate;
    }

    private ComponentBuilder<?, ?> createMultiRowHorizontalList(List<BordereauReceptionWrapper> brwrapper) {
        HorizontalListBuilder horizontalList = cmp.horizontalList();

        RectangleBuilder rect = cmp.rectangle();

        horizontalList.add(cmp.text(" Date livraison : " + brwrapper.get(0).getDateLivraison()),
                cmp.text(" Destination : " + brwrapper.get(0).getMagasin())).setStyle(stl.style(stl.pen1Point()));
        //.setBackgroundComponent(rect); 
        horizontalList.newRow();
        horizontalList.add(cmp.text(""));
        horizontalList.newRow();
        String tabInfo[] = {" N° Liv. : " + brwrapper.get(0).getNumeroLivraison(), " Valeur liv. : " + brwrapper.get(0).getValeurLivraison(), " Fabricant : " + brwrapper.get(0).getFabricant(), " N°Cmde : " + brwrapper.get(0).getNumeroCommandelivrée()};
        String tabInfo2[] = {" F. test : " + brwrapper.get(0).getFraisTest(), " F. Transit : " + brwrapper.get(0).getFraisTransit(), " F. Manut. : " + brwrapper.get(0).getFraisManutention(), " F. ass. : " + brwrapper.get(0).getFraisAssuranceLocale()};
        for (int i = 0; i < tabInfo.length; i++) {
            horizontalList.add(cmp.text(tabInfo[i]).setStyle(stl.style(stl.pen1Point())));
        }
        horizontalList.newRow();
        for (int i = 0; i < tabInfo2.length; i++) {
            horizontalList.add(cmp.text(tabInfo2[i]).setStyle(stl.style(stl.pen1Point())));
        }
        horizontalList.newRow();
        horizontalList.add(cmp.text(""));

        return horizontalList;
    }

}
