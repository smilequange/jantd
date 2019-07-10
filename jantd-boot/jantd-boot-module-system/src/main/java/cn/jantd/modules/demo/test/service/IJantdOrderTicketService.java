package cn.jantd.modules.demo.test.service;

import java.util.List;

import cn.jantd.modules.demo.test.entity.JantdOrderTicket;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 订单机票
 * @Author xiagf
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface IJantdOrderTicketService extends IService<JantdOrderTicket> {

	public List<JantdOrderTicket> selectTicketsByMainId(String mainId);
}
