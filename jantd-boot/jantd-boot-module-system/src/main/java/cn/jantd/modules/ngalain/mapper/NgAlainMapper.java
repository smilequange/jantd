package cn.jantd.modules.ngalain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xiagf
 * @date 2019-07-04
 */
public interface NgAlainMapper extends BaseMapper {
    /**
     * 动态获取表数据
     *
     * @param table
     * @param key
     * @param value
     * @return
     */
    List<Map<String, String>> getDictByTable(@Param("table") String table, @Param("key") String key, @Param("value") String value);

}
