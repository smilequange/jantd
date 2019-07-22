package cn.jantd.modules.system.service.impl;

import java.util.*;

import cn.jantd.core.util.YouBianCodeUtil;
import cn.jantd.modules.system.entity.SysDepart;
import cn.jantd.modules.system.mapper.SysDepartMapper;
import cn.jantd.modules.system.model.SysDepartTreeModel;
import cn.jantd.modules.system.service.ISysDepartService;
import cn.jantd.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.netty.util.internal.StringUtil;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

    /**
     * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
     */
    @Override
    public List<SysDepartTreeModel> queryTreeList() {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getDelFlag, 0);
        query.orderByAsc(SysDepart::getDepartOrder);
        List<SysDepart> list = this.list(query);
        // 调用wrapTreeDataToTreeList方法生成树状数据
        return FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
    }

    /**
     * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDepartData(SysDepart sysDepart, String username) {
        if (sysDepart != null && username != null) {
            if (sysDepart.getParentId() == null) {
                sysDepart.setParentId("");
            }
            String s = UUID.randomUUID().toString().replace("-", "");
            sysDepart.setId(s);
            // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
            // 获取父级ID
            String parentId = sysDepart.getParentId();
            String[] codeArray = generateOrgCode(parentId);
            sysDepart.setOrgCode(codeArray[0]);
            String orgType = codeArray[1];
            sysDepart.setOrgType(String.valueOf(orgType));
            sysDepart.setCreateTime(new Date());
            sysDepart.setDelFlag("0");
            this.save(sysDepart);
        }

    }

    /**
     * saveDepartData 的调用方法,生成部门编码和部门类型
     *
     * @param parentId
     * @return
     */
    private String[] generateOrgCode(String parentId) {
        //update-begin--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        LambdaQueryWrapper<SysDepart> query1 = new LambdaQueryWrapper<SysDepart>();
        String[] strArray = new String[2];
        // 创建一个List集合,存储查询返回的所有SysDepart对象
        List<SysDepart> departList;
        // 定义新编码字符串
        String newOrgCode = "";
        // 定义旧编码字符串
        String oldOrgCode = "";
        // 定义部门类型
        String orgType = "";
        // 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
        if (StringUtil.isNullOrEmpty(parentId)) {
            // 线判断数据库中的表是否为空,空则直接返回初始编码
            query1.eq(SysDepart::getParentId, "");
            query1.orderByDesc(SysDepart::getOrgCode);
            departList = this.list(query1);
            if (departList.isEmpty()) {
                strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
                strArray[1] = "1";
                return strArray;
            } else {
                SysDepart depart = departList.get(0);
                oldOrgCode = depart.getOrgCode();
                orgType = depart.getOrgType();
                newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
            }
        } else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
            // 封装查询同级的条件
            query.eq(SysDepart::getParentId, parentId);
            // 降序排序
            query.orderByDesc(SysDepart::getOrgCode);
            // 查询出同级部门的集合
            List<SysDepart> parentList = this.list(query);
            // 查询出父级部门
            SysDepart depart = this.getById(parentId);
            // 获取父级部门的Code
            String parentCode = depart.getOrgCode();
            // 根据父级部门类型算出当前部门的类型
            orgType = String.valueOf(Integer.valueOf(depart.getOrgType()) + 1);
            // 处理同级部门为null的情况
            if (parentList.isEmpty()) {
                // 直接生成当前的部门编码并返回
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
            } else { //处理有同级部门的情况
                // 获取同级部门的编码,利用工具类
                String subCode = parentList.get(0).getOrgCode();
                // 返回生成的当前部门编码
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
            }
        }
        // 返回最终封装了部门编码和部门类型的数组
        strArray[0] = newOrgCode;
        strArray[1] = orgType;
        return strArray;
        //update-end--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
    }

    /**
     * updateDepartDataById 对应 edit 根据部门主键来更新对应的部门数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDepartDataById(SysDepart sysDepart, String username) {
        if (sysDepart != null && username != null) {
            sysDepart.setUpdateTime(new Date());
            sysDepart.setUpdateBy(username);
            this.updateById(sysDepart);
            return true;
        } else {
            return false;
        }

    }


    /**
     * <p>
     * 根据关键字搜索相关的部门数据
     * </p>
     */
    @Override
    public List<SysDepartTreeModel> searhBy(String keyWord) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.like(SysDepart::getDepartName, keyWord);
        //update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
        SysDepartTreeModel model;
        List<SysDepart> departList = this.list(query);
        List<SysDepartTreeModel> newList = new ArrayList<>();
        if (!departList.isEmpty()) {
            for (SysDepart depart : departList) {
                model = new SysDepartTreeModel(depart);
                model.setChildren(null);
                //update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
                newList.add(model);
            }
            return newList;
        }
        return Collections.emptyList();
    }

    /**
     * 根据部门id删除并且删除其可能存在的子级任何部门
     */
    @Override
    public boolean delete(String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        this.checkChildrenExists(id, idList);
        return this.removeByIds(idList);
    }

    /**
     * delete 方法调用
     *
     * @param id
     * @param idList
     */
    private void checkChildrenExists(String id, List<String> idList) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        query.eq(SysDepart::getParentId, id);
        List<SysDepart> departList = this.list(query);
        if (!departList.isEmpty()) {
            for (SysDepart depart : departList) {
                idList.add(depart.getId());
                this.checkChildrenExists(depart.getId(), idList);
            }
        }
    }

    @Override
    public List<SysDepart> queryUserDeparts(String userId) {
        return baseMapper.queryUserDeparts(userId);
    }


}
