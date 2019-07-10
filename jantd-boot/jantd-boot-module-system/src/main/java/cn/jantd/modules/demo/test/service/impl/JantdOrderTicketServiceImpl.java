package cn.jantd.modules.demo.test.service.impl;

import java.util.List;

import cn.jantd.modules.demo.test.entity.JantdOrderTicket;
import cn.jantd.modules.demo.test.mapper.JantdOrderTicketMapper;
import cn.jantd.modules.demo.test.service.IJantdOrderTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单机票
 * @Author xiagf
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class JantdOrderTicketServiceImpl extends ServiceImpl<JantdOrderTicketMapper, JantdOrderTicket> implements IJantdOrderTicketService {
	@Autowired
	private JantdOrderTicketMapper jantdOrderTicketMapper;

	@Override
	public List<JantdOrderTicket> selectTicketsByMainId(String mainId) {
		return jantdOrderTicketMapper.selectTicketsByMainId(mainId);
	}

}
