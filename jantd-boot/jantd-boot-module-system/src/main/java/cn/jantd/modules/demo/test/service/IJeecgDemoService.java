package cn.jantd.modules.demo.test.service;

import cn.jantd.modules.demo.test.entity.JeecgDemo;
import cn.jantd.core.system.base.service.BaseService;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface IJeecgDemoService extends BaseService<JeecgDemo> {

	public void testTran();

	public JeecgDemo getByIdCacheable(String id);
}
