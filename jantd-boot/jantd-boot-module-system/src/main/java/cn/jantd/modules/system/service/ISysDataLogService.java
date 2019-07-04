package cn.jantd.modules.system.service;

import cn.jantd.modules.system.entity.SysDataLog;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysDataLogService extends IService<SysDataLog> {

    /**
     * 添加数据日志
     *
     * @param tableName
     * @param dataId
     * @param dataContent
     */
    void addDataLog(String tableName, String dataId, String dataContent);

}
