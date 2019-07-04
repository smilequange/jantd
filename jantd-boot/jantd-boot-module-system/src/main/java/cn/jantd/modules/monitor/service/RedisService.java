package cn.jantd.modules.monitor.service;

import java.util.List;
import java.util.Map;

import cn.jantd.modules.monitor.domain.RedisInfo;
import cn.jantd.modules.monitor.exception.RedisConnectException;

/**
 * @author xiagf
 * @date 2019-07-04
 */
public interface RedisService {

    /**
     * 获取 redis 的详细信息
     *
     * @return list
     * @throws RedisConnectException
     */
    List<RedisInfo> getRedisInfo() throws RedisConnectException;

    /**
     * 获取 redis key 数量
     *
     * @return Map
     * @throws RedisConnectException
     */
    Map<String, Object> getKeysSize() throws RedisConnectException;

    /**
     * 获取 redis 内存信息
     *
     * @return Map
     * @throws RedisConnectException
     */
    Map<String, Object> getMemoryInfo() throws RedisConnectException;

}
