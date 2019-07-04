package cn.jantd.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import cn.jantd.modules.system.entity.SysAnnouncement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 系统通告表
 * @Author xiagf
 * @date 2019-07-04
 * @Version: V1.0
 */
public interface SysAnnouncementMapper extends BaseMapper<SysAnnouncement> {

    /**
     * 查询系统消息通过用户Id
     *
     * @param page
     * @param userId
     * @param msgCategory
     * @return
     */
    List<SysAnnouncement> querySysCementListByUserId(Page<SysAnnouncement> page, @Param("userId") String userId, @Param("msgCategory") String msgCategory);

}
