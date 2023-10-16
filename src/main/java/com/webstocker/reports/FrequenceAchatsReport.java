package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
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
import net.sf.dynamicreports.report.builder.component.RectangleBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class FrequenceAchatsReport {
    List<FrequenceAchatWrapper> reportData;
    String dateDebut;
    String dateFin;
    
    public FrequenceAchatsReport(List<LigneBonDeSortie> reportData, String dateDebut, String dateFin){
         this.reportData = generateReportData(reportData);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        build();
    }
    
    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new FrequenceAchatsReport.SubreportExpression())
                .setDataSource(new FrequenceAchatsReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("\n\nFrequence d'achat client "),                           
                            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                            cmp.hListCell(afficherPeriode()),
                             cmp.hListCell(cmp.text("                                       ")) ),
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

            TextColumnBuilder<String> clientColumn = col.column("Client", "nomClient", type.stringType());
            TextColumnBuilder<String> localiteColumn = col.column("Localité", "localiteClient", type.stringType());
//           TextColumnBuilder<Long> frequenceColumn = col.column("Frequence", "nombreFacture", type.longType());
            TextColumnBuilder<Long> quantityColumn = col.column("Quantite","quantiteAchetee", type.longType());
            //TextColumnBuilder<Long> valeurVenteColumn = col.column("Chiffre d'affaires (F CFA)", "prixvente", type.longType());
            

            report
                .columns(clientColumn, localiteColumn,quantityColumn)
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(stl.padding(3)));

                   
            return report;
        }
    }
    
    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<FrequenceAchatWrapper> reportData;

        public SubreportDataSourceExpression(List<FrequenceAchatWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<FrequenceAchatWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        List<FrequenceAchatWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lbs : ligneBonDeSortie) {
            wrapperList.add(new FrequenceAchatWrapper(lbs));
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
            list.add(cmp.text(label + " :").setFixedColumns(17).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

     private ComponentBuilder<?, ?> afficherPeriode(){
         RectangleBuilder rectangle = cmp.roundRectangle();
         HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
         addCustomerAttribute(list, " Période fréquence d'achat", " ");
        addCustomerAttribute(list, " Date début période  ", dateDebut);
        addCustomerAttribute(list, " Date fin période ", dateFin);
         
        return cmp.verticalList( list).setBackgroundComponent(rectangle);
         
     }
}
