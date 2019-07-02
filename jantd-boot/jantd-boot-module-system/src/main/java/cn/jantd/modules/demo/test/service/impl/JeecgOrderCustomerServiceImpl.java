package cn.jantd.modules.demo.test.service.impl;

import java.util.List;

import cn.jantd.modules.demo.test.entity.JeecgOrderCustomer;
import cn.jantd.modules.demo.test.mapper.JeecgOrderCustomerMapper;
import cn.jantd.modules.demo.test.service.IJeecgOrderCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单客户
 * @Author: jeecg-boot
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class JeecgOrderCustomerServiceImpl extends ServiceImpl<JeecgOrderCustomerMapper, JeecgOrderCustomer> implements IJeecgOrderCustomerService {

	@Autowired
	private JeecgOrderCustomerMapper jeecgOrderCustomerMapper;

	@Override
	public List<JeecgOrderCustomer> selectCustomersByMainId(String mainId) {
		return jeecgOrderCustomerMapper.selectCustomersByMainId(mainId);
	}

}
