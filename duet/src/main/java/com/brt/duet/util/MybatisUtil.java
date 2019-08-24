package com.brt.duet.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brt.duet.constant.OperatorConstant;

public class MybatisUtil {
	/**
	 * @author 方杰
	 * @date 2019年7月20日
	 * @param mapWhere 原mapWhere
	 * @param columnName 表名.列名
	 * @param operator 操作(参考OperatorConstant,例如=,Like,>,<)
	 * @param dataScope 值
	 * @return 
	 * @description 
	 */
	public static Map<String, List<Map<String, Object>>> addMapWhere(Map<String, List<Map<String, Object>>> mapWhere, String columnName, String operator, Object dataScope){
		if (mapWhere == null) {
			mapWhere = new HashMap<>();
		}
		List<Map<String, Object>> column = null;
		if (mapWhere.containsKey(columnName)) {
			column = mapWhere.get(columnName);
		}
		if (column == null) {
			column = new ArrayList<>();
		}
		Map<String, Object> scope = new HashMap<>();
		scope.put("columnName", columnName);
		scope.put("operator", operator);
		if (OperatorConstant.LIKE.equals(operator)) {
			scope.put("dataScope", "%" + sqlLikeConvert(dataScope.toString()) + "%");
		} else {
			scope.put("dataScope", dataScope);
		}
		column.add(scope);
		mapWhere.put(columnName, column);
		return mapWhere;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param str like模糊查询的字符串
	 * @return 处理过的like模糊查询的字符串
	 * @description 处理\,%,_为模糊查询的情况，进行转换
	 */
	private static String sqlLikeConvert(String str) {
		return str.replaceAll("\\\\", "\\\\\\\\").replaceAll("%", "\\\\\\%").replaceAll("_", "\\\\\\_");
	}
}
