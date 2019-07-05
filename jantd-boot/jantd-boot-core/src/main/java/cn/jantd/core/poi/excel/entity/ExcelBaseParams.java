
package cn.jantd.core.poi.excel.entity;

import cn.jantd.core.poi.handler.inter.IExcelDataHandler;


/**
 * 基础参数
 *
 * @author  圈哥
 * @date 2014年6月20日 下午1:56:52
 */
public class ExcelBaseParams {

	/**
	 * 数据处理接口,以此为主,replace,format都在这后面
	 */
	private IExcelDataHandler dataHanlder;

	public IExcelDataHandler getDataHanlder() {
		return dataHanlder;
	}

	public void setDataHanlder(IExcelDataHandler dataHanlder) {
		this.dataHanlder = dataHanlder;
	}

}
