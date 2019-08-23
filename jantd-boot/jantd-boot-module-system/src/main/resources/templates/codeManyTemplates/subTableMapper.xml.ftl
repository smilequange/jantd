<#list cfg.subTables as subTab>
<#assign originalForeignKeys = subTab.originalForeignKeys>
#segment#${subTab.entity}Mapper.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${subTab.entity}Mapper">

	<delete id="deleteByMainId" parameterType="java.lang.String">
		DELETE 
		FROM  ${subTab.tableName} 
		WHERE
		<#list originalForeignKeys as key>
		<#if key?lower_case?index_of("${cfg.primaryKeyField}")!=-1>
			 ${key} = ${r'#'}{${cfg.primaryKeyField}} <#rt/>
		<#else>
			 ${key} = ${r'#'}{${key}} <#rt/>
		</#if>
		<#if key_has_next>AND</#if>
		</#list>
	</delete>
	
	<select id="selectByMainId" parameterType="java.lang.String" resultType="${package.Entity}.${subTab.entity}">
		SELECT *
		FROM  ${subTab.tableName}
		WHERE
		<#list originalForeignKeys as key>
		<#if key?lower_case?index_of("${cfg.primaryKeyField}")!=-1>
			 ${key} = ${r'#'}{${cfg.primaryKeyField}} <#rt/>
		<#else>
			 ${key} = ${r'#'}{${key}} <#rt/>
		</#if>
		<#if key_has_next>AND</#if>
		</#list>
	</select>
</mapper>

</#list>