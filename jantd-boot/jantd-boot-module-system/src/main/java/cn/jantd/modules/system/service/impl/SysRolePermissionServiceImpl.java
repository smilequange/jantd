package cn.jantd.modules.system.service.impl;

import java.util.*;

import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.service.ISysRolePermissionService;
import cn.jantd.modules.system.entity.SysRolePermission;
import cn.jantd.modules.system.mapper.SysRolePermissionMapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Override
    @CacheEvict(value = "loginUser_cacheRules", allEntries = true)
    public void saveRolePermission(String roleId, String permissionIds) {
        LambdaQueryWrapper<SysRolePermission> query = new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId);
        this.remove(query);
        List<SysRolePermission> list = new ArrayList<SysRolePermission>();
        String[] arr = permissionIds.split(",");
        for (String p : arr) {
            if (oConvertUtils.isNotEmpty(p)) {
                SysRolePermission rolepms = new SysRolePermission(roleId, p);
                list.add(rolepms);
            }
        }
        this.saveBatch(list);
    }

    @Override
    @CacheEvict(value = "loginUser_cacheRules", allEntries = true)
    public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds) {
        List<String> add = getDiff(lastPermissionIds, permissionIds);
        if (!add.isEmpty()) {
            List<SysRolePermission> list = new ArrayList<SysRolePermission>();
            for (String p : add) {
                if (oConvertUtils.isNotEmpty(p)) {
                    SysRolePermission rolepms = new SysRolePermission(roleId, p);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }

        List<String> delete = getDiff(permissionIds, lastPermissionIds);
        if (!delete.isEmpty()) {
            for (String permissionId : delete) {
                this.remove(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId).eq(SysRolePermission::getPermissionId, permissionId));
            }
        }
    }

    /**
     * 从diff中找出main中没有的元素
     *
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main, String diff) {
        if (oConvertUtils.isEmpty(diff)) {
            return Collections.emptyList();
        }
        if (oConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>(16);
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if (oConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }

}
