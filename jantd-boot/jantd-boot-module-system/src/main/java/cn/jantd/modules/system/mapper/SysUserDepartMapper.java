package cn.jantd.modules.system.mapper;

import java.util.List;

import cn.jantd.modules.system.entity.SysUserDepart;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart> {

    /**
     * 通过userId查询用户部门信息
     * @param userId
     * @return
     */
    List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);
}
