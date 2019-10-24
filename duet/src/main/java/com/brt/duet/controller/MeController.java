package com.brt.duet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.brt.duet.constant.OperatorConstant;
import com.brt.duet.constant.table.sys.UserConstant;
import com.brt.duet.service.sys.UserService;
import com.brt.duet.util.MybatisUtil;
import com.brt.duet.util.ResponseUtil;
import com.brt.duet.util.SessionUtil;
import com.brt.duet.util.ValidateUtil;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 个人中心控制层
 */
@RestController
@RequestMapping("/me")
public class MeController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 菜单
	 * @description 获取菜单
	 */
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public JSONObject menu() {
		JSONArray menu = SessionUtil.getUserMenu();
		return ResponseUtil.responseResult(true, "查询成功", menu);
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 权限
	 * @description 获取所拥有的权限
	 */
	@RequestMapping(value = "/permission", method = RequestMethod.GET)
	public JSONObject permission() {
		Set<String> permission = SessionUtil.getUserPermission();
		return ResponseUtil.responseResult(true, "查询成功", permission);
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 个人信息
	 * @description 获取个人信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public JSONObject info() {
		Map<String, Object> userSession = SessionUtil.getUserSession();
		return ResponseUtil.responseResult(true, "查询成功", userSession);
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param info 新密码等信息
	 * @return 是否成功
	 * @description 修改密码
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
	public JSONObject changePassword(@RequestBody String info) {
		// 验证参数不为空
		if (StringUtils.isBlank(info)) {
			return ResponseUtil.responseResult(false, "参数不能为空", null);
		} else {
			JSONObject infoJson = JSON.parseObject(info);
			
			String code = infoJson.getString("code");
			if (!ValidateUtil.checkLength(code, 0, 6)) {
				return ResponseUtil.responseResult(false, "验证码错误", null);
			}
			String oldPassword = infoJson.getString("oldPassword");
			if (!ValidateUtil.match(UserConstant.PASSWORD.getRegex(), oldPassword)) {
				return ResponseUtil.responseResult(false, "旧密码错误", null);
			}
			String newPassword = infoJson.getString("newPassword");
			if (!ValidateUtil.match(UserConstant.PASSWORD.getRegex(), newPassword)) {
				return ResponseUtil.responseResult(false, "新密码不符合规则", null);
			}
			if (oldPassword != null && oldPassword.equals(newPassword)) {
				return ResponseUtil.responseResult(false, "旧密码与新密码相同", null);
			}
			Map<String, Object> userSession = SessionUtil.getUserSession();
			String userId = userSession.get(UserConstant.ID.getKey()).toString();
			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, UserConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, userId);
			mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.PASSWORD.getTableColumn(), OperatorConstant.EQUAL_TO, oldPassword);
			
			Map<String,Object> mapUpdate = new HashMap<String, Object>();
			mapUpdate.put(UserConstant.PASSWORD.getTableColumn(), newPassword);
			
			if (userService.update(mapUpdate, mapWhere) > 0) {
				SecurityUtils.getSubject().getSession().setTimeout(3000);
				return ResponseUtil.responseResult(true, "修改成功,请重新登录", null);
			} else {
				return ResponseUtil.responseResult(false, "旧密码错误", null);
			}
		}
	}
}
