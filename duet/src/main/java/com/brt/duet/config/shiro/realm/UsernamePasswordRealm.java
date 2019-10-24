package com.brt.duet.config.shiro.realm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;
import com.brt.duet.constant.OperatorConstant;
import com.brt.duet.constant.table.sys.UserConstant;
import com.brt.duet.service.sys.UserService;
import com.brt.duet.util.MybatisUtil;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 自定义Realm
 */
public class UsernamePasswordRealm extends BaseRealm {
	@Autowired
	private UserService userService;
	
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
		
		Map<String, List<Map<String, Object>>> mapWhere = null;
		mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.ENABLED.getTableColumn(), OperatorConstant.EQUAL_TO, true);
		mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.DEL_FLAG.getTableColumn(), OperatorConstant.EQUAL_TO, false);
		
		Set<String> columns = getDefaultColumns();
		//查询密码
		columns.add(UserConstant.PASSWORD.getTableColumn());
		
		Map<String, Object> user = null;
		
		String username = (String) authcToken.getPrincipal();
		mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.USERNAME.getTableColumn(), OperatorConstant.EQUAL_TO, username);
		
		Page<Map<String, Object>> users = userService.select(columns, mapWhere);
		if (users == null || users.size() == 0) {
			throw new UnknownAccountException();
		}
		user = users.get(0);
		
		//密码匹配
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				user.get(UserConstant.USERNAME.getKey()),
				user.get(UserConstant.PASSWORD.getKey()),
				//ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
				getName()
		);
		//session中不需要保存密码
		user.remove(UserConstant.PASSWORD.getKey());
		
		//用户信息存入session
		setSession(user);
		
		return authenticationInfo;
	}
	
}

