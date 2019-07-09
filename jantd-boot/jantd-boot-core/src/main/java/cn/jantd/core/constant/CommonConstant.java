package cn.jantd.core.constant;

/**
 * @Description
 * @Author 圈哥
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2019/7/2
 */
public interface CommonConstant {

    /**
     * 状态：正常状态
     */
    Integer STATUS_NORMAL = 0;

    /**
     * 状态：禁用状态
     */
    Integer STATUS_DISABLE = -1;

    /**
     * 删除标志：已经删除
     */
    Integer DEL_FLAG_YES = 1;

    /**
     * 删除标志：未删除
     */
    Integer DEL_FLAG_NO = 0;

    /**
     * 系统日志类型： 登录
     */
    int LOG_TYPE_LOGIN = 1;

    /**
     * 系统日志类型： 操作
     */
    int LOG_TYPE_OPERATE = 2;


    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    Integer SC_OK_200 = 200;

    /**
     * 登录用户拥有角色缓存KEY前缀
     */
    String LOGIN_USER_CACHERULES_ROLE = "loginUser_cacheRules::Roles_";
    /**
     * 登录用户拥有权限缓存KEY前缀
     */
    String LOGIN_USER_CACHERULES_PERMISSION = "loginUser_cacheRules::Permissions_";
    /**
     * 登录用户令牌缓存KEY前缀 3600秒即是一小时
     */
    int TOKEN_EXPIRE_TIME = 3600;

    String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN_";

    /**
     * 0：一级菜单
     */
    Integer MENU_TYPE_0 = 0;
    /**
     * 1：子菜单
     */
    Integer MENU_TYPE_1 = 1;
    /**
     * 2：按钮权限
     */
    Integer MENU_TYPE_2 = 2;

    /**
     * 通告对象类型（USER:指定用户，ALL:全体用户）
     */
    String MSG_TYPE_UESR = "USER";
    String MSG_TYPE_ALL = "ALL";

    /**
     * 发布状态（0未发布，1已发布，2已撤销）
     */
    String NO_SEND = "0";
    String HAS_SEND = "1";
    String HAS_CANCLE = "2";

    /**
     * 阅读状态（0未读，1已读）
     */
    String HAS_READ_FLAG = "1";
    String NO_READ_FLAG = "0";

    /**
     * 优先级（L低，M中，H高）
     */
    String PRIORITY_L = "L";
    String PRIORITY_M = "M ";
    String PRIORITY_H = "H";

    /**
     * 是否用户已被冻结 1(解冻)正常 2冻结
     */
    Integer USER_UNFREEZE = 1;
    Integer USER_FREEZE = 2;
}
