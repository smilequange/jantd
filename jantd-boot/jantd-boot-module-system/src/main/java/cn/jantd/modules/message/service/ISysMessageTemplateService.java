package cn.jantd.modules.message.service;

import java.util.List;

import cn.jantd.core.system.base.service.BaseService;
import cn.jantd.modules.message.entity.SysMessageTemplate;

/**
 * @Description: 消息模板
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageTemplateService extends BaseService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
