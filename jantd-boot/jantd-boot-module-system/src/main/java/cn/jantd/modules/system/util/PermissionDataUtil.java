package cn.jantd.modules.system.util;

import java.util.List;

import cn.jantd.core.constant.SystemConstant;
import cn.jantd.core.util.oConvertUtils;
import cn.jantd.modules.system.entity.SysPermission;

/**
 * @Author xiagf
 * @Date: 2019-04-03
 */
public class PermissionDataUtil {
	private PermissionDataUtil(){}

	/**
	 * 智能处理错误数据，简化用户失误操作
	 *
	 * @param permission
	 */
	public static SysPermission intelligentProcessData(SysPermission permission) {
		if (permission == null) {
			return null;
		}

		// 组件
		if (oConvertUtils.isNotEmpty(permission.getComponent())) {
			String component = permission.getComponent();
			if (component.startsWith(SystemConstant.LEFT_SLASH)) {
				component = component.substring(1);
			}
			if (component.startsWith(SystemConstant.PATH_VIEWS)) {
				component = component.replaceFirst("views/", "");
			}
			if (component.startsWith(SystemConstant.PATH_SRC_VIEWS)) {
				component = component.replaceFirst("src/views/", "");
			}
			if (component.endsWith(SystemConstant.FILE_NAME_END_WITH_VUE)) {
				component = component.replace(".vue", "");
			}
			permission.setComponent(component);
		}

		// 请求URL
		if (oConvertUtils.isNotEmpty(permission.getUrl())) {
			String url = permission.getUrl();
			if (url.endsWith(SystemConstant.FILE_NAME_END_WITH_VUE)) {
				url = url.replace(".vue", "");
			}
			if (!url.startsWith(SystemConstant.WEB_PREFIX) && !url.startsWith(SystemConstant.LEFT_SLASH)&&!url.trim().startsWith("{{")) {
				url = "/" + url;
			}
			permission.setUrl(url);
		}

		// 一级菜单默认组件
		if (0 == permission.getMenuType() && oConvertUtils.isEmpty(permission.getComponent())) {
			// 一级菜单默认组件
			permission.setComponent("layouts/RouteView");
		}
		return permission;
	}

	/**
	 * 如果没有index页面 需要new 一个放到list中
	 * @param metaList
	 */
	public static void addIndexPage(List<SysPermission> metaList) {
		boolean hasIndexMenu = false;
		for (SysPermission sysPermission : metaList) {
			if("首页".equals(sysPermission.getName())) {
				hasIndexMenu = true;
				break;
			}
		}
		if(!hasIndexMenu) {
			metaList.add(0,new SysPermission(true));
		}
	}

}
