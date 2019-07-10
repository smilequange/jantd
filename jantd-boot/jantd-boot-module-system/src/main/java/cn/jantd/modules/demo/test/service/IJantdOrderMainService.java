package cn.jantd.modules.demo.test.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.jantd.modules.demo.test.entity.JantdOrderCustomer;
import cn.jantd.modules.demo.test.entity.JantdOrderMain;
import cn.jantd.modules.demo.test.entity.JantdOrderTicket;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 订单
 * @Author xiagf
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface IJantdOrderMainService extends IService<JantdOrderMain> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(JantdOrderMain jantdOrderMain, List<JantdOrderCustomer> jantdOrderCustomerList, List<JantdOrderTicket> jantdOrderTicketList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(JantdOrderMain jantdOrderMain, List<JantdOrderCustomer> jantdOrderCustomerList, List<JantdOrderTicket> jantdOrderTicketList);

	/**
	 * 删除一对多
	 * @param jformOrderMain
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 * @param jformOrderMain
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
}
