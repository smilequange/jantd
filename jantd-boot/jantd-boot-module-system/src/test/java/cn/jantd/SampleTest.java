package cn.jantd;

import cn.jantd.modules.demo.mock.MockController;
import cn.jantd.modules.demo.test.entity.JantdDemo;
import cn.jantd.modules.demo.test.mapper.JantdDemoMapper;
import cn.jantd.modules.demo.test.service.IJantdDemoService;
import cn.jantd.modules.system.service.ISysDataLogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Resource
    private JantdDemoMapper jantdDemoMapper;
    @Resource
    private IJantdDemoService jantdDemoService;
    @Resource
    private ISysDataLogService sysDataLogService;
    @Resource
    private MockController mock;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method xiagf ------"));
        List<JantdDemo> userList = jantdDemoMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void testXmlSql() {
        System.out.println(("----- selectAll method xiagf ------"));
        List<JantdDemo> userList = jantdDemoMapper.getDemoByName("Sandy12");
        userList.forEach(System.out::println);
    }

    /**
     * 测试事务
     */
    @Test
    public void testTran() {
        jantdDemoService.testTran();
    }

    //author:lvdandan-----date：20190315---for:添加数据日志测试----

    /**
     * 测试数据日志添加
     */
    @Test
    public void testDataLogSave() {
        System.out.println(("----- datalog xiagf ------"));
        String tableName = "demo";
        String dataId = "4028ef81550c1a7901550c1cd6e70001";
        String dataContent = mock.sysDataLogJson();
        sysDataLogService.addDataLog(tableName, dataId, dataContent);
    }
    //author:lvdandan-----date：20190315---for:添加数据日志测试----

    @Test
    @Transactional
    @Rollback(false)
    public void testDbInsert() {
        List<JantdDemo> list = new ArrayList<>();
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            JantdDemo jantdDemo = new JantdDemo();
            jantdDemo.setAge(11);
            jantdDemo.setName("xiagf-test");
            jantdDemo.setSex(String.valueOf(1));
            jantdDemo.setCreateBy("xiagf");
            jantdDemo.setId("xiagf-test" + i);
            jantdDemo.setCreateTime(new Date());
            list.add(jantdDemo);
        }
        jantdDemoService.saveBatch(list);
        long endTime = System.currentTimeMillis();
        System.out.println((float) (endTime - startTime) / 1000);
    }
}
