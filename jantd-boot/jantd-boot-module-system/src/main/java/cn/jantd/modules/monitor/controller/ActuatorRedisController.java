package cn.jantd.modules.monitor.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.modules.monitor.service.RedisService;
import cn.jantd.modules.monitor.domain.RedisInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xiagf
 * @date 2019-07-04
 */
@Slf4j
@RestController
@RequestMapping("/actuator/redis")
public class ActuatorRedisController {

    @Autowired
    private RedisService redisService;

    /**
     * Redis详细信息
     *
     * @return
     * @throws Exception
     */
    @AutoLog(value = "获取redis信息")
    @ApiOperation(value = "获取redis信息", httpMethod = "GET")
    @GetMapping("/info")
    public Result<?> getRedisInfo() throws Exception {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        log.info(infoList.toString());
        return Result.ok(infoList);
    }

    @AutoLog(value = "查看Radis Key 实时数量")
    @ApiOperation(value = "查看Radis Key 实时数量", httpMethod = "GET")
    @GetMapping("/keysSize")
    public Map<String, Object> getKeysSize() throws Exception {
        return redisService.getKeysSize();
    }

    @AutoLog(value = "查看redis内存信息")
    @ApiOperation(value = "查看redis内存信息", httpMethod = "GET")
    @GetMapping("/memoryInfo")
    public Map<String, Object> getMemoryInfo() throws Exception {
        return redisService.getMemoryInfo();
    }

    /**
     * @param request
     * @param response
     * @return
     * @功能：获取磁盘信息
     */
    @AutoLog(value = "查看磁盘信息")
    @ApiOperation(value = "查看磁盘信息", httpMethod = "GET")
    @GetMapping("/queryDiskInfo")
    public Result<List<Map<String, Object>>> queryDiskInfo(HttpServletRequest request, HttpServletResponse response) {
        Result<List<Map<String, Object>>> res = new Result<>();
        try {
            // 当前文件系统类
            FileSystemView fsv = FileSystemView.getFileSystemView();
            // 列出所有windows 磁盘
            File[] fs = File.listRoots();
            log.info("查询磁盘信息:" + fs.length + "个");
            List<Map<String, Object>> list = new ArrayList<>();

            for (int i = 0; i < fs.length; i++) {
                if (fs[i].getTotalSpace() == 0) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>(16);
                map.put("name", fsv.getSystemDisplayName(fs[i]));
                map.put("max", fs[i].getTotalSpace());
                map.put("rest", fs[i].getFreeSpace());
                map.put("restPPT", (fs[i].getTotalSpace() - fs[i].getFreeSpace()) * 100 / fs[i].getTotalSpace());
                list.add(map);
                log.info(map.toString());
            }
            res.setResult(list);
            res.success("查询成功");
        } catch (Exception e) {
            res.error500("查询失败" + e.getMessage());
        }
        return res;
    }
}
