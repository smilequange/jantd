package cn.jantd.core.util.jsonschema.validate;

import cn.jantd.core.system.vo.DictModel;
import cn.jantd.core.util.jsonschema.BaseCommonProperty;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  圈哥 2019-07-03
 */
public class StringPropertyBase extends BaseCommonProperty {

    private static final long serialVersionUID = -3200493311633999539L;

    private Integer maxLength;

    private Integer minLength;

    /**
     * 根据ECMA 262正则表达式方言，该字符串应该是有效的正则表达式。
     */
    private String pattern;

    public Integer getMaxLength() {
        return maxLength;
    }


    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public StringPropertyBase() {
    }

    /**
     * 一般字符串类型走这个构造器
     *
     * @param key       字段名
     * @param title     字段备注
     * @param view      展示控件
     * @param maxLength 数据库字段最大长度
     */
    public StringPropertyBase(String key, String title, String view, Integer maxLength) {
        this.maxLength = maxLength;
        this.key = key;
        this.view = view;
        this.title = title;
        this.type = "string";
    }

    /**
     * 列表类型的走这个构造器
     *
     * @param key       字段名
     * @param title     字段备注
     * @param view      展示控件 list-checkbox-radio
     * @param maxLength 数据库字段最大长度
     * @param include   数据字典
     */
    public StringPropertyBase(String key, String title, String view, Integer maxLength, List<DictModel> include) {
        this.maxLength = maxLength;
        this.key = key;
        this.view = view;
        this.title = title;
        this.type = "string";
        this.include = include;
    }

    @Override
    public Map<String, Object> getPropertyJson() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("key", getKey());
        JSONObject prop = getCommonJson();
        if (maxLength != null) {
            prop.put("maxLength", maxLength);
        }
        if (minLength != null) {
            prop.put("minLength", minLength);
        }
        if (pattern != null) {
            prop.put("pattern", pattern);
        }
        map.put("prop", prop);
        return map;
    }

}
