package ${package.ServiceImpl};

import ${package.Entity}.${entity};
<#list cfg.subTables as sub>
import ${package.Entity}.${sub.entity};
</#list>
<#list cfg.subTables as sub>
import ${package.Mapper}.${sub.entity}Mapper;
</#list>
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: ${table.comment}服务实现类
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */
@Service
public class ${table.serviceImplName} extends ServiceImpl<${table.mapperName}, ${entity}> implements ${table.serviceName} {

	@Autowired
	private ${entity}Mapper ${entity?uncap_first}Mapper;
	<#list cfg.subTables as sub>
	@Autowired
	private ${sub.entity}Mapper ${sub.entity?uncap_first}Mapper;
	</#list>
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(${entity} ${entity?uncap_first}, <#list cfg.subTables as sub>List<${sub.entity}> ${sub.entity?uncap_first}List<#if sub_has_next>,</#if></#list>) {
		${entity?uncap_first}Mapper.insert(${entity?uncap_first});
		<#list cfg.subTables as sub>
		for(${sub.entity} entity:${sub.entity?uncap_first}List) {
			<#list sub.foreignKeys as key>
			//外键设置
			<#if key?lower_case?index_of("${cfg.primaryKeyField}")!=-1>
			entity.set${key?cap_first}(${entity?uncap_first}.get${cfg.primaryKeyField?cap_first}());
			<#else>
			entity.set${key?cap_first}(${entity?uncap_first}.get${key}());
			</#if>
			</#list>
			${sub.entity?uncap_first}Mapper.insert(entity);
		}
		</#list>
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(${entity} ${entity?uncap_first},<#list cfg.subTables as sub>List<${sub.entity}> ${sub.entity?uncap_first}List<#if sub_has_next>,</#if></#list>) {
		${entity?uncap_first}Mapper.updateById(${entity?uncap_first});
		
		//1.先删除子表数据
		<#list cfg.subTables as sub>
		${sub.entity?uncap_first}Mapper.deleteByMainId(${entity?uncap_first}.getId());
		</#list>
		
		//2.子表数据重新插入
		<#list cfg.subTables as sub>
		for(${sub.entity} entity:${sub.entity?uncap_first}List) {
			<#list sub.foreignKeys as key>
			//外键设置
			<#if key?lower_case?index_of("${cfg.primaryKeyField}")!=-1>
			entity.set${key?cap_first}(${entity?uncap_first}.get${cfg.primaryKeyField?cap_first}());
			<#else>
			entity.set${key?cap_first}(${entity?uncap_first}.get${key}());
			</#if>
			</#list>
			${sub.entity?uncap_first}Mapper.insert(entity);
		}
		</#list>
	}

	@Override
 	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		<#list cfg.subTables as sub>
		${sub.entity?uncap_first}Mapper.deleteByMainId(id);
		</#list>
		${entity?uncap_first}Mapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			<#list cfg.subTables as sub>
			${sub.entity?uncap_first}Mapper.deleteByMainId(id.toString());
			</#list>
			${entity?uncap_first}Mapper.deleteById(id);
		}
	}
	
}
