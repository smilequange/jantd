package cn.jantd.modules.demo.test.service;

import java.util.List;

import cn.jantd.modules.demo.test.entity.JantdOrderCustomer;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 订单客户
 * @Author xiagf
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface IJantdOrderCustomerService extends IService<JantdOrderCustomer> {

	public List<JantdOrderCustomer> selectCustomersByMainId(String mainId);
}
