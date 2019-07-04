package cn.jantd.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import cn.jantd.modules.system.entity.SysPermission;
import cn.jantd.modules.system.model.TreeModel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 通过父菜单ID查询子菜单
     *
     * @param parentId
     * @return
     */
    List<TreeModel> queryListByParentId(@Param("parentId") String parentId);

    /**
     * 根据用户查询用户权限
     *
     * @param username
     * @return
     */
    List<SysPermission> queryByUser(@Param("username") String username);

    /**
     * 修改菜单状态字段： 是否子节点
     *
     * @param id
     * @param leaf
     * @return
     */
    @Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
    int setMenuLeaf(@Param("id") String id, @Param("leaf") int leaf);

    /**
     * 获取模糊匹配规则的数据权限URL
     *
     * @return
     */
    @Select("SELECT url FROM sys_permission WHERE del_flag = 0 and menu_type = 2 and url like '%*%'")
    List<String> queryPermissionUrlWithStar();

}
