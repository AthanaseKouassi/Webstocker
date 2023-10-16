package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.domain.Lot;
import java.awt.Rectangle;
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
public class ListeLotProduitSortieParMagasinsReport {

    List<ListeLotProduitSortieParMagasinWrapper> reportData;
    String datedebut;
    String datefin;

    public ListeLotProduitSortieParMagasinsReport(List<LigneBonDeSortie> reportData, String datedebut, String datefin) {
        this.reportData = generateReportData(reportData);
        this.datedebut = datedebut;
        this.datefin = datefin;
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new ListeLotProduitSortieParMagasinsReport.SubreportExpression())
                .setDataSource(new ListeLotProduitSortieParMagasinsReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Liste lots de produit sortie par magasin"),
                            // bordereauInfoComponent(), bordereauInfoClientDemandeurComponent())
                            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                            cmp.hListCell(informationRapport(reportData))),
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

            TextColumnBuilder<String> descriptionColumn = col.column("Produit", "nomProduit", type.stringType());
            TextColumnBuilder<String> numeroLotColumn = col.column("Numéro Lot", "numeroLot", type.stringType());
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité vendue", "quantite", type.longType());
//            TextColumnBuilder<Long> valeurVenteColumn = col.column("Valeur des ventes", "montantVente", type.longType());
//            TextColumnBuilder<BigDecimal> montantColumn = quantityColumn.multiply(valeurVenteColumn)
//                    .setTitle("Montant vente");

            report
                    .columns(descriptionColumn, numeroLotColumn, quantityColumn);
            //.subtotalsAtSummary(sbt.sum(montantColumn));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<ListeLotProduitSortieParMagasinWrapper> reportData;

        public SubreportDataSourceExpression(List<ListeLotProduitSortieParMagasinWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<ListeLotProduitSortieParMagasinWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        long sommeQte = 0;

        List<ListeLotProduitSortieParMagasinWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lignebondesortie : ligneBonDeSortie) {
            wrapperList.add(new ListeLotProduitSortieParMagasinWrapper(lignebondesortie));

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

    private ComponentBuilder<?, ?> informationRapport(List<ListeLotProduitSortieParMagasinWrapper> listeWrapper) {
//        RectangleBuilder rectangle = cmp.roundRectangle();
//        rectangle.setDimension(200, 80);//.setStyle(stl.style().setBorder(stl.pen1Point()));
       
        LocalDate localdate1 = LocalDate.parse(datedebut);
        LocalDate localdate2 = LocalDate.parse(datefin);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, " Magasin", listeWrapper.get(0).getNomMagasin());
        addCustomerAttribute(list, " Produit", listeWrapper.get(0).nomProduit);
        addCustomerAttribute(list, " Type de sortie", listeWrapper.get(0).getTypeDeSortie());
        addCustomerAttribute(list, "        Période  du", dateFormat.format(localdate1)+" au "+dateFormat.format(localdate2));


        return cmp.verticalList(list);//.setBackgroundComponent(rectangle);
    }

}
