package cn.jantd.modules.system.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.core.util.PasswordUtil;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysUser;
import cn.jantd.modules.system.entity.SysUserDepart;
import cn.jantd.modules.system.entity.SysUserRole;
import cn.jantd.modules.system.model.DepartIdModel;
import cn.jantd.modules.system.model.SysUserDepartsVO;
import cn.jantd.modules.system.service.ISysUserDepartService;
import cn.jantd.modules.system.service.ISysUserRoleService;
import cn.jantd.modules.system.service.ISysUserService;
import cn.jantd.modules.system.vo.SysDepartUsersVO;
import cn.jantd.modules.system.vo.SysUserRoleVO;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @Author xiagf
 * @date 2019-07-04
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysUserDepartService sysUserDepartService;

    @Autowired
    private ISysUserRoleService userRoleService;

    /**
     * 分页列表查询
     *
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户管理-分页列表查询")
    @ApiOperation(value = "用户管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SysUser>> queryPageList(SysUser user, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<>();
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "用户管理-添加")
    @ApiOperation(value = "用户管理-添加")
    @PostMapping(value = "/add")
    public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<>();
        String selectedRoles = jsonObject.getString("selectedroles");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            // 设置创建时间
            user.setCreateTime(new Date());
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(1);
            user.setDelFlag("0");
            sysUserService.addUserWithRole(user, selectedRoles);
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
     * @param jsonObject
     * @return
     */

    @AutoLog(value = "用户管理-编辑")
    @ApiOperation(value = "用户管理-编辑")
    @PutMapping(value = "/edit")
    public Result<SysUser> edit(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<>();
        try {
            SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
            if (sysUser == null) {
                result.error500("未找到对应实体");
            } else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setUpdateTime(new Date());
                user.setPassword(sysUser.getPassword());
                String roles = jsonObject.getString("selectedroles");
                sysUserService.editUserWithRole(user, roles);
                result.success("修改成功!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户管理-通过id删除")
    @ApiOperation(value = "用户管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<SysUser> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysUser> result = new Result<>();
        // 定义SysUserDepart实体类的数据库查询LambdaQueryWrapper
        LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<>();
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            result.error500("未找到对应实体");
        } else {
            // 当某个用户被删除时,删除其ID下对应的部门数据
            query.eq(SysUserDepart::getUserId, id);
            boolean ok = sysUserService.removeById(id);
            sysUserDepartService.remove(query);
            if (ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }

    /**
     * 批量删除用户
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "用户管理-批量删除用户")
    @ApiOperation(value = "用户管理-批量删除用户")
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysUser> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        // 定义SysUserDepart实体类的数据库查询对象LambdaQueryWrapper
        LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<>();
        String[] idArry = ids.split(",");
        Result<SysUser> result = new Result<>();
        if (StringUtils.isEmpty(ids)) {
            result.error500("参数不识别！");
        } else {
            this.sysUserService.removeByIds(Arrays.asList(ids.split(",")));
            // 当批量删除时,删除在SysUserDepart中对应的所有部门数据
            for (String id : idArry) {
                query.eq(SysUserDepart::getUserId, id);
                this.sysUserDepartService.remove(query);
            }
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 冻结&解冻用户
     *
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "用户管理-冻结&解冻用户")
    @ApiOperation(value = "用户管理-冻结&解冻用户")
    @PutMapping(value = "/frozenBatch")
    public Result<SysUser> frozenBatch(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            for (String id : arr) {
                if (oConvertUtils.isNotEmpty(id)) {
                    this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
                            new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId, id));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败" + e.getMessage());
        }
        result.success("操作成功!");
        return result;

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户管理-通过id查询")
    @ApiOperation(value = "用户管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SysUser> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysUser> result = new Result<>();
        SysUser sysUser = sysUserService.getById(id);
        if (sysUser == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysUser);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 通过userId查询用户角色
     *
     * @param userid
     * @return
     */
    @AutoLog(value = "用户管理-通过userId查询用户角色")
    @ApiOperation(value = "用户管理-通过userId查询用户角色")
    @GetMapping(value = "/queryUserRole")
    public Result<List<String>> queryUserRole(@RequestParam(name = "userid", required = true) String userid) {
        Result<List<String>> result = new Result<>();
        List<String> list = new ArrayList<>();
        List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userid));
        if (userRole.isEmpty()) {
            result.error500("未找到用户相关角色信息");
        } else {
            for (SysUserRole sysUserRole : userRole) {
                list.add(sysUserRole.getRoleId());
            }
            result.setSuccess(true);
            result.setResult(list);
        }
        return result;
    }


    /**
     * 校验用户账号是否唯一<br>
     * 可以校验其他 需要检验什么就传什么。。。
     *
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "用户管理-校验用户账号是否唯一")
    @GetMapping(value = "/checkOnlyUser")
    public Result<Boolean> checkUsername(SysUser sysUser) {
        Result<Boolean> result = new Result<>();
        // 如果此参数为false则程序发生异常
        result.setResult(true);
        String id = sysUser.getId();
        log.info("--验证用户信息是否唯一---id:" + id);
        try {
            SysUser oldUser = null;
            if (oConvertUtils.isNotEmpty(id)) {
                oldUser = sysUserService.getById(id);
            } else {
                sysUser.setId(null);
            }
            //通过传入信息查询新的用户信息
            SysUser newUser = sysUserService.getOne(new QueryWrapper<>(sysUser));
            if (newUser != null) {
                //如果根据传入信息查询到用户了，那么就需要做校验了。
                if (oldUser == null) {
                    //oldUser为空=>新增模式=>只要用户信息存在则返回false
                    result.setSuccess(false);
                    result.setMessage("用户账号已存在");
                    return result;
                } else if (!id.equals(newUser.getId())) {
                    //否则=>编辑模式=>判断两者ID是否一致-
                    result.setSuccess(false);
                    result.setMessage("用户账号已存在");
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
     * 修改密码
     */
    @AutoLog(value = "用户管理-修改密码")
    @ApiOperation(value = "用户管理-修改密码")
    @PutMapping(value = "/changPassword")
    public Result<SysUser> changPassword(@RequestBody SysUser sysUser) {
        Result<SysUser> result = new Result<>();
        String password = sysUser.getPassword();
        sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
        if (sysUser == null) {
            result.error500("未找到对应实体");
        } else {
            String salt = oConvertUtils.randomGen(8);
            sysUser.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
            sysUser.setPassword(passwordEncode);
            this.sysUserService.updateById(sysUser);
            result.setResult(sysUser);
            result.success("密码修改完成！");
        }
        return result;
    }

    /**
     * 查询指定用户和部门关联的数据
     *
     * @param userId
     * @return
     */
    @AutoLog(value = "用户管理-查询指定用户和部门关联的数据")
    @ApiOperation(value = "用户管理-查询指定用户和部门关联的数据")
    @GetMapping(value = "/userDepartList")
    public Result<List<DepartIdModel>> getUserDepartsList(@RequestParam(name = "userId", required = true) String userId) {
        Result<List<DepartIdModel>> result = new Result<>();
        try {
            List<DepartIdModel> depIdModelList = this.sysUserDepartService.queryDepartIdsOfUser(userId);
            if (!depIdModelList.isEmpty()) {
                result.setSuccess(true);
                result.setMessage("查找成功");
                result.setResult(depIdModelList);
            } else {
                result.setSuccess(false);
                result.setMessage("查找失败");
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查找过程中出现了异常: " + e.getMessage());
            return result;
        }

    }

    /**
     * 给指定用户添加对应的部门
     *
     * @param sysUserDepartsVO
     * @return
     */
    @AutoLog(value = "用户管理-给指定用户添加对应的部门")
    @ApiOperation(value = "用户管理-给指定用户添加对应的部门")
    @PostMapping(value = "/addUDepartIds")
    public Result<String> addSysUseWithrDepart(@RequestBody SysUserDepartsVO sysUserDepartsVO) {
        boolean ok = this.sysUserDepartService.addSysUseWithrDepart(sysUserDepartsVO);
        Result<String> result = new Result<>();
        try {
            if (ok) {
                result.setMessage("添加成功!");
                result.setSuccess(true);
            } else {
                throw new Exception("添加失败!");
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(true);
            result.setMessage("添加数据的过程中出现市场了: " + e.getMessage());
            return result;
        }

    }

    /**
     * 根据用户id编辑对应的部门信息
     *
     * @param sysUserDepartsVO
     * @return
     */
    @ApiOperation(value = "用户管理-根据用户id编辑对应的部门信息")
    @PutMapping(value = "/editUDepartIds")
    public Result<String> editSysUserWithDepart(@RequestBody SysUserDepartsVO sysUserDepartsVO) {
        Result<String> result = new Result<>();
        boolean ok = sysUserDepartService.editSysUserWithDepart(sysUserDepartsVO);
        if (ok) {
            result.setMessage("更新成功!");
            result.setSuccess(true);
            return result;
        }
        result.setMessage("更新失败!");
        result.setSuccess(false);
        return result;
    }

    /**
     * 生成在添加用户情况下没有主键的问题,返回给前端,根据该id绑定部门数据
     *
     * @return
     */
    @ApiOperation(value = "用户管理-生成在添加用户情况下没有主键的问题,返回给前端,根据该id绑定部门数据")
    @GetMapping(value = "/generateUserId")
    public Result<String> generateUserId() {
        Result<String> result = new Result<>();
        log.info("我执行了,生成用户ID==============================");
        String userId = UUID.randomUUID().toString().replace("-", "");
        result.setSuccess(true);
        result.setResult(userId);
        return result;
    }

    /**
     * 根据部门id查询用户信息
     *
     * @param id
     * @return
     */
    @AutoLog(value = "用户管理-根据部门id查询用户信息")
    @ApiOperation(value = "用户管理-根据部门id查询用户信息")
    @GetMapping(value = "/queryUserByDepId")
    public Result<List<SysUser>> queryUserByDepId(@RequestParam(name = "id", required = true) String id) {
        Result<List<SysUser>> result = new Result<>();
        List<SysUser> userList = sysUserDepartService.queryUserByDepId(id);
        try {
            result.setSuccess(true);
            result.setResult(userList);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            return result;
        }
    }

    /**
     * 查询所有用户所对应的角色信息
     *
     * @return
     */
    @AutoLog(value = "用户管理-查询所有用户所对应的角色信息")
    @ApiOperation(value = "用户管理-查询所有用户所对应的角色信息")
    @GetMapping(value = "/queryUserRoleMap")
    public Result<Map<String, String>> queryUserRole() {
        Result<Map<String, String>> result = new Result<>();
        Map<String, String> map = userRoleService.queryUserRole();
        result.setResult(map);
        result.setSuccess(true);
        return result;
    }

    /**
     * 导出excel
     *
     * @param sysUser
     * @param request
     * @return
     */
    @AutoLog(value = "用户管理-导出excel")
    @ApiOperation(value = "用户管理-导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysUser sysUser, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
        List<SysUser> pageList = sysUserService.list(queryWrapper);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "用户列表");
        mv.addObject(NormalExcelConstants.CLASS, SysUser.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户列表数据", "导出人:"+user.getRealname(), "导出信息"));
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
    @AutoLog(value = "用户管理-通过excel导入数据")
    @ApiOperation(value = "用户管理-通过excel导入数据")
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
                List<SysUser> listSysUsers = ExcelImportUtil.importExcel(file.getInputStream(), SysUser.class, params);
                for (SysUser sysUserExcel : listSysUsers) {
                    if (sysUserExcel.getPassword() == null) {
                        // 密码默认为“123456”
                        sysUserExcel.setPassword("123456");
                    }
                    sysUserService.save(sysUserExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + listSysUsers.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("抱歉! 您导入的数据中用户名已经存在.");
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
     * @param userIds
     * @return
     * @功能：根据id 批量查询
     */
    @AutoLog(value = "用户管理-根据id 批量查询")
    @ApiOperation(value = "用户管理-根据id 批量查询")
    @GetMapping(value = "/queryByIds")
    public Result<Collection<SysUser>> queryByIds(@RequestParam String userIds) {
        Result<Collection<SysUser>> result = new Result<>();
        String[] userId = userIds.split(",");
        Collection<String> idList = Arrays.asList(userId);
        Collection<SysUser> userRole = sysUserService.listByIds(idList);
        result.setSuccess(true);
        result.setResult(userRole);
        return result;
    }

    /**
     * 首页密码修改
     */
    @AutoLog(value = "用户管理-首页密码修改")
    @ApiOperation(value = "用户管理-首页密码修改")
    @PutMapping(value = "/updatePassword")
    public Result<SysUser> changPassword(@RequestBody JSONObject json) {
        Result<SysUser> result = new Result<>();
        String username = json.getString("username");
        String oldpassword = json.getString("oldpassword");
        SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (user == null) {
            result.error500("未找到用户!");
            return result;
        }
        String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            result.error500("旧密码输入错误!");
            return result;
        }

        String password = json.getString("password");
        String confirmpassword = json.getString("confirmpassword");
        if (oConvertUtils.isEmpty(password)) {
            result.error500("新密码不存在!");
            return result;
        }

        if (!password.equals(confirmpassword)) {
            result.error500("两次输入密码不一致!");
            return result;
        }
        String newpassword = PasswordUtil.encrypt(username, password, user.getSalt());
        this.sysUserService.update(new SysUser().setPassword(newpassword), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        result.success("密码修改完成！");
        return result;
    }

    /**
     * 根据角色Id查询用户
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户管理-根据角色Id查询用户")
    @ApiOperation(value = "用户管理-根据角色Id查询用户")
    @GetMapping(value = "/userRoleList")
    public Result<IPage<SysUser>> userRoleList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<>();
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        String roleId = req.getParameter("roleId");
        String username = req.getParameter("username");
        IPage<SysUser> pageList = sysUserService.getUserByRoleId(page, roleId, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 给指定角色添加用户
     *
     * @param sysUserRoleVO
     * @return
     */

    @AutoLog(value = "用户管理-给指定角色添加用户")
    @ApiOperation(value = "用户管理-给指定角色添加用户")
    @PostMapping(value = "/addSysUserRole")
    public Result<String> addSysUserRole(@RequestBody SysUserRoleVO sysUserRoleVO) {
        Result<String> result = new Result<>();
        try {
            String sysRoleId = sysUserRoleVO.getRoleId();
            for (String sysUserId : sysUserRoleVO.getUserIdList()) {
                SysUserRole sysUserRole = new SysUserRole(sysUserId, sysRoleId);
                QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("role_id", sysRoleId).eq("user_id", sysUserId);
                SysUserRole one = sysUserRoleService.getOne(queryWrapper);
                if (one == null) {
                    sysUserRoleService.save(sysUserRole);
                }

            }

            result.setMessage("添加成功!");
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("出错了: " + e.getMessage());
            return result;
        }
    }

    /**
     * 删除指定角色的用户关系
     *
     * @param
     * @return
     */
    @AutoLog(value = "用户管理-删除指定角色的用户关系")
    @ApiOperation(value = "用户管理-删除指定角色的用户关系")
    @DeleteMapping(value = "/deleteUserRole")
    public Result<SysUserRole> deleteUserRole(@RequestParam(name = "roleId") String roleId,
                                              @RequestParam(name = "userId", required = true) String userId
    ) {
        Result<SysUserRole> result = new Result<>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", roleId).eq("user_id", userId);
            sysUserRoleService.remove(queryWrapper);
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 批量删除指定角色的用户关系
     *
     * @param roleId
     * @param userIds
     * @return
     */
    @AutoLog(value = "用户管理-批量删除指定角色的用户关系")
    @ApiOperation(value = "用户管理-批量删除指定角色的用户关系")
    @DeleteMapping(value = "/deleteUserRoleBatch")
    public Result<SysUserRole> deleteUserRoleBatch(
            @RequestParam(name = "roleId") String roleId,
            @RequestParam(name = "userIds", required = true) String userIds) {
        Result<SysUserRole> result = new Result<>();
        try {
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", roleId).in("user_id", Arrays.asList(userIds.split(",")));
            sysUserRoleService.remove(queryWrapper);
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 部门用户列表
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "用户管理-部门用户列表")
    @ApiOperation(value = "用户管理-部门用户列表")
    @GetMapping(value = "/departUserList")
    public Result<IPage<SysUser>> departUserList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<>();
        Page<SysUser> page = new Page<>(pageNo, pageSize);
        String depId = req.getParameter("depId");
        String username = req.getParameter("username");
        IPage<SysUser> pageList = sysUserService.getUserByDepId(page, depId, username);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 给指定部门添加对应的用户
     *
     * @param sysDepartUsersVO
     * @return
     */
    @AutoLog(value = "用户管理-给指定部门添加对应的用户")
    @ApiOperation(value = "用户管理-给指定部门添加对应的用户")
    @PostMapping(value = "/editSysDepartWithUser")
    public Result<String> editSysDepartWithUser(@RequestBody SysDepartUsersVO sysDepartUsersVO) {
        Result<String> result = new Result<>();
        try {
            String sysDepId = sysDepartUsersVO.getDepId();
            for (String sysUserId : sysDepartUsersVO.getUserIdList()) {
                SysUserDepart sysUserDepart = new SysUserDepart(null, sysUserId, sysDepId);
                QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("dep_id", sysDepId).eq("user_id", sysUserId);
                SysUserDepart one = sysUserDepartService.getOne(queryWrapper);
                if (one == null) {
                    sysUserDepartService.save(sysUserDepart);
                }
            }

            result.setMessage("添加成功!");
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("出错了: " + e.getMessage());
            return result;
        }
    }

    /**
     * 删除指定机构的用户关系
     *
     * @param depId
     * @param userId
     * @return
     */
    @AutoLog(value = "用户管理-删除指定机构的用户关系")
    @ApiOperation(value = "用户管理-删除指定机构的用户关系")
    @DeleteMapping(value = "/deleteUserInDepart")
    public Result<SysUserDepart> deleteUserInDepart(@RequestParam(name = "depId") String depId,
                                                    @RequestParam(name = "userId", required = true) String userId
    ) {
        Result<SysUserDepart> result = new Result<>();
        try {
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dep_id", depId).eq("user_id", userId);
            sysUserDepartService.remove(queryWrapper);
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

    /**
     * 批量删除指定机构的用户关系
     *
     * @param depId
     * @param userIds
     * @return
     */
    @AutoLog(value = "用户管理-批量删除指定机构的用户关系")
    @ApiOperation(value = "用户管理-批量删除指定机构的用户关系")
    @DeleteMapping(value = "/deleteUserInDepartBatch")
    public Result<SysUserDepart> deleteUserInDepartBatch(
            @RequestParam(name = "depId") String depId,
            @RequestParam(name = "userIds", required = true) String userIds) {
        Result<SysUserDepart> result = new Result<>();
        try {
            QueryWrapper<SysUserDepart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dep_id", depId).in("user_id", Arrays.asList(userIds.split(",")));
            sysUserDepartService.remove(queryWrapper);
            result.success("删除成功!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("删除失败！");
        }
        return result;
    }

}
