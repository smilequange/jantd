package ${package.Entity};

import java.util.Date;
<#list table.importPackages as pkg>
import ${pkg};
</#list>
import cn.jantd.core.poi.excel.annotation.Excel;
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

/**
 * @Description: ${table.comment}
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */
@Data
@TableName("${table.name}")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "${entity}对象", description = "${table.comment}")
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;

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
}
