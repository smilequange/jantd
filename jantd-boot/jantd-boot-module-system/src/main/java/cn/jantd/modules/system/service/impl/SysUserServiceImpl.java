package cn.jantd.modules.system.service.impl;

import cn.jantd.core.api.vo.Result;
import cn.jantd.core.constant.CommonConstant;
import cn.jantd.core.system.api.SystemBaseApi;
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.core.system.vo.SysUserCacheInfo;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysDepart;
import cn.jantd.modules.system.entity.SysPermission;
import cn.jantd.modules.system.entity.SysUser;
import cn.jantd.modules.system.entity.SysUserRole;
import cn.jantd.modules.system.mapper.SysDepartMapper;
import cn.jantd.modules.system.mapper.SysPermissionMapper;
import cn.jantd.modules.system.mapper.SysUserMapper;
import cn.jantd.modules.system.mapper.SysUserRoleMapper;
import cn.jantd.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SystemBaseApi systemBaseAPI;
    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private SystemBaseApi sysBaseAPI;

    @Override
    public SysUser getUserByName(String username) {
        return userMapper.getUserByName(username);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserWithRole(SysUser user, String roles) {
        this.save(user);
        if (oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @CacheEvict(value = "loginUser_cacheRules", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void editUserWithRole(SysUser user, String roles) {
        this.updateById(user);
        //先删后加
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
        if (oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }


    @Override
    public List<String> getRole(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    @Override
    @Cacheable(value = "loginUser_cacheRules", key = "'Roles_'+#username")
    public Set<String> getUserRolesSet(String username) {
        // 查询用户拥有的角色集合
        List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
        log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
        return new HashSet<>(roles);
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    @Override
    @Cacheable(value = "loginUser_cacheRules", key = "'Permissions_'+#username")
    public Set<String> getUserPermissionsSet(String username) {
        Set<String> permissionSet = new HashSet<>();
        List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
        for (SysPermission po : permissionList) {
            if (oConvertUtils.isNotEmpty(po.getPerms())) {
                permissionSet.add(po.getPerms());
            }
        }
        log.info("-------通过数据库读取用户拥有的权限Perms------username： " + username + ",Perms size: " + (permissionSet == null ? 0 : permissionSet.size()));
        return permissionSet;
    }

    @Override
    public SysUserCacheInfo getCacheUser(String username) {
        SysUserCacheInfo info = new SysUserCacheInfo();
        info.setOneDepart(true);
        LoginUser user = systemBaseAPI.getUserByName(username);
        if (user != null) {
            info.setSysUserCode(user.getUsername());
            info.setSysUserName(user.getRealname());
            info.setSysOrgCode(user.getOrgCode());
        }

        //多部门支持in查询
        List<SysDepart> list = sysDepartMapper.queryUserDeparts(user.getId());
        List<String> sysMultiOrgCode = new ArrayList<String>();
        if (list.isEmpty()) {
        } else if (list.size() == 1) {
            sysMultiOrgCode.add(list.get(0).getOrgCode());
        } else {
            info.setOneDepart(false);
            for (SysDepart dpt : list) {
                sysMultiOrgCode.add(dpt.getOrgCode());
            }
        }
        info.setSysMultiOrgCode(sysMultiOrgCode);

        return info;
    }

    /**
     * 根据部门Id查询
     *
     * @param page
     * @param departId
     * @param username
     * @return
     */
    @Override
    public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username) {
        return userMapper.getUserByDepId(page, departId, username);
    }


    /**
     * 根据角色Id查询
     *
     * @param page
     * @param roleId
     * @param username
     * @return
     */
    @Override
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
        return userMapper.getUserByRoleId(page, roleId, username);
    }


    @Override
    public void updateUserDepart(String username, String orgCode) {
        baseMapper.updateUserDepart(username, orgCode);
    }

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(SysUser sysUser) {
        Result<?> result = new Result<Object>();
        // 情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在，请注册");
            sysBaseAPI.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_LOGIN, null, sysUser.getUsername(), sysUser.getRealname());
            return result;
        }
        // 情况2：根据用户信息查询，该用户已注销
        if (CommonConstant.DEL_FLAG_YES.toString().equals(sysUser.getDelFlag())) {
            sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_LOGIN, null, sysUser.getUsername(), sysUser.getRealname());
            result.error500("该用户已注销");
            return result;
        }
        // 情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
            sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_LOGIN, null, sysUser.getUsername(), sysUser.getRealname());
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }


}
