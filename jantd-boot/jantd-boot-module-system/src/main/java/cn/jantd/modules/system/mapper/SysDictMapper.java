package cn.jantd.modules.system.mapper;

import java.util.List;

import cn.jantd.core.system.vo.DictModel;
import org.apache.ibatis.annotations.Param;
import cn.jantd.modules.system.entity.SysDict;
import cn.jantd.modules.system.model.DuplicateCheckVo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * 重复检查SQL
     *
     * @param duplicateCheckVo
     * @return
     */
    Long duplicateCheckCountSql(DuplicateCheckVo duplicateCheckVo);

    /**
     * 重复检查SQL
     *
     * @param duplicateCheckVo
     * @return
     */
    Long duplicateCheckCountSqlNoDataId(DuplicateCheckVo duplicateCheckVo);

    /**
     * 通过字典code获取字典数据
     *
     * @param code
     * @return
     */
    List<DictModel> queryDictItemsByCode(@Param("code") String code);

    /**
     * 通过查询指定table的 text code 获取字典
     *
     * @param table
     * @param text
     * @param code
     * @return
     */
    List<DictModel> queryTableDictItemsByCode(@Param("table") String table, @Param("text") String text, @Param("code") String code);

    /**
     * 通过字典code获取字典数据
     *
     * @param code
     * @param key
     * @return
     */
    String queryDictTextByKey(@Param("code") String code, @Param("key") String key);

    /**
     * 通过查询指定table的 text code key 获取字典值
     *
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    String queryTableDictTextByKey(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("key") String key);


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
