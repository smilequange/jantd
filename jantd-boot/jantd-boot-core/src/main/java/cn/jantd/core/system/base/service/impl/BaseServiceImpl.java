package cn.jantd.core.system.base.service.impl;

import cn.jantd.core.system.base.entity.BaseEntity;
import cn.jantd.core.system.base.service.BaseService;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description ServiceImpl基类
 * @Author 圈哥
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2019/7/2
 */

@Slf4j
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

}
