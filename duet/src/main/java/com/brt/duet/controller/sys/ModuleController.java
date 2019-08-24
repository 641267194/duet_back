package com.brt.duet.controller.sys;

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
import com.brt.duet.constant.table.sys.ModuleConstant;
import com.brt.duet.constant.table.sys.RoleModuleConstant;
import com.brt.duet.service.sys.ModuleService;
import com.brt.duet.service.sys.RoleModuleService;
import com.brt.duet.util.IDUtils;
import com.brt.duet.util.MybatisUtil;
import com.brt.duet.util.ResponseUtil;
import com.brt.duet.util.TreeUtil;
import com.brt.duet.util.ValidateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author 方杰
 * @date 2019年8月10日
 * @description 模块管理控制层
 */
@RestController
@RequestMapping("/sys/module")
public class ModuleController {
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private RoleModuleService roleModuleService;
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param info  模块信息
	 * @return 是否成功
	 * @description 新增模块
	 */
	@RequiresPermissions(value="sys:module:create")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public JSONObject create(@RequestBody String info) {
		//验证参数不为空
		if (StringUtils.isBlank(info)) {
			return ResponseUtil.responseResult(false, "参数[info]不能为空", null);
		} else {
			JSONObject infoJson = JSON.parseObject(info);

			Map<String, Object> mapInsert = new HashMap<>();

			//验证模块名称
			if (infoJson.containsKey(ModuleConstant.NAME.getKey())) {
				String name = infoJson.getString(ModuleConstant.NAME.getKey());
				if (ValidateUtil.match(ModuleConstant.NAME.getRegex(), name)) {
					mapInsert.put(ModuleConstant.NAME.getTableColumn(), name);
				} else {
					return ResponseUtil.responseResult(false, "模块名称不符合规则", null);
				}
			}
			
			//验证模块标题
			if (infoJson.containsKey(ModuleConstant.TITLE.getKey())) {
				String title = infoJson.getString(ModuleConstant.TITLE.getKey());
				if (ValidateUtil.match(ModuleConstant.TITLE.getRegex(), title)) {
					mapInsert.put(ModuleConstant.TITLE.getTableColumn(), title);
				} else {
					return ResponseUtil.responseResult(false, "模块标题不符合规则", null);
				}
			}
			
			//验证模块编码
			String code = infoJson.getString(ModuleConstant.CODE.getKey());
			if (ValidateUtil.match(ModuleConstant.CODE.getRegex(), code)) {
				Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null,ModuleConstant.CODE.getTableColumn(), OperatorConstant.EQUAL_TO, code);
				Page<Map<String, Object>> modules = moduleService.select(null, mapWhere);
				if (modules.size() > 0) {
					return ResponseUtil.responseResult(false, "模块编码已存在", null);
				} else {
					mapInsert.put(ModuleConstant.CODE.getTableColumn(), code);
				}
			} else {
				return ResponseUtil.responseResult(false, "模块编码不符合规则", null);
			}
			
			//验证链接地址
			if (infoJson.containsKey(ModuleConstant.URI.getKey())) {
				String uri = infoJson.getString(ModuleConstant.URI.getKey());
				if (ValidateUtil.match(ModuleConstant.URI.getRegex(), uri)) {
					Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null,ModuleConstant.URI.getTableColumn(), OperatorConstant.EQUAL_TO, uri);
					Page<Map<String, Object>> modules = moduleService.select(null, mapWhere);
					if (modules == null || modules.isEmpty()) {
						mapInsert.put(ModuleConstant.URI.getTableColumn(), uri);
					} else {
						return ResponseUtil.responseResult(false, "该连接地址已存在", null);
					}
				} else {
					return ResponseUtil.responseResult(false, "连接地址不符合规则", null);
				}
			} else {
				return ResponseUtil.responseResult(false, "连接地址不能为空", null);
			}
			
			//验证是否是菜单
			if (infoJson.containsKey(ModuleConstant.IS_MENU.getKey())) {
				if (ValidateUtil.match(ModuleConstant.IS_MENU.getRegex(), infoJson.getString(ModuleConstant.IS_MENU.getKey()))) {
					mapInsert.put(ModuleConstant.IS_MENU.getTableColumn(), infoJson.getBoolean(ModuleConstant.IS_MENU.getKey()));
				} else {
					return ResponseUtil.responseResult(false, "是否是菜单不符合规则", null);
				}
			} else {
				mapInsert.put(ModuleConstant.IS_MENU.getTableColumn(), 0);
			}
			
