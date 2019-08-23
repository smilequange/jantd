<#list cfg.subTables as subTab>
#segment#I${subTab.entity}Service.java
package ${package.Service};

import ${package.Entity}.${subTab.entity};
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: ${subTab.ftlDescription}
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */
public interface I${subTab.entity}Service extends IService<${subTab.entity}> {

    List<${subTab.entity}> selectByMainId(String id);
}

</#list>
