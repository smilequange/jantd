package cn.jantd.modules.system.controller;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.constant.SystemConstant;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysLog;
import cn.jantd.modules.system.entity.SysRole;
import cn.jantd.modules.system.service.ISysLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@RestController
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    /**
     * @param syslog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：查询日志记录
     */
    @AutoLog(value = "日志管理-分页查询日志记录")
    @ApiOperation(value = "日志管理-分页查询日志记录")
    @GetMapping(value = "/list")
    public Result<IPage<SysLog>> queryPageList(SysLog syslog, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysLog>> result = new Result<>();
        QueryWrapper<SysLog> queryWrapper = QueryGenerator.initQueryWrapper(syslog, req.getParameterMap());
        Page<SysLog> page = new Page<SysLog>(pageNo, pageSize);
        // 日志关键词
        String keyWord = req.getParameter("keyWord");
        if (oConvertUtils.isNotEmpty(keyWord)) {
            queryWrapper.like("log_content", keyWord);
        }
        // 创建时间/创建人的赋值
        IPage<SysLog> pageList = sysLogService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @param id
     * @return
     * @功能：删除单个日志记录
     */
    @AutoLog(value = "日志管理-通过id删除")
    @ApiOperation(value = "日志管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<SysLog> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysLog> result = new Result<>();
        SysLog sysLog = sysLogService.getById(id);
        if (sysLog == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysLogService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * @param ids
     * @return
     * @功能：批量，全部清空日志记录
     */
    @AutoLog(value = "日志管理-批量删除")
    @ApiOperation(value = "日志管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysRole> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysRole> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            if (SystemConstant.ALL_CLEAR.equals(ids)) {
                this.sysLogService.removeAll();
                result.success("清除成功!");
            }
            this.sysLogService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }


}
