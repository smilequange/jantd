package cn.jantd.modules.demo.test.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.jantd.modules.demo.test.entity.JantdOrderCustomer;
import cn.jantd.modules.demo.test.entity.JantdOrderMain;
import cn.jantd.modules.demo.test.entity.JantdOrderTicket;
import cn.jantd.modules.demo.test.mapper.JantdOrderCustomerMapper;
import cn.jantd.modules.demo.test.mapper.JantdOrderMainMapper;
import cn.jantd.modules.demo.test.mapper.JantdOrderTicketMapper;
import cn.jantd.modules.demo.test.service.IJantdOrderMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单
 * @Author xiagf
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class JantdOrderMainServiceImpl extends ServiceImpl<JantdOrderMainMapper, JantdOrderMain> implements IJantdOrderMainService {

    @Autowired
    private JantdOrderMainMapper jantdOrderMainMapper;
    @Autowired
    private JantdOrderCustomerMapper jantdOrderCustomerMapper;
    @Autowired
    private JantdOrderTicketMapper jantdOrderTicketMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(JantdOrderMain jantdOrderMain, List<JantdOrderCustomer> jantdOrderCustomerList, List<JantdOrderTicket> jantdOrderTicketList) {
        jantdOrderMainMapper.insert(jantdOrderMain);
        if (jantdOrderCustomerList != null) {
            for (JantdOrderCustomer entity : jantdOrderCustomerList) {
                entity.setOrderId(jantdOrderMain.getId());
                jantdOrderCustomerMapper.insert(entity);
            }
        }
        if (jantdOrderTicketList != null) {
            for (JantdOrderTicket entity : jantdOrderTicketList) {
                entity.setOrderId(jantdOrderMain.getId());
                jantdOrderTicketMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(JantdOrderMain jantdOrderMain, List<JantdOrderCustomer> jantdOrderCustomerList, List<JantdOrderTicket> jantdOrderTicketList) {
        jantdOrderMainMapper.updateById(jantdOrderMain);

        //1.先删除子表数据
        jantdOrderTicketMapper.deleteTicketsByMainId(jantdOrderMain.getId());
        jantdOrderCustomerMapper.deleteCustomersByMainId(jantdOrderMain.getId());

        //2.子表数据重新插入
        if (jantdOrderCustomerList != null) {
            for (JantdOrderCustomer entity : jantdOrderCustomerList) {
                entity.setOrderId(jantdOrderMain.getId());
                jantdOrderCustomerMapper.insert(entity);
            }
        }
        if (jantdOrderTicketList != null) {
            for (JantdOrderTicket entity : jantdOrderTicketList) {
                entity.setOrderId(jantdOrderMain.getId());
                jantdOrderTicketMapper.insert(entity);
            }
        }
    }

	@Override
    @Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		jantdOrderMainMapper.deleteById(id);
		jantdOrderTicketMapper.deleteTicketsByMainId(id);
		jantdOrderCustomerMapper.deleteCustomersByMainId(id);
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			jantdOrderMainMapper.deleteById(id);
			jantdOrderTicketMapper.deleteTicketsByMainId(id.toString());
			jantdOrderCustomerMapper.deleteCustomersByMainId(id.toString());
		}
	}

}
