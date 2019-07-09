package cn.jantd.modules.system.service;

import java.util.List;
import java.util.Set;

import cn.jantd.core.api.vo.Result;
import cn.jantd.core.system.vo.SysUserCacheInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.jantd.modules.system.entity.SysUser;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 通过用户名称查询用户信息
     *
     * @param username
     * @return
     */
    SysUser getUserByName(String username);

    /**
     * 添加用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    void addUserWithRole(SysUser user, String roles);


    /**
     * 修改用户和用户角色关系
     *
     * @param user
     * @param roles
     */
    void editUserWithRole(SysUser user, String roles);

    /**
     * 获取用户的授权角色
     *
     * @param username
     * @return
     */
    List<String> getRole(String username);

    /**
     * 查询用户信息包括 部门信息
     *
     * @param username
     * @return
     */
    SysUserCacheInfo getCacheUser(String username);

    /**
     * 根据部门Id查询
     *
     * @param page
     * @param departId
     * @param username
     * @return
     */
    IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

    /**
     * 根据角色Id查询
     *
     * @param page
     * @param roleId
     * @param username
     * @return
     */
    IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username
     * @return
     */
    Set<String> getUserRolesSet(String username);

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    Set<String> getUserPermissionsSet(String username);

    /**
     * 根据用户名设置部门ID
     *
     * @param username
     * @param orgCode
     */
    void updateUserDepart(String username, String orgCode);

    /**
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    Result checkUserIsEffective(SysUser sysUser);
}
