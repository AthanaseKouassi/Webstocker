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
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class ChiffreAffaireParClientsReport {

    List<ChiffreAffaireParClientWrapper> reportData;
    String dateDebut;
    String dateFin;

    public ChiffreAffaireParClientsReport(List<LigneBonDeSortie> reportData,String dateDebutPeriode, String dateFinPeriode){
        this.reportData = generateReportData(reportData);
        this.dateDebut = dateDebutPeriode;
        this.dateFin = dateFinPeriode;
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new ChiffreAffaireParClientsReport.SubreportExpression())
                .setDataSource(new ChiffreAffaireParClientsReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Chiffre d'affaire par client" ),
                             afficherPeriode(),
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

            TextColumnBuilder<String> clientColumn = col.column("Client", "nomClient", type.stringType()).setFixedWidth(200);//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<String> descriptionColumn = col.column("Produit", "nomProduit", type.stringType());
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité vendue","quantiteFacture", type.longType());
//            TextColumnBuilder<BigDecimal> prixUnitaireColumn = col.column("Prix unitaire (F CFA)", "prixvente", type.bigDecimalType());
            TextColumnBuilder<Long> valeurVenteColumn = col.column("Chiffre d'affaires (F CFA)", "montantVente", type.longType());
//            TextColumnBuilder<BigDecimal> valeurVenteColumn = col.column("Chiffre d'affaires (F CFA)", "montantVente", type.bigDecimalType());
            report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));
            report
                .columns(clientColumn,descriptionColumn, quantityColumn, valeurVenteColumn)
                .groupBy(clientColumn)
                .subtotalsAtFirstGroupFooter(sbt.sum(valeurVenteColumn));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<ChiffreAffaireParClientWrapper> reportData;

        public SubreportDataSourceExpression(List<ChiffreAffaireParClientWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<ChiffreAffaireParClientWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        List<ChiffreAffaireParClientWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lbs : ligneBonDeSortie) {
            wrapperList.add(new ChiffreAffaireParClientWrapper(lbs));
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

     private ComponentBuilder<?, ?> afficherPeriode(){
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        
        LocalDate localdate1 = LocalDate.parse(dateDebut);
        LocalDate localdate2 = LocalDate.parse(dateFin);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        
        addCustomerAttribute(list, "        Période du", dateFormat.format(localdate1) + " au " + dateFormat.format(localdate2));
       
     
        return cmp.verticalList( list);

    }

}
