package com.webstocker.reports;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.awt.*;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;

/**
 *
 * @author Athanase
 */
public class TemplatesEtatReconciliation {

    public static final StyleBuilder rootStyle;
    public static final StyleBuilder boldStyle;
    public static final StyleBuilder italicStyle;
    public static final StyleBuilder boldCenteredStyle;
    public static final StyleBuilder bold12CenteredStyle;
    public static final StyleBuilder bold18CenteredStyle;
    public static final StyleBuilder bold22CenteredStyle;
    public static final StyleBuilder columnStyle;
    public static final StyleBuilder columnTitleStyle;
    public static final StyleBuilder groupStyle;
    public static final StyleBuilder subtotalStyle;
    public static final StyleBuilder summaryStyle;

    public static final ReportTemplateBuilder reportTemplate;
    public static final CurrencyType currencyType;
    public static final ComponentBuilder<?, ?> dynamicReportsComponent;
    public static final ComponentBuilder<?, ?> footerComponent;

    static {
        rootStyle = stl.style().setPadding(2);
        boldStyle = stl.style(rootStyle).bold();
        italicStyle = stl.style(rootStyle).italic();
        boldCenteredStyle = stl.style(boldStyle)
                .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
        bold12CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(9);
        bold18CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(18);
        bold22CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(22);
        columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
        columnTitleStyle = stl.style(columnStyle)
                .setBorder(stl.pen1Point())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setBackgroundColor(Color.LIGHT_GRAY)
                .bold();
        groupStyle = stl.style(boldStyle)
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
        subtotalStyle = stl.style(boldStyle)
                .setTopBorder(stl.pen1Point());
        summaryStyle = stl.style(rootStyle);
        StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
        StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(170, 170, 170));
        StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(140, 140, 140));
        StyleBuilder crosstabCellStyle = stl.style(columnStyle)
                .setBorder(stl.pen1Point());

        TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
                .setHeadingStyle(0, stl.style(rootStyle).bold());
        //.ENGLISH
        reportTemplate = template()
                .setLocale(Locale.FRENCH)
                .setColumnStyle(columnStyle)
                .setColumnTitleStyle(columnTitleStyle)
                .setSummaryStyle(rootStyle)
                .setGroupStyle(groupStyle)
                .setGroupTitleStyle(groupStyle)
                .setSubtotalStyle(subtotalStyle)
                .highlightDetailEvenRows()
                .crosstabHighlightEvenRows()
                .setCrosstabGroupStyle(crosstabGroupStyle)
                .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
                .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
                .setCrosstabCellStyle(crosstabCellStyle)
                .setTableOfContentsCustomizer(tableOfContentsCustomizer);

        currencyType = new CurrencyType();

//        cmp.image(Templates.class.getResource("images/blanc_aimas")).setFixedDimension(90, 70),
//         cmp.image(Templates.class.getResource("images/blanc_texte_aimas")).setFixedDimension(350, 70)
        dynamicReportsComponent
                = cmp.horizontalList(
                        cmp.image(Templates.class.getResource("images/logoAimas.png")).setFixedDimension(90, 70).setHorizontalImageAlignment(HorizontalImageAlignment.CENTER),
                        cmp.image(Templates.class.getResource("images/texte-entete-aimas.png")).setFixedDimension(350, 70).setHorizontalImageAlignment(HorizontalImageAlignment.CENTER)
                );

        footerComponent = cmp.pageXofY()
                .setStyle(
                        stl.style(boldCenteredStyle)
                        .setTopBorder(stl.pen1Point()));
    }

    /**
     * Creates custom component which is possible to add to any report band
     * component
     */
    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
        return cmp.verticalList()
                .add(
                        dynamicReportsComponent,
                        cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER))
                //.add(cmp.line())

                .add(cmp.verticalGap(10));
        //.newRow().newRow()
    }

    public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
        return new CurrencyValueFormatter(label);
    }

    public static class CurrencyType extends BigDecimalType {

        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return "$ #,###.00";
        }
    }

    private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {

        private static final long serialVersionUID = 1L;

        private String label;

        public CurrencyValueFormatter(String label) {
            this.label = label;
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            return label + currencyType.valueToString(value, reportParameters.getLocale());
        }
    }

}
