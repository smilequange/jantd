package cn.jantd.core.poi.excel;

import cn.jantd.core.poi.excel.html.ExcelToHtmlServer;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel 变成界面
 *
 * @author  圈哥
 * @date 2015年5月10日 上午11:51:48
 */
public final class ExcelToHtmlUtil {

	private ExcelToHtmlUtil() {
	}

	/**
	 * 转换成为Table
	 *
	 * @param wb
	 *            Excel
	 * @return
	 */
	public static String toTableHtml(Workbook wb) {
		return new ExcelToHtmlServer(wb, false, 0).printPage();
	}

	/**
	 * 转换成为Table
	 *
	 * @param wb
	 *            Excel
	 * @param sheetNum
	 *            sheetNum
	 * @return
	 */
	public static String toTableHtml(Workbook wb, int sheetNum) {
		return new ExcelToHtmlServer(wb, false, sheetNum).printPage();
	}

	/**
	 * 转换成为完整界面
	 *
	 * @param wb
	 *            Excel
	 * @param sheetNum
	 *            sheetNum
	 * @return
	 */
	public static String toAllHtml(Workbook wb) {
		return new ExcelToHtmlServer(wb, true, 0).printPage();
	}

	/**
	 * 转换成为完整界面
	 *
	 * @param wb
	 *            Excel
	 * @param sheetNum
	 *            sheetNum
	 * @return
	 */
	public static String toAllHtml(Workbook wb, int sheetNum) {
		return new ExcelToHtmlServer(wb, true, sheetNum).printPage();
	}

}
