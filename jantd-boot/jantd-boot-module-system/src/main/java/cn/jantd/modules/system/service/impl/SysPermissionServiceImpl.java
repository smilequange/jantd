package cn.jantd.modules.system.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.jantd.core.constant.CommonConstant;
import cn.jantd.core.exception.JantdBootException;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysPermissionDataRule;
import cn.jantd.modules.system.service.ISysPermissionDataRuleService;
import cn.jantd.modules.system.entity.SysPermission;
import cn.jantd.modules.system.mapper.SysPermissionMapper;
import cn.jantd.modules.system.model.TreeModel;
import cn.jantd.modules.system.service.ISysPermissionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Resource
    private ISysPermissionDataRuleService permissionDataRuleService;

    @Override
    public List<TreeModel> queryListByParentId(String parentId) {
        return sysPermissionMapper.queryListByParentId(parentId);
    }

    /**
     * 真实删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "permission", allEntries = true)
    public void deletePermission(String id) {
        SysPermission sysPermission = this.getById(id);
        if (sysPermission == null) {
            throw new JantdBootException("未找到菜单信息");
        }
        String pid = sysPermission.getParentId();
        int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
        if (count == 1) {
            //若父节点无其他子节点，则该父节点是叶子节点
            this.sysPermissionMapper.setMenuLeaf(pid, 1);
        }
        sysPermissionMapper.deleteById(id);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        this.removeChildrenBy(sysPermission.getId());
    }

    /**
     * 根据父id删除其关联的子节点数据
     *
     * @return
     */
    public void removeChildrenBy(String parentId) {
        LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<>();
        // 封装查询条件parentId为主键,
        query.eq(SysPermission::getParentId, parentId);
        // 查出该主键下的所有子级
        List<SysPermission> permissionList = this.list(query);
        if (!permissionList.isEmpty()) {
            // id
            String id = "";
            // 查出的子级数量
            int num = 0;
            // 如果查出的集合不为空, 则先删除所有
            this.remove(query);
            // 再遍历刚才查出的集合, 根据每个对象,查找其是否仍有子级
            for (int i = 0, len = permissionList.size(); i < len; i++) {
                id = permissionList.get(i).getId();
                num = this.count(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getParentId, id));
                // 如果有, 则递归
                if (num > 0) {
                    this.removeChildrenBy(id);
                }
            }
        }
    }

    /**
     * 逻辑删除
     */
    @Override
    @CacheEvict(value = "permission", allEntries = true)
    public void deletePermissionLogical(String id) {
        SysPermission sysPermission = this.getById(id);
        if (sysPermission == null) {
            throw new JantdBootException("未找到菜单信息");
        }
        String pid = sysPermission.getParentId();
        int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
        if (count == 1) {
            //若父节点无其他子节点，则该父节点是叶子节点
            this.sysPermissionMapper.setMenuLeaf(pid, 1);
        }
        sysPermission.setDelFlag(1);
        this.updateById(sysPermission);
    }

    @Override
    @CacheEvict(value = "permission", allEntries = true)
    public void addPermission(SysPermission sysPermission) {
        //----------------------------------------------------------------------
        //判断是否是一级菜单，是的话清空父菜单
        if (CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
            sysPermission.setParentId(null);
        }
        //----------------------------------------------------------------------
        String pid = sysPermission.getParentId();
        if (oConvertUtils.isNotEmpty(pid)) {
            //设置父节点不为叶子节点
            this.sysPermissionMapper.setMenuLeaf(pid, 0);
        }
        sysPermission.setCreateTime(new Date());
        sysPermission.setDelFlag(0);
        sysPermission.setLeaf(true);
        this.save(sysPermission);
    }

    @Override
    @CacheEvict(value = "permission", allEntries = true)
    public void editPermission(SysPermission sysPermission) {
        SysPermission p = this.getById(sysPermission.getId());
        if (p == null) {
            throw new JantdBootException("未找到菜单信息");
        } else {
            sysPermission.setUpdateTime(new Date());
            //----------------------------------------------------------------------
            //Step1.判断是否是一级菜单，是的话清空父菜单ID
            if (CommonConstant.MENU_TYPE_0.equals(sysPermission.getMenuType())) {
                sysPermission.setParentId("");
            }
            //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
            int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, sysPermission.getId()));
            if (count == 0) {
                sysPermission.setLeaf(true);
            }
            //----------------------------------------------------------------------
            this.updateById(sysPermission);

            //如果当前菜单的父菜单变了，则需要修改新父菜单和老父菜单的，叶子节点状态
            String pid = sysPermission.getParentId();
            Boolean flag = (oConvertUtils.isNotEmpty(pid) && !pid.equals(p.getParentId())) || oConvertUtils.isEmpty(pid) && oConvertUtils.isNotEmpty(p.getParentId());
            if (flag) {
                //a.设置新的父菜单不为叶子节点
                this.sysPermissionMapper.setMenuLeaf(pid, 0);
                //b.判断老的菜单下是否还有其他子菜单，没有的话则设置为叶子节点
                int cc = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, p.getParentId()));
                if (cc == 0) {
                    if (oConvertUtils.isNotEmpty(p.getParentId())) {
                        this.sysPermissionMapper.setMenuLeaf(p.getParentId(), 1);
                    }
                }

            }
        }

    }

    @Override
    public List<SysPermission> queryByUser(String username) {
        return this.sysPermissionMapper.queryByUser(username);
    }

    /**
     * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
     */
    @Override
    public void deletePermRuleByPermId(String id) {
        LambdaQueryWrapper<SysPermissionDataRule> query = new LambdaQueryWrapper<>();
        query.eq(SysPermissionDataRule::getPermissionId, id);
        int countValue = this.permissionDataRuleService.count(query);
        if (countValue > 0) {
            this.permissionDataRuleService.remove(query);
        }
    }

    /**
     * 获取模糊匹配规则的数据权限URL
     */
    @Override
    @Cacheable(value = "permission")
    public List<String> queryPermissionUrlWithStar() {
        return this.baseMapper.queryPermissionUrlWithStar();
    }

}
