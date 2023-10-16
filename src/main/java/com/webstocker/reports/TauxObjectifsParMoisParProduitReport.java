package com.webstocker.reports;

import com.webstocker.domain.Objectifs;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.ctab;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.chart.CategoryChartSerieBuilder;
import net.sf.dynamicreports.report.builder.chart.PieChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;
/**
 *
 * @author Athanase
 */
public class TauxObjectifsParMoisParProduitReport {
    List<TauxAtteinteObjectifWrapper> reportData;
    
    public TauxObjectifsParMoisParProduitReport(List<Objectifs> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }
    
    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new TauxObjectifsParMoisParProduitReport.SubreportExpression())
                .setDataSource(new TauxObjectifsParMoisParProduitReport.SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();

        try {
            reportBuilder
                    .title(Templates.createTitleComponent("Taux d'atteinte des objectifs de vente par mois"))
                    .detail(subreport, cmp.verticalGap(20))
//                    .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
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

            FieldBuilder<String> produitField = field("nomProduit", type.stringType());
            FieldBuilder<String> dateField = field("dateObjectif", type.stringType());
            FieldBuilder<Double> tauxfield = field("taux", type.doubleType());

            CategoryChartSerieBuilder tauxSerie = cht.serie(tauxfield).setLabel("Taux");
            // CategoryChartSerieBuilder produitSerie = cht.serie(produitField).setLabel("Produit");

            TextColumnBuilder<String> dateColumn = col.column("Mois", "dateObjectif", type.stringType());
            TextColumnBuilder<String> descriptionColumn = col.column("Produit", "nomProduit", type.stringType());            
            TextColumnBuilder<Double> tauxColumn = col.column("Taux (%)", "taux", type.doubleType());
//            CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("nomProduit", String.class).setShowTotal(Boolean.FALSE);
//            CrosstabColumnGroupBuilder<String> columnGroup = ctab.columnGroup("dateObjectif", String.class).setShowTotal(Boolean.FALSE);
//
//            CrosstabBuilder crosstab;
//            crosstab = ctab.crosstab()
//                    .headerCell(cmp.text("Produit / Mois").setStyle(Templates.boldCenteredStyle))
//                    .rowGroups(rowGroup)
//                    .columnGroups(columnGroup)
//                    .addMeasure(ctab.measure("Taux", "taux", Double.class, Calculation.NOTHING));
            // .measures(ctab.measure("Taux",   "taux",  String.class,   Calculation.NOTHING));
            //ctab.measure("Unit price", "unitprice", BigDecimal.class, Calculation.SUM));
            
            PieChartBuilder chartParProduit = cht.pieChart()
//            BarChartBuilder chartParProduit = cht.barChart()
                    .setTitle("taux d'atteinte par produit")
//                    .customizers(new ChartCustomizer())
                    .setShowValues(true)
                    .setShowLegend(true)
                    
                    .setKey(descriptionColumn)
//                    .setCategory(descriptionColumn).setShowPercentages(Boolean.TRUE)
                    .series(cht.serie(tauxColumn));
                    
//                    .setValueAxisFormat(cht.axisFormat().setRangeMaxValueExpression(110)).setShowTickMarks(Boolean.FALSE)
//                    .setCategoryAxisFormat(cht.axisFormat().setLabel("Produit "));

//            BarChartBuilder chartParMois = cht.barChart()
//                    .setTitle("Taux d'atteinte par mois")
//                    .customizers(new ChartCustomizer())
//                    .setCategory(dateField)
//                    .series(tauxSerie)
//                    .setValueAxisFormat(cht.axisFormat().setRangeMaxValueExpression(100))
//                    .setCategoryAxisFormat(cht.axisFormat().setLabel("Date "));

            report
                    .summary(cmp.horizontalList(chartParProduit) )                           

              .columns(dateColumn, descriptionColumn,  tauxColumn);
//                .columns(descriptionColumn, janvierColumn, fevrierColumn, marsColumn, avrilColumn,maiColumn,
//                        juinColumn,juilletColumn, aoutColumn, septembreColumn, octobreColumn, novembreColumn, decembreColumn );
            // .columns(descriptionColumn, quantityColumn, valeurVenteColumn);
            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<TauxAtteinteObjectifWrapper> reportData;

        public SubreportDataSourceExpression(List<TauxAtteinteObjectifWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<TauxAtteinteObjectifWrapper> generateReportData(List<Objectifs> objectifs) {

        List<TauxAtteinteObjectifWrapper> wrapperList = new LinkedList<>();
        for (Objectifs obj : objectifs) {
            wrapperList.add(new TauxAtteinteObjectifWrapper(obj));
        }
        return wrapperList;
    }

    private class ChartCustomizer implements DRIChartCustomizer, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public void customize(JFreeChart chart, ReportParameters reportParameters) {

            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            renderer.setBaseItemLabelsVisible(true);
        }
    }

    private class YearExpression extends AbstractSimpleExpression<Integer> {

        private static final long serialVersionUID = 1L;

        @Override
        public Integer evaluate(ReportParameters reportParameters) {
            Calendar c = Calendar.getInstance();
            c.setTime((Date) reportParameters.getValue("orderdate"));

            return c.get(Calendar.YEAR);
        }
    }

    
    
}
