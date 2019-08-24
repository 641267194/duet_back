package com.brt.duet.service.sys;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pagehelper.Page;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 用户表Service接口
 */
public interface UserService {

    int insert(Map<String, Object> mapInsert);

    int delete(Map<String, List<Map<String, Object>>> mapWhere);

    int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere);

    Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere);

	/**
	 * @author 方杰
	 * @date 2019年7月23日
	 * @param user 用户信息
	 * @param userRoles 用户用户组信息
	 * @return 是否成功
	 * @description 新增用户
	 */
	int insert(Map<String, Object> user, List<Map<String, Object>> userRoles);

	/**
	 * @author 方杰
	 * @date 2019年7月23日
	 * @param user 用户信息
	 * @param mapWhere 用户过滤条件
	 * @param userRoles 用户用户组关联信息
	 * @param userRoleMapWhere 用户用户过滤条件
	 * @return 是否成功
	 * @description 修改用户
	 */
	int update(Map<String, Object> user, Map<String, List<Map<String, Object>>> mapWhere,
			List<Map<String, Object>> userRoles, Map<String, List<Map<String, Object>>> userRoleMapWhere);

	/**
	 * @author 方杰
	 * @date 2019年7月26日
	 * @param columns 需要查询的列
	 * @param mapWhere 过滤条件
	 * @return 用户集合
	 * @description 获取用户通过用户组
	 */
	Page<Map<String, Object>> selectWithRole(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere);

}
