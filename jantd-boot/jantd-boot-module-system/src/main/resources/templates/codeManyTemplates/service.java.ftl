package ${package.Service};

<#list cfg.subTables as sub>
import ${package.Entity}.${sub.entity};
</#list>
import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: ${table.comment}服务类
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */
public interface ${table.serviceName} extends IService<${entity}> {

	/**
	 * 添加一对多
	 * 
	 */
	void saveMain(${entity} ${entity?uncap_first},<#list cfg.subTables as sub>List<${sub.entity}> ${sub.entity?uncap_first}List<#if sub_has_next>,</#if></#list>) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	void updateMain(${entity} ${entity?uncap_first},<#list cfg.subTables as sub>List<${sub.entity}> ${sub.entity?uncap_first}List<#if sub_has_next>,</#if></#list>);
	
	/**
	 * 删除一对多
	 */
	void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	void delBatchMain (Collection<? extends Serializable> idList);
	
}
