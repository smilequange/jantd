package cn.jantd.modules.demo.test.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.jantd.core.api.vo.Result;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 订单模拟
 * @Author: ZhiLin
 * @Date: 2019-02-20
 * @Version: v1.0
 */
@Slf4j
@RestController
@RequestMapping("/test/order")
public class JantdOrderDMainController {
    @Autowired
    private IJantdOrderMainService jantdOrderMainService;
    @Autowired
    private IJantdOrderCustomerService jantdOrderCustomerService;
    @Autowired
    private IJantdOrderTicketService jantdOrderTicketService;
    @Autowired
    private IJantdOrderCustomerService customerService;
    @Autowired
    private IJantdOrderTicketService ticketService;

    /**
     * 分页列表查询
     *
     * @param jantdOrderMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/orderList")
    public Result<IPage<JantdOrderMain>> respondePagedData(JantdOrderMain jantdOrderMain,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
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
            jantdOrderMainService.save(jantdOrderMain);
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
    @PutMapping("/edit")
    public Result<JantdOrderMain> edit(@RequestBody JantdOrderMainPage jantdOrderMainPage) {
        Result<JantdOrderMain> result = new Result<JantdOrderMain>();
        JantdOrderMain jantdOrderMain = new JantdOrderMain();
        BeanUtils.copyProperties(jantdOrderMainPage, jantdOrderMain);
        JantdOrderMain jantdOrderMainEntity = jantdOrderMainService.getById(jantdOrderMain.getId());
        if (jantdOrderMainEntity == null) {
            result.error500("未找到对应实体");
        } else {
            jantdOrderMainService.updateById(jantdOrderMain);
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
            this.jantdOrderMainService.removeByIds(Arrays.asList(ids.split(",")));
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
     * @param mainId
     * @return
     */
    @GetMapping(value = "/listOrderCustomerByMainId")
    public Result<List<JantdOrderCustomer>> queryOrderCustomerListByMainId(@RequestParam(name = "mainId", required = false) String mainId) {
        Result<List<JantdOrderCustomer>> result = new Result<List<JantdOrderCustomer>>();
        List<JantdOrderCustomer> jantdOrderCustomerList = null;
        if (mainId != null) {
            jantdOrderCustomerList = jantdOrderCustomerService.selectCustomersByMainId(mainId);
            result.setResult(jantdOrderCustomerList);
            result.setSuccess(true);
            return result;
        } else {
            return null;
        }
    }

    /**
     * 通过id查询
     *
     * @param mainId
     * @return
     */
    @GetMapping(value = "/listOrderTicketByMainId")
    public Result<List<JantdOrderTicket>> queryOrderTicketListByMainId(@RequestParam(name = "mainId", required = false) String mainId) {
        Result<List<JantdOrderTicket>> result = new Result<List<JantdOrderTicket>>();
        List<JantdOrderTicket> jantdOrderTicketList = null;
        if (mainId != null) {
            jantdOrderTicketList = jantdOrderTicketService.selectTicketsByMainId(mainId);
            result.setResult(jantdOrderTicketList);
            result.setSuccess(true);
            return result;
        } else {
            return null;
        }
    }

// ================================以下是客户信息相关的API=================================

    /**
     * 添加
     *
     * @param jantdOrderCustomer
     * @return
     */
    @PostMapping(value = "/addCustomer")
    public Result<JantdOrderCustomer> addCustomer(@RequestBody JantdOrderCustomer jantdOrderCustomer) {
        Result<JantdOrderCustomer> result = new Result<>();
        try {
            boolean ok = customerService.save(jantdOrderCustomer);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("添加数据成功");
            } else {
                result.setSuccess(false);
                result.setMessage("添加数据失败");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("遇到问题了!");
            return result;
        }

    }

    /**
     * 编辑
     *
     * @param jantdOrderCustomer
     * @return
     */
    @PutMapping("/editCustomer")
    public Result<JantdOrderCustomer> editCustomer(@RequestBody JantdOrderCustomer jantdOrderCustomer) {
        Result<JantdOrderCustomer> result = new Result<>();
        try {
            boolean ok = customerService.updateById(jantdOrderCustomer);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("更新成功");
            } else {
                result.setSuccess(false);
                result.setMessage("更新失败");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(true);
            result.setMessage("更新中碰到异常了");
            return result;
        }

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteCustomer")
    public Result<JantdOrderCustomer> deleteCustomer(@RequestParam(name = "id", required = true) String id) {
        Result<JantdOrderCustomer> result = new Result<>();
        try {
            boolean ok = customerService.removeById(id);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("删除成功");
            } else {
                result.setSuccess(false);
                result.setMessage("删除失败");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("删除过程中碰到异常了");
            return result;
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchCustomer")
    public Result<JantdOrderCustomer> deleteBatchCustomer(@RequestParam(name = "ids", required = true) String ids) {
        Result<JantdOrderCustomer> result = new Result<JantdOrderCustomer>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.customerService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }
//===========================以下是机票的相关API====================================

    /**
     * 添加
     *
     * @param jantdOrderTicket
     * @return
     */
    @PostMapping(value = "/addTicket")
    public Result<JantdOrderTicket> addTicket(@RequestBody JantdOrderTicket jantdOrderTicket) {
        Result<JantdOrderTicket> result = new Result<>();
        try {
            boolean ok = ticketService.save(jantdOrderTicket);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("添加机票信息成功.");
            } else {
                result.setSuccess(false);
                result.setMessage("添加机票信息失败!");
            }
            return result;
        } catch (Exception e) {
            e.fillInStackTrace();
            result.setSuccess(false);
            result.setMessage("添加机票信息过程中出现了异常: " + e.getMessage());
            return result;
        }

    }

    /**
     * 编辑
     *
     * @param jantdOrderTicket
     * @return
     */
    @PutMapping("/editTicket")
    public Result<JantdOrderTicket> editTicket(@RequestBody JantdOrderTicket jantdOrderTicket) {
        Result<JantdOrderTicket> result = new Result<>();
        try {
            boolean ok = ticketService.updateById(jantdOrderTicket);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("更新数据成功.");
            } else {
                result.setSuccess(false);
                result.setMessage("更新机票 信息失败!");
            }
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("更新数据过程中出现异常啦: " + e.getMessage());
            return result;
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteTicket")
    public Result<JantdOrderTicket> deleteTicket(@RequestParam(name = "id", required = true) String id) {
        Result<JantdOrderTicket> result = new Result<>();
        try {
            boolean ok = ticketService.removeById(id);
            if (ok) {
                result.setSuccess(true);
                result.setMessage("删除机票信息成功.");
            } else {
                result.setSuccess(false);
                result.setMessage("删除机票信息失败!");
            }
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("删除机票信息过程中出现异常啦: " + e.getMessage());
            return result;
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatchTicket")
    public Result<JantdOrderTicket> deleteBatchTicket(@RequestParam(name = "ids", required = true) String ids) {
        Result<JantdOrderTicket> result = new Result<JantdOrderTicket>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.ticketService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

}
