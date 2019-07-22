package cn.jantd.modules.system.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.poi.def.NormalExcelConstants;
import cn.jantd.core.poi.excel.ExcelImportUtil;
import cn.jantd.core.poi.excel.entity.ExportParams;
import cn.jantd.core.poi.excel.entity.ImportParams;
import cn.jantd.core.poi.view.JantdEntityExcelViewBase;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.system.util.JwtUtil;
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.modules.system.entity.SysDepart;
import cn.jantd.modules.system.model.DepartIdModel;
import cn.jantd.modules.system.model.SysDepartTreeModel;
import cn.jantd.modules.system.service.ISysDepartService;
import cn.jantd.modules.system.service.ISysUserDepartService;
import cn.jantd.modules.system.service.ISysUserService;
import cn.jantd.modules.system.util.FindsDepartsChildrenUtil;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 部门表 前端控制器
 * <p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@RestController
@RequestMapping("/sysdepart/sysDepart")
@Slf4j
public class SysDepartController {

    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @AutoLog(value = "部门管理-查询部门树")
    @ApiOperation(value = "部门管理-查询部门树")
    @GetMapping(value = "/queryTreeList")
    public Result<List<SysDepartTreeModel>> queryTreeList() {
        Result<List<SysDepartTreeModel>> result = new Result<>();
        try {
            List<SysDepartTreeModel> list = sysDepartService.queryTreeList();
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 添加新数据 添加用户新建的部门对象数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    @AutoLog(value = "部门管理-添加部门")
    @ApiOperation(value = "部门管理-添加部门")
    @PostMapping(value = "/add")
    public Result<SysDepart> add(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
        Result<SysDepart> result = new Result<SysDepart>();
        String username = JwtUtil.getUserNameByToken(request);
        try {
            sysDepart.setCreateBy(username);
            sysDepartService.saveDepartData(sysDepart, username);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑数据 编辑部门的部分数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    @AutoLog(value = "部门管理-编辑部门")
    @ApiOperation(value = "部门管理-编辑部门")
    @PutMapping(value = "/edit")
    public Result<SysDepart> edit(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        sysDepart.setUpdateBy(username);
        Result<SysDepart> result = new Result<SysDepart>();
        SysDepart sysDepartEntity = sysDepartService.getById(sysDepart.getId());
        if (sysDepartEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartService.updateDepartDataById(sysDepart, username);
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
    @AutoLog(value = "部门管理-通过id删除")
    @ApiOperation(value = "部门管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<SysDepart> delete(@RequestParam(name = "id", required = true) String id) {

        Result<SysDepart> result = new Result<SysDepart>();
        SysDepart sysDepart = sysDepartService.getById(id);
        if (sysDepart == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartService.delete(id);
            if (ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }


    /**
     * 批量删除 根据前端请求的多个ID,对数据库执行删除相关部门数据的操作
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "部门管理-批量删除")
    @ApiOperation(value = "部门管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysDepart> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        Result<SysDepart> result = new Result<SysDepart>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysDepartService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 查询数据 添加或编辑页面对该方法发起请求,以树结构形式加载所有部门的名称,方便用户的操作
     *
     * @return
     */
    @AutoLog(value = "部门管理-查询数据")
    @ApiOperation(value = "部门管理-查询数据")
    @GetMapping(value = "/queryIdTree")
    public Result<List<DepartIdModel>> queryIdTree() {
        Result<List<DepartIdModel>> result = new Result<List<DepartIdModel>>();
        List<DepartIdModel> idList;
        try {
            idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
            if (!idList.isEmpty()) {
                result.setResult(idList);
                result.setSuccess(true);
            } else {
                sysDepartService.queryTreeList();
                idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
                result.setResult(idList);
                result.setSuccess(true);
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            return result;
        }
    }

    /**
     * <p>
     * 部门搜索功能方法,根据关键字模糊搜索相关部门
     * </p>
     *
     * @param keyWord
     * @return
     */
    @AutoLog(value = "部门管理-部门搜索")
    @ApiOperation(value = "部门管理-部门搜索")
    @GetMapping(value = "/searchBy")
    public Result<List<SysDepartTreeModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord) {
        Result<List<SysDepartTreeModel>> result = new Result<List<SysDepartTreeModel>>();
        try {
            List<SysDepartTreeModel> treeList = this.sysDepartService.searhBy(keyWord);
            if (treeList.size() == 0 || treeList == null) {
                throw new Exception();
            }
            result.setSuccess(true);
            result.setResult(treeList);
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("查询失败或没有您想要的任何数据!");
            return result;
        }
    }


    /**
     * 导出excel
     *
     * @param sysDepart
     * @param request
     * @return
     */
    @AutoLog(value = "部门管理-导出excel")
    @ApiOperation(value = "部门管理-导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysDepart sysDepart, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysDepart> queryWrapper = QueryGenerator.initQueryWrapper(sysDepart, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
        List<SysDepart> pageList = sysDepartService.list(queryWrapper);
        //按字典排序
        Collections.sort(pageList, Comparator.comparing(SysDepart::getOrgCode));
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "部门列表");
        mv.addObject(NormalExcelConstants.CLASS, SysDepart.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("部门列表数据", "导出人:" + user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @AutoLog(value = "部门管理-通过excel导入数据")
    @ApiOperation(value = "部门管理-通过excel导入数据")
    @PostMapping(value = "/importExcel")
    public Result<Object> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                // orgCode编码长度
                int codeLength = 3;
                List<SysDepart> listSysDeparts = ExcelImportUtil.importExcel(file.getInputStream(), SysDepart.class, params);
                //按长度排序
                Collections.sort(listSysDeparts, Comparator.comparingInt(arg0 -> arg0.getOrgCode().length()));
                for (SysDepart sysDepart : listSysDeparts) {
                    String orgCode = sysDepart.getOrgCode();
                    if (orgCode.length() > codeLength) {
                        String parentCode = orgCode.substring(0, orgCode.length() - codeLength);
                        QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<SysDepart>();
                        queryWrapper.eq("org_code", parentCode);
                        getSysdepartParentId(sysDepart, queryWrapper);
                    } else {
                        sysDepart.setParentId("");
                    }
                    sysDepartService.save(sysDepart);
                }
                return Result.ok("文件导入成功！数据行数：" + listSysDeparts.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 获取父部门id
     *
     * @param sysDepart
     * @param queryWrapper
     */
    private void getSysdepartParentId(SysDepart sysDepart, QueryWrapper<SysDepart> queryWrapper) {
        try {
            SysDepart parentDept = sysDepartService.getOne(queryWrapper);
            if (!ObjectUtils.isEmpty(parentDept)) {
                sysDepart.setParentId(parentDept.getId());
            } else {
                sysDepart.setParentId("");
            }
        } catch (Exception e) {
            //没有查找到parentDept
        }
    }
}
