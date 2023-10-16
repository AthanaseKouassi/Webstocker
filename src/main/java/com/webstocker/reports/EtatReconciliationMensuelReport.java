package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatDeReconciliationWrapper;
import com.webstocker.utilitaires.PremierEtDernierJourDuMois;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
public class EtatReconciliationMensuelReport {

    List<EtatReconciliationMensuelWrapper> reportData;
    String dateEtat;

    public EtatReconciliationMensuelReport(List<EtatDeReconciliationWrapper> reportData, String dateEtat) {
        this.reportData = generateReportData(reportData);
        this.dateEtat = dateEtat;
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new EtatReconciliationMensuelReport.SubreportExpression())
                .setDataSource(new EtatReconciliationMensuelReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();
        

        try {
            reportBuilder
                    .title(TemplatesEtatReconciliation.createTitleComponent("Etat de reconciliation mensuelle"),
                            afficherPeriode(reportData),
                            cmp.verticalGap(10))
                    .detail(subreport, cmp.verticalGap(20))                                      
                    .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
                    .pageFooter(TemplatesEtatReconciliation.footerComponent)
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
            report.setTemplate(TemplatesEtatReconciliation.reportTemplate);
            

//            CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("typeSortie", String.class);
//
//            CrosstabColumnGroupBuilder<String> columnGroup = ctab.columnGroup("produit", String.class);//.setTotalHeader("Total for state");
//
//            CrosstabBuilder crosstab = ctab.crosstab()
//                    .headerCell(cmp.text("résultat / produit").setStyle(Templates.boldCenteredStyle))
//                    .rowGroups(rowGroup)
//                    .columnGroups(columnGroup)
//                    .measures(
//                            ctab.measure( "quantite", Long.class, Calculation.SUM));//,
//            // ctab.measure("Unit price", "unitprice", Long.class, Calculation.SUM));
//
//            report.summary(crosstab);

            TextColumnBuilder<String> productColumn = col.column("Produit", "produit", type.stringType()).setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> balanceOuvertureColumn = col.column("Balance d'ouverture", "quantiteTotal", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> arrivageColumn = col.column("Arrivage", "quantiteLivree", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qteVenteColumn = col.column("Quantité sortie (vente)", "quantiteVente", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qtePromoColumn = col.column("Quantité sortie (promo)" , "quantitePromo", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> balanceClotureColumn = col.column("Balance de cloture" , "quantiteFinMois", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<BigDecimal> creanceRecouvreColumn = col.column("Recouvrements" , "sommeRecouvre", type.bigDecimalType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<BigDecimal> creance30JourColumn = col.column("Créances < 30 jrs" , "sommeCreance30", type.bigDecimalType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<BigDecimal> creance30A90JourColumn = col.column("Créances 30-90 jrs" , "sommeCreance30_90", type.bigDecimalType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<BigDecimal> creance90Column = col.column("Créances > 90 jrs" , "sommeCreance90", type.bigDecimalType());//.setFixedWidth(200);//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> balOvertCreanceColumn = col.column("Balance ouverture créance" , "balanceOuvertureCreance", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> balClotureCreanceColumn = col.column("Balance cloture créance" , "balanceClotureCreance", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
//            TextColumnBuilder<bigDecimalType> qtePerteColumn = col.column("Quantité perte" , "quantitePerte", type.longType());//.setFixedWidth(100);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qteVenteCashColumn = col.column("Quantité vente cash", "quantiteVendueCash", type.longType());//.setFixedWidth(100);           
            TextColumnBuilder<Long> qteVenteCreditColumn = col.column("Quantité vente credit","quantiteVendueCredit", type.longType());//.setFixedWidth(100);
            TextColumnBuilder<BigDecimal> sommeVenteCashColumn = col.column("Valeur vente Cash", "sommeVenteCash", type.bigDecimalType());//.setFixedWidth(100);
            TextColumnBuilder<BigDecimal> sommeVenteCreditColumn = col.column("Valeur vente credit", "sommeVenteCredit", type.bigDecimalType());//.setFixedWidth(100);
           
            report
                .columns(productColumn,balanceOuvertureColumn, arrivageColumn,qteVenteColumn, qtePromoColumn,qteVenteCashColumn, qteVenteCreditColumn,balanceClotureColumn, 
                   sommeVenteCashColumn,sommeVenteCreditColumn,balOvertCreanceColumn,creanceRecouvreColumn,balClotureCreanceColumn,  creance30JourColumn,creance30A90JourColumn,creance90Column)
//                    .setDetailStyle(stl.style(stl.font().setFontSize(5)))
//                    .setColumnStyle(stl.style(stl.font().setFontSize(5)))
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(5));
                //.groupBy(clientColumn)
//                .subtotalsAtFirstGroupFooter(sbt.sum(valeurVenteColumn));
            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<EtatReconciliationMensuelWrapper> reportData;

        public SubreportDataSourceExpression(List<EtatReconciliationMensuelWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<EtatReconciliationMensuelWrapper> generateReportData(List<EtatDeReconciliationWrapper> etatRecon) {

        List<EtatReconciliationMensuelWrapper> wrapperList = new LinkedList<>();
        for (EtatDeReconciliationWrapper erm : etatRecon) {
            wrapperList.add(new EtatReconciliationMensuelWrapper(erm));
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
            list.add(cmp.text(label + " :").setFixedColumns(12).setStyle(TemplatesEtatReconciliation.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> afficherPeriode(List<EtatReconciliationMensuelWrapper> data) {      
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateEtatRecon = LocalDate.parse(dateEtat, formatter);
        
        String dateDujour = dateEtatRecon.format(DateTimeFormatter.ofPattern("MMMM-yyyy"));
        
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        //addCustomerAttribute(list, "La période ", " ");
        addCustomerAttribute(list, "Bureau  ", "Aimas");
        addCustomerAttribute(list, "Mois/Année ",dateDujour );
        addCustomerAttribute(list, "préparé par  ", data.get(0).getUserPrenom().toUpperCase()+" "+data.get(0).getUserNom().toLowerCase());
//        addCustomerAttribute(list, "Revu par ", "Ouattara Mariatou");
//        addCustomerAttribute(list, "Approuvé par", "Goussou K Lazare");

        return cmp.verticalList(list);

    }
}
