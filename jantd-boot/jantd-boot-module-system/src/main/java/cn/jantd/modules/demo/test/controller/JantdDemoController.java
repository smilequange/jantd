package cn.jantd.modules.demo.test.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jantd.core.api.vo.Result;
import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.annotation.PermissionData;
import cn.jantd.core.system.base.controller.BaseController;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.util.DateUtils;
import cn.jantd.core.util.RedisUtil;
import cn.jantd.modules.demo.test.entity.JantdDemo;
import cn.jantd.modules.demo.test.service.IJantdDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 测试demo
 * @Author xiagf
 * @Date:2018-12-29
 * @Version:V1.0
 */
@Slf4j
@Api(tags = "单表DEMO")
@RestController
@RequestMapping("/test/jantdDemo")
public class JantdDemoController extends BaseController<JantdDemo, IJantdDemoService> {
    @Autowired
    private IJantdDemoService jantdDemoService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param jantdDemo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "获取Demo数据列表", notes = "获取所有Demo数据列表")
    @GetMapping(value = "/list")
    @PermissionData(pageComponent = "jantd/JantdDemoList")
    public Result<IPage<JantdDemo>> list(JantdDemo jantdDemo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest req) {
        Result<IPage<JantdDemo>> result = new Result<IPage<JantdDemo>>();

        QueryWrapper<JantdDemo> queryWrapper = QueryGenerator.initQueryWrapper(jantdDemo, req.getParameterMap());
        Page<JantdDemo> page = new Page<JantdDemo>(pageNo, pageSize);

        IPage<JantdDemo> pageList = jantdDemoService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param jantdDemo
     * @return
     */
    @PostMapping(value = "/add")
    @AutoLog(value = "添加测试DEMO")
    @ApiOperation(value = "添加DEMO", notes = "添加DEMO")
    public Result<JantdDemo> add(@RequestBody JantdDemo jantdDemo) {
        Result<JantdDemo> result = new Result<JantdDemo>();
        try {
            jantdDemoService.save(jantdDemo);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param jantdDemo
     * @return
     */
    @PutMapping(value = "/edit")
    @ApiOperation(value = "编辑DEMO", notes = "编辑DEMO")
    public Result<JantdDemo> eidt(@RequestBody JantdDemo jantdDemo) {
        Result<JantdDemo> result = new Result<JantdDemo>();
        JantdDemo jantdDemoEntity = jantdDemoService.getById(jantdDemo.getId());
        if (jantdDemoEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = jantdDemoService.updateById(jantdDemo);
            // TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "删除测试DEMO")
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "通过ID删除DEMO", notes = "通过ID删除DEMO")
    public Result<JantdDemo> delete(@RequestParam(name = "id", required = true) String id) {
        Result<JantdDemo> result = new Result<JantdDemo>();
        JantdDemo jantdDemo = jantdDemoService.getById(id);
        if (jantdDemo == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = jantdDemoService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除DEMO", notes = "批量删除DEMO")
    public Result<JantdDemo> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<JantdDemo> result = new Result<JantdDemo>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.jantdDemoService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation(value = "通过ID查询DEMO", notes = "通过ID查询DEMO")
    public Result<JantdDemo> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
        Result<JantdDemo> result = new Result<JantdDemo>();
        JantdDemo jantdDemo = jantdDemoService.getById(id);
        if (jantdDemo == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(jantdDemo);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param jantdDemo
     * @return
     */
    @RequestMapping(value = "/exportXls")
    @PermissionData(pageComponent = "jantd/JantdDemoList")
    public ModelAndView exportXls(HttpServletRequest request, JantdDemo jantdDemo) {
        return super.exportXls(request, jantdDemo, JantdDemo.class, "单表模型");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, JantdDemo.class);
    }

    // ================================================================================================================

    /**
     * redis操作 -- set
     */
    @GetMapping(value = "/redisSet")
    public void redisSet() {
        redisUtil.set("name", "张三" + DateUtils.now());
    }

    /**
     * redis操作 -- get
     */
    @GetMapping(value = "/redisGet")
    public String redisGet() {
        return (String) redisUtil.get("name");
    }

    /**
     * redis操作 -- setObj
     */
    @GetMapping(value = "/redisSetObj")
    public void redisSetObj() {
        JantdDemo p = new JantdDemo();
        p.setAge(10);
        p.setBirthday(new Date());
        p.setContent("hello");
        p.setName("张三");
        p.setSex("男");
        redisUtil.set("user-zdh", p);
    }

    /**
     * redis操作 -- setObj
     */
    @GetMapping(value = "/redisGetObj")
    public Object redisGetObj() {
        return redisUtil.get("user-zdh");
    }

    /**
     * redis操作 -- get
     */
    @GetMapping(value = "/redisDemo/{id}")
    public JantdDemo redisGetJantdDemo(@PathVariable("id") String id) {
        JantdDemo t = jantdDemoService.getByIdCacheable(id);
        System.out.println(t);
        return t;
    }

    /**
     * freemaker方式 【页面路径： src/main/resources/templates】
     *
     * @param modelAndView
     * @return
     */
    @RequestMapping("/demo3")
    public ModelAndView demo3(ModelAndView modelAndView) {
        modelAndView.setViewName("demo3");
        List<String> userList = new ArrayList<String>();
        userList.add("admin");
        userList.add("user1");
        userList.add("user2");
        log.info("--------------test--------------");
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }


    /**
     * JSON接收测试
     *
     * @param json
     * @return
     */
    @PostMapping(value = "/testOnlineAdd")
    public Result<JantdDemo> testOnlineAdd(@RequestBody JSONObject json) {
        Result<JantdDemo> result = new Result<JantdDemo>();
        log.info(json.toJSONString());
        result.success("添加成功！");
        return result;
    }

}
