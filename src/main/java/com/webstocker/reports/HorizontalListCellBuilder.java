package com.webstocker.reports;


import net.sf.dynamicreports.report.base.component.DRListCell;
import net.sf.dynamicreports.report.builder.AbstractBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.constant.Constants;
import net.sf.dynamicreports.report.constant.HorizontalCellComponentAlignment;
import net.sf.dynamicreports.report.constant.VerticalCellComponentAlignment;


public class HorizontalListCellBuilder extends AbstractBuilder<HorizontalListCellBuilder, DRListCell> {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

	protected HorizontalListCellBuilder(ComponentBuilder<?, ?> component) {
		super(new DRListCell(component.build()));
	}

	//width
	public HorizontalListCellBuilder widthFixed() {
		getObject().setHorizontalAlignment(HorizontalCellComponentAlignment.LEFT);
		return this;
	}

	public HorizontalListCellBuilder widthFloat() {
		getObject().setHorizontalAlignment(HorizontalCellComponentAlignment.FLOAT);
		return this;
	}

	public HorizontalListCellBuilder widthExpand() {
		getObject().setHorizontalAlignment(HorizontalCellComponentAlignment.EXPAND);
		return this;
	}

	//height
	public HorizontalListCellBuilder heightFixedOnTop() {
		getObject().setVerticalAlignment(VerticalCellComponentAlignment.TOP);
		return this;
	}

	public HorizontalListCellBuilder heightFixedOnMiddle() {
		getObject().setVerticalAlignment(VerticalCellComponentAlignment.MIDDLE);
		return this;
	}

	public HorizontalListCellBuilder heightFixedOnBottom() {
		getObject().setVerticalAlignment(VerticalCellComponentAlignment.BOTTOM);
		return this;
	}

	public HorizontalListCellBuilder heightExpand() {
		getObject().setVerticalAlignment(VerticalCellComponentAlignment.EXPAND);
		return this;
	}

	public DRListCell getListCell() {
		return build();
	}
}