			//验证父ID
			if (infoJson.containsKey(ModuleConstant.PARENT_ID.getKey())) {
				String parentId = infoJson.getString(ModuleConstant.PARENT_ID.getKey());
				if (ValidateUtil.match(ModuleConstant.PARENT_ID.getRegex(), parentId)) {
					mapInsert.put(ModuleConstant.PARENT_ID.getTableColumn(), parentId);
				} else {
					return ResponseUtil.responseResult(false, "父ID不符合规则", null);
				}
			}
			
			//验证模块图标
			if (infoJson.containsKey(ModuleConstant.ICON.getKey())) {
				String icon = infoJson.getString(ModuleConstant.ICON.getKey());
				if (ValidateUtil.match(ModuleConstant.ICON.getRegex(), icon)) {
					mapInsert.put(ModuleConstant.ICON.getTableColumn(), icon);
				} else {
					return ResponseUtil.responseResult(false, "模块图标不符合规则", null);
				}
			}
			
			//验证模块的排序
			if (infoJson.containsKey(ModuleConstant.SORT.getKey())) {
				String sort = infoJson.getString(ModuleConstant.SORT.getKey());
				if (ValidateUtil.match(ModuleConstant.SORT.getRegex(), sort)) {
					mapInsert.put(ModuleConstant.SORT.getTableColumn(), infoJson.getInteger(ModuleConstant.SORT.getKey()));
				} else {
					return ResponseUtil.responseResult(false, "排序不符合规则", null);
				}
			}
			
			String id = IDUtils.getUUID();
			mapInsert.put(ModuleConstant.ID.getTableColumn(), id);

