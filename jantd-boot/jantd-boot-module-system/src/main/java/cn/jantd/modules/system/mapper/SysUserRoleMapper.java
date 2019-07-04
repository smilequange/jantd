package cn.jantd.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import cn.jantd.modules.system.entity.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 通过用户名称查询角色信息
     *
     * @param username
     * @return
     */
    @Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where username=#{username}))")
    List<String> getRoleByUserName(@Param("username") String username);
}
