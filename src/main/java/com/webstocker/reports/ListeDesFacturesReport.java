package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class ListeDesFacturesReport {

    private AggregationSubtotalBuilder<Long> totalSum;
    List<ListeDesFactureWrapper> reportData;
    String dateDebut;
    String dateFin;

    public ListeDesFacturesReport(List<LigneBonDeSortie> reportData, String dateDebutPeriode, String dateFinPeriode) {
        this.reportData = generateReportData(reportData);
        this.dateDebut = dateDebutPeriode;
        this.dateFin = dateFinPeriode;
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new ListeDesFacturesReport.SubreportExpression())
                .setDataSource(new ListeDesFacturesReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("\nListe des factures "),                           
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
         
                           
            TextColumnBuilder<String> clientColumn = col.column("Client", "nomClient", type.stringType())
                    .setFixedWidth(200);   
            TextColumnBuilder<String> dateFactureColumn = col.column("Date facture", "dateFacture", type.stringType());  
            TextColumnBuilder<String> numeroBonDeSortieColumn = col.column("Numéro bon de sortie", "numeroBondeSortie", type.stringType());  
            TextColumnBuilder<String> numeroFacturePhysiquecolumn = col.column("Nunméro facture", "numeroFactureNormalise", type.stringType());            
            TextColumnBuilder<Long> montantColumn = col.column("Montant facture (FCFA)", "montantFacture", type.longType());
            
             totalSum = sbt.sum(montantColumn).setLabel("Total");
             
            report
                    .columns(clientColumn, dateFactureColumn, numeroBonDeSortieColumn, numeroFacturePhysiquecolumn, montantColumn)
                    .subtotalsAtSummary( totalSum)
                    .setColumnStyle(stl.style().setPadding(stl.padding(5))
                    .setBorder(stl.pen1Point()));
                   
            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<ListeDesFactureWrapper> reportData;

        public SubreportDataSourceExpression(List<ListeDesFactureWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<ListeDesFactureWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        long sommeQte = 0;

        List<ListeDesFactureWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lignebondesortie : ligneBonDeSortie) {
            wrapperList.add(new ListeDesFactureWrapper(lignebondesortie));
        }

        return wrapperList;
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(12).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> informationRapport() {

        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));

        LocalDate localdate1 = LocalDate.parse(dateDebut);
        LocalDate localdate2 = LocalDate.parse(dateFin);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
      
        addCustomerAttribute(list, "        Période du", dateFormat.format(localdate1)+" au "+dateFormat.format(localdate2));

        return cmp.verticalList(list);//.setBackgroundComponent(rectangle);
    }

}
