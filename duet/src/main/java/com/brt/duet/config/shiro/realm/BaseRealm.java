package com.brt.duet.config.shiro.realm;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.brt.duet.constant.SessionConstant;
import com.brt.duet.constant.table.sys.ModuleConstant;
import com.brt.duet.constant.table.sys.RoleConstant;
import com.brt.duet.constant.table.sys.UserConstant;
import com.brt.duet.service.sys.ModuleService;
import com.brt.duet.util.TreeUtil;

public abstract class BaseRealm extends AuthorizingRealm {
	
	@Autowired
	private ModuleService moduleService;
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param principals
	 * @return 权限信息
	 * @description 从session中获取权限
	 * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection) 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Session session = SecurityUtils.getSubject().getSession();
		//为当前用户设置权限
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addStringPermissions((Collection<String>) session.getAttribute(SessionConstant.USER_PERMISSION));
		//为当前用户设置角色
		Map<String, Object> user = (Map<String, Object>) session.getAttribute(SessionConstant.USER_SEESION);
		List<Map<String, Object>> roles = (List<Map<String, Object>>) user.get(UserConstant.ROLES.getKey());
		Set<String> roleSet = new HashSet<>();
		for (Map<String, Object> role : roles) {
			roleSet.add(role.get(RoleConstant.CODE.getKey()).toString());
		}
		authorizationInfo.addRoles(roleSet);
		return authorizationInfo;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年9月7日
	 * @return 
	 * @description 获取需要在session中存储的列
	 */
	protected Set<String> getDefaultColumns() {
		Set<String> columns = new HashSet<>();
		columns.add(UserConstant.ID.getTableColumn());
		columns.add(UserConstant.USERNAME.getTableColumn());
		columns.add(UserConstant.NAME.getTableColumn());
		columns.add(UserConstant.PHONE.getTableColumn());
		columns.add(UserConstant.ROLES.getTableColumn());
		return columns;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年9月7日
	 * @param user
	 * @param moduleService 
	 * @description 将用户和权限等信息放入session
	 */
	protected void setSession(Map<String, Object> user) {
		//查询用户权限和菜单
		String userId = user.get(UserConstant.ID.getKey()).toString();
		List<Map<String, Object>> modules = moduleService.getModulesByUserId(userId);
		Set<String> permissionSet = new HashSet<>();
		for (Map<String, Object> module : modules) {
			if (! (boolean) module.get(ModuleConstant.IS_MENU.getKey())) {
				if (module.get(ModuleConstant.URI.getKey()) != null) {
					permissionSet.add(module.get(ModuleConstant.URI.getKey()).toString());
				}
			}
		}
		JSONArray menu = JSONObject.parseArray(JSONObject.toJSONString(modules));
		menu = TreeUtil.jsonArrayToTree(menu, "id", "parentId");
		menu = TreeUtil.sortTree(menu, "sort", "children");
		
		Session session = SecurityUtils.getSubject().getSession();
		//将用户信息放入session中
		session.setAttribute(SessionConstant.USER_SEESION, user);
		//将菜单信息放入session中
		session.setAttribute(SessionConstant.USER_MENU, menu);
		//将权限信息放入session中
		session.setAttribute(SessionConstant.USER_PERMISSION, permissionSet);
		
		session.setTimeout(600000);
	}

}
