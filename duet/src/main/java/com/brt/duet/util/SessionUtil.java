package com.brt.duet.util;

import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;

import com.alibaba.fastjson.JSONArray;
import com.brt.duet.constant.SessionConstant;

public class SessionUtil {
	/**
	 * @author 方杰
	 * @date 2019年8月2日
	 * @return 用户基本信息
	 * @description 从session中获取用户基本信息
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getUserSession() {
		Map<String, Object> userSession = (Map<String, Object>) SecurityUtils.getSubject().getSession().getAttribute(SessionConstant.USER_SEESION);
		return userSession;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年8月2日
	 * @return 用户菜单
	 * @description 从session中获取用户菜单
	 */
	public static JSONArray getUserMenu() {
		JSONArray menu = (JSONArray) SecurityUtils.getSubject().getSession().getAttribute(SessionConstant.USER_MENU);
		return menu;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年8月2日
	 * @return 用户权限集合
	 * @description 从session中获取用户权限
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getUserPermission() {
		Set<String> permission = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute(SessionConstant.USER_PERMISSION);
		return permission;
	}
}
