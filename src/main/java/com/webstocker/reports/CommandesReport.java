package com.webstocker.reports;

import com.webstocker.domain.Lignecommande;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import static net.sf.dynamicreports.report.builder.component.Components.horizontalList;
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
public class CommandesReport {

    List<CommandeWrapper> reportData;

    public CommandesReport(List<Lignecommande> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new CommandesReport.SubreportExpression())
                .setDataSource(new CommandesReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Commande fabricant"))
                    .detail(
                            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
                            cmp.hListCell(informationCommande(reportData)).heightFixedOnTop(),
                            cmp.hListCell(autreInformation(reportData)).heightFixedOnTop()),
                            cmp.verticalGap(10),
                            subreport,
                            cmp.verticalGap(20))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource());
//                .toPdf(Exporters.pdfExporter("/home/komi/reportaimas.pdf"));
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
            int masterRowNumber = reportParameters.getReportRowNumber();
            JasperReportBuilder report = report();
            report.setTemplate(Templates.reportTemplate);

            TextColumnBuilder<String> produitColumn = col.column("Produit", "produit", type.stringType())
                    .setFixedWidth(270);
            TextColumnBuilder<String> calendrierFabrication = col.column("Calendrier de fabrication", "dateFabrication", type.stringType());
            TextColumnBuilder<Integer> quantite = col.column("Quantité", "quantite", type.integerType());
            

            report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));
            report
                    .columns(produitColumn, calendrierFabrication, quantite)
                    .summary(horizontalList().add(cmp.verticalGap(50),
                            cmp.text("                                                                                   "),
                            cmp.text("                                                                                   "),
                            cmp.text("                                                                      Signataire")));
                    

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<CommandeWrapper> reportData;

        public SubreportDataSourceExpression(List<CommandeWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<CommandeWrapper> generateReportData(List<Lignecommande> lignecommande) {

        List<CommandeWrapper> wrapperList = new LinkedList<>();

        for (Lignecommande c : lignecommande) {
            wrapperList.add(new CommandeWrapper(c));
        }
        return wrapperList;
    }

    private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
        
        if (value != null) {
            list.add(cmp.text(label + " :").setFixedColumns(12).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> informationCommande(List<CommandeWrapper> commande) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, " Date", convertirDate(commande.get(0).getDateCommande()));
        addCustomerAttribute(list, " Numéro Commande", commande.get(0).getNumeroCommande());
        addCustomerAttribute(list, " Produit ", commande.get(0).getProduit());
        addCustomerAttribute(list, " Quantité Totale ", commande.get(0).getQuantiteTotalCommande());
        
        return cmp.verticalList(list).setBackgroundComponent(rectangle);

    }

    private ComponentBuilder<?, ?> autreInformation(List<CommandeWrapper> commande) {
        RectangleBuilder rectangle = cmp.roundRectangle();
        HorizontalListBuilder list = cmp.horizontalList();//.setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
        addCustomerAttribute(list, " Bailleur", commande.get(0).getBailleur());
        addCustomerAttribute(list, " Fabricant", commande.get(0).getFabricant());
        //addCustomerAttribute(list, "Bailleur", commande.get(0).getBailleur());

        return cmp.verticalList(list).setBackgroundComponent(rectangle);
    }

    private String convertirDate(String dateAConvertir) {
        String dateConvertie = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = sdf.parse(dateAConvertir);
            dateConvertie = sdf2.format(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
       // System.out.println("La date de la commande "+dateConvertie);
        return dateConvertie;
    }
}
