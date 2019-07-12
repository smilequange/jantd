package cn.jantd.modules.system.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.constant.CommonConstant;
import cn.jantd.core.constant.CommonSendStatus;
import cn.jantd.core.constant.SystemConstant;
import cn.jantd.core.poi.def.NormalExcelConstants;
import cn.jantd.core.poi.excel.ExcelImportUtil;
import cn.jantd.core.poi.excel.entity.ExportParams;
import cn.jantd.core.poi.excel.entity.ImportParams;
import cn.jantd.core.poi.view.JantdEntityExcelViewBase;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.system.util.JwtUtil;
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.core.util.oConvertUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import cn.jantd.modules.system.entity.SysAnnouncement;
import cn.jantd.modules.system.entity.SysAnnouncementSend;
import cn.jantd.modules.system.service.ISysAnnouncementSendService;
import cn.jantd.modules.system.service.ISysAnnouncementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 系统通告表
 * @Author xiagf
 * @Version: V1.0
 * @since 2019-07-04
 */
@RestController
@RequestMapping("/sys/annountCement")
@Slf4j
public class SysAnnouncementController {
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;

    /**
     * 分页列表查询
     *
     * @param sysAnnouncement
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "系统通告-分页列表查询")
    @ApiOperation(value = "系统通告-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysAnnouncement>> queryPageList(SysAnnouncement sysAnnouncement,
                                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest req) {
        Result<IPage<SysAnnouncement>> result = new Result<IPage<SysAnnouncement>>();
        sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_NO.toString());
        QueryWrapper<SysAnnouncement> queryWrapper = new QueryWrapper<SysAnnouncement>(sysAnnouncement);
        Page<SysAnnouncement> page = new Page<SysAnnouncement>(pageNo, pageSize);
        //排序逻辑 处理
        String column = req.getParameter("column");
        String order = req.getParameter("order");
        if (oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
            if (SystemConstant.SORT_ASC.equals(order)) {
                queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
            } else {
                queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
            }
        }
        IPage<SysAnnouncement> pageList = sysAnnouncementService.page(page, queryWrapper);
        log.info("查询当前页：" + pageList.getCurrent());
        log.info("查询当前页数量：" + pageList.getSize());
        log.info("查询结果数量：" + pageList.getRecords().size());
        log.info("数据总数：" + pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysAnnouncement
     * @return
     */
    @AutoLog(value = "系统通告-添加")
    @ApiOperation(value = "系统通告-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysAnnouncement> add(@RequestBody SysAnnouncement sysAnnouncement) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();
        try {
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_NO.toString());
            // 未发布
            sysAnnouncement.setSendStatus(CommonSendStatus.UNPUBLISHED_STATUS_0);
            sysAnnouncementService.saveAnnouncement(sysAnnouncement);
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
     * @param sysAnnouncement
     * @return
     */
    @AutoLog(value = "系统通告-编辑")
    @ApiOperation(value = "系统通告-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<SysAnnouncement> eidt(@RequestBody SysAnnouncement sysAnnouncement) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();
        SysAnnouncement sysAnnouncementEntity = sysAnnouncementService.getById(sysAnnouncement.getId());
        if (sysAnnouncementEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysAnnouncementService.upDateAnnouncement(sysAnnouncement);
            //TODO 返回false说明什么？
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
    @AutoLog(value = "系统通告-通过id删除")
    @ApiOperation(value = "系统通告-通过id删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<SysAnnouncement> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();
        SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
        if (sysAnnouncement == null) {
            result.error500("未找到对应实体");
        } else {
            sysAnnouncement.setDelFlag(CommonConstant.DEL_FLAG_YES.toString());
            boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
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
    @AutoLog(value = "系统通告-批量删除")
    @ApiOperation(value = "系统通告-批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<SysAnnouncement> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            String[] id = ids.split(",");
            for (int i = 0; i < id.length; i++) {
                SysAnnouncement announcement = sysAnnouncementService.getById(id[i]);
                announcement.setDelFlag(CommonConstant.DEL_FLAG_YES.toString());
                sysAnnouncementService.updateById(announcement);
            }
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
    @AutoLog(value = "系统通告-通过id查询")
    @ApiOperation(value = "系统通告-通过id查询")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Result<SysAnnouncement> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();
        SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
        if (sysAnnouncement == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysAnnouncement);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 更新发布操作
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统通告-更新发布操作")
    @ApiOperation(value = "系统通告-更新发布操作")
    @RequestMapping(value = "/doReleaseData", method = RequestMethod.GET)
    public Result<SysAnnouncement> doReleaseData(@RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();
        SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
        if (sysAnnouncement == null) {
            result.error500("未找到对应实体");
        } else {
            // 发布中
            sysAnnouncement.setSendStatus(CommonSendStatus.PUBLISHED_STATUS_1);
            sysAnnouncement.setSendTime(new Date());
            String currentUserName = JwtUtil.getUserNameByToken(request);
            sysAnnouncement.setSender(currentUserName);
            boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
            if (ok) {
                result.success("该系统通知发布成功");
            }
        }

        return result;
    }

    /**
     * 更新撤销操作
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统通告-更新撤销操作")
    @ApiOperation(value = "系统通告-更新撤销操作")
    @RequestMapping(value = "/doReovkeData", method = RequestMethod.GET)
    public Result<SysAnnouncement> doReovkeData(@RequestParam(name = "id", required = true) String id, HttpServletRequest request) {
        Result<SysAnnouncement> result = new Result<SysAnnouncement>();
        SysAnnouncement sysAnnouncement = sysAnnouncementService.getById(id);
        if (sysAnnouncement == null) {
            result.error500("未找到对应实体");
        } else {
            // 撤销发布
            sysAnnouncement.setSendStatus(CommonSendStatus.REVOKE_STATUS_2);
            sysAnnouncement.setCancelTime(new Date());
            boolean ok = sysAnnouncementService.updateById(sysAnnouncement);
            if (ok) {
                result.success("该系统通知撤销成功");
            }
        }

        return result;
    }

    /**
     * @return
     * @功能：补充用户数据，并返回系统消息
     */
    @ApiOperation(value = "系统通告-补充用户数据，并返回系统消息")
    @RequestMapping(value = "/listByUser", method = RequestMethod.GET)
    public Result<Map<String, Object>> listByUser() {
        Result<Map<String, Object>> result = new Result<Map<String, Object>>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        // 1.将系统消息补充到用户通告阅读标记表中
        Collection<String> anntIds = sysAnnouncementSendService.queryByUserId(userId);
        LambdaQueryWrapper<SysAnnouncement> querySaWrapper = new LambdaQueryWrapper<SysAnnouncement>();
        // 全部人员
        querySaWrapper.eq(SysAnnouncement::getMsgType, CommonConstant.MSG_TYPE_ALL);
        // 未删除
        querySaWrapper.eq(SysAnnouncement::getDelFlag, CommonConstant.DEL_FLAG_NO);
        // 已发布
        querySaWrapper.eq(SysAnnouncement::getSendStatus, CommonConstant.HAS_SEND);
        if (!CollectionUtils.isEmpty(anntIds)) {
            querySaWrapper.notIn(SysAnnouncement::getId, anntIds);
        }
        List<SysAnnouncement> announcements = sysAnnouncementService.list(querySaWrapper);
        if (announcements.size() > 0) {
            for (int i = 0; i < announcements.size(); i++) {
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(announcements.get(i).getId());
                announcementSend.setUserId(userId);
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendService.save(announcementSend);
            }
        }
        // 2.查询用户未读的系统消息
        Page<SysAnnouncement> anntMsgList = new Page<SysAnnouncement>(0, 5);
        // 通知公告消息
        anntMsgList = sysAnnouncementService.querySysCementPageByUserId(anntMsgList, userId, "1");
        Page<SysAnnouncement> sysMsgList = new Page<SysAnnouncement>(0, 5);
        // 系统消息
        sysMsgList = sysAnnouncementService.querySysCementPageByUserId(sysMsgList, userId, "2");
        Map<String, Object> sysMsgMap = new HashMap<String, Object>(16);
        sysMsgMap.put("sysMsgList", sysMsgList.getRecords());
        sysMsgMap.put("sysMsgTotal", sysMsgList.getTotal());
        sysMsgMap.put("anntMsgList", anntMsgList.getRecords());
        sysMsgMap.put("anntMsgTotal", anntMsgList.getTotal());
        result.setSuccess(true);
        result.setResult(sysMsgMap);
        return result;
    }


    /**
     * 导出excel
     *
     * @param sysAnnouncement
     * @param request
     * @return
     */
    @AutoLog(value = "系统通告-导出excel")
    @ApiOperation(value = "系统通告-导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysAnnouncement sysAnnouncement, HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysAnnouncement> queryWrapper = QueryGenerator.initQueryWrapper(sysAnnouncement, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
        queryWrapper.eq("del_flag", "0");
        List<SysAnnouncement> pageList = sysAnnouncementService.list(queryWrapper);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "系统通告列表");
        mv.addObject(NormalExcelConstants.CLASS, SysAnnouncement.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("系统通告列表数据", "导出人:"+user.getRealname(), "导出信息"));
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
    @AutoLog(value = "系统通告-通过excel导入数据")
    @ApiOperation(value = "系统通告-通过excel导入数据")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
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
                List<SysAnnouncement> listSysAnnouncements = ExcelImportUtil.importExcel(file.getInputStream(), SysAnnouncement.class, params);
                for (SysAnnouncement sysAnnouncementExcel : listSysAnnouncements) {
                    if (sysAnnouncementExcel.getDelFlag() == null) {
                        sysAnnouncementExcel.setDelFlag("0");
                    }
                    sysAnnouncementService.save(sysAnnouncementExcel);
                }
                return Result.ok("文件导入成功！数据行数：" + listSysAnnouncements.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败！");
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }
}
