package cn.jantd.modules.system.mapper;

import cn.jantd.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户账号查询用户信息
     *
     * @param username
     * @return SysUser
     */
    SysUser getUserByName(@Param("username") String username);

    /**
     * 根据部门Id查询用户信息
     *
     * @param page
     * @param departId
     * @param username
     * @return
     */
    IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

    /**
     * 根据角色Id查询用户信息
     *
     * @param page
     * @param roleId
     * @param username
     * @return
     */
    IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param orgCode
     */
    void updateUserDepart(@Param("username") String username, @Param("orgCode") String orgCode);
}
