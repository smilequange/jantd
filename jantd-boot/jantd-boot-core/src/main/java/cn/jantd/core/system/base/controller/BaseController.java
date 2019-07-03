package cn.jantd.core.system.base.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jantd.core.poi.def.NormalExcelConstants;
import cn.jantd.core.poi.excel.ExcelImportUtil;
import cn.jantd.core.poi.excel.entity.ExportParams;
import cn.jantd.core.poi.excel.entity.ImportParams;
import cn.jantd.core.poi.view.JantdEntityExcelViewBase;
import org.apache.shiro.SecurityUtils;
import cn.jantd.core.api.vo.Result;
import cn.jantd.core.system.base.entity.BaseEntity;
import cn.jantd.core.system.base.service.BaseService;
import cn.jantd.core.system.query.QueryGenerator;
import cn.jantd.core.system.vo.LoginUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description Controller基类
 * @Author 圈哥
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2019/7/2
 */
@Slf4j
public class BaseController<T extends BaseEntity, S extends BaseService<T>> {
	@Autowired
	S service;
	/**
	 * 导出excel
	 * @param request
	 * @param object
	 * @param clazz
	 * @param title
	 * @return
	 */
	protected ModelAndView exportXls(HttpServletRequest request,T object,Class<T> clazz,String title) {
		//获取当前用户
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		// 组装查询条件
		QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
		//AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JantdEntityExcelViewBase());
		List<T> pageList = service.list(queryWrapper);
		//注意：此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.FILE_NAME, title);
		mv.addObject(NormalExcelConstants.CLASS, clazz);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:"+sysUser.getRealname(), title + "表"));
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
	protected Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<T> clazz) {
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
				List<T> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
				for (T t : list) {
					service.save(t);
				}
				return Result.ok("文件导入成功！数据行数：" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
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
