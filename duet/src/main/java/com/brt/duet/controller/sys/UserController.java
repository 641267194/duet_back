package com.brt.duet.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.brt.duet.constant.DefaultConstant;
import com.brt.duet.constant.OperatorConstant;
import com.brt.duet.constant.RegexConstant;
import com.brt.duet.constant.table.sys.UserConstant;
import com.brt.duet.constant.table.sys.UserRoleConstant;
import com.brt.duet.service.sys.UserService;
import com.brt.duet.util.IDUtil;
import com.brt.duet.util.MybatisUtil;
import com.brt.duet.util.ResponseUtil;
import com.brt.duet.util.ValidateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 用户管理控制层
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param info 用户信息
	 * @return 是否成功
	 * @description 新增用户
	 */
	@RequiresPermissions(value="sys:user:create")
	@RequestMapping(value = "", method = RequestMethod.POST)
	//@RequiresPermissions(value = {})
	public JSONObject create(@RequestBody String info) {
		// 验证参数不为空
		if (StringUtils.isBlank(info)) {
			return ResponseUtil.responseResult(false, "参数不能为空", null);
		} else {
			JSONObject infoJson = JSON.parseObject(info);

			Map<String,Object> mapInsert = new HashMap<>();
			// 验证用户名
			String username = infoJson.getString(UserConstant.USERNAME.getKey());
			if (ValidateUtil.match(UserConstant.USERNAME.getRegex(), username)) {
				Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, UserConstant.USERNAME.getTableColumn(), OperatorConstant.EQUAL_TO, username);
				Page<Map<String, Object>> users = userService.select(null, mapWhere);
				if (users == null || users.isEmpty()) {
					mapInsert.put(UserConstant.USERNAME.getTableColumn(), username);
				} else {
					return ResponseUtil.responseResult(false, "用户名已被注册", null);
				}
			} else {
				return ResponseUtil.responseResult(false, "用户名不符合规则", null);
			}
			// 验证密码
			if (infoJson.containsKey(UserConstant.PASSWORD.getKey())) {
				String password = infoJson.getString(UserConstant.PASSWORD.getKey());
				if (ValidateUtil.match(UserConstant.PASSWORD.getRegex(), password)) {
					mapInsert.put(UserConstant.PASSWORD.getTableColumn(), password);
				} else {
					return ResponseUtil.responseResult(false, "密码不符合规则", null);
				}
			} else {
				mapInsert.put(UserConstant.PASSWORD.getTableColumn(), DefaultConstant.PASSWORD);
			}
			// 验证真实姓名
			if (infoJson.containsKey(UserConstant.NAME.getKey())) {
				String name = infoJson.getString(UserConstant.NAME.getKey());
				if (ValidateUtil.match(UserConstant.NAME.getRegex(), name)) {
					mapInsert.put(UserConstant.NAME.getTableColumn(), name);
				} else {
					return ResponseUtil.responseResult(false, "真实姓名不符合规则", null);
				}
			}
			// 验证手机号码
			if (infoJson.containsKey(UserConstant.PHONE.getKey())) {
				String phone = infoJson.getString(UserConstant.PHONE.getKey());
				if (ValidateUtil.match(UserConstant.PHONE.getRegex(), phone)) {
					mapInsert.put(UserConstant.PHONE.getTableColumn(), phone);
				} else {
					return ResponseUtil.responseResult(false, "手机号码不符合规则", null);
				}
			}
			// 启用
			if (infoJson.containsKey(UserConstant.ENABLED.getKey())) {
				if (ValidateUtil.match(UserConstant.ENABLED.getRegex(), infoJson.getString(UserConstant.ENABLED.getKey()))) {
					Boolean enabled = infoJson.getBoolean(UserConstant.ENABLED.getKey());
					mapInsert.put(UserConstant.ENABLED.getTableColumn(), enabled);
				} else {
					return ResponseUtil.responseResult(false, "是否启用不符合规则", null);
				}
			} else {
				mapInsert.put(UserConstant.ENABLED.getTableColumn(), 0);
			}
			// id
			String id = IDUtil.getUUID();
			mapInsert.put(UserConstant.ID.getTableColumn(), id);

			List<Map<String, Object>> userRoles = new ArrayList<>();
			// 验证roles是否合法
			if (infoJson.containsKey(UserConstant.ROLES.getKey())) {
				JSONArray rolesJson = infoJson.getJSONArray(UserConstant.ROLES.getKey());
				if (rolesJson != null && rolesJson.size() > 0) {
					for (Object roleIdObj : rolesJson) {
						if (roleIdObj instanceof String) {
							Map<String, Object> userRole = new HashMap<>();
							userRole.put(UserRoleConstant.USER_ID.getTableColumn(), id);
							userRole.put(UserRoleConstant.ROLE_ID.getTableColumn(), roleIdObj);
							userRoles.add(userRole);
						}
					}
				} else {
					return ResponseUtil.responseResult(false, "角色不能为空", null);
				}
			}

			if (userService.insert(mapInsert, userRoles) > 0) {
				return ResponseUtil.responseResult(true, "新增成功", null);
			} else {
				return ResponseUtil.responseResult(false, "新增失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param id 用户id
	 * @return 是否成功
	 * @description 软删除用户
	 */
	@RequiresPermissions(value="sys:user:delete")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JSONObject delete(@PathVariable("id") String id) {
		if (!ValidateUtil.match(UserConstant.ID.getRegex(), id)) {
			return ResponseUtil.responseResult(false, "用户id不符合规则", null);
		} else {
			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, UserConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
			Map<String, Object> mapUpdate = new HashMap<>();
			mapUpdate.put(UserConstant.DEL_FLAG.getTableColumn(), 1);
			if (userService.update(mapUpdate, mapWhere) > 0) {
				return ResponseUtil.responseResult(true, "删除成功", null);
			} else {
				return ResponseUtil.responseResult(false, "删除失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param info 需要修改的用户信息
	 * @param id 用户id
	 * @return 是否成功
	 * @description 修改用户信息
	 */
	@RequiresPermissions(value="sys:user:update")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public JSONObject update(@RequestBody String info, @PathVariable("id") String id) {
		if (StringUtils.isBlank(info)) {
			return ResponseUtil.responseResult(false, "参数[info]不能为空", null);
		} else if (!ValidateUtil.match(UserConstant.ID.getRegex(), id)){
			return ResponseUtil.responseResult(false, "用户id不符合规则", null);
		} else {
			JSONObject infoJson = JSON.parseObject(info);

			Map<String, Object> mapUpdate = new HashMap<>();

			// 验证真实姓名
			if (infoJson.containsKey(UserConstant.NAME.getKey())) {
				String name = infoJson.getString(UserConstant.NAME.getKey());
				if (ValidateUtil.match(UserConstant.NAME.getRegex(), name)) {
					mapUpdate.put(UserConstant.NAME.getTableColumn(), name);
				} else {
					return ResponseUtil.responseResult(false, "真实姓名不符合规则", null);
				}
			}
			// 验证手机号码
			if (infoJson.containsKey(UserConstant.PHONE.getKey())) {
				String phone = infoJson.getString(UserConstant.PHONE.getKey());
				if (ValidateUtil.match(UserConstant.PHONE.getRegex(), phone)) {
					mapUpdate.put(UserConstant.PHONE.getTableColumn(), phone);
				} else {
					return ResponseUtil.responseResult(false, "手机号码不符合规则", null);
				}
			}
			// 启用
			if (infoJson.containsKey(UserConstant.ENABLED.getKey())) {
				if (ValidateUtil.match(UserConstant.ENABLED.getRegex(), infoJson.getString(UserConstant.ENABLED.getKey()))) {
					Boolean enabled = infoJson.getBoolean(UserConstant.ENABLED.getKey());
					mapUpdate.put(UserConstant.ENABLED.getTableColumn(), enabled);
				} else {
					return ResponseUtil.responseResult(false, "是否启用不符合规则", null);
				}
			}

			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, UserConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);

			List<Map<String, Object>> userRoles = new ArrayList<>();
			// 验证roles是否合法
			if (infoJson.containsKey(UserConstant.ROLES.getKey())) {
				JSONArray rolesJson = infoJson.getJSONArray(UserConstant.ROLES.getKey());
				if (rolesJson != null && rolesJson.size() > 0) {
					for (Object roleIdObj : rolesJson) {
						if (roleIdObj instanceof String) {
							Map<String, Object> userRole = new HashMap<>();
							userRole.put(UserRoleConstant.USER_ID.getTableColumn(), id);
							userRole.put(UserRoleConstant.ROLE_ID.getTableColumn(), roleIdObj);
							userRoles.add(userRole);
						}
					}
				} else {
					return ResponseUtil.responseResult(false, "角色不能为空", null);
				}
			}
			Map<String, List<Map<String, Object>>> userRoleMapWhere = MybatisUtil.addMapWhere(null, UserRoleConstant.USER_ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);

			if (userService.update(mapUpdate, mapWhere, userRoles, userRoleMapWhere) > 0) {
				return ResponseUtil.responseResult(true, "修改成功", null);
			} else {
				return ResponseUtil.responseResult(false, "修改失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param id 用户id
	 * @return 用户信息
	 * @description 根据id获取用户信息
	 */
	@RequiresPermissions(value="sys:user:detail")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public JSONObject detail(@PathVariable("id") String id) {
		if (!ValidateUtil.match(UserConstant.ID.getRegex(), id)) {
			return ResponseUtil.responseResult(false, "用户id不符合规则", null);
		} else {
			Set<String> columns = new HashSet<>();
			columns.add(UserConstant.ID.getTableColumn());
			columns.add(UserConstant.USERNAME.getTableColumn());
			columns.add(UserConstant.NAME.getTableColumn());
			columns.add(UserConstant.PHONE.getTableColumn());
			columns.add(UserConstant.ENABLED.getTableColumn());
			columns.add(UserConstant.CREATE_AT.getTableColumn());
			columns.add(UserConstant.UPDATE_AT.getTableColumn());
			columns.add(UserConstant.ROLES.getTableColumn());

			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, UserConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);

			Page<Map<String, Object>> users = userService.select(columns, mapWhere);
			if (users != null && users.size() == 1) {
				return ResponseUtil.responseResult(true, "查询成功", users.get(0));
			} else {
				return ResponseUtil.responseResult(false, "查询失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param param 分页参数+搜索参数
	 * @return 用户列表
	 * @description 分页搜索用户
	 */
	@RequiresPermissions(value="sys:user:list")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public JSONObject list(@RequestParam(value = "param", required = false) String param) {
		JSONObject paramJson = null;
		JSONObject pageJson = null;
		JSONObject searchJson = null;
		if (!StringUtils.isBlank(param)) {
			paramJson = JSON.parseObject(param);
			pageJson = paramJson.getJSONObject("pageParam");
			searchJson = paramJson.getJSONObject("searchParam");
		}

		// 验证pageParam参数是否合法
		if (pageJson != null) {
			if (!pageJson.containsKey("pageNo") || pageJson.getInteger("pageNo") < 1) {
				return ResponseUtil.responseResult(false, "参数[pageParam.pageNo]不符合规则", null);
			}
			if (!pageJson.containsKey("pageSize") || !ValidateUtil.match(RegexConstant.PAGE_SIZE, pageJson.getString("pageSize"))) {
				return ResponseUtil.responseResult(false, "参数[pageParam.pageSize]不符合规则", null);
			}
			// 分页
			PageHelper.startPage(pageJson.getInteger("pageNo"), pageJson.getInteger("pageSize"));
		} else {
			PageHelper.startPage(1, 100);
		}

		PageHelper.orderBy(UserConstant.CREATE_AT.getTableColumn() + " DESC");

		Map<String, List<Map<String, Object>>> mapWhere = null;

		if (searchJson != null) {
			// 用户名验证
			String username = searchJson.getString(UserConstant.USERNAME.getKey());
			if (StringUtils.isNotBlank(username)) {
				if (ValidateUtil.match("^[A-Za-z0-9_]{1,50}$", username)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.USERNAME.getTableColumn(), OperatorConstant.LIKE, username);
				} else {
					return ResponseUtil.responseResult(false, "用户名不符合规则", null);
				}
			}
			// 真实姓名验证
			String name = searchJson.getString(UserConstant.NAME.getKey());
			if (StringUtils.isNotBlank(name)) {
				if (ValidateUtil.match(UserConstant.NAME.getRegex(), name)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.NAME.getTableColumn(), OperatorConstant.LIKE, name);
				} else {
					return ResponseUtil.responseResult(false, "真实姓名不符合规则", null);
				}
			}
			// 手机号码验证
			String phone = searchJson.getString(UserConstant.PHONE.getKey());
			if (StringUtils.isNotBlank(phone)) {
				if (ValidateUtil.match("^[0-9]{1,11}$", phone)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.PHONE.getTableColumn(), OperatorConstant.LIKE, phone);
				} else {
					return ResponseUtil.responseResult(false, "手机号码不符合规则", null);
				}
			}
			// 角色验证
			if (searchJson.containsKey(UserRoleConstant.ROLE_ID.getKey())) {
				String roleId = searchJson.getString(UserRoleConstant.ROLE_ID.getKey());
				if (ValidateUtil.match(UserRoleConstant.ROLE_ID.getRegex(), roleId)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, UserRoleConstant.ROLE_ID.getTableColumn(), OperatorConstant.EQUAL_TO, roleId);
				} else {
					return ResponseUtil.responseResult(false, "用户组不符合规则", null);
				}
			}
		}

		// 过滤要查询的列
		Set<String> columns = new HashSet<>();
		columns.add(UserConstant.ID.getTableColumn());
		columns.add(UserConstant.USERNAME.getTableColumn());
		columns.add(UserConstant.NAME.getTableColumn());
		columns.add(UserConstant.PHONE.getTableColumn());
		columns.add(UserConstant.ENABLED.getTableColumn());
		columns.add(UserConstant.CREATE_AT.getTableColumn());
		columns.add(UserConstant.UPDATE_AT.getTableColumn());
		columns.add(UserConstant.ROLES.getTableColumn());

		// 删除标识
		mapWhere = MybatisUtil.addMapWhere(mapWhere, UserConstant.DEL_FLAG.getTableColumn(), OperatorConstant.EQUAL_TO, 0);

		if (mapWhere.containsKey(UserRoleConstant.ROLE_ID.getTableColumn())) {
			Page<Map<String, Object>> list = userService.selectWithRole(columns, mapWhere);
			return ResponseUtil.responseResult(true, "查询成功", new PageInfo<>(list));
		} else {
			Page<Map<String, Object>> list = userService.select(columns, mapWhere);
			return ResponseUtil.responseResult(true, "查询成功", new PageInfo<>(list));
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param id 用户id
	 * @return 是否成功
	 * @description 重置用户密码为DefaultConstant.PASSWORD
	 */
	@RequiresPermissions(value="sys:user:resetPassword")
	@RequestMapping(value = "/resetPassword/{id}", method = RequestMethod.PUT)
	public JSONObject resetPassword(@PathVariable("id") String id) {
		if (!ValidateUtil.match(UserConstant.ID.getRegex(), id)){
			return ResponseUtil.responseResult(false, "用户id不符合规则", null);
		} else {
			Map<String, Object> mapUpdate = new HashMap<>();

			mapUpdate.put(UserConstant.PASSWORD.getTableColumn(), DefaultConstant.PASSWORD);

			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, UserConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);

			if (userService.update(mapUpdate, mapWhere) > 0) {
				return ResponseUtil.responseResult(true, "重置密码成功", null);
			} else {
				return ResponseUtil.responseResult(false, "重置密码失败", null);
			}
		}
	}

}
