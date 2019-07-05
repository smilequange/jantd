
package cn.jantd.core.poi.excel.entity.enmus;

import cn.jantd.core.poi.excel.export.styler.ExcelExportStylerBorderImpl;
import cn.jantd.core.poi.excel.export.styler.ExcelExportStylerColorImpl;
import cn.jantd.core.poi.excel.export.styler.ExcelExportStylerDefaultImpl;

/**
 * 插件提供的几个默认样式
 *
 * @author  圈哥
 * @date 2015年1月9日 下午9:02:24
 */
public enum ExcelStyleType {

	/**
	 * 默认样式
	 */
	NONE("默认样式", ExcelExportStylerDefaultImpl.class),
	/**
	 * 边框样式
	 */
	BORDER("边框样式", ExcelExportStylerBorderImpl.class),
	/**
	 * 间隔行样式
	 */
	COLOR("间隔行样式", ExcelExportStylerColorImpl.class);

	/**
	 * 名称
	 */
	private String name;
	/**
	 * clazz
	 */
	private Class<?> clazz;

	ExcelStyleType(String name, Class<?> clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getName() {
		return name;
	}

}
