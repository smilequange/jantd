package cn.jantd.modules.system.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import cn.jantd.modules.system.service.ISysLogService;
import cn.jantd.modules.system.entity.SysLog;
import cn.jantd.modules.system.mapper.SysLogMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-26
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

	@Resource
	private SysLogMapper sysLogMapper;

	/**
	 * @功能：清空所有日志记录
	 */
	@Override
	public void removeAll() {
		sysLogMapper.removeAll();
	}

	@Override
	public Long findTotalVisitCount() {
		return sysLogMapper.findTotalVisitCount();
	}

	//update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
	@Override
	public Long findTodayVisitCount(Date dayStart, Date dayEnd) {
		return sysLogMapper.findTodayVisitCount(dayStart,dayEnd);
	}

	@Override
	public Long findTodayIp(Date dayStart, Date dayEnd) {
		return sysLogMapper.findTodayIp(dayStart,dayEnd);
	}
	//update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
}
