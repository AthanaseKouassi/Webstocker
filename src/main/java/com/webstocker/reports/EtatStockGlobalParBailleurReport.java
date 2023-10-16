package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatStockGlobalAimasWrapper;
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
public class EtatStockGlobalParBailleurReport {
    List<EtatStockGlobalParBailleurWrapper> reportData;
    String dateDebut;
    String dateFin;
        
    public EtatStockGlobalParBailleurReport(List<EtatStockGlobalAimasWrapper> reportData, String dateDebut, String dateFin){
        this.reportData = generateReportData(reportData);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        build();
    }
    
     public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new EtatStockGlobalParBailleurReport.SubreportExpression())
                .setDataSource(new EtatStockGlobalParBailleurReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("\nEtat du stock d'un bailleur"),
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


            TextColumnBuilder<String> productColumn = col.column("Produit", "produit", type.stringType()).setFixedWidth(100);//.setPrintRepeatedDetailValues(false); 
            TextColumnBuilder<Long> quantiteVendueColumn = col.column("Quantité Vendue", "quantiteVendue", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> arrivageColumn = col.column("Valeur des ventes", "valeurVente", type.longType());//.setPrintRepeatedDetailValues(false);
//            TextColumnBuilder<Long> qteVenteColumn = col.column("Quantité sortie(vente)", "quantiteVente", type.longType());//.setFixedWidth(200);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qtePromoColumn = col.column("Quantité sortie (promotion)" , "quantitePromotion", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> balanceClotureColumn = col.column("stock" , "quantiteFinale", type.longType());//.setPrintRepeatedDetailValues(false);
//            TextColumnBuilder<Long> creanceRecouvreColumn = col.column("Recouvrements" , "sommeRecouvre", type.longType());//.setFixedWidth(200);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qteTransfertColumn = col.column("Quantité transfert" , "quantiteTransfert", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qtePerteColumn = col.column("Quantité perte" , "quantitePerte", type.longType());//.setPrintRepeatedDetailValues(false);
//            TextColumnBuilder<Long> qteVenteCashColumn = col.column("Quantité vente cash", "quantiteVendueCash", type.longType());           
//            TextColumnBuilder<Long> qteVenteCreditColumn = col.column("Quantité vente credit","quantiteVendueCredit", type.longType());
//            TextColumnBuilder<Long> sommeVenteCashColumn = col.column("Valeur vente Cash", "sommeVenteCash", type.longType());
//            TextColumnBuilder<Long> sommeVenteCreditColumn = col.column("Valeur vente credit", "sommeVenteCredit", type.longType());
           
            report
                    //productColumn,
                .columns(productColumn,quantiteVendueColumn, arrivageColumn,qtePromoColumn,qteTransfertColumn, qtePerteColumn,balanceClotureColumn)
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(5));
               
            return report;
        }
    }
    
    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<EtatStockGlobalParBailleurWrapper> reportData;

        public SubreportDataSourceExpression(List<EtatStockGlobalParBailleurWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }
    
     private List<EtatStockGlobalParBailleurWrapper> generateReportData(List<EtatStockGlobalAimasWrapper> etatStockBailleur) {

        List<EtatStockGlobalParBailleurWrapper> wrapperList = new LinkedList<>();
        for (EtatStockGlobalAimasWrapper erm : etatStockBailleur) {
            wrapperList.add(new EtatStockGlobalParBailleurWrapper(erm));
        }
        return wrapperList;
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(20).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> afficherPeriode(List<EtatStockGlobalParBailleurWrapper> etatBailleur) {
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        //addCustomerAttribute(list, "La période ", " ");
       // addCustomerAttribute(list, "Bureau  ", "Aimas");
        addCustomerAttribute(list, "Période du stock global", " ");
        addCustomerAttribute(list, "Bailleur", etatBailleur.get(0).getBailleur());
       // addCustomerAttribute(list, "Produit", etatBailleur.get(0).getProduit());
        addCustomerAttribute(list, "Date début période", dateDebut);
        addCustomerAttribute(list, "Date fin période", dateFin);
       // addCustomerAttribute(list, "Approuvé par", "Goussou K Lazare");

        return cmp.verticalList(list);
    }
    

}
