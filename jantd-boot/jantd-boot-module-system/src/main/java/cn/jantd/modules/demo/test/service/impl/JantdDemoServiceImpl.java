package cn.jantd.modules.demo.test.service.impl;

import cn.jantd.modules.demo.test.entity.JantdDemo;
import cn.jantd.modules.demo.test.mapper.JantdDemoMapper;
import cn.jantd.core.system.base.service.impl.BaseServiceImpl;
import cn.jantd.modules.demo.test.service.IJantdDemoService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: jantd 测试demo
 * @Author xiagf
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
public class JantdDemoServiceImpl extends BaseServiceImpl<JantdDemoMapper, JantdDemo> implements IJantdDemoService {
	@Autowired
	JantdDemoMapper jantdDemoMapper;

	/**
	 * 事务控制在service层面
	 * 加上注解：@Transactional，声明的方法就是一个独立的事务（有异常DB操作全部回滚）
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void testTran() {
		JantdDemo pp = new JantdDemo();
		pp.setAge(1111);
		pp.setName("测试事务  小白兔 1");
		jantdDemoMapper.insert(pp);

		JantdDemo pp2 = new JantdDemo();
		pp2.setAge(2222);
		pp2.setName("测试事务  小白兔 2");
		jantdDemoMapper.insert(pp2);

		//自定义异常
		Integer.parseInt("hello");

		JantdDemo pp3 = new JantdDemo();
		pp3.setAge(3333);
		pp3.setName("测试事务  小白兔 3");
		jantdDemoMapper.insert(pp3);
		return ;
	}


	/**
	 * 缓存注解测试： redis
	 */
	@Override
	@Cacheable(cacheNames="jantdDemo", key="#id")
	public JantdDemo getByIdCacheable(String id) {
		JantdDemo t = jantdDemoMapper.selectById(id);
		System.err.println("---未读缓存，读取数据库---");
		System.err.println(t);
		return t;
	}

	@Override
	public Integer saveBatch(List<JantdDemo> jantdDemoList) {
		return ListUtils.partition(jantdDemoList,100)
				.stream()
				.map(partition -> jantdDemoMapper.saveBatch(partition))
				.mapToInt(Integer::intValue)
				.sum();
	}

}
