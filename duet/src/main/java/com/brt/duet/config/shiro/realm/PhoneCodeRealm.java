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

import com.brt.duet.constant.OperatorConstant;
import com.brt.duet.constant.table.sys.UserConstant;
import com.brt.duet.service.sys.UserService;
import com.brt.duet.util.MybatisUtil;
import com.github.pagehelper.Page;

public class PhoneCodeRealm extends BaseRealm {
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
		
		Map<String, Object> user = null;
		
		String phone = (String) authcToken.getPrincipal();
		mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.PHONE.getTableColumn(), OperatorConstant.EQUAL_TO, phone);
		
		Page<Map<String, Object>> users = userService.select(columns, mapWhere);
		if (users == null || users.size() == 0) {
			throw new UnknownAccountException();
		}
		user = users.get(0);
		
		//验证码验证
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				user.get(UserConstant.PHONE.getKey()),
				null,
				getName()
		);
		
		//用户信息存入session
		setSession(user);
		
		return authenticationInfo;
	}
	
}