			if (moduleService.insert(mapInsert) > 0) {
				return ResponseUtil.responseResult(true, "新增成功", null);
			} else {
				return ResponseUtil.responseResult(false, "新增失败", null);
			}
		}
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param id 模块id
	 * @return 是否成功
	 * @description 删除模块
	 */
	@RequiresPermissions(value="sys:module:delete")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public JSONObject delete(@PathVariable("id") String id) {
		if (!ValidateUtil.match(ModuleConstant.ID.getRegex(), id)) {
			return ResponseUtil.responseResult(false, "模块id不符合规则", null);
		} else {
			Map<String, List<Map<String, Object>>> mapModuleWhere = MybatisUtil.addMapWhere(null,RoleModuleConstant.MODULE_ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
			if (!roleModuleService.select(null, mapModuleWhere).isEmpty()) {
				return ResponseUtil.responseResult(false, "有角色拥有该模块，删除失败", null);
			}

			Map<String, List<Map<String, Object>>> mapParentIdWhere = MybatisUtil.addMapWhere(null,ModuleConstant.PARENT_ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
			if (!moduleService.select(null, mapParentIdWhere).isEmpty()) {
				return ResponseUtil.responseResult(false, "存在子模块，删除失败", null);
			}

			Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null,ModuleConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
			if (moduleService.delete(mapWhere) > 0) {
				return ResponseUtil.responseResult(true, "删除成功", null);
			} else {
				return ResponseUtil.responseResult(false, "删除失败", null);
			}
		}
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param info 需要修改的模块信息
	 * @param id 模块id
	 * @return 是否成功
	 * @description 修改模块信息
	 */
	@RequiresPermissions(value="sys:module:update")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JSONObject update(@RequestBody String info, @PathVariable("id") String id) {
        if (StringUtils.isBlank(info)) {
            return ResponseUtil.responseResult(false, "参数[info]不能为空", null);
        } else if (!ValidateUtil.match(ModuleConstant.ID.getRegex(), id)){
            return ResponseUtil.responseResult(false, "模块id不符合规则", null);
        } else {
            JSONObject infoJson = JSON.parseObject(info);
            
            Map<String, Object> mapUpdate = new HashMap<>();
            
            // 验证父ID
            if (infoJson.containsKey(ModuleConstant.PARENT_ID.getKey())) {
            	String parentId = infoJson.getString(ModuleConstant.PARENT_ID.getKey());
            	if (ValidateUtil.match(ModuleConstant.PARENT_ID.getRegex(), parentId)) {
            		mapUpdate.put(ModuleConstant.PARENT_ID.getTableColumn(), parentId);
                } else {
                	return ResponseUtil.responseResult(false, "父ID不符合规则", null);
                }
            }
            // 验证模块名称
            if (infoJson.containsKey(ModuleConstant.TITLE.getKey())) {
	            String title = infoJson.getString(ModuleConstant.TITLE.getKey());
	            if (ValidateUtil.match(ModuleConstant.TITLE.getRegex(), title)) {
	            	mapUpdate.put(ModuleConstant.TITLE.getTableColumn(), title);
	            } else {
	            	return ResponseUtil.responseResult(false, "模块名称不符合规则", null);
	            }
            }
            
            //验证模块编码
            if (infoJson.containsKey(ModuleConstant.CODE.getKey())) {
	            String code = infoJson.getString(ModuleConstant.CODE.getKey());
	            if (ValidateUtil.match(ModuleConstant.CODE.getRegex(), code)) {
	            	mapUpdate.put(ModuleConstant.CODE.getTableColumn(), code);
	            } else {
	            	return ResponseUtil.responseResult(false, "模块编码不符合规则", null);
	            }
            }
            
            //验证链接地址 ---------------------
            if (infoJson.containsKey(ModuleConstant.URI.getKey())) {
	            String uri = infoJson.getString(ModuleConstant.URI.getKey());
	            if (ValidateUtil.match(ModuleConstant.URI.getRegex(), uri)) {
	            	Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null,ModuleConstant.URI.getTableColumn(), OperatorConstant.EQUAL_TO, uri);
					Page<Map<String, Object>> modules = moduleService.select(null, mapWhere);
					if (modules == null || modules.isEmpty()) {
						mapUpdate.put(ModuleConstant.URI.getTableColumn(), uri);
					} else if (modules != null && modules.size() == 1 && modules.get(0).get(ModuleConstant.ID.getKey()).equals(id)) {
						mapUpdate.put(ModuleConstant.URI.getTableColumn(), uri);
					} else {
						return ResponseUtil.responseResult(false, "该模块连接地址已存在", null);
					}
	            } else {
	            	return ResponseUtil.responseResult(false, "链接地址不符合规则", null);
	            }
            }

            //验证排序
            if (infoJson.containsKey(ModuleConstant.SORT.getKey())) {
	            String sort = infoJson.getString(ModuleConstant.SORT.getKey());
	            if (ValidateUtil.match(ModuleConstant.SORT.getRegex(), sort)) {
	            	mapUpdate.put(ModuleConstant.SORT.getTableColumn(), infoJson.getInteger(ModuleConstant.SORT.getKey()));
	            } else {
	            	return ResponseUtil.responseResult(false, "描述不符合规则", null);
	            }
            }
            //验证模块图标
			if (infoJson.containsKey(ModuleConstant.ICON.getKey())) {
				String icon = infoJson.getString(ModuleConstant.ICON.getKey());
				if (ValidateUtil.match(ModuleConstant.ICON.getRegex(), icon)) {
					mapUpdate.put(ModuleConstant.ICON.getTableColumn(), icon);
				} else {
					return ResponseUtil.responseResult(false, "模块图标不符合规则", null);
				}
			}
            
			//验证是否是菜单
			if (infoJson.containsKey(ModuleConstant.IS_MENU.getKey())) {
				if (ValidateUtil.match(ModuleConstant.IS_MENU.getRegex(), infoJson.getString(ModuleConstant.IS_MENU.getKey()))) {
					mapUpdate.put(ModuleConstant.IS_MENU.getTableColumn(), infoJson.getBoolean(ModuleConstant.IS_MENU.getKey()));
				} else {
					return ResponseUtil.responseResult(false, "是否是菜单不符合规则", null);
				}
			}
            
            Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, ModuleConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
            
            if (moduleService.update(mapUpdate, mapWhere) > 0) {
                return ResponseUtil.responseResult(true, "修改成功", null);
            } else {
                return ResponseUtil.responseResult(false, "修改失败", null);
            }
        }
    }
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param id 模块id
	 * @return 模块信息
	 * @description 根据id获取模块信息
	 */
	@RequiresPermissions(value="sys:module:detail")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JSONObject detail(@PathVariable("id") String id) {
        if (!ValidateUtil.match(ModuleConstant.ID.getRegex(), id)) {
            return ResponseUtil.responseResult(false, "模块id不符合规则", null);
        } else {
        	
        	Set<String> columns = new HashSet<>();
			columns.add(ModuleConstant.ID.getTableColumn());
			columns.add(ModuleConstant.NAME.getTableColumn());
			columns.add(ModuleConstant.TITLE.getTableColumn());
			columns.add(ModuleConstant.CODE.getTableColumn());
			columns.add(ModuleConstant.PARENT_ID.getTableColumn());
			columns.add(ModuleConstant.URI.getTableColumn());
			columns.add(ModuleConstant.ICON.getTableColumn());
			columns.add(ModuleConstant.SORT.getTableColumn());
			columns.add(ModuleConstant.IS_MENU.getTableColumn());
			columns.add(ModuleConstant.CREATE_AT.getTableColumn());
			columns.add(ModuleConstant.UPDATE_AT.getTableColumn());
        	
            Map<String, List<Map<String, Object>>> mapWhere = MybatisUtil.addMapWhere(null, ModuleConstant.ID.getTableColumn(), OperatorConstant.EQUAL_TO, id);
            
            Page<Map<String, Object>> modules = moduleService.select(columns, mapWhere);
            if (modules != null && modules.size() == 1) {
            	return ResponseUtil.responseResult(true, "查询成功", modules.get(0));
            } else {
            	return ResponseUtil.responseResult(false, "查询失败", null);
            }
        }
    }
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param param 分页参数+搜索参数
	 * @return 模块列表
	 * @description 分页搜索模块
	 */
	@RequiresPermissions(value="sys:module:list")
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
		
		PageHelper.orderBy(ModuleConstant.CREATE_AT.getTableColumn() + " DESC");
		
		Map<String, List<Map<String, Object>>> mapWhere = null;
		if (searchJson != null) {
			// 是否是菜单（isMenu）验证
			if (searchJson.containsKey(ModuleConstant.IS_MENU.getKey())) {
				if (ValidateUtil.match(ModuleConstant.IS_MENU.getRegex(), searchJson.getString(ModuleConstant.IS_MENU.getKey()))) {
					Boolean isMenu = searchJson.getBoolean(ModuleConstant.IS_MENU.getKey());
					mapWhere = MybatisUtil.addMapWhere(mapWhere, ModuleConstant.IS_MENU.getTableColumn(), OperatorConstant.LIKE, isMenu);
				} else {
					return ResponseUtil.responseResult(false, "是否是菜单不符合规则", null);
				}
			}
			
			// 模块名称验证
			if (searchJson.containsKey(ModuleConstant.NAME.getKey())) {
				String name = searchJson.getString(ModuleConstant.NAME.getKey());
				if (ValidateUtil.match(ModuleConstant.NAME.getRegex(), name)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, ModuleConstant.NAME.getTableColumn(), OperatorConstant.LIKE, name);
				} else {
					return ResponseUtil.responseResult(false, "模块名称不符合规则", null);
				}
			}
			
			// 模块标题验证
			if (searchJson.containsKey(ModuleConstant.TITLE.getKey())) {
				String title = searchJson.getString(ModuleConstant.TITLE.getKey());
				if (ValidateUtil.match(ModuleConstant.TITLE.getRegex(), title)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, ModuleConstant.TITLE.getTableColumn(), OperatorConstant.LIKE, title);
				} else {
					return ResponseUtil.responseResult(false, "模块标题不符合规则", null);
				}
			}
			
			// 模块编码验证
			if (searchJson.containsKey(ModuleConstant.CODE.getKey())) {
				String code = searchJson.getString(ModuleConstant.CODE.getKey());
				if (ValidateUtil.match(ModuleConstant.CODE.getRegex(), code)) {
					mapWhere = MybatisUtil.addMapWhere(mapWhere, ModuleConstant.CODE.getTableColumn(), OperatorConstant.LIKE, code);
				} else {
					return ResponseUtil.responseResult(false, "模块编码不符合规则", null);
				}
			}
		}
		
		// 过滤要查询的列
		Set<String> columns = new HashSet<>();
		columns.add(ModuleConstant.ID.getTableColumn());
		columns.add(ModuleConstant.NAME.getTableColumn());
		columns.add(ModuleConstant.TITLE.getTableColumn());
		columns.add(ModuleConstant.CODE.getTableColumn());
		columns.add(ModuleConstant.PARENT_ID.getTableColumn());
		columns.add(ModuleConstant.URI.getTableColumn());
		columns.add(ModuleConstant.ICON.getTableColumn());
		columns.add(ModuleConstant.SORT.getTableColumn());
		columns.add(ModuleConstant.IS_MENU.getTableColumn());
		columns.add(ModuleConstant.CREATE_AT.getTableColumn());
		columns.add(ModuleConstant.UPDATE_AT.getTableColumn());
		
		Page<Map<String, Object>> list = moduleService.select(columns, mapWhere);
		return ResponseUtil.responseResult(true, "查询成功", new PageInfo<>(list));
    }
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 模块树
	 * @description 获取模块树
	 */
	@RequiresPermissions(value="sys:module:tree")
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public JSONObject tree() {
		
		Page<Map<String, Object>> modules = moduleService.select(null, null);
		
		JSONArray moduleTree = JSONObject.parseArray(JSONObject.toJSONStringWithDateFormat(modules, "yyyy-MM-dd HH:mm:ss"));
		
		moduleTree = TreeUtil.jsonArrayToTree(moduleTree, "id", "parentId");
		moduleTree = TreeUtil.sortTree(moduleTree, "sort", "children");
		
		if (modules.isEmpty()) {
			return ResponseUtil.responseResult(false, "没有查询到数据", null);
		} else {
			return ResponseUtil.responseResult(true,"查询成功", moduleTree);
		}
	}
}
