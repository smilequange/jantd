package cn.jantd.modules.system.service;

import cn.jantd.modules.system.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    /**
     * 查询字典属性
     *
     * @param mainId
     * @return
     */
    List<SysDictItem> selectItemsByMainId(String mainId);
}
