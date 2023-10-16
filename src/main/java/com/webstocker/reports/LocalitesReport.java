package com.webstocker.reports;

import com.webstocker.domain.Localite;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class LocalitesReport {
    List<LocaliteWrapper> reportData;

    public LocalitesReport(List<Localite> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
            .setDataSource(new SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                .title(Templates.createTitleComponent("Liste des localités "))
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

            report.addColumn(col.column("Region", "region", type.stringType()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
            report.addColumn(col.column("Ville", "ville", type.stringType()));
            report.addColumn(col.column("Commune", "commune", type.stringType()));
            report.addColumn(col.column("Village", "village", type.stringType()));
            report.addColumn(col.column("Quartier", "quartier", type.stringType()));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<LocaliteWrapper> reportData;

        public SubreportDataSourceExpression(List<LocaliteWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<LocaliteWrapper> generateReportData(List<Localite> localites) {

        List<LocaliteWrapper> wrapperList = new LinkedList<>();

        for (Localite l : localites) {
            wrapperList.add(new LocaliteWrapper(l));
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
}
