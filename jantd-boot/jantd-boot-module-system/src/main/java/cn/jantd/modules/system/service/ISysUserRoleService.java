package cn.jantd.modules.system.service;

import java.util.Map;

import cn.jantd.modules.system.entity.SysUserRole;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 查询所有的用户角色信息
     *
     * @return
     */
    Map<String, String> queryUserRole();
}
