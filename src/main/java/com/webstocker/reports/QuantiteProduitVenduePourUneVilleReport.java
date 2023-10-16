package com.webstocker.reports;

import com.webstocker.domain.wrapper.VenteParDistrictWrapper;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 *
 * @author Athanase
 */
public class QuantiteProduitVenduePourUneVilleReport {

    List<QuantiteVendueParProduitEtParDistrictWrapper> reportData;
    String dateDebut;
    String dateFin;

    public QuantiteProduitVenduePourUneVilleReport(List<VenteParDistrictWrapper> reportData, String dateDebut, String dateFin) {
        this.reportData = generateReportData(reportData);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new QuantiteProduitVenduePourUneVilleReport.SubreportExpression())
                .setDataSource(new QuantiteProduitVenduePourUneVilleReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Quantité de produit vendue par Antenne"),
                            informationRapport(reportData),
                            cmp.verticalGap(10))
                    .detail(subreport, cmp.verticalGap(20))
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

            TextColumnBuilder<String> produitColumn = col.column("Produit", "nomProduit", type.stringType());
//            TextColumnBuilder<String> villeColumn = col.column("Antenne", "ville", type.stringType());
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité vendue", "quantite", type.longType());
            // TextColumnBuilder<Long> valeurVenteColumn = col.column("Valeur des ventes", "prixVente", type.longType());

            report
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3))
                    .setPageMargin(margin(20))
                    .columns(produitColumn, quantityColumn);

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<QuantiteVendueParProduitEtParDistrictWrapper> reportData;

        public SubreportDataSourceExpression(List<QuantiteVendueParProduitEtParDistrictWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<QuantiteVendueParProduitEtParDistrictWrapper> generateReportData(List<VenteParDistrictWrapper> ligneBonDeSortie) {

        long sommeQte = 0;

        List<QuantiteVendueParProduitEtParDistrictWrapper> wrapperList = new LinkedList<>();
        for (VenteParDistrictWrapper lignebondesortie : ligneBonDeSortie) {
            wrapperList.add(new QuantiteVendueParProduitEtParDistrictWrapper(lignebondesortie));

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

    private ComponentBuilder<?, ?> informationRapport(List<QuantiteVendueParProduitEtParDistrictWrapper> qtevendue) {

        LocalDate localdate1 = LocalDate.parse(dateDebut);
        LocalDate localdate2 = LocalDate.parse(dateFin);
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd MMMM uuuu");

        HorizontalListBuilder list = cmp.horizontalList();
        addCustomerAttribute(list, "        Vente réalisée à ", qtevendue.get(0).getVille());
        addCustomerAttribute(list, "        Date du ", dTF.format(localdate1) + " au " + dTF.format(localdate2));

        return cmp.verticalList(list);//.setBackgroundComponent(rectangle);
    }

}
