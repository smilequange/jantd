package cn.jantd.modules.ngalain.service;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @author xiagf
 * @date 2019-07-04
 */
public interface NgAlainService {
    /**
     * 获取菜单
     *
     * @param id
     * @return
     * @throws Exception
     */
    JSONArray getMenu(String id) throws Exception;

    /**
     * 获取jantd菜单
     *
     * @param id
     * @return
     * @throws Exception
     */
    JSONArray getJantdMenu(String id) throws Exception;

    /**
     * 通过表名获取字典
     *
     * @param table
     * @param key
     * @param value
     * @return
     */
    List<Map<String, String>> getDictByTable(String table, String key, String value);
}
