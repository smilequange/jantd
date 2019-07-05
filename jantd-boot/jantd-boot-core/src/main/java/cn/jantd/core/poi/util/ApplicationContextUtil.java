package cn.jantd.core.poi.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author  圈哥
 *
 */
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;


	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		ApplicationContextUtil.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}
}
