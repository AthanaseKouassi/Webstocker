
package com.webstocker.reports;

import com.webstocker.domain.wrapper.NouvelleFactureWrapper;
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
public class FactureNouvelleReport {
     private List<FactureNouvelleWrapper> reportData;
     private AggregationSubtotalBuilder<BigDecimal> totalSum;
     
     public FactureNouvelleReport(List<NouvelleFactureWrapper> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }
     
      public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new FactureNouvelleReport.SubreportExpression())
                .setDataSource(new FactureNouvelleReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("   \n\n"),
                            // bordereauInfoComponent(), bordereauInfoClientDemandeurComponent())
                            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                            cmp.hListCell(createClientComponent("  Informations client", reportData)).heightFixedOnTop(),
                            cmp.hListCell(createMagasinComponent("  Informations facture", reportData)).heightFixedOnTop()),
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
                    .setFixedWidth(200);
//            TextColumnBuilder<String> numeroLotColumn = col.column("Numéro Lot", "numeroLot", type.stringType())
//                    .setFixedWidth(80);
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité", "quantiteProduitFacture", type.longType())
                    .setFixedWidth(100);
            TextColumnBuilder<Long> montDeVenteColumn = col.column("Prix de Vente", "prixDeVente", type.longType())
                    .setFixedWidth(85);
            TextColumnBuilder<BigDecimal> montantColumn = (montDeVenteColumn.multiply(1))
                    .setTitle("Montant (FCFA)");
            totalSum = sbt.sum(montantColumn).setLabel("Total");

         
            report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(5));
            report.columns(
                    descriptionColumn, quantityColumn, montDeVenteColumn, montantColumn)
                    .columnGrid(descriptionColumn, quantityColumn, montDeVenteColumn, montantColumn)
                    .subtotalsAtSummary( totalSum)
                    .summary(horizontalList().add(cmp.verticalGap(28),
                            cmp.text(new FactureNouvelleReport.SommeAPayer())),
                            cmp.text("    "),
                            cmp.text("    "),
                            cmp.text("                                                                                    RMC/DE")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
            return report;
        }
    }
     
      private class SommeAPayer extends AbstractSimpleExpression<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public String evaluate(ReportParameters reportParameters) {
            String rep, rep2;
            StringBuilder nombre = new StringBuilder();
            Long parseLong = 0L;
            String stringNombre = null;
            String valeurString = null;
            char let;
          
            BigDecimal sommeTotale = reportParameters.getValue(totalSum);

//            som²  meTotale = sommeTotale.subtract(new BigDecimal(remiseSumValue).setScale(0, BigDecimal.ROUND_DOWN));
//            BigDecimal sommeTotale = reportParameters.getValue(totalSum);
            String str = Templates.currencyType.valueToString(sommeTotale, reportParameters.getLocale());
//            String str = Templates.currencyType.valueToString(sommeTotale.setScale(0, BigDecimal.ROUND_DOWN), reportParameters.getLocale());

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
                e.printStackTrace();
            }

            if ("".equals(valeurString)) {
                parseLong = 0L;
            } else {
                parseLong = Long.parseLong(valeurString);
            }

            return "Arretée la présente facture à la somme de : " + NombreEnChiffre.getLettre(parseLong).toUpperCase() + " Francs CFA";
        }

    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<FactureNouvelleWrapper> reportData;

        public SubreportDataSourceExpression(List<FactureNouvelleWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }
    
     private List<FactureNouvelleWrapper> generateReportData(List<NouvelleFactureWrapper> nfwrap) {

        List<FactureNouvelleWrapper> wrapperList = new LinkedList<>();
        for (NouvelleFactureWrapper lbs : nfwrap) {
            wrapperList.add(new FactureNouvelleWrapper(lbs));
        }
        return wrapperList;
    }
    
      private ComponentBuilder<?, ?> createClientComponent(String label, List<FactureNouvelleWrapper> factureNouvelleWrapper) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
      try{
        addCustomerAttribute(list, " Client", factureNouvelleWrapper.get(0).getNomClient());
        addCustomerAttribute(list, " Localité", factureNouvelleWrapper.get(0).getLocaliteClient());
        addCustomerAttribute(list, " Téléphone", factureNouvelleWrapper.get(0).getTelephoneClient());
        addCustomerAttribute(list, " Adresse postale", factureNouvelleWrapper.get(0).getBpClient()); 
      }catch (IndexOutOfBoundsException e){
          e.printStackTrace();
      }
        return cmp.verticalList(cmp.text(label).setStyle(Templates.boldStyle), list).setBackgroundComponent(rectangle);
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(12).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> createMagasinComponent(String label, List<FactureNouvelleWrapper> factNwrapp) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, " Date facture", factNwrapp.get(0).getDateFacture());
        addCustomerAttribute(list, " N° facture", factNwrapp.get(0).getNumeroFactureNormalise());
        addCustomerAttribute(list, " N° bon de sortie", factNwrapp.get(0).getNumeroBondeSortie());
        addCustomerAttribute(list, " Magasin", factNwrapp.get(0).getMagasin());
        addCustomerAttribute(list, " Type de vente", factNwrapp.get(0).getTypeVente());
        addCustomerAttribute(list, " Date de paiement", factNwrapp.get(0).getDateLimite());

        return cmp.verticalList(cmp.text(label).setStyle(Templates.boldStyle), list).setBackgroundComponent(rectangle);
    } 

     
}
