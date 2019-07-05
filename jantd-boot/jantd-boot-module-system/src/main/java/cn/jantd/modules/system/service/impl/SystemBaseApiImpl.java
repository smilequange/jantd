package cn.jantd.modules.system.service.impl;

import cn.jantd.core.constant.CommonConstant;
import cn.jantd.core.constant.SystemConstant;
import cn.jantd.core.exception.JantdBootException;
import cn.jantd.core.system.api.SystemBaseApi;
import cn.jantd.core.system.vo.DictModel;
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.core.util.IPUtils;
import cn.jantd.core.util.SpringContextUtils;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysAnnouncement;
import cn.jantd.modules.system.entity.SysAnnouncementSend;
import cn.jantd.modules.system.entity.SysLog;
import cn.jantd.modules.system.entity.SysUser;
import cn.jantd.modules.system.mapper.*;
import cn.jantd.modules.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author xiagf
 * @date 2019-07-04
 * @Version:V1.0
 */
@Slf4j
@Service
public class SystemBaseApiImpl implements SystemBaseApi {
    public static final String DB_TYPE_MYSQL = "MYSQL";
    public static final String DB_TYPE_ORACLE = "ORACLE";
    public static final String DB_TYPE_POSTGRESQL = "POSTGRESQL";
    public static final String DB_TYPE_SQLSERVER = "SQLSERVER";
    public static String DB_TYPE = "";

    @Resource
    private SysLogMapper sysLogMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ISysDictService sysDictService;
    @Resource
    private SysAnnouncementMapper sysAnnouncementMapper;
    @Resource
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    @Override
    public void addLog(String logContent, Integer logType, Integer operateType, String username, String realName) {
        SysLog sysLog = new SysLog();
        //注解上的描述,操作日志内容
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operateType);

        //请求的方法名
        //请求的参数

        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(IPUtils.getIpAddr(request));
            // 设置请求url
            sysLog.setRequestUrl(request.getRequestURI());
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }

        //获取登录用户信息
        sysLog.setUserid(username);
        sysLog.setUsername(realName);

        sysLog.setCreateTime(new Date());
        //保存系统日志
        sysLogMapper.insert(sysLog);
    }

    @Override
    public LoginUser getUserByName(String username) {
        if (oConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = userMapper.getUserByName(username);
        if (sysUser == null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    @Override
    public String getDatabaseType() throws SQLException {
        return getDatabaseType();
    }

    @Override
    public List<DictModel> queryDictItemsByCode(String code) {
        return sysDictService.queryDictItemsByCode(code);
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        return sysDictService.queryTableDictItemsByCode(table, text, code);
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return sysDictService.queryAllDepartBackDictModel();
    }

    @Override
    public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent) {
        SysAnnouncement announcement = new SysAnnouncement();
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        announcement.setMsgCategory("2");
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_NO));
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for (int i = 0; i < userIds.length; i++) {
            if (oConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if (sysUser == null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
            }
        }
    }

    /**
     * 获取数据库类型
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private static String getDatabaseType(DataSource dataSource) throws SQLException {
        if ("".equals(DB_TYPE)) {
            Connection connection = dataSource.getConnection();
            try {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if (dbType.indexOf(SystemConstant.DB_TYPE_MYSQL) >= 0) {
                    DB_TYPE = DB_TYPE_MYSQL;
                } else if (dbType.indexOf(SystemConstant.DB_TYPE_ORACLE) >= 0) {
                    DB_TYPE = DB_TYPE_ORACLE;
                } else if (dbType.indexOf(SystemConstant.DB_TYPE_SQLSERVER) >= 0 || dbType.indexOf(SystemConstant.DB_TYPE_SQL_SERVER) >= 0) {
                    DB_TYPE = DB_TYPE_SQLSERVER;
                } else if (dbType.indexOf(SystemConstant.DB_TYPE_POSTGRESQL) >= 0) {
                    DB_TYPE = DB_TYPE_POSTGRESQL;
                } else {
                    throw new JantdBootException("数据库类型:[" + dbType + "]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                connection.close();
            }
        }
        return DB_TYPE;

    }
}
