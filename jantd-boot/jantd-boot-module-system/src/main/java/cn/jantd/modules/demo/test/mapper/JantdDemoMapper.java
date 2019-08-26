package cn.jantd.modules.demo.test.mapper;

import java.util.List;
import cn.jantd.modules.demo.test.entity.JantdDemo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: jantd 测试demo
 * @Author xiagf
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface JantdDemoMapper extends BaseMapper<JantdDemo> {

	public List<JantdDemo> getDemoByName(@Param("name") String name);

	Integer saveBatch(@Param("objs") List<JantdDemo> list);

}
