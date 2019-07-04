package cn.jantd.modules.system.service;

import cn.jantd.modules.system.entity.SysAnnouncement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 系统通告表
 * @Author xiagf
 * @date 2019-07-04
 * @Version: V1.0
 */
public interface ISysAnnouncementService extends IService<SysAnnouncement> {

    /**
     * 保存通知公告
     *
     * @param sysAnnouncement
     */
    void saveAnnouncement(SysAnnouncement sysAnnouncement);

    /**
     * 更新通知通告
     *
     * @param sysAnnouncement
     * @return boolean
     */
    boolean upDateAnnouncement(SysAnnouncement sysAnnouncement);

    /**
     * 流程执行完成保存消息通知
     *
     * @param title
     * @param msgContent
     */
    void saveSysAnnouncement(String title, String msgContent);

    /**
     * 通过userID查询
     *
     * @param page
     * @param userId
     * @param msgCategory
     * @return
     */
    Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId, String msgCategory);


}
