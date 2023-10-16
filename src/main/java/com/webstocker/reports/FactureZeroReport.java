
package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.utilitaires.NombreEnChiffre;
import java.math.BigDecimal;
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
import static net.sf.dynamicreports.report.builder.component.Components.horizontalList;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.RectangleBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Athanase
 */
public class FactureZeroReport {
   
    List<FactureZeroWrapper> reportData;
    private AggregationSubtotalBuilder<BigDecimal> totalSum;
     
    public FactureZeroReport (List<LigneBonDeSortie> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }
    
    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
                .setDataSource(new FactureZeroReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("\n\nFACTURE ZERO"),
                            // bordereauInfoComponent(), bordereauInfoClientDemandeurComponent())
                            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                            cmp.hListCell(createClientComponent(reportData)),
                              cmp.hListCell(createMagasinComponent("  Informations facture", reportData)).heightFixedOnTop()),
//                                    cmp.hListCell(cmp.text("                                        "))),
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
             
            TextColumnBuilder<String> descriptionColumn = col.column("Designation", "nomProduit", type.stringType())
                    .setFixedWidth(160);
            TextColumnBuilder<String> numeroLotColumn = col.column("Numéro Lot", "numeroLot", type.stringType())
                    .setFixedWidth(100);
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité", "quantiteFacture", type.longType())
                    .setFixedWidth(70);
            TextColumnBuilder<BigDecimal> priceColumn = col.column("Prix Unitaire H.T", "prixvente", type.bigDecimalType())
                    .setFixedWidth(50);
            //TextColumnBuilder<Long> montantColumn = col.column("Montant", "montantVente", type.longType());
            
            TextColumnBuilder<BigDecimal> montantColumn = priceColumn.multiply(quantityColumn)
                    .setTitle("Montant (FCFA)");

            totalSum = sbt.sum(montantColumn).setLabel("Total à payer");
            String sd;
            report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(5));
            report.columns(
                    descriptionColumn, numeroLotColumn, quantityColumn, priceColumn, montantColumn)
                    .columnGrid(descriptionColumn, numeroLotColumn, quantityColumn, priceColumn, montantColumn)
                    .subtotalsAtSummary(totalSum)
                    .summary(horizontalList().add(cmp.verticalGap(20),
                         //   cmp.text(new FactureZeroReport.SommeAPayer())),
                            cmp.text("                        ")),
                            cmp.verticalGap(50),
                            cmp.text("                                                                                                                    RMC / DE")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));

            //System.out.println("VOICI L'ESSAI : "+ );            
            return report;
        }
    }
    
     private class SommeAPayer extends AbstractSimpleExpression<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public String evaluate(ReportParameters reportParameters) {
            String rep, rep2;
            StringBuilder nombre = new StringBuilder();

            String stringNombre = null;
            String valeurString = null;
            char let;

            BigDecimal sommeTotale = reportParameters.getValue(totalSum);
            String str = Templates.currencyType.valueToString(sommeTotale, reportParameters.getLocale());
            try {
                if (str.contains("$")) {
                    rep = str.replace("$ ", "");
                    rep2 = rep.replace(",00", "");

                    char[] toCharArray = rep2.toCharArray();
                    for (int i = 0; i < toCharArray.length; i++) {
                        let = toCharArray[i];

                        if (Character.isDigit(let)) {
                            nombre.append(let);
                        }
                    }
                    valeurString = nombre.toString();
                } else {
                    rep2 = str;
                    valeurString = rep2;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();;
            }
            long parseLong = Long.parseLong(valeurString);
            return "Arretée la présente facture à la somme de : " + NombreEnChiffre.getLettre(parseLong).toUpperCase();
        }

    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<FactureZeroWrapper> reportData;

        public SubreportDataSourceExpression(List<FactureZeroWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<FactureZeroWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        List<FactureZeroWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lbs : ligneBonDeSortie) {
            wrapperList.add(new FactureZeroWrapper(lbs));
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

    private ComponentBuilder<?, ?> createClientComponent( List<FactureZeroWrapper> facturewrapper) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, " Béneficiare", facturewrapper.get(0).getNomClient());
        addCustomerAttribute(list, " Téléphone", facturewrapper.get(0).getTelephoneClient());
//        addCustomerAttribute(list, " Date facture", facturewrapper.get(0).getDateFacture());
//        addCustomerAttribute(list, " N° bon de sortie", facturewrapper.get(0).getNumeroBondeSortie());
        
//        addCustomerAttribute(list, " Localité", facturewrapper.get(0).getLocaliteClient());
//        addCustomerAttribute(list, " Téléphone", facturewrapper.get(0).getTelephoneClient());
//        addCustomerAttribute(list, " Adresse postale", facturewrapper.get(0).getBpClient());
        return cmp.verticalList( list).setBackgroundComponent(rectangle);   
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(12).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> createMagasinComponent(String label, List<FactureZeroWrapper> facturewrapper) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, " N°facture", facturewrapper.get(0).getNumeroFactureNormalise());
        addCustomerAttribute(list, " Date", facturewrapper.get(0).getDateFacture());
        addCustomerAttribute(list, " N° bon de sortie", facturewrapper.get(0).getNumeroBondeSortie());
        addCustomerAttribute(list, " Magasin", facturewrapper.get(0).getMagasin());
       // addCustomerAttribute(list, " Type de vente", facturewrapper.get(0).getTypeVente());
       // addCustomerAttribute(list, " Date de paiement", facturewrapper.get(0).getDateLimite());

        return cmp.verticalList(cmp.text(label).setStyle(Templates.boldStyle), list).setBackgroundComponent(rectangle);
        
    }
}
