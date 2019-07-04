package cn.jantd.modules.system.mapper;

import org.apache.ibatis.annotations.Param;
import cn.jantd.modules.system.entity.SysDataLog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Author xiagf
 * @date 2019-07-04
 */
public interface SysDataLogMapper extends BaseMapper<SysDataLog>{
	/**
	 * 通过表名及数据Id获取最大版本
	 * @param tableName
	 * @param dataId
	 * @return
	 */
	public String queryMaxDataVer(@Param("tableName") String tableName,@Param("dataId") String dataId);

}
