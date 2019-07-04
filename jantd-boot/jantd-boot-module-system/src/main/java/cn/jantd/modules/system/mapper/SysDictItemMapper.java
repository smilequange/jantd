package cn.jantd.modules.system.mapper;

import cn.jantd.modules.system.entity.SysDictItem;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {
    /**
     * 查询字典属相
     *
     * @param mainId
     * @return
     */
    @Select("SELECT * FROM SYS_DICT_ITEM WHERE DICT_ID = #{mainId}")
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
