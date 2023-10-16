package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.wrapper.ChiffreAffaireWrapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
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
public class ChiffreAffaireParCategorieClientReport {

    List<ChiffreAffaireParCategorieClientWrapper> reportData;
    String dateDebut;
    String dateFin;

    public ChiffreAffaireParCategorieClientReport(List<ChiffreAffaireWrapper> reportData, String dateDebut, String dateFin) {
        this.reportData = generateReportData(reportData);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new ChiffreAffaireParCategorieClientReport.SubreportExpression())
                .setDataSource(new ChiffreAffaireParCategorieClientReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Chiffre d'affaire par type de client"),
                            afficherPeriode(reportData),
                            cmp.verticalGap(10))
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

//            TextColumnBuilder<String> categorieClientColumn = col.column("Type client", "nomCategorie", type.stringType()).setFixedWidth(200);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<String> columnProduit = col.column("Produit", "nomProduit", type.stringType());
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité vendue", "quantiteProduit", type.longType());
//            TextColumnBuilder<BigDecimal> valeurVenteColumn = col.column("Chiffre d'affaires (F CFA)", "montant", type.bigDecimalType());
            TextColumnBuilder<Long> valeurVenteColumn = col.column("Chiffre d'affaires (F CFA)", "montant", type.longType());

            report
                    .setPageMargin(margin(20))
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3))
                    .columns(columnProduit, quantityColumn, valeurVenteColumn)
                    .subtotalsAtSummary(sbt.sum(valeurVenteColumn).setLabel("Montant Total"));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<ChiffreAffaireParCategorieClientWrapper> reportData;

        public SubreportDataSourceExpression(List<ChiffreAffaireParCategorieClientWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<ChiffreAffaireParCategorieClientWrapper> generateReportData(List<ChiffreAffaireWrapper> ligneBonDeSortie) {

        List<ChiffreAffaireParCategorieClientWrapper> wrapperList = new LinkedList<>();
        for (ChiffreAffaireWrapper lbs : ligneBonDeSortie) {
            wrapperList.add(new ChiffreAffaireParCategorieClientWrapper(lbs));
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

    private ComponentBuilder<?, ?> afficherPeriode(List<ChiffreAffaireParCategorieClientWrapper> caw) {

        LocalDate localdate1 = LocalDate.parse(dateDebut);
        LocalDate localdate2 = LocalDate.parse(dateFin);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, "       Type de client", caw.get(0).getNomCategorie());
        addCustomerAttribute(list, "        Période du", dateFormat.format(localdate1) + " au " + dateFormat.format(localdate2));

        return cmp.verticalList(list);

    }

}
