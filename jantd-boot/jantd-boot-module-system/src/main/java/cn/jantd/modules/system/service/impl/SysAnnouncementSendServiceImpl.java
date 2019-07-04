package cn.jantd.modules.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.jantd.modules.system.service.ISysAnnouncementSendService;
import cn.jantd.modules.system.entity.SysAnnouncementSend;
import cn.jantd.modules.system.mapper.SysAnnouncementSendMapper;
import cn.jantd.modules.system.model.AnnouncementSendModel;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户通告阅读标记表
 * @Author xiagf
 * @date 2019-07-04
 * @Version: V1.0
 */
@Service
public class SysAnnouncementSendServiceImpl extends ServiceImpl<SysAnnouncementSendMapper, SysAnnouncementSend> implements ISysAnnouncementSendService {

    @Resource
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    @Override
    public List<String> queryByUserId(String userId) {
        return sysAnnouncementSendMapper.queryByUserId(userId);
    }

    @Override
    public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,
                                                                 AnnouncementSendModel announcementSendModel) {
        return page.setRecords(sysAnnouncementSendMapper.getMyAnnouncementSendList(page, announcementSendModel));
    }

}
