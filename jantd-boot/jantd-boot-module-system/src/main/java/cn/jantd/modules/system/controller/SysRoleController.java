package cn.jantd.modules.system.controller;


import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.annotation.PermissionData;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.poi.def.NormalExcelConstants;
import cn.jantd.core.poi.excel.ExcelImportUtil;
import cn.jantd.core.poi.excel.entity.ExportParams;
import cn.jantd.core.poi.excel.entity.ImportParams;
import cn.jantd.core.poi.view.JantdEntityExcelViewBase;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysPermission;
import cn.jantd.modules.system.entity.SysPermissionDataRule;
import cn.jantd.modules.system.entity.SysRole;
import cn.jantd.modules.system.entity.SysRolePermission;
import cn.jantd.modules.system.model.TreeModel;
import cn.jantd.modules.system.service.ISysPermissionDataRuleService;
import cn.jantd.modules.system.service.ISysPermissionService;
import cn.jantd.modules.system.service.ISysRolePermissionService;
import cn.jantd.modules.system.service.ISysRoleService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {
    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysPermissionDataRuleService sysPermissionDataRuleService;

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;

    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 分页列表查询
     *
     * @param role
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "角色管理-分页列表查询")
    @ApiOperation(value = "角色管理-分页列表查询")
    @PermissionData(pageComponent = "system/RoleList")
    @GetMapping(value = "/list")
    public Result<IPage<SysRole>> queryPageList(SysRole role,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        Result<IPage<SysRole>> result = new Result<IPage<SysRole>>();
        QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(role, req.getParameterMap());
        Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);
        IPage<SysRole> pageList = sysRoleService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param role
     * @return
     */
    @AutoLog(value = "角色管理-添加")
    @ApiOperation(value = "角色管理-添加")
    @PostMapping(value = "/add")
    public Result<SysRole> add(@RequestBody SysRole role) {
        Result<SysRole> result = new Result<SysRole>();
        try {
            role.setCreateTime(new Date());
            sysRoleService.save(role);
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
     * @param role
     * @return
     */
    @AutoLog(value = "角色管理-编辑")
    @ApiOperation(value = "角色管理-编辑")
    @PutMapping(value = "/edit")
    public Result<SysRole> edit(@RequestBody SysRole role) {
        Result<SysRole> result = new Result<SysRole>();
        SysRole sysrole = sysRoleService.getById(role.getId());
        if (sysrole == null) {
            result.error500("未找到对应实体");
        } else {
            role.setUpdateTime(new Date());
            boolean ok = sysRoleService.updateById(role);
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
    @AutoLog(value = "角色管理-通过id删除")
    @ApiOperation(value = "角色管理-通过id删除")
    @CacheEvict(value = "loginUser_cacheRules", allEntries = true)
    @DeleteMapping(value = "/delete")
    public Result<SysRole> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysRole> result = new Result<SysRole>();
        SysRole sysrole = sysRoleService.getById(id);
        if (sysrole == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysRoleService.removeById(id);
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
    @AutoLog(value = "角色管理-批量删除")
    @ApiOperation(value = "角色管理-批量删除")
    @CacheEvict(value = "loginUser_cacheRules", allEntries = true)
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysRole> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysRole> result = new Result<SysRole>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysRoleService.removeByIds(Arrays.asList(ids.split(",")));
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
    @AutoLog(value = "角色管理-通过id查询")
    @ApiOperation(value = "角色管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SysRole> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysRole> result = new Result<SysRole>();
        SysRole sysrole = sysRoleService.getById(id);
        if (sysrole == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysrole);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 查询全部角色
     *
     * @return
     */
    @GetMapping(value = "/queryall")
    public Result<List<SysRole>> queryall() {
        Result<List<SysRole>> result = new Result<>();
        List<SysRole> list = sysRoleService.list();
        if (list.isEmpty()) {
            result.error500("未找到角色信息");
        } else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 校验角色编码唯一
     */
    @AutoLog(value = "角色管理-校验角色编码唯一")
    @ApiOperation(value = "角色管理-校验角色编码唯一")
    @GetMapping(value = "/checkRoleCode")
    public Result<Boolean> checkUsername(String id, String roleCode) {
        Result<Boolean> result = new Result<>();
        // 如果此参数为false则程序发生异常
        result.setResult(true);
        log.info("--验证角色编码是否唯一---id:" + id + "--roleCode:" + roleCode);
        try {
            SysRole role = null;
            if (oConvertUtils.isNotEmpty(id)) {
                role = sysRoleService.getById(id);
            }
            SysRole newRole = sysRoleService.getOne(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleCode, roleCode));
            if (newRole != null) {
                //如果根据传入的roleCode查询到信息了，那么就需要做校验了。
                if (role == null) {
                    //role为空=>新增模式=>只要roleCode存在则返回false
                    result.setSuccess(false);
                    result.setMessage("角色编码已存在");
                    return result;
                } else if (!id.equals(newRole.getId())) {
                    //否则=>编辑模式=>判断两者ID是否一致-
                    result.setSuccess(false);
                    result.setMessage("角色编码已存在");
                    return result;
                }
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 导出excel
     *
     * @param sysRole
     * @param request
     * @return
     */
    @AutoLog(value = "角色管理-导出excel")
    @ApiOperation(value = "角色管理-导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysRole sysRole, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysRole> queryWrapper = QueryGenerator.initQueryWrapper(sysRole, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
        List<SysRole> pageList = sysRoleService.list(queryWrapper);
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "角色列表");
        mv.addObject(NormalExcelConstants.CLASS, SysRole.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("角色列表数据", "导出人:" + user.getRealname(), "导出信息"));
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
    @AutoLog(value = "角色管理-通过excel导入数据")
    @ApiOperation(value = "角色管理-通过excel导入数据")
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
                List<SysRole> listSysRoles = ExcelImportUtil.importExcel(file.getInputStream(), SysRole.class, params);
                for (SysRole sysRoleExcel : listSysRoles) {
                    sysRoleService.save(sysRoleExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + listSysRoles.size());
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
     * 查询数据规则数据
     */
    @AutoLog(value = "角色管理-查询数据规则数据")
    @ApiOperation(value = "角色管理-查询数据规则数据")
    @GetMapping(value = "/datarule/{permissionId}/{roleId}")
    public Result<Object> loadDatarule(@PathVariable("permissionId") String permissionId, @PathVariable("roleId") String roleId) {
        List<SysPermissionDataRule> list = sysPermissionDataRuleService.getPermRuleListByPermId(permissionId);
        if (list.isEmpty()) {
            return Result.error("未找到权限配置信息");
        } else {
            Map<String, Object> map = new HashMap<>(16);
            map.put("datarule", list);
            LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>()
                    .eq(SysRolePermission::getPermissionId, permissionId)
                    .eq(SysRolePermission::getRoleId, roleId);
            SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
            if (sysRolePermission == null) {
            } else {
                String drChecked = sysRolePermission.getDataRuleIds();
                if (oConvertUtils.isNotEmpty(drChecked)) {
                    map.put("drChecked", drChecked.endsWith(",") ? drChecked.substring(0, drChecked.length() - 1) : drChecked);
                }
            }
            return Result.ok(map);
        }
    }

    /**
     * 保存数据规则至角色菜单关联表
     */
    @AutoLog(value = "角色管理-保存数据规则至角色菜单关联表")
    @ApiOperation(value = "角色管理-保存数据规则至角色菜单关联表")
    @PostMapping(value = "/datarule")
    public Result<Object> saveDatarule(@RequestBody JSONObject jsonObject) {
        try {
            String permissionId = jsonObject.getString("permissionId");
            String roleId = jsonObject.getString("roleId");
            String dataRuleIds = jsonObject.getString("dataRuleIds");
            log.info("保存数据规则>>" + "菜单ID:" + permissionId + "角色ID:" + roleId + "数据权限ID:" + dataRuleIds);
            LambdaQueryWrapper<SysRolePermission> query = new LambdaQueryWrapper<SysRolePermission>()
                    .eq(SysRolePermission::getPermissionId, permissionId)
                    .eq(SysRolePermission::getRoleId, roleId);
            SysRolePermission sysRolePermission = sysRolePermissionService.getOne(query);
            if (sysRolePermission == null) {
                return Result.error("请先保存角色菜单权限!");
            } else {
                sysRolePermission.setDataRuleIds(dataRuleIds);
                this.sysRolePermissionService.updateById(sysRolePermission);
            }
        } catch (Exception e) {
            log.error("SysRoleController.saveDatarule()发生异常：" + e.getMessage());
            return Result.error("保存失败");
        }
        return Result.ok("保存成功!");
    }


    /**
     * 用户角色授权功能，查询菜单权限树
     *
     * @param request
     * @return
     */
    @AutoLog(value = "角色管理-授权-查询菜单权限树")
    @ApiOperation(value = "角色管理-授权-查询菜单权限树")
    @GetMapping(value = "/queryTreeList")
    public Result<Map<String, Object>> queryTreeList(HttpServletRequest request) {
        Result<Map<String, Object>> result = new Result<>();
        //全部权限ids
        List<String> ids = new ArrayList<>();
        try {
            LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
            query.eq(SysPermission::getDelFlag, 0);
            query.orderByAsc(SysPermission::getSortNo);
            List<SysPermission> list = sysPermissionService.list(query);
            for (SysPermission sysPer : list) {
                ids.add(sysPer.getId());
            }
            List<TreeModel> treeList = new ArrayList<>();
            getTreeModelList(treeList, list, null);
            Map<String, Object> resMap = new HashMap<String, Object>(16);
            // 全部树节点数据
            resMap.put("treeList", treeList);
            // 全部树ids
            resMap.put("ids", ids);
            result.setResult(resMap);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    private void getTreeModelList(List<TreeModel> treeList, List<SysPermission> metaList, TreeModel temp) {
        for (SysPermission permission : metaList) {
            String tempPid = permission.getParentId();
            TreeModel tree = new TreeModel(permission.getId(), tempPid, permission.getName(), permission.getRuleFlag(), permission.isLeaf());
            if (temp == null && oConvertUtils.isEmpty(tempPid)) {
                treeList.add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(tree);
                if (!tree.getIsLeaf()) {
                    getTreeModelList(treeList, metaList, tree);
                }
            }

        }
    }


}
