package cn.jantd.modules.demo.test.service.impl;

import java.util.List;

import cn.jantd.modules.demo.test.entity.JantdOrderCustomer;
import cn.jantd.modules.demo.test.mapper.JantdOrderCustomerMapper;
import cn.jantd.modules.demo.test.service.IJantdOrderCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单客户
 * @Author xiagf
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class JantdOrderCustomerServiceImpl extends ServiceImpl<JantdOrderCustomerMapper, JantdOrderCustomer> implements IJantdOrderCustomerService {

	@Autowired
	private JantdOrderCustomerMapper jantdOrderCustomerMapper;

	@Override
	public List<JantdOrderCustomer> selectCustomersByMainId(String mainId) {
		return jantdOrderCustomerMapper.selectCustomersByMainId(mainId);
	}

}
