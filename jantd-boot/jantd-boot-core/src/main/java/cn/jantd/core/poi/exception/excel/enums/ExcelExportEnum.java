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
package cn.jantd.core.poi.exception.excel.enums;

/**
 * 导出异常类型枚举
 *
 * @author  圈哥
 * @date 2014年6月19日 下午10:59:51
 */
public enum ExcelExportEnum {

	/**
	 * Excel 导出   参数错误
	 */
	PARAMETER_ERROR("Excel 导出   参数错误"),
	/**
	 * Excel导出错误
	 */
	EXPORT_ERROR("Excel导出错误");

	private String msg;

	ExcelExportEnum(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
