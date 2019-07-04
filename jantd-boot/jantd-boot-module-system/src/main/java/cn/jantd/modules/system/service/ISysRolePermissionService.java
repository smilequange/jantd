package cn.jantd.modules.system.service;

import cn.jantd.modules.system.entity.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 保存授权/先删后增
     *
     * @param roleId
     * @param permissionIds
     */
    void saveRolePermission(String roleId, String permissionIds);

    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     *
     * @param roleId
     * @param permissionIds
     * @param lastPermissionIds
     */
    void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds);

}
