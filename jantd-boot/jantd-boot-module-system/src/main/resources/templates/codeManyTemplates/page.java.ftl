package ${cfg.bussiPackage}.${cfg.entityPackage}.vo;

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#list cfg.subTables as sub>
import ${package.Entity}.${sub.entity};
</#list>
import lombok.Data;
import cn.jantd.core.poi.excel.annotation.Excel;
import cn.jantd.core.poi.excel.annotation.ExcelCollection;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: ${table.comment}
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */
@Data
public class ${entity}Page {

<#list table.commonFields as field>
	/**
	 * ${field.comment}
	 */
	@TableId(type = IdType.UUID)
	private ${field.propertyType} ${field.propertyName};

</#list>
	<#list table.fields as field>
	/**
	 * ${field.comment}
	 */
	<#if field.propertyName == 'id'>
	@TableId(type = IdType.UUID)
	<#else>
	<#--<#if field.propertyType.contains("date")>-->
	<#if field.type =='date'>
	@Excel(name = "${field.comment}", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	<#elseif field.type =='datetime'>
	<#if !"createTime,updateTime"?contains(field.propertyName)>
	@Excel(name = "${field.comment}", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	</#if>
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	<#else>
	<#if !"createBy,updateBy"?contains(field.propertyName)>
	@Excel(name = "${field.comment}", width = 15)
	</#if>
	</#if>
	<#--</#if>-->
	</#if>
	@ApiModelProperty(value = "${field.comment}")
	private <#if field.columnType=='BOOLEAN'>Boolean<#elseif field.columnType=='LOCAL_DATE_TIME' || field.columnType=='LOCAL_DATE'>Date<#else >${field.propertyType}</#if> ${field.propertyName};

	</#list>
	<#list cfg.subTables as sub>
	@ExcelCollection(name="${sub.ftlDescription}")
	private List<${sub.entity}> ${sub.entity?uncap_first}List;
	</#list>
	
}
