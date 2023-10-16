package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatAgentWrapper;
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
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class RapportDesDeleguesReport {
    List<RapportDesDeleguesWrapper> reportData;
    String dateDebut;
    String dateFin;
    
    public RapportDesDeleguesReport(List<EtatAgentWrapper> reportData,String dateDebut,String dateFin ){
        this.reportData = generateReportData(reportData);
        this.dateFin = dateFin;
        this.dateDebut = dateDebut;
        build();
    }
    
    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new RapportDesDeleguesReport.SubreportExpression())
                .setDataSource(new RapportDesDeleguesReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("\nRAPPORT DELEGUE" ),
                            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                            cmp.hListCell(createMagasinComponent( reportData))),
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

           TextColumnBuilder<String> produitColumn = col.column("Produit", "nomProduit", type.stringType())
                    .setFixedWidth(160);
//           TextColumnBuilder<String> agentColumn = col.column("Produit", "nomAgent", type.stringType())
//                    .setFixedWidth(160);
            TextColumnBuilder<Long> quantiteRecueColumn = col.column("Quantité Reçue", "quantiteRecue", type.longType());                   
            TextColumnBuilder<Long> quantiteSortieColumn = col.column("Quantité sortie", "quantiteSortie", type.longType());                    
            TextColumnBuilder<Long> quantiteDetenueColumn = col.column("Quantité détenue", "quantiteDetenue", type.longType());
                   
//            TextColumnBuilder<Long> quantityColumn = col.column("Quantité", "quantiteTransfert", type.longType())
//                    .setFixedWidth(70);
//            TextColumnBuilder<Long> priceColumn = col.column("Montant des ventes", "prix", type.longType());
//            TextColumnBuilder<BigDecimal> montantColumn = priceColumn.multiply(1)
//                    .setTitle("Montant (FCFA)");

           report
                   .addColumn(produitColumn,quantiteRecueColumn,quantiteSortieColumn,quantiteDetenueColumn)
                   .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(5));
                      
            return report;
        }
    }
    
     

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<RapportDesDeleguesWrapper> reportData;

        public SubreportDataSourceExpression(List<RapportDesDeleguesWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<RapportDesDeleguesWrapper> generateReportData(List<EtatAgentWrapper> ligneEtatAgent) {

        List<RapportDesDeleguesWrapper> wrapperList = new LinkedList<>();
        for (EtatAgentWrapper lbs : ligneEtatAgent) {
            wrapperList.add(new RapportDesDeleguesWrapper(lbs));
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
            list.add(cmp.text(label + " : ").setFixedColumns(12).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> createMagasinComponent(List<RapportDesDeleguesWrapper> rptdelegue) {

        HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        //addCustomerAttribute(list, "Date ", "");
        addCustomerAttribute(list, "Nom du délégué", rptdelegue.get(0).getNomAgent());
        addCustomerAttribute(list, " Date debut période", dateDebut);
        addCustomerAttribute(list, "Date fin période", dateFin);
       // addCustomerAttribute(list, "délai de paiement", Integer.toString(facturewrapper.get(0).getDelaiPaiement()) + " jours");

        return cmp.verticalList( list);
    }
}
