package com.webstocker.reports;

import com.webstocker.domain.Client;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
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
public class ClientsReport {
    List<ClientWrapper> reportData;
    
    public ClientsReport (List<Client> reportData){
       this.reportData = generateReportData(reportData);
        build();
    }
    
     public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
            .setDataSource(new SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();
       
        try {
            reportBuilder
                .title(Templates.createTitleComponent("Liste des client par catégorie"))
                .detail(
                    subreport,                        
                    cmp.verticalGap(20))
//                 .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)  
                   
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
            
            report.addColumn(col.column("Catégorie", "categorieclient", type.stringType())).setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(stl.padding(3)));
            report.addColumn(col.column("Client", "nomClient", type.stringType()).setPrintRepeatedDetailValues(false));
            //ColumnGroupBuilder stateGroup = grp.group(col.column("Catégorie", "categorie", type.stringType()));            
            report.addColumn(col.column("Localité", "localiteClient", type.stringType())); 
            report.addColumn(col.column("Telephone", "telephoneClient", type.stringType()));   
//            report.addColumn(col.column("adresse postale", "boitepostale", type.stringType()));   
           // report.addColumn(col.column("Facture", "nbreFacture", type.integerType()));
            
           // report.groupBy(stateGroup);
            
            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<ClientWrapper> reportData;

        public SubreportDataSourceExpression(List<ClientWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<ClientWrapper> generateReportData(List<Client> clients) {

        List<ClientWrapper> wrapperList = new LinkedList<>();
        for (Client c : clients) {
            wrapperList.add(new ClientWrapper(c));
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
