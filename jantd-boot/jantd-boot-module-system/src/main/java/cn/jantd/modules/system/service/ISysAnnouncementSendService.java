package cn.jantd.modules.system.service;

import java.util.List;

import cn.jantd.modules.system.entity.SysAnnouncementSend;
import cn.jantd.modules.system.model.AnnouncementSendModel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户通告阅读标记表
 * @Author xiagf
 * @date 2019-07-04
 * @Version: V1.0
 */
public interface ISysAnnouncementSendService extends IService<SysAnnouncementSend> {

    /**
     * 通过userID查询用户的通知公告
     *
     * @param userId
     * @return List<String>
     */
    List<String> queryByUserId(String userId);

    /**
     * 获取我的消息
     *
     * @param page
     * @param announcementSendModel
     * @return
     */
    Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page, AnnouncementSendModel announcementSendModel);

}
