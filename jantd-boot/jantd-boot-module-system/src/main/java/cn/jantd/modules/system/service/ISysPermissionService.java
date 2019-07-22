package cn.jantd.modules.system.service;

import java.util.List;

import cn.jantd.core.exception.JantdBootException;
import cn.jantd.modules.system.entity.SysPermission;
import cn.jantd.modules.system.model.TreeModel;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 通过父id查询菜单
     *
     * @param parentId
     * @return
     */
    List<TreeModel> queryListByParentId(String parentId);

    /**
     * 真实删除
     *
     * @param id
     * @throws JantdBootException
     */
    void deletePermission(String id);

    /**
     * 逻辑删除
     *
     * @param id
     * @throws JantdBootException
     */
    void deletePermissionLogical(String id);

    /**
     * 添加菜单
     *
     * @param sysPermission
     * @throws JantdBootException
     */
    void addPermission(SysPermission sysPermission);

    /**
     * 编辑菜单
     *
     * @param sysPermission
     * @throws JantdBootException
     */
    void editPermission(SysPermission sysPermission);

    /**
     * 通过用户名查询菜单
     *
     * @param username
     * @return
     */
    List<SysPermission> queryByUser(String username);

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     *
     * @param id
     * @return
     */
    void deletePermRuleByPermId(String id);

    /**
     * 查询出带有特殊符号的菜单地址的集合
     *
     * @return
     */
    List<String> queryPermissionUrlWithStar();
}
