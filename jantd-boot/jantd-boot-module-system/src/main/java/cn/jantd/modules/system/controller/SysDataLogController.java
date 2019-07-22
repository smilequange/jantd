package cn.jantd.modules.system.controller;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.constant.CommonConstant;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.modules.system.entity.SysDataLog;
import cn.jantd.modules.system.service.ISysDataLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 系统日志
 * @Author xiagf
 * @date 2019-07-04
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sys/dataLog")
@Slf4j
public class SysDataLogController {
    @Autowired
    private ISysDataLogService service;

    @AutoLog(value = "数据日志-分页列表查询")
    @ApiOperation(value = "数据日志-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SysDataLog>> queryPageList(SysDataLog dataLog, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysDataLog>> result = new Result<IPage<SysDataLog>>();
        QueryWrapper<SysDataLog> queryWrapper = QueryGenerator.initQueryWrapper(dataLog, req.getParameterMap());
        Page<SysDataLog> page = new Page<SysDataLog>(pageNo, pageSize);
        IPage<SysDataLog> pageList = service.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 查询对比数据
     *
     * @param req
     * @return
     */
    @AutoLog(value = "数据日志-查询对比数据")
    @ApiOperation(value = "数据日志-查询对比数据")
    @GetMapping(value = "/queryCompareList")
    public Result<List<SysDataLog>> queryCompareList(HttpServletRequest req) {
        Result<List<SysDataLog>> result = new Result<>();
        String dataId1 = req.getParameter("dataId1");
        String dataId2 = req.getParameter("dataId2");
        List<String> idList = new ArrayList<String>();
        idList.add(dataId1);
        idList.add(dataId2);
        try {
            List<SysDataLog> list = (List<SysDataLog>) service.listByIds(idList);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询版本信息
     *
     * @param req
     * @return
     */
    @AutoLog(value = "数据日志-查询版本信息")
    @ApiOperation(value = "数据日志-查询版本信息")
    @GetMapping(value = "/queryDataVerList")
    public Result<List<SysDataLog>> queryDataVerList(HttpServletRequest req) {
        Result<List<SysDataLog>> result = new Result<>();
        String dataTable = req.getParameter("dataTable");
        String dataId = req.getParameter("dataId");
        QueryWrapper<SysDataLog> queryWrapper = new QueryWrapper<SysDataLog>();
        queryWrapper.eq("data_table", dataTable);
        queryWrapper.eq("data_id", dataId);
        List<SysDataLog> list = service.list(queryWrapper);
        if (list.isEmpty()) {
            result.error500("未找到版本信息");
        } else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }

}
