package cn.jantd.modules.system.service;


import java.util.List;

import cn.jantd.modules.system.entity.SysUser;
import cn.jantd.modules.system.entity.SysUserDepart;
import cn.jantd.modules.system.model.DepartIdModel;
import cn.jantd.modules.system.model.SysUserDepartsVO;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysUserDepartService extends IService<SysUserDepart> {

    /**
     * 根据指定用户添加部门信息
     *
     * @param sysUserDepartsVO
     * @return
     */
    boolean addSysUseWithrDepart(SysUserDepartsVO sysUserDepartsVO);

    /**
     * 根据指定用户id查询部门信息
     *
     * @param userId
     * @return
     */
    List<DepartIdModel> queryDepartIdsOfUser(String userId);

    /**
     * 根据指定用户id编辑部门信息
     *
     * @param sysUserDepartsVO
     * @return
     */
    boolean editSysUserWithDepart(SysUserDepartsVO sysUserDepartsVO);

    /**
     * 根据部门id查询用户信息
     *
     * @param depId
     * @return
     */
    List<SysUser> queryUserByDepId(String depId);
}
