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
import com.brt.duet.constant.OperatorConstant;
import com.brt.duet.constant.table.sys.RoleConstant;
import com.brt.duet.constant.table.sys.RoleModuleConstant;
import com.brt.duet.constant.table.sys.UserRoleConstant;
import com.brt.duet.service.sys.RoleService;
import com.brt.duet.service.sys.UserRoleService;
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
 * @description 角色管理控制层
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param info 角色信息
	 * @return 是否成功
	 * @description 新增角色
	 */
	@RequiresPermissions(value="sys:role:create")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JSONObject create(@RequestBody String info) {
		// 验证参数不为空
		if (StringUtils.isBlank(info)) {
			return ResponseUtil.responseResult(false, "参数不能为空", null);
		} else {
			JSONObject infoJson = JSON.parseObject(info);

			Map<String,Object> mapInsert = new HashMap<>();
			// 验证角色名
			if (infoJson.containsKey(RoleConstant.NAME.getKey())) {
				String name = infoJson.getString(RoleConstant.NAME.getKey());
				if (ValidateUtil.match(RoleConstant.NAME.getRegex(), name)) {
					mapInsert.put(RoleConstant.NAME.getTableColumn(), name);
				} else {
					return ResponseUtil.responseResult(false, "角色名不符合规则", null);
				}
			} else {
				return ResponseUtil.responseResult(false, "角色名不能为空", null);
			}
			// 验证编码
			String code = infoJson.getString(RoleConstant.CODE.getKey());
			if (ValidateUtil.match(code, RoleConstant.CODE.getRegex())) {
				mapInsert.put(RoleConstant.CODE.getTableColumn(), code);
			} else {
				return ResponseUtil.responseResult(false, "角色编码不符合规则", null);
			}
			// id
			String id = IDUtil.getUUID();
			mapInsert.put(RoleConstant.ID.getTableColumn(), id);

			List<Map<String, Object>> roleModules = new ArrayList<>();
			// 验证modules是否合法
			if (infoJson.containsKey(RoleConstant.MODULES.getKey())) {
				JSONArray modulesJson = infoJson.getJSONArray(RoleConstant.MODULES.getKey());
				if (modulesJson != null && modulesJson.size() > 0) {
					for (Object moduleIdObj : modulesJson) {
						if (moduleIdObj instanceof String) {
							Map<String, Object> roleModule = new HashMap<>();
							roleModule.put(RoleModuleConstant.ROLE_ID.getTableColumn(), id);
							roleModule.put(RoleModuleConstant.MODULE_ID.getTableColumn(), moduleIdObj);
							roleModules.add(roleModule);
						}
					}
				} else {
					return ResponseUtil.responseResult(false, "模块不能为空", null);
				}
			}

			if (roleService.insert(mapInsert, roleModules) > 0) {
				return ResponseUtil.responseResult(true, "新增成功", null);
			} else {
				return ResponseUtil.responseResult(false, "新增失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param id 角色id
	 * @return 是否成功
	 * @description 删除角色
	 */
	@RequiresPermissions(value="sys:role:delete")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JSONObject delete(@PathVariable("id") String id) {
		if (!ValidateUtil.match(RoleConstant.ID.getRegex(), id)) {
			return ResponseUtil.responseResult(false, "角色id不符合规则", null);
		} else {
			//与用户存在关联则无法删除
			Map<String, List<Map<String, Object>>> userRoleMapWhere = MybatisUtil.addMapWhere(null, UserRoleConstant.ROLE_ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
			Page<Map<String, Object>> userRoles = userRoleService.select(null, userRoleMapWhere);
			if (userRoles.size() > 0) {
				return ResponseUtil.responseResult(false, "与用户存在关联，无法删除", null);
			}
			//删除角色和角色模块关联
			Map<String, List<Map<String, Object>>> roleMapWhere = MybatisUtil.addMapWhere(null, RoleConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
			Map<String, List<Map<String, Object>>> roleModuleMapWhere = MybatisUtil.addMapWhere(null, RoleModuleConstant.ROLE_ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
			if (roleService.delete(roleMapWhere, roleModuleMapWhere) > 0) {
				return ResponseUtil.responseResult(true, "删除成功", null);
			} else {
				return ResponseUtil.responseResult(false, "删除失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param info 需要修改的角色信息
	 * @param id 角色id
	 * @return 是否成功
	 * @description 修改角色信息
	 */
	@RequiresPermissions(value="sys:role:update")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public JSONObject update(@RequestBody String info, @PathVariable("id") String id) {
		if (StringUtils.isBlank(info)) {
			return ResponseUtil.responseResult(false, "参数[info]不能为空", null);
		} else if (!ValidateUtil.match(RoleConstant.ID.getRegex(), id)){
			return ResponseUtil.responseResult(false, "角色id不符合规则", null);
		} else {
			JSONObject infoJson = JSON.parseObject(info);

			Map<String, Object> mapUpdate = new HashMap<>();

			// 验证角色名
			if (infoJson.containsKey(RoleConstant.NAME.getKey())) {
				String name = infoJson.getString(RoleConstant.NAME.getKey());
				if (ValidateUtil.match(RoleConstant.NAME.getRegex(), name)) {
					mapUpdate.put(RoleConstant.NAME.getTableColumn(), name);
				} else {
					return ResponseUtil.responseResult(false, "角色名不符合规则", null);
				}
			}
			// 验证编码
			if (infoJson.containsKey(RoleConstant.CODE.getKey())) {
				String code = infoJson.getString(RoleConstant.CODE.getKey());
				if (ValidateUtil.match(RoleConstant.CODE.getRegex(), code)) {
					mapUpdate.put(RoleConstant.CODE.getTableColumn(), code);
				} else {
					return ResponseUtil.responseResult(false, "角色编码不符合规则", null);
				}
			}

			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, RoleConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);

			List<Map<String, Object>> roleModules = new ArrayList<>();
			// 验证modules是否合法
			if (infoJson.containsKey(RoleConstant.MODULES.getKey())) {
				JSONArray modulesJson = infoJson.getJSONArray(RoleConstant.MODULES.getKey());
				if (modulesJson != null && modulesJson.size() > 0) {
					for (Object moduleIdObj : modulesJson) {
						if (moduleIdObj instanceof String) {
							Map<String, Object> roleModule = new HashMap<>();
							roleModule.put(RoleModuleConstant.ROLE_ID.getTableColumn(), id);
							roleModule.put(RoleModuleConstant.MODULE_ID.getTableColumn(), moduleIdObj);
							roleModules.add(roleModule);
						}
					}
				} else {
					return ResponseUtil.responseResult(false, "模块不能为空", null);
				}
			}
			Map<String, List<Map<String, Object>>> roleModuleMapWhere = MybatisUtil.addMapWhere(null, RoleModuleConstant.ROLE_ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);

			if (roleService.update(mapUpdate, mapWhere, roleModules, roleModuleMapWhere) > 0) {
				return ResponseUtil.responseResult(true, "修改成功", null);
			} else {
				return ResponseUtil.responseResult(false, "修改失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param id 角色id
	 * @return 角色信息
	 * @description 根据id获取角色信息
	 */
	@RequiresPermissions(value="sys:role:detail")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public JSONObject detail(@PathVariable("id") String id) {
		if (!ValidateUtil.match(RoleConstant.ID.getRegex(), id)) {
			return ResponseUtil.responseResult(false, "参数[id]不符合规则", null);
		} else {
			Set<String> columns = new HashSet<>();
			columns.add(RoleConstant.ID.getTableColumn());
			columns.add(RoleConstant.NAME.getTableColumn());
			columns.add(RoleConstant.CODE.getTableColumn());
			columns.add(RoleConstant.CREATE_AT.getTableColumn());
			columns.add(RoleConstant.UPDATE_AT.getTableColumn());
			columns.add(RoleConstant.MODULES.getTableColumn());

			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, RoleConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);

			Page<Map<String, Object>> roles = roleService.select(columns, mapWhere);
			if (roles != null && roles.size() == 1) {
				return ResponseUtil.responseResult(true, "查询成功", roles.get(0));
			} else {
				return ResponseUtil.responseResult(false, "查询失败", null);
			}
		}
	}

	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param param 分页参数+搜索参数
	 * @return 角色列表
	 * @description 分页搜索角色
	 */
	@RequiresPermissions(value="sys:role:list")
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
			if (!pageJson.containsKey("pageSize") || pageJson.getInteger("pageSize") < 1) {
				return ResponseUtil.responseResult(false, "参数[pageParam.pageSize]不符合规则", null);
			}
			// 分页
			PageHelper.startPage(pageJson.getInteger("pageNo"), pageJson.getInteger("pageSize"));
		}

		PageHelper.orderBy(RoleConstant.CREATE_AT.getTableColumn() + " DESC");

		Map<String, List<Map<String, Object>>> mapWhere = null;

		if (searchJson != null) {
			// 角色名验证
			String name = searchJson.getString(RoleConstant.NAME.getKey());
			if (StringUtils.isNotBlank(name)) {
				if (ValidateUtil.match(RoleConstant.NAME.getRegex(), name)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, RoleConstant.NAME.getTableColumn(), OperatorConstant.LIKE, name);
				} else {
					return ResponseUtil.responseResult(false, "角色名不符合规则", null);
				}
			}
			// 角色名验证
			String code = searchJson.getString(RoleConstant.CODE.getKey());
			if (StringUtils.isNotBlank(code)) {
				if (ValidateUtil.match(RoleConstant.CODE.getRegex(), code)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, RoleConstant.CODE.getTableColumn(), OperatorConstant.LIKE, code);
				} else {
					return ResponseUtil.responseResult(false, "角色编码不符合规则", null);
				}
			}
		}

		// 过滤要查询的列
		Set<String> columns = new HashSet<>();
		columns.add(RoleConstant.ID.getTableColumn());
		columns.add(RoleConstant.NAME.getTableColumn());
		columns.add(RoleConstant.CODE.getTableColumn());
		columns.add(RoleConstant.CREATE_AT.getTableColumn());
		columns.add(RoleConstant.UPDATE_AT.getTableColumn());

		Page<Map<String, Object>> list = roleService.select(columns, mapWhere);
		return ResponseUtil.responseResult(true, "查询成功", new PageInfo<>(list));
	}

}
