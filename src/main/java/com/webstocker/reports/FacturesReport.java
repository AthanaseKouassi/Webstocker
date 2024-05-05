package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import com.webstocker.utilitaires.NombreEnChiffre;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.RectangleBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.builder.subtotal.CustomSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import static net.sf.dynamicreports.report.builder.component.Components.horizontalList;

/**
 * @author Athanase
 */
public class FacturesReport {

    List<FactureWrapper> reportData;
    private AggregationSubtotalBuilder<BigDecimal> totalSum;
    private AggregationSubtotalBuilder<Integer> remiseMontant;
    private AggregationSubtotalBuilder<BigDecimal> totSum;
    private CustomSubtotalBuilder<BigDecimal> unitPriceSbt;
    private TextColumnBuilder<BigDecimal> montantHT;

    private VariableBuilder<BigDecimal> montantSum;
    private VariableBuilder<Integer> remiseSum;

    public FacturesReport(List<LigneBonDeSortie> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
            .setDataSource(new SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                .title(Templates.createTitleComponent("   \n\n"),
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

    private List<FactureWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        List<FactureWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lbs : ligneBonDeSortie) {
            wrapperList.add(new FactureWrapper(lbs));
        }
        return wrapperList;
    }

    private ComponentBuilder<?, ?> createClientComponent(String label, List<FactureWrapper> facturewrapper) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();
        try {
            addCustomerAttribute(list, " Client", facturewrapper.get(0).getNomClient());
            addCustomerAttribute(list, " Localité", facturewrapper.get(0).getLocaliteClient());
            addCustomerAttribute(list, " Téléphone", facturewrapper.get(0).getTelephoneClient());
            addCustomerAttribute(list, " Adresse postale", facturewrapper.get(0).getBpClient());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return cmp.verticalList(cmp.text(label).setStyle(Templates.boldStyle), list).setBackgroundComponent(rectangle);
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(12).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> createMagasinComponent(String label, List<FactureWrapper> facturewrapper) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();
        addCustomerAttribute(list, " Date facture", facturewrapper.get(0).getDateFacture());
        addCustomerAttribute(list, " N° facture", facturewrapper.get(0).getNumeroFactureNormalise().toUpperCase());
        addCustomerAttribute(list, " N° bon de sortie", facturewrapper.get(0).getNumeroBondeSortie());
        addCustomerAttribute(list, " Magasin", facturewrapper.get(0).getMagasin());
        addCustomerAttribute(list, " Type de vente", facturewrapper.get(0).getTypeVente());
        addCustomerAttribute(list, " Date de paiement", facturewrapper.get(0).getDateLimite());

        return cmp.verticalList(cmp.text(label).setStyle(Templates.boldStyle), list).setBackgroundComponent(rectangle);
    }

    private class SubreportExpression extends AbstractSimpleExpression<JasperReportBuilder> {

        private static final long serialVersionUID = 1L;

        @Override
        public JasperReportBuilder evaluate(ReportParameters reportParameters) {

            JasperReportBuilder report = report();
            report.setTemplate(Templates.reportTemplate);

            TextColumnBuilder<String> descriptionColumn = col.column("Designation", "nomProduit", type.stringType())
                .setFixedWidth(160);
            TextColumnBuilder<String> numeroLotColumn = col.column("Numéro Lot", "numeroLot", type.stringType())
                .setFixedWidth(80);
            TextColumnBuilder<Long> quantityColumn = col.column("Quantité", "quantiteFacture", type.longType())
                .setFixedWidth(100);
            TextColumnBuilder<Long> montDeVenteColumn = col.column("Prix de Vente", "montantVente", type.longType())
                .setFixedWidth(75);
            TextColumnBuilder<BigDecimal> montantColumn = (montDeVenteColumn.multiply(1))
                .setTitle("Montant (FCFA)");

            totalSum = sbt.sum(montantColumn).setLabel("Total");

            String sd;
            report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(5));
            report.columns(
                    descriptionColumn, numeroLotColumn, quantityColumn, montDeVenteColumn, montantColumn)
                .columnGrid(descriptionColumn, numeroLotColumn, quantityColumn, montDeVenteColumn, montantColumn)

                .subtotalsAtSummary(totalSum)
                .summary(horizontalList().add(cmp.verticalGap(28),
                        cmp.text(new SommeAPayer())),
                    cmp.text("    "),
                    cmp.text("    "),
                    cmp.text("                                                                                    RMC/DE")
                        .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER));
            return report;
        }
    }

    private class SommeApresRemise extends AbstractSimpleExpression<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public String evaluate(ReportParameters reportParameters) {
            String valeur = null;
            BigDecimal totalSumValue = reportParameters.getValue(totalSum);
            String str = Templates.currencyType.valueToString(totalSumValue, reportParameters.getLocale());
            if (str.contains("$")) {
                valeur = str.replace("$ ", "");
            } else {
                valeur = str;
            }
            return valeur;
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
        List<FactureWrapper> reportData;

        public SubreportDataSourceExpression(List<FactureWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
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

}
