package cn.jantd.modules.system.service;

import java.util.List;

import cn.jantd.core.system.vo.DictModel;
import cn.jantd.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jantd.modules.system.entity.SysDictItem;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 通过查询指定code 获取字典
     *
     * @param code
     * @return
     */
    List<DictModel> queryDictItemsByCode(String code);

    /**
     * 通过查询指定table的 text code 获取字典
     *
     * @param table
     * @param text
     * @param code
     * @return
     */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 通过查询指定code 获取字典值text
     *
     * @param code
     * @param key
     * @return
     */
    String queryDictTextByKey(String code, String key);

    /**
     * 通过查询指定table的 text code 获取字典值text
     *
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    String queryTableDictTextByKey(String table, String text, String code, String key);

    /**
     * 根据字典类型删除关联表中其对应的数据
     *
     * @param sysDict
     * @return
     */
    boolean deleteByDictId(SysDict sysDict);

    /**
     * 添加一对多
     *
     * @param sysDict
     * @param sysDictItemList
     */
    void saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     *
     * @return
     */
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 查询所有用户  作为字典信息 username -->value,realname -->text
     *
     * @return
     */
    List<DictModel> queryAllUserBackDictModel();

}
