<#list cfg.subTables as subTab>
package ${package.Entity};

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import cn.jantd.core.poi.excel.annotation.Excel;
import java.util.Date;

/**
 * @Description: ${subTab.ftlDescription}
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */

@Data
@TableName("${subTab.tableName}")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "${subTab.ftlDescription}对象")
public class ${subTab.entity} implements Serializable {
    private static final long serialVersionUID = 1L;
    
    <#list subTab.originalColumns as po>
	/**
	 * ${po.filedComment}
	 */
	<#if po.fieldName == cfg.primaryKeyField>
	@TableId(type = IdType.UUID)
	<#else>
	<#if po.fieldDbType =='date'>
	@Excel(name = "${po.filedComment}", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	<#elseif po.fieldDbType =='datetime'>
		<#if !"createTime,updateTime"?contains(po.fieldName)>
	@Excel(name = "${po.filedComment}", width = 20, format = "yyyy-MM-dd HH:mm:ss")
		</#if>
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	<#else>
		<#if !"createBy,updateBy"?contains(po.fieldName)>
	@Excel(name = "${po.filedComment}", width = 15)
		</#if>
    </#if>
  </#if>
	@ApiModelProperty(value = "${po.filedComment}")
	private <#if po.fieldType=='Blob'>byte[]<#else>${po.fieldType}</#if> ${po.fieldName};
	</#list>
}
</#list>