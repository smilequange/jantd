package cn.jantd.modules.system.controller;


import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.constant.CommonConstant;
import cn.jantd.core.constant.SystemConstant;
import cn.jantd.core.poi.def.NormalExcelConstants;
import cn.jantd.core.poi.excel.ExcelImportUtil;
import cn.jantd.core.poi.excel.entity.ExportParams;
import cn.jantd.core.poi.excel.entity.ImportParams;
import cn.jantd.core.poi.view.JantdEntityExcelViewBase;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.system.vo.DictModel;
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysDict;
import cn.jantd.modules.system.entity.SysDictItem;
import cn.jantd.modules.system.model.SysDictTree;
import cn.jantd.modules.system.service.ISysDictItemService;
import cn.jantd.modules.system.service.ISysDictService;
import cn.jantd.modules.system.vo.SysDictPage;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class SysDictController {

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private ISysDictItemService sysDictItemService;

    @AutoLog(value = "数据字典-分页列表查询")
    @ApiOperation(value = "数据字典-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SysDict>> queryPageList(SysDict sysDict, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
        queryWrapper.eq("del_flag", "0");
        Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
        IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * @param sysDict
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：获取树形字典数据
     */
    @AutoLog(value = "数据字典-获取树形字典数据")
    @ApiOperation(value = "数据字典-获取树形字典数据")
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/treeList")
    public Result<List<SysDictTree>> treeList(SysDict sysDict, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<List<SysDictTree>> result = new Result<>();
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        // 构造查询条件
        String dictName = sysDict.getDictName();
        if (oConvertUtils.isNotEmpty(dictName)) {
            query.like(true, SysDict::getDictName, dictName);
        }
        query.eq(true, SysDict::getDelFlag, "1");
        query.orderByDesc(true, SysDict::getCreateTime);
        List<SysDict> list = sysDictService.list(query);
        List<SysDictTree> treeList = new ArrayList<>();
        for (SysDict node : list) {
            treeList.add(new SysDictTree(node));
        }
        result.setSuccess(true);
        result.setResult(treeList);
        return result;
    }

    /**
     * 获取字典数据
     *
     * @param dictCode 字典code
     * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
     * @return
     */
    @AutoLog(value = "数据字典-获取字典数据")
    @ApiOperation(value = "数据字典-获取字典数据")
    @GetMapping(value = "/getDictItems/{dictCode}")
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
        log.info(" dictCode : " + dictCode);
        Result<List<DictModel>> result = new Result<List<DictModel>>();
        List<DictModel> ls = null;
        try {
            if (dictCode.indexOf(SystemConstant.COMMA) != -1) {
                //关联表字典（举例：sys_user,realname,id）
                String[] params = dictCode.split(",");
                if (params.length != SystemConstant.NUMBER_THREE) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                ls = sysDictService.queryTableDictItemsByCode(params[0], params[1], params[2]);
            } else {
                //字典表
                ls = sysDictService.queryDictItemsByCode(dictCode);
            }

            result.setSuccess(true);
            result.setResult(ls);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

    /**
     * 获取字典数据
     *
     * @param dictCode
     * @return
     */
    @AutoLog(value = "数据字典-通过查询指定code 获取字典值text")
    @ApiOperation(value = "数据字典-通过查询指定code 获取字典值text")
    @GetMapping(value = "/getDictText/{dictCode}/{key}")
    public Result<String> getDictItems(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
        log.info(" dictCode : " + dictCode);
        Result<String> result = new Result<String>();
        String text = null;
        try {
            text = sysDictService.queryDictTextByKey(dictCode, key);
            result.setSuccess(true);
            result.setResult(text);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
            return result;
        }
        return result;
    }

    /**
     * @param sysDict
     * @return
     * @功能：新增
     */
    @AutoLog(value = "数据字典-新增")
    @ApiOperation(value = "部门管理-新增")
    @PostMapping(value = "/add")
    public Result<SysDict> add(@RequestBody SysDict sysDict) {
        Result<SysDict> result = new Result<SysDict>();
        try {
            sysDict.setCreateTime(new Date());
            sysDict.setDelFlag(CommonConstant.DEL_FLAG_NO);
            sysDictService.save(sysDict);
            result.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * @param sysDict
     * @return
     * @功能：编辑
     */
    @AutoLog(value = "数据字典-编辑")
    @ApiOperation(value = "数据字典-编辑")
    @PutMapping(value = "/edit")
    public Result<SysDict> edit(@RequestBody SysDict sysDict) {
        Result<SysDict> result = new Result<SysDict>();
        SysDict sysdict = sysDictService.getById(sysDict.getId());
        if (sysdict == null) {
            result.error500("未找到对应实体");
        } else {
            sysDict.setUpdateTime(new Date());
            boolean ok = sysDictService.updateById(sysDict);
            if (ok) {
                result.success("编辑成功!");
            }
        }
        return result;
    }

    /**
     * @param id
     * @return
     * @功能：删除
     */
    @AutoLog(value = "数据字典-通过id删除")
    @ApiOperation(value = "数据字典-通过id删除")
    @DeleteMapping(value = "/delete")
    @CacheEvict(value = "dictCache", allEntries = true)
    public Result<SysDict> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysDict> result = new Result<SysDict>();
        SysDict sysDict = sysDictService.getById(id);
        if (sysDict == null) {
            result.error500("未找到对应实体");
        } else {
            //update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]数据字典增加级联删除功能--------------------
            boolean ok = sysDictService.deleteByDictId(sysDict);
            //update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]数据字典增加级联删除功能--------------------
            if (ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * @param ids
     * @return
     * @功能：批量删除
     */
    @AutoLog(value = "数据字典-批量删除")
    @ApiOperation(value = "数据字典-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    @CacheEvict(value = "dictCache", allEntries = true)
    public Result<SysDict> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysDict> result = new Result<SysDict>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            String[] id = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                SysDict sysDict = sysDictService.getById(id[i]);
                sysDict.setDelFlag(CommonConstant.DEL_FLAG_YES);
                sysDictService.updateById(sysDict);
            }
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param sysDict
     * @param request
     * @return
     */
    @AutoLog(value = "数据字典-导出excel")
    @ApiOperation(value = "数据字典-导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysDict sysDict, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
        List<SysDictPage> pageList = new ArrayList<SysDictPage>();
        queryWrapper.eq("del_flag","0");
        List<SysDict> sysDictList = sysDictService.list(queryWrapper);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        for (SysDict dictMain : sysDictList) {
            SysDictPage vo = new SysDictPage();
            BeanUtils.copyProperties(dictMain, vo);
            // 查询机票
            List<SysDictItem> sysDictItemList = sysDictItemService.selectItemsByMainId(dictMain.getId());
            vo.setSysDictItemList(sysDictItemList);
            pageList.add(vo);
        }

        // 导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "数据字典");
        // 注解对象Class
        mv.addObject(NormalExcelConstants.CLASS, SysDictPage.class);
        // 自定义表格参数
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据字典列表", "导出人:"+user.getRealname(), "数据字典"));
        // 导出数据列表
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param
     * @return
     */
    @AutoLog(value = "数据字典-通过excel导入数据")
    @ApiOperation(value = "数据字典-通过excel导入数据")
    @PostMapping(value = "/importExcel")
    public Result<Object> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(2);
            params.setNeedSave(true);
            try {
                List<SysDictPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SysDictPage.class, params);
                for (SysDictPage page : list) {
                    SysDict po = new SysDict();
                    BeanUtils.copyProperties(page, po);
                    if (page.getDelFlag() == null) {
                        po.setDelFlag(1);
                    }
                    sysDictService.saveMain(po, page.getSysDictItemList());
                }
                return Result.ok("文件导入成功！");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return Result.error("文件导入失败！");
    }

}
