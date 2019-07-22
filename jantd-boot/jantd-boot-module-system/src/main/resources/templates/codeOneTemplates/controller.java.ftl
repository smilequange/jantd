package ${package.Controller};

import cn.jantd.core.annotation.AutoLog;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.poi.def.NormalExcelConstants;
import cn.jantd.core.poi.excel.ExcelImportUtil;
import cn.jantd.core.poi.excel.entity.ExportParams;
import cn.jantd.core.poi.excel.entity.ImportParams;
import cn.jantd.core.poi.view.JantdEntityExcelViewBase;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.system.vo.LoginUser;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Description: ${table.comment}
 * @Author: ${author}
 * @Date: ${.now?string["yyyy-MM-dd"]}
 */
@Slf4j
@Api(tags = "${table.comment}")
@RestController
@RequestMapping("/${entity?uncap_first}/${entity?uncap_first}")
public class ${entity}Controller {
	@Autowired
	private I${entity}Service ${entity?uncap_first}Service;

	/**
	 * 分页列表查询
	 *
	 * @param ${entity?uncap_first}
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "${table.comment}-分页列表查询")
	@ApiOperation(value = "${table.comment}-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<${entity}>> queryPageList(${entity} ${entity?uncap_first},
											 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									  		 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									  		 HttpServletRequest req) {
		Result<IPage<${entity}>> result = new Result<>();
		QueryWrapper<${entity}> queryWrapper = QueryGenerator.initQueryWrapper(${entity?uncap_first}, req.getParameterMap());
		Page<${entity}> page = new Page<${entity}>(pageNo, pageSize);
		IPage<${entity}> pageList = ${entity?uncap_first}Service.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加
     *
	 * @param ${entity?uncap_first}
	 * @return
	 */
	@AutoLog(value = "${table.comment}-添加")
	@ApiOperation(value = "${table.comment}-添加")
	@PostMapping(value = "/add")
	public Result<${entity}> add(@RequestBody ${entity} ${entity?uncap_first}) {
		Result<${entity}> result = new Result<>();
		try {
			${entity?uncap_first}Service.save(${entity?uncap_first});
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
     *  编辑
	 *
	 * @param ${entity?uncap_first}
	 * @return
	 */
	@AutoLog(value = "${table.comment}-编辑")
	@ApiOperation(value = "${table.comment}-编辑")
	@PutMapping(value = "/edit")
	public Result<${entity}> edit(@RequestBody ${entity} ${entity?uncap_first}) {
		Result<${entity}> result = new Result<>();
		${entity} ${entity?uncap_first}Entity = ${entity?uncap_first}Service.getById(${entity?uncap_first}.getId());
		if(${entity?uncap_first}Entity == null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = ${entity?uncap_first}Service.updateById(${entity?uncap_first});
			if(ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
     *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "${table.comment}-通过id删除")
	@ApiOperation(value = "${table.comment}-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<${entity}> delete(@RequestParam(name = "id", required = true) String id) {
		Result<${entity}> result = new Result<>();
		${entity} ${entity?uncap_first} = ${entity?uncap_first}Service.getById(id);
		if(${entity?uncap_first} == null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = ${entity?uncap_first}Service.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}

		return result;
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "${table.comment}-批量删除")
	@ApiOperation(value = "${table.comment}-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<${entity}> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<${entity}> result = new Result<>();
		if(ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.${entity?uncap_first}Service.removeByIds(Arrays.asList(ids.split(",")));
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
	@AutoLog(value = "${table.comment}-通过id查询")
	@ApiOperation(value = "${table.comment}-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<${entity}> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<${entity}> result = new Result<>();
		${entity} ${entity?uncap_first} = ${entity?uncap_first}Service.getById(id);
		if(${entity?uncap_first} == null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(${entity?uncap_first});
			result.setSuccess(true);
		}
		return result;
	}

  /**
   * 导出excel
   *
   * @param request
   * @param ${entity?uncap_first}
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(${entity} ${entity?uncap_first}, HttpServletRequest request) {
      // Step.1 组装查询条件
      QueryWrapper<${entity}> queryWrapper = QueryGenerator.initQueryWrapper(${entity?uncap_first}, request.getParameterMap());
      List<${entity}> pageList = ${entity?uncap_first}Service.list(queryWrapper);
      // Step.2 AutoPoi 导出Excel
      LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "${table.comment}列表");
      mv.addObject(NormalExcelConstants.CLASS, ${entity}.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("${table.comment}列表数据", "导出人:"+user.getRealname(), "导出信息"));
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
              List<${entity}> list${entity}s = ExcelImportUtil.importExcel(file.getInputStream(), ${entity}.class, params);
              for (${entity} ${entity?uncap_first}Excel : list${entity}s) {
                  ${entity?uncap_first}Service.save(${entity?uncap_first}Excel);
              }
              return Result.ok("文件导入成功！数据行数:" + list${entity}s.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
				  log.error(e.getMessage(),e);
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
