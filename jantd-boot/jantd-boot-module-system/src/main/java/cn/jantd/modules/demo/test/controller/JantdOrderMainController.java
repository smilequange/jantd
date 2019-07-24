package cn.jantd.modules.demo.test.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jantd.core.api.vo.Result;
import cn.jantd.core.poi.def.NormalExcelConstants;
import cn.jantd.core.poi.excel.ExcelImportUtil;
import cn.jantd.core.poi.excel.entity.ExportParams;
import cn.jantd.core.poi.excel.entity.ImportParams;
import cn.jantd.core.poi.view.JantdEntityExcelViewBase;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.modules.demo.test.entity.JantdOrderCustomer;
import cn.jantd.modules.demo.test.entity.JantdOrderMain;
import cn.jantd.modules.demo.test.entity.JantdOrderTicket;
import cn.jantd.modules.demo.test.service.IJantdOrderCustomerService;
import cn.jantd.modules.demo.test.service.IJantdOrderMainService;
import cn.jantd.modules.demo.test.service.IJantdOrderTicketService;
import cn.jantd.modules.demo.test.vo.JantdOrderMainPage;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 订单
 * @Author xiagf
 * @Date:2019-02-15
 * @Version: V1.0
 */
@RestController
@RequestMapping("/test/jantdOrderMain")
@Slf4j
public class JantdOrderMainController {
    @Autowired
    private IJantdOrderMainService jantdOrderMainService;
    @Autowired
    private IJantdOrderCustomerService jantdOrderCustomerService;
    @Autowired
    private IJantdOrderTicketService jantdOrderTicketService;

    /**
     * 分页列表查询
     *
     * @param jantdOrderMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<JantdOrderMain>> queryPageList(JantdOrderMain jantdOrderMain, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<JantdOrderMain>> result = new Result<IPage<JantdOrderMain>>();
        QueryWrapper<JantdOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(jantdOrderMain, req.getParameterMap());
        Page<JantdOrderMain> page = new Page<JantdOrderMain>(pageNo, pageSize);
        IPage<JantdOrderMain> pageList = jantdOrderMainService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param jantdOrderMainPage
     * @return
     */
    @PostMapping(value = "/add")
    public Result<JantdOrderMain> add(@RequestBody JantdOrderMainPage jantdOrderMainPage) {
        Result<JantdOrderMain> result = new Result<JantdOrderMain>();
        try {
            JantdOrderMain jantdOrderMain = new JantdOrderMain();
            BeanUtils.copyProperties(jantdOrderMainPage, jantdOrderMain);
            jantdOrderMainService.saveMain(jantdOrderMain, jantdOrderMainPage.getJantdOrderCustomerList(), jantdOrderMainPage.getJantdOrderTicketList());
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
     * @param jantdOrderMainPage
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<JantdOrderMain> eidt(@RequestBody JantdOrderMainPage jantdOrderMainPage) {
        Result<JantdOrderMain> result = new Result<JantdOrderMain>();
        JantdOrderMain jantdOrderMain = new JantdOrderMain();
        BeanUtils.copyProperties(jantdOrderMainPage, jantdOrderMain);
        JantdOrderMain jantdOrderMainEntity = jantdOrderMainService.getById(jantdOrderMain.getId());
        if (jantdOrderMainEntity == null) {
            result.error500("未找到对应实体");
        } else {
            jantdOrderMainService.updateMain(jantdOrderMain, jantdOrderMainPage.getJantdOrderCustomerList(), jantdOrderMainPage.getJantdOrderTicketList());
            result.success("修改成功!");
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<JantdOrderMain> delete(@RequestParam(name = "id", required = true) String id) {
        Result<JantdOrderMain> result = new Result<JantdOrderMain>();
        JantdOrderMain jantdOrderMain = jantdOrderMainService.getById(id);
        if (jantdOrderMain == null) {
            result.error500("未找到对应实体");
        } else {
            jantdOrderMainService.delMain(id);
            result.success("删除成功!");
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
    public Result<JantdOrderMain> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<JantdOrderMain> result = new Result<JantdOrderMain>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.jantdOrderMainService.delBatchMain(Arrays.asList(ids.split(",")));
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
    public Result<JantdOrderMain> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<JantdOrderMain> result = new Result<JantdOrderMain>();
        JantdOrderMain jantdOrderMain = jantdOrderMainService.getById(id);
        if (jantdOrderMain == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(jantdOrderMain);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryOrderCustomerListByMainId")
    public Result<List<JantdOrderCustomer>> queryOrderCustomerListByMainId(@RequestParam(name = "id", required = true) String id) {
        Result<List<JantdOrderCustomer>> result = new Result<List<JantdOrderCustomer>>();
        List<JantdOrderCustomer> jantdOrderCustomerList = jantdOrderCustomerService.selectCustomersByMainId(id);
        result.setResult(jantdOrderCustomerList);
        result.setSuccess(true);
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryOrderTicketListByMainId")
    public Result<List<JantdOrderTicket>> queryOrderTicketListByMainId(@RequestParam(name = "id", required = true) String id) {
        Result<List<JantdOrderTicket>> result = new Result<List<JantdOrderTicket>>();
        List<JantdOrderTicket> jantdOrderTicketList = jantdOrderTicketService.selectTicketsByMainId(id);
        result.setResult(jantdOrderTicketList);
        result.setSuccess(true);
        return result;
    }

    /**
     * 导出excel
     *
     * @param request
     * @param jantdOrderMain
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, JantdOrderMain jantdOrderMain) {
        // Step.1 组装查询条件
        QueryWrapper<JantdOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(jantdOrderMain, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
        List<JantdOrderMainPage> pageList = new ArrayList<JantdOrderMainPage>();

        List<JantdOrderMain> jantdOrderMainList = jantdOrderMainService.list(queryWrapper);
        for (JantdOrderMain orderMain : jantdOrderMainList) {
            JantdOrderMainPage vo = new JantdOrderMainPage();
            BeanUtils.copyProperties(orderMain, vo);
            // 查询机票
            List<JantdOrderTicket> jantdOrderTicketList = jantdOrderTicketService.selectTicketsByMainId(orderMain.getId());
            vo.setJantdOrderTicketList(jantdOrderTicketList);
            // 查询客户
            List<JantdOrderCustomer> jantdOrderCustomerList = jantdOrderCustomerService.selectCustomersByMainId(orderMain.getId());
            vo.setJantdOrderCustomerList(jantdOrderCustomerList);
            pageList.add(vo);
        }

        // 导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "一对多导出文件名字");
        // 注解对象Class
        mv.addObject(NormalExcelConstants.CLASS, JantdOrderMainPage.class);
        // 自定义表格参数
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("自定义导出Excel内容标题", "导出人:Jantd", "自定义Sheet名字"));
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
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
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
                List<JantdOrderMainPage> list = ExcelImportUtil.importExcel(file.getInputStream(), JantdOrderMainPage.class, params);
                for (JantdOrderMainPage page : list) {
                    JantdOrderMain po = new JantdOrderMain();
                    BeanUtils.copyProperties(page, po);
                    jantdOrderMainService.saveMain(po, page.getJantdOrderCustomerList(), page.getJantdOrderTicketList());
                }
                return Result.ok("文件导入成功！");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败：" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

}
