<#list cfg.subTables as subTab>
#segment#${subTab.entity}Mapper.java
package ${package.Mapper};

import java.util.List;
import ${package.Entity}.${subTab.entity};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: ${subTab.ftlDescription}
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */
public interface ${subTab.entity}Mapper extends BaseMapper<${subTab.entity}> {

    boolean deleteByMainId(String id);
    
    List<${subTab.entity}> selectByMainId(String id);
}

</#list>