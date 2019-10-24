package com.brt.duet.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.brt.duet.config.shiro.token.PhoneCodeToken;
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
	 * @param info 登录信息
	 * @return 是否成功
	 * @description 后台用户登录
	 */
	@RequestMapping("/login")
	public JSONObject login(String info) {
		//验证参数不为空
		if (StringUtils.isBlank(info)) {
			return ResponseUtil.responseResult(false, "参数[info]不能为空", null);
		} else {
			JSONObject infoJson = JSON.parseObject(info);
			
			AuthenticationToken token = null;
			
			String type = infoJson.getString("type");
			if ("USERNAMEPASSWORD".equals(type)) {
				String username = infoJson.getString("username");
				String password = infoJson.getString("password");
				token = new UsernamePasswordToken(username,password);
			} else if ("PHONECODE".equals(type)) {
				String phone = infoJson.getString("phone");
				String code = infoJson.getString("code");
				token = new PhoneCodeToken(phone,code);
			}
			
			Subject subject = SecurityUtils.getSubject();
			try{
				//如果执行subject.login(tocken);没有抛出异常，则证明登陆成功。
				//走到这里会进入下一个类
				subject.login(token);
				return ResponseUtil.responseResult(true, "登录成功", null);
			}catch(Exception e){
				//如果执行subject.login(tocken);抛出异常，则证明登陆失败。
				if ("USERNAMEPASSWORD".equals(type)) {
					return ResponseUtil.responseResult(false, "用户名密码错误", null);
				} else if ("PHONECODE".equals(type)) {
					return ResponseUtil.responseResult(false, "验证码错误", null);
				} else {
					return ResponseUtil.responseResult(false, "未知的登录类型", null);
				}
			}
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
