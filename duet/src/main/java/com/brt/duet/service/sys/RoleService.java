package com.brt.duet.service.sys;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 角色表Service接口
 */
public interface RoleService {

    int insert(Map<String, Object> mapInsert);

    int delete(Map<String, List<Map<String, Object>>> mapWhere);

    int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere);

    Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere);

	/**
	 * @author 方杰
	 * @date 2019年7月23日
	 * @param role 角色信息
	 * @param roleModules 角色模块关联信息
	 * @return 是否成功
	 * @description 新增角色
	 */
	int insert(Map<String, Object> role, List<Map<String, Object>> roleModules);
	
	/**
	 * @author 方杰
	 * @date 2019年7月23日
	 * @param roleMapWhere 需要删除的角色
	 * @param roleModuleMapWhere 需要删除的角色权限关联
	 * @return 是否成功
	 * @description 删除角色及关联
	 */
	int delete(Map<String, List<Map<String, Object>>> roleMapWhere,
			Map<String, List<Map<String, Object>>> roleModuleMapWhere);

	/**
	 * @author 方杰
	 * @date 2019年7月23日
	 * @param role 角色信息
	 * @param mapWhere 角色过滤条件
	 * @param roleModules 角色模块关联信息
	 * @param roleModuleMapWhere 角色模块过滤条件
	 * @return 
	 * @description 
	 */
	int update(Map<String, Object> role, Map<String, List<Map<String, Object>>> mapWhere,
			List<Map<String, Object>> roleModules, Map<String, List<Map<String, Object>>> roleModuleMapWhere);

}
