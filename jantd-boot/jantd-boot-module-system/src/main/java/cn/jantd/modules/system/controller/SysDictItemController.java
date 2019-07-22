package cn.jantd.modules.system.controller;


import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.modules.system.entity.SysDictItem;
import cn.jantd.modules.system.service.ISysDictItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@RestController
@RequestMapping("/sys/dictItem")
@Slf4j
public class SysDictItemController {

    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * @param sysDictItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：查询字典数据
     */
    @AutoLog(value = "字典配置-查询字典数据")
    @ApiOperation(value = "字典配置-查询字典数据")
    @GetMapping(value = "/list")
    public Result<IPage<SysDictItem>> queryPageList(SysDictItem sysDictItem, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysDictItem>> result = new Result<IPage<SysDictItem>>();
        QueryWrapper<SysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        queryWrapper.orderByAsc("sort_order");
        Page<SysDictItem> page = new Page<SysDictItem>(pageNo, pageSize);
        IPage<SysDictItem> pageList = sysDictItemService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 新增
     *
     * @param sysDictItem
     * @return
     */
    @AutoLog(value = "字典配置-新增")
    @ApiOperation(value = "字典配置-新增")
    @PostMapping(value = "/add")
    @CacheEvict(value = "dictCache", allEntries = true)
    public Result<SysDictItem> add(@RequestBody SysDictItem sysDictItem) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        try {
            sysDictItem.setCreateTime(new Date());
            sysDictItemService.save(sysDictItem);
            result.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @param sysDictItem
     * @return
     * @功能：编辑
     */
    @AutoLog(value = "字典配置-编辑")
    @ApiOperation(value = "字典配置-编辑")
    @PutMapping(value = "/edit")
    @CacheEvict(value = "dictCache", allEntries = true)
    public Result<SysDictItem> edit(@RequestBody SysDictItem sysDictItem) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        SysDictItem sysdict = sysDictItemService.getById(sysDictItem.getId());
        if (sysdict == null) {
            result.error500("未找到对应实体");
        } else {
            sysDictItem.setUpdateTime(new Date());
            boolean ok = sysDictItemService.updateById(sysDictItem);
            if (ok) {
                result.success("编辑成功!");
            }
        }
        return result;
    }

    /**
     * @param id
     * @return
     * @功能：删除字典数据
     */
    @AutoLog(value = "字典配置-通过id删除")
    @ApiOperation(value = "字典配置-通过id删除")
    @DeleteMapping(value = "/delete")
    @CacheEvict(value = "dictCache", allEntries = true)
    public Result<SysDictItem> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        SysDictItem joinSystem = sysDictItemService.getById(id);
        if (joinSystem == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDictItemService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * @param ids
     * @return
     * @功能：批量删除字典数据
     */
    @AutoLog(value = "字典配置-批量删除字典数据")
    @ApiOperation(value = "字典配置-批量删除字典数据")
    @DeleteMapping(value = "/deleteBatch")
    @CacheEvict(value = "dictCache", allEntries = true)
    public Result<SysDictItem> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysDictItem> result = new Result<SysDictItem>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysDictItemService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

}
