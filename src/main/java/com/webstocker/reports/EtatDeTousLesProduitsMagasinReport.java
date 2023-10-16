package com.webstocker.reports;

import com.webstocker.domain.wrapper.EtatDeTousLesProduitsDunMagasinWrapper;
import com.webstocker.domain.wrapper.EtatProduitParMagasinWrapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class EtatDeTousLesProduitsMagasinReport {

    List<EtatDeTousLesProduitsMagasinWrapper> reportData;
    String dateDebut;
    String dateFin;

    public EtatDeTousLesProduitsMagasinReport(List<EtatDeTousLesProduitsDunMagasinWrapper> reportData, String dateDebut, String dateFin) {
        this.reportData = generateReportData(reportData);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        build();

    }
    
    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new EtatDeTousLesProduitsMagasinReport.SubreportExpression())
                .setDataSource(new EtatDeTousLesProduitsMagasinReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Etat du stock des produits dans un magasin"),
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

            TextColumnBuilder<String> magasinColumn = col.column("Produit", "nomProduit", type.stringType()).setFixedWidth(180);;//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> stockInitialColumn = col.column("Stock initial", "stockInitial", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qteArriveeColumn = col.column("Arrivage", "quantiteArrivee", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> quantiteVendueColumn = col.column("Quantité Vendue", "quantiteVente", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qtePromoColumn = col.column("Quantité sortie (promotion)", "quantitePromo", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qteTransfertColumn = col.column("Quantité transferée", "quantiteTransfere", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> qtePerteColumn = col.column("Quantité perte", "quantitePerte", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> stocksursiteColumn = col.column("Quantité reçue magasins antenne", "quantiteRecuparTransfert", type.longType());//.setPrintRepeatedDetailValues(false);
            TextColumnBuilder<Long> stockMagasinCentralColumn = col.column("Stock disponible", "quantiteTotalEnStock", type.longType());//.setFixedWidth(200);//.setPrintRepeatedDetailValues(false);

            report
                    .columns(magasinColumn,stockInitialColumn,qteArriveeColumn, quantiteVendueColumn, qtePromoColumn, qteTransfertColumn, qtePerteColumn, stocksursiteColumn, stockMagasinCentralColumn)
                    .setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));
            //.groupBy(clientColumn)
//                .subtotalsAtFirstGroupFooter(sbt.sum(valeurVenteColumn));
            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<EtatDeTousLesProduitsMagasinWrapper> reportData;

        public SubreportDataSourceExpression(List<EtatDeTousLesProduitsMagasinWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<EtatDeTousLesProduitsMagasinWrapper> generateReportData(List<EtatDeTousLesProduitsDunMagasinWrapper> etatStockParMagasin) {

        List<EtatDeTousLesProduitsMagasinWrapper> wrapperList = new LinkedList<>();
        for (EtatDeTousLesProduitsDunMagasinWrapper etlprod : etatStockParMagasin) {
            wrapperList.add(new EtatDeTousLesProduitsMagasinWrapper(etlprod));
        }
        return wrapperList;
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(10).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> afficherPeriode(List<EtatDeTousLesProduitsMagasinWrapper> etlprod) {
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        
        LocalDate localdate1 = LocalDate.parse(dateDebut);
        LocalDate localdate2 = LocalDate.parse(dateFin);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        addCustomerAttribute(list, "Magasin", etlprod.get(0).getMagasin());
        addCustomerAttribute(list, "Période du",  dateFormat.format(localdate1) + " au " +  dateFormat.format(localdate2));

        return cmp.verticalList(list);
    }
      
}
