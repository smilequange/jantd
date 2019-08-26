package cn.jantd.modules.demo.test.service;

import cn.jantd.modules.demo.test.entity.JantdDemo;
import cn.jantd.core.system.base.service.BaseService;

import java.util.List;

/**
 * @Description: jantd 测试demo
 * @Author xiagf
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IJantdDemoService extends BaseService<JantdDemo> {

	public void testTran();

	public JantdDemo getByIdCacheable(String id);
	Integer saveBatch(List<JantdDemo> jantdDemoList);
}
