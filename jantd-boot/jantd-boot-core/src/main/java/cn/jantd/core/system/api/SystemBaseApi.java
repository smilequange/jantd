package cn.jantd.core.system.api;

import java.sql.SQLException;
import java.util.List;

import cn.jantd.core.system.vo.DictModel;
import cn.jantd.core.system.vo.LoginUser;

/**
 * @Description 底层共通业务API，提供其他独立模块调用
 * @Author 圈哥
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2019/7/2
 */
public interface SystemBaseApi {

    /**
     * 日志添加
     *
     * @param logContent  内容
     * @param logType     日志类型(0:操作日志;1:登录日志;2:定时任务)
     * @param operateType 操作类型(1:添加;2:修改;3:删除;)
     * @param userName 登录用户名
     * @param realNamae 用户名称
     */
    void addLog(String logContent, Integer logType, Integer operateType, String userName, String realNamae);

    /**
     * 根据用户账号查询登录用户信息
     *
     * @param username
     * @return
     */
    LoginUser getUserByName(String username);

    /**
     * 通过用户账号查询角色集合
     *
     * @param username
     * @return
     */
    List<String> getRolesByUsername(String username);

    /**
     * 获取当前数据库类型
     *
     * @return
     * @throws SQLException
     */
    String getDatabaseType() throws SQLException;

    /**
     * 获取数据字典
     *
     * @param code
     * @return
     */
    List<DictModel> queryDictItemsByCode(String code);

    /**
     * 获取表数据字典
     *
     * @param table
     * @param text
     * @param code
     * @return
     */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     *
     * @return
     */
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 发送系统消息
     *
     * @param fromUser   发送人(用户登录账户)
     * @param toUser     发送给(用户登录账户)
     * @param title      消息主题
     * @param msgContent 消息内容
     */
    void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent);
}
