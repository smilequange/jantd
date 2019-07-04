package cn.jantd.modules.system.mapper;

import cn.jantd.modules.system.entity.SysDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户ID查询部门集合
     *
     * @param userId
     * @return
     */
    public List<SysDepart> queryUserDeparts(@Param("userId") String userId);
}
