/**
 * DynamicReports - Free Java reporting library for creating reports dynamically
 * <p>
 * Copyright (C) 2010 - 2015 Ricardo Mariaca
 * http://www.dynamicreports.org
 * <p>
 * This file is part of DynamicReports.
 * <p>
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */
package com.webstocker.reports;

import com.webstocker.domain.Produit;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.LinkedList;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class ProduitsReport {
    List<ProduitWrapper> reportData;

    public ProduitsReport(List<Produit> reportData) {
        this.reportData = generateReportData(reportData);
        build();
    }

    public JasperReportBuilder build() {
        SubreportBuilder subreport = cmp.subreport(new SubreportExpression())
            .setDataSource(new SubreportDataSourceExpression(reportData));
        JasperReportBuilder reportBuilder = report();
       
        try {
            reportBuilder
                .title(Templates.createTitleComponent("Liste des produits par catégorie"))
                .detail(
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
            report
                .setTemplate(Templates.reportTemplate);
            
            report.addColumn(col.column("Catégorie", "categorie", type.stringType()).setPrintRepeatedDetailValues(false));
            //ColumnGroupBuilder stateGroup = grp.group(col.column("Catégorie", "categorie", type.stringType()));            
            report.addColumn(col.column("Produit", "nomProduit", type.stringType()));   
            
           // report.groupBy(stateGroup);
            
            return report;
        }
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {

        private static final long serialVersionUID = 1L;
        List<ProduitWrapper> reportData;

        public SubreportDataSourceExpression(List<ProduitWrapper> reportData) {
            super();
            this.reportData = reportData;
        }

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(reportData);
            return beanCollectionDataSource;
        }
    }

    private List<ProduitWrapper> generateReportData(List<Produit> produits) {

        List<ProduitWrapper> wrapperList = new LinkedList<>();

        for (Produit p : produits) {
            wrapperList.add(new ProduitWrapper(p));
        }

        return wrapperList;
    }

//    public static void main(String[] args) {
//        new ProduitsReport(null);
//    }

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
