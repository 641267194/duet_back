package com.brt.duet.config.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.brt.duet.constant.OperatorConstant;
import com.brt.duet.constant.SessionConstant;
import com.brt.duet.constant.table.sys.ModuleConstant;
import com.brt.duet.constant.table.sys.UserConstant;
import com.brt.duet.service.sys.ModuleService;
import com.brt.duet.service.sys.UserService;
import com.brt.duet.util.MybatisUtil;
import com.brt.duet.util.TreeUtil;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 自定义Realm
 */
public class UserRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	
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
	@Override
	@SuppressWarnings("unchecked")
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Session session = SecurityUtils.getSubject().getSession();
		//为当前用户设置角色和权限
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addStringPermissions((Collection<String>) session.getAttribute(SessionConstant.USER_PERMISSION));
		return authorizationInfo;
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param authcToken
	 * @return
	 * @throws AuthenticationException 
	 * @description 验证当前登录的Subject,LoginController.login()方法中执行Subject.login()时 执行此方法
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken) 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		String username = (String) authcToken.getPrincipal();
		// 获取用户密码
		String password = new String((char[]) authcToken.getCredentials());
		Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, UserConstant.USERNAME.getTableColumn(), OperatorConstant.EQUAL_TO, username);
		mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.ENABLED.getTableColumn(), OperatorConstant.EQUAL_TO, true);
		mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.DEL_FLAG.getTableColumn(), OperatorConstant.EQUAL_TO, false);
		
		Set<String> columns = new HashSet<>();
		columns.add(UserConstant.ID.getTableColumn());
		columns.add(UserConstant.USERNAME.getTableColumn());
		columns.add(UserConstant.PASSWORD.getTableColumn());
		columns.add(UserConstant.NAME.getTableColumn());
		columns.add(UserConstant.PHONE.getTableColumn());
		columns.add(UserConstant.ENABLED.getTableColumn());
		columns.add(UserConstant.CREATE_AT.getTableColumn());
		columns.add(UserConstant.UPDATE_AT.getTableColumn());
		columns.add(UserConstant.ROLES.getTableColumn());
		
		Page<Map<String, Object>> users = userService.select(columns, mapWhere);
		if (users == null || users.size() == 0) {
			//没找到帐号
			throw new UnknownAccountException();
		}
		Map<String, Object> user = users.get(0);
		if (!user.get(UserConstant.PASSWORD.getKey()).toString().equals(password)) {
			//密码错误
			throw new IncorrectCredentialsException();
		}
		//交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				user.get(UserConstant.USERNAME.getKey()),
				user.get(UserConstant.PASSWORD.getKey()),
				//ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
				getName()
		);
		//session中不需要保存密码
		user.remove(UserConstant.PASSWORD.getKey());
		
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
		return authenticationInfo;
	}
	
}

