<#list cfg.subTables as subTab>
#segment#${subTab.entity}ServiceImpl.java
package ${package.ServiceImpl};

import ${package.Entity}.${subTab.entity};
import ${package.Mapper}.${subTab.entity}Mapper;
import ${package.Service}.I${subTab.entity}Service;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: ${subTab.ftlDescription}
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */


@Service
public class ${subTab.entity}ServiceImpl extends ServiceImpl<${subTab.entity}Mapper, ${subTab.entity}> implements I${subTab.entity}Service {
	
	@Autowired
	private ${subTab.entity}Mapper ${subTab.entity?uncap_first}Mapper;
	
	@Override
	public List<${subTab.entity}> selectByMainId(String mainId) {
		return ${subTab.entity?uncap_first}Mapper.selectByMainId(mainId);
	}
}
</#list>