package com.webstocker.reports;

import com.webstocker.domain.LigneBonDeSortie;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.component.RectangleBuilder;
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
public class BonDeSortiesReport {

    List<BonDeSortieWrapper> reportData;
    private AggregationSubtotalBuilder<Long> totalQuantiteProduit;


    public BonDeSortiesReport(List<LigneBonDeSortie> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
                .setDataSource(new SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("BON DE SORTIE"),
                            bordereauInfoComponent(reportData), bordereauInfoClientDemandeurComponent(reportData))
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

            int masterRowNumber = reportParameters.getReportRowNumber();
            JasperReportBuilder report = report();
            report.setTemplate(Templates.reportTemplate);

            TextColumnBuilder<String> designationColumn = col.column("Designation", "nomProduit", type.stringType());
            TextColumnBuilder<String> numeroLotColumn = col.column("Numéro lot", "numeroLot", type.stringType());
            TextColumnBuilder<Long> quantiteColumn = col.column("Quantite", "quantite", type.longType());
            totalQuantiteProduit = sbt.sum(quantiteColumn);
            
           report.setColumnStyle(stl.style().setBorder(stl.pen1Point()).setPadding(3));
           
            report.columns(designationColumn, numeroLotColumn, quantiteColumn)
                    .columnGrid(designationColumn, numeroLotColumn, quantiteColumn)
                    .subtotalsAtSummary(totalQuantiteProduit);
            
            report.summary(cmp.horizontalList().add(cmp.hListCell(cmp.text("Signature du demandeur: ")
                    .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                    .setStyle(stl.style().setBorder(stl.pen1Point())))).setFixedHeight(80)
                    .add(cmp.hListCell(cmp.text("Signature chef de cellule:")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                            .setStyle(stl.style().setBorder(stl.pen1Point()))))
                    .add(cmp.hListCell(cmp.text("Signature RMC/DE ")
                            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                            .setStyle(stl.style().setBorder(stl.pen1Point())))));

            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<BonDeSortieWrapper> reportData;

        public SubreportDataSourceExpression(List<BonDeSortieWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<BonDeSortieWrapper> generateReportData(List<LigneBonDeSortie> ligneBonDeSortie) {

        List<BonDeSortieWrapper> wrapperList = new LinkedList<>();
        for (LigneBonDeSortie lbs : ligneBonDeSortie) {
            try{
            wrapperList.add(new BonDeSortieWrapper(lbs));
            }catch(NullPointerException e){
                e.printStackTrace();
            }
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
            list.add(cmp.text(label + " : ").setFixedColumns(15).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
        }
    }

    private ComponentBuilder<?, ?> bordereauInfoComponent(List<BonDeSortieWrapper> bonDeSortieWrapper) {

        RectangleBuilder rect = cmp.rectangle();
        HorizontalListBuilder dateBl = cmp.horizontalList().add(cmp.text( bonDeSortieWrapper.get(0)!= null? "Date : "+bonDeSortieWrapper.get(0).getDateBonDeSortie():"Date : ")).setBackgroundComponent(rect);
//        HorizontalListBuilder dateBl = cmp.horizontalList().add(cmp.text( "Date : "+bonDeSortieWrapper.get(0).getDateBonDeSortie())).setBackgroundComponent(rect);
        HorizontalListBuilder numeroBonSortie = cmp.horizontalList().add(cmp.text("N° bon de sortie : " + bonDeSortieWrapper.get(0).getNumeroBondeSortie())).setBackgroundComponent(rect);
        HorizontalListBuilder typesortie = cmp.horizontalList().add(cmp.text("Type de sortie : " + bonDeSortieWrapper.get(0).getTypeSortie())).setBackgroundComponent(rect);
        HorizontalListBuilder containtDate = cmp.horizontalList()
                .add(dateBl, numeroBonSortie, typesortie) //, cmp.horizontalGap(20), title2
                .setStyle(stl.style(4));

        return containtDate;
    }

    private ComponentBuilder<?, ?> bordereauInfoClientDemandeurComponent(List<BonDeSortieWrapper> bonDeSortieWrapper) {

        RectangleBuilder rect = cmp.rectangle();
        String typesortie = bonDeSortieWrapper.get(0).getTypeSortie();
         HorizontalListBuilder destinataire = null;
        switch(typesortie){
            case "VENTE":
                destinataire =  cmp.horizontalList().add(cmp.text("Client : " + bonDeSortieWrapper.get(0).getNomClient())).setBackgroundComponent(rect).setFixedWidth(200); 
            break;
            case "TRANSFERT":
               destinataire =  cmp.horizontalList().add(cmp.text("Destination : " + bonDeSortieWrapper.get(0).getMagasinDestinataire())).setBackgroundComponent(rect).setFixedWidth(200); 
            break;
            case "PROMOTION":
               destinataire =  cmp.horizontalList().add(cmp.text("Beneficiaire : " + bonDeSortieWrapper.get(0).getNomClient())).setBackgroundComponent(rect).setFixedWidth(200);     
             break;
            case "PERTE":
               destinataire =  cmp.horizontalList().add(cmp.text("Beneficiaire : " + " ")).setBackgroundComponent(rect).setFixedWidth(200);     
             break;
            default:
                destinataire =  cmp.horizontalList().add(cmp.text("Client : " + bonDeSortieWrapper.get(0).getNomClient())).setBackgroundComponent(rect).setFixedWidth(200);
             break;        
        }
        
        HorizontalListBuilder dem = cmp.horizontalList().add(cmp.text("Demandeur : " + bonDeSortieWrapper.get(0).getDemandeur())).setBackgroundComponent(rect);
        HorizontalListBuilder magasinOrigine = cmp.horizontalList().add(cmp.text("Origine : " + bonDeSortieWrapper.get(0).getMagasinOrigine())).setBackgroundComponent(rect);
        HorizontalListBuilder containtInfoClientDemandeur = cmp.horizontalList()
                .add(destinataire, dem, magasinOrigine) //, cmp.horizontalGap(20), title2
                .setStyle(stl.style(4));
        return containtInfoClientDemandeur;
    }

}
