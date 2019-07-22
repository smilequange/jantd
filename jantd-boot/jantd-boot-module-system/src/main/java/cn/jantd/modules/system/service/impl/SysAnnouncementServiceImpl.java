package cn.jantd.modules.system.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.jantd.core.constant.CommonConstant;
import cn.jantd.modules.system.service.ISysAnnouncementService;
import cn.jantd.modules.system.entity.SysAnnouncement;
import cn.jantd.modules.system.entity.SysAnnouncementSend;
import cn.jantd.modules.system.mapper.SysAnnouncementMapper;
import cn.jantd.modules.system.mapper.SysAnnouncementSendMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 系统通告表
 * @Author xiagf
 * @date 2019-07-04
 * @Version: V1.0
 */
@Service
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {

    @Resource
    private SysAnnouncementMapper sysAnnouncementMapper;

    @Resource
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAnnouncement(SysAnnouncement sysAnnouncement) {
        if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
            sysAnnouncementMapper.insert(sysAnnouncement);
        } else {
            // 1.插入通告表记录
            sysAnnouncementMapper.insert(sysAnnouncement);
            // 2.插入用户通告阅读标记表记录
            String userId = sysAnnouncement.getUserIds();
            String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
            String anntId = sysAnnouncement.getId();
            Date refDate = new Date();
            for (int i = 0; i < userIds.length; i++) {
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(userIds[i]);
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                announcementSend.setReadTime(refDate);
                sysAnnouncementSendMapper.insert(announcementSend);
            }
        }
    }

    /**
     * @功能：编辑消息信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean upDateAnnouncement(SysAnnouncement sysAnnouncement) {
        // 1.更新系统信息表数据
        sysAnnouncementMapper.updateById(sysAnnouncement);
        if (sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_UESR)) {
            // 2.补充新的通知用户数据
            String userId = sysAnnouncement.getUserIds();
            String[] userIds = userId.substring(0, (userId.length() - 1)).split(",");
            String anntId = sysAnnouncement.getId();
            Date refDate = new Date();
            for (int i = 0; i < userIds.length; i++) {
                LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
                queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
                queryWrapper.eq(SysAnnouncementSend::getUserId, userIds[i]);
                List<SysAnnouncementSend> announcementSends = sysAnnouncementSendMapper.selectList(queryWrapper);
                if (announcementSends.isEmpty()) {
                    SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                    announcementSend.setAnntId(anntId);
                    announcementSend.setUserId(userIds[i]);
                    announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                    announcementSend.setReadTime(refDate);
                    sysAnnouncementSendMapper.insert(announcementSend);
                }
            }
            // 3. 删除多余通知用户数据
            Collection<String> delUserIds = Arrays.asList(userIds);
            LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
            queryWrapper.notIn(SysAnnouncementSend::getUserId, delUserIds);
            queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
            sysAnnouncementSendMapper.delete(queryWrapper);
        }
        return true;
    }

    /**
     * 流程执行完成保存消息通知
     *
     * @param title
     * @param msgContent
     */
    @Override
    public void saveSysAnnouncement(String title, String msgContent) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender("共享平台");
        announcement.setPriority(CommonConstant.PRIORITY_L);
        announcement.setMsgType(CommonConstant.MSG_TYPE_ALL);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_NO));
        sysAnnouncementMapper.insert(announcement);
    }

    @Override
    public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId, String msgCategory) {
        return page.setRecords(sysAnnouncementMapper.querySysCementListByUserId(page, userId, msgCategory));
    }

}
