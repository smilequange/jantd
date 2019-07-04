package cn.jantd.modules.monitor.exception;

/**
 * Redis 连接异常
 *
 * @author xiagf
 * @date 2019-07-04
 */
public class RedisConnectException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public RedisConnectException(String message) {
        super(message);
    }
}
