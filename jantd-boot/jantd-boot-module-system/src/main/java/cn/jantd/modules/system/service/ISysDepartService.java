package cn.jantd.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.jantd.modules.system.entity.SysDepart;
import cn.jantd.modules.system.model.SysDepartTreeModel;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysDepartService extends IService<SysDepart> {


    /**
     * 查询所有部门信息,并分节点进行显示
     *
     * @return
     */
    List<SysDepartTreeModel> queryTreeList();

    /**
     * 保存部门数据
     *
     * @param sysDepart
     * @param username
     */
    void saveDepartData(SysDepart sysDepart, String username);

    /**
     * 更新depart数据
     *
     * @param sysDepart
     * @param username
     * @return
     */
    Boolean updateDepartDataById(SysDepart sysDepart, String username);

    /**
     * 根据关键字搜索相关的部门数据
     *
     * @param keyWord
     * @return
     */
    List<SysDepartTreeModel> searhBy(String keyWord);

    /**
     * 根据部门id删除并删除其可能存在的子级部门
     *
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 查询SysDepart集合
     *
     * @param userId
     * @return
     */
    List<SysDepart> queryUserDeparts(String userId);

}
