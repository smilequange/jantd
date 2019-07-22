package cn.jantd.modules.system.service.impl;

import cn.jantd.core.poi.service.AutoPoiDictServiceI;
import cn.jantd.core.system.vo.DictModel;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.mapper.SysDictMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：AutoPoi Excel注解支持字典参数设置
 * 举例： @Excel(name = "性别", width = 15, dicCode = "sex")
 * 1、导出的时候会根据字典配置，把值1,2翻译成：男、女;
 * 2、导入的时候，会把男、女翻译成1,2存进数据库;
 *
 * @Author xiagf
 * @date 2019-07-04
 * @Version:1.0
 */
@Slf4j
@Service
public class AutoPoiDictService implements AutoPoiDictServiceI {
    @Autowired
    private SysDictMapper sysDictMapper;

    /**
     * 通过字典查询easypoi，所需字典文本
     *
     * @return
     * @Author:scott
     * @since：2019-04-09
     */
    @Override
    public String[] queryDict(String dicTable, String dicCode, String dicText) {
        List<String> dictReplaces = new ArrayList<String>();
        List<DictModel> dictList = null;
        // step.1 如果没有字典表则使用系统字典表
        if (oConvertUtils.isEmpty(dicTable)) {
            dictList = sysDictMapper.queryDictItemsByCode(dicCode);
        } else {
            try {
                dicText = oConvertUtils.getString(dicText, dicCode);
                dictList = sysDictMapper.queryTableDictItemsByCode(dicTable, dicText, dicCode);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        for (DictModel t : dictList) {
            dictReplaces.add(t.getText() + "_" + t.getValue());
        }
        if (!dictReplaces.isEmpty()) {
            log.info("---AutoPoi--Get_DB_Dict------" + dictReplaces.toString());
            return dictReplaces.toArray(new String[dictReplaces.size()]);
        }
        return null;
    }
}
