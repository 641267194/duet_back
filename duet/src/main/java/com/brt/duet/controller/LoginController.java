package com.brt.duet.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.brt.duet.util.ResponseUtil;

/**
 * @author 方杰
 * @date 2019年8月10日
 * @description 登录登出控制层
 */
@RestController
public class LoginController {
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param username 用户名
	 * @param password 密码
	 * @return 是否成功
	 * @description 后台用户登录
	 */
	@RequestMapping("/login")
	public JSONObject login(String username, String password) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		try{
			//如果执行subject.login(tocken);没有抛出异常，则证明登陆成功。
			//走到这里会进入下一个类
			subject.login(token);
			return ResponseUtil.responseResult(true, "登录成功", null);
		}catch(Exception e){
			//如果执行subject.login(tocken);抛出异常，则证明登陆失败。
			return ResponseUtil.responseResult(false, "用户名密码错误", null);
		}
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 是否成功
	 * @description 后台退出
	 */
	@RequestMapping("/logout")
	public JSONObject logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return ResponseUtil.responseResult(true, "退出成功", null);
	}
	
}
