/**
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.jantd.core.poi.excel.export.styler;

import cn.jantd.core.poi.excel.entity.params.ExcelExportEntity;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Excel导出样式接口
 *
 * @author  圈哥
 * @date 2015年1月9日 下午5:32:30
 */
public interface IExcelExportStyler {

	/**
	 * 列表头样式
	 *
	 * @param headerColor
	 * @return
	 */
	 CellStyle getHeaderStyle(short headerColor);

	/**
	 * 标题样式
	 *
	 * @param color
	 * @return
	 */
	 CellStyle getTitleStyle(short color);

	/**
	 * 获取样式方法
	 *
	 * @param noneStyler
	 * @param entity
	 * @return
	 */
	public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity);

}
