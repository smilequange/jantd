package cn.jantd.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import cn.jantd.modules.system.entity.SysAnnouncementSend;
import cn.jantd.modules.system.model.AnnouncementSendModel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 用户通告阅读标记表
 * @Author xiagf
 * @date 2019-07-04
 * @Version: V1.0
 */
public interface SysAnnouncementSendMapper extends BaseMapper<SysAnnouncementSend> {

    /**
     * 通过用户id获取通告ID
     *
     * @param userId
     * @return
     */
    List<String> queryByUserId(@Param("userId") String userId);

    /**
     * 获取我的消息
     *
     * @param page
     * @param announcementSendModel
     * @return
     */
    List<AnnouncementSendModel> getMyAnnouncementSendList(Page<AnnouncementSendModel> page, @Param("announcementSendModel") AnnouncementSendModel announcementSendModel);

}
