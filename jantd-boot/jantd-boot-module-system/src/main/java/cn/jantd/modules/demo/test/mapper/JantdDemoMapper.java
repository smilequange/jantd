package cn.jantd.modules.demo.test.mapper;

import java.util.List;
import cn.jantd.modules.demo.test.entity.JantdDemo;
import org.springframework.data.repository.query.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: jantd 测试demo
 * @Author xiagf
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface JantdDemoMapper extends BaseMapper<JantdDemo> {

	public List<JantdDemo> getDemoByName(@Param("name") String name);

}
