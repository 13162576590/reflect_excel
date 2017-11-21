package com.cyhong.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取登陆后用户信息
 * @author chenyouhong
 *
 */
public final class ContextUtil {
	
	private ContextUtil() {
	}

	/**
	 * 获取登陆用户
	 * @return
	 */
	public static String getOperator() {
		SessionInfo sessionInfo = (SessionInfo)getSession().getAttribute(ConfigUtil.getSessionInfoName());
		return sessionInfo.getName();
	}
	
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		return request;
	}
	
}
