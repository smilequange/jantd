package cn.jantd.modules.ngalain.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.system.vo.DictModel;
import cn.jantd.core.system.vo.LoginUser;
import cn.jantd.modules.ngalain.service.NgAlainService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import cn.jantd.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xiagf
 * @date 2019-07-04
 */
@Slf4j
@RestController
@RequestMapping("/sys/ng-alain")
public class NgAlainController {
    @Autowired
    private NgAlainService ngAlainService;
    @Autowired
    private ISysDictService sysDictService;

    @AutoLog(value = "获取AppData")
    @ApiOperation(value = "获取AppData", httpMethod = "GET")
    @RequestMapping(value = "/getAppData")
    @ResponseBody
    public JSONObject getAppData(HttpServletRequest request) throws Exception {
        String token = request.getHeader("X-Access-Token");
        JSONObject j = new JSONObject();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        JSONObject userObjcet = new JSONObject();
        userObjcet.put("name", user.getUsername());
        userObjcet.put("avatar", user.getAvatar());
        userObjcet.put("email", user.getEmail());
        userObjcet.put("token", token);
        j.put("user", userObjcet);
        j.put("menu", ngAlainService.getMenu(user.getUsername()));
        JSONObject app = new JSONObject();
        app.put("name", "jantd-boot-angular");
        app.put("description", "jantd+ng-alain整合版本");
        j.put("app", app);
        return j;
    }

    @AutoLog(value = "通过查询指定code获取字典")
    @ApiOperation(value = "通过查询指定code获取字典")
    @RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
    public Object getDictItems(@PathVariable String dictCode) {
        log.info(" dictCode : " + dictCode);
        Result<List<DictModel>> result = new Result<List<DictModel>>();
        List<DictModel> ls = null;
        try {
            ls = sysDictService.queryDictItemsByCode(dictCode);
            result.setSuccess(true);
            result.setResult(ls);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
            return result;
        }
        List<JSONObject> dictlist = new ArrayList<>();
        for (DictModel l : ls) {
            JSONObject dict = new JSONObject();
            try {
                dict.put("value", Integer.parseInt(l.getValue()));
            } catch (NumberFormatException e) {
                dict.put("value", l.getValue());
            }
            dict.put("label", l.getText());
            dictlist.add(dict);
        }
        return dictlist;
    }

    @AutoLog(value = "通过表名获取字典")
    @ApiOperation(value = "通过表名获取字典")
    @RequestMapping(value = "/getDictItemsByTable/{table}/{key}/{value}", method = RequestMethod.GET)
    public Object getDictItemsByTable(@PathVariable String table, @PathVariable String key, @PathVariable String value) {
        return this.ngAlainService.getDictByTable(table, key, value);
    }
}
