package cn.jantd.modules.system.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.jantd.modules.system.entity.SysLog;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * 清空所有日志记录
     */
    void removeAll();

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    /**
     * 获取系统今日访问次数
     *
     * @param dayStart
     * @param dayEnd
     * @return
     */
    Long findTodayVisitCount(Date dayStart, Date dayEnd);

    /**
     * 获取系统今日访问 IP数
     *
     * @param dayStart
     * @param dayEnd
     * @return
     */
    Long findTodayIp(Date dayStart, Date dayEnd);

    /**
     * 首页：根据时间统计访问数量/ip数量
     *
     * @param dayStart
     * @param dayEnd
     * @return
     */
    List<Map<String, Object>> findVisitCount(Date dayStart, Date dayEnd);
}
