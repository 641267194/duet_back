package com.brt.duet.constant;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 表字段说明
 */
public class TableColumn {
	
	/**
	 * @description 查询结果Map里的key值,或者前端参数key值
	 */
	private String key;
	/**
	 * @description 表名.字段名
	 */
	private String tableColumn;
	/**
	 * @description 类型
	 */
	private int type;
	/**
	 * @description 正则表达式
	 */
	private String regex;
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 查询结果Map里的key值,或者前端参数key值
	 * @description 查询结果Map里的key值,或者前端参数key值
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param key 查询结果Map里的key值,或者前端参数key值
	 * @description 查询结果Map里的key值,或者前端参数key值
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 表名.字段名
	 * @description 表名.字段名
	 */
	public String getTableColumn() {
		return tableColumn;
	}
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 表名.字段名
	 * @description 表名.字段名
	 */
	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 类型
	 * @description 类型
	 */
	public int getType() {
		return type;
	}
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param type 类型
	 * @description 类型
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return regex 正则表达式
	 * @description 正则表达式
	 */
	public String getRegex() {
		return regex;
	}
	/**
	 * @author 方杰
	 * @date 2019年8月8日
	 * @param regex 正则表达式
	 * @description 正则表达式
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param key 查询结果Map里的key值,或者前端参数key值
	 * @param tableColumn 表名.字段名
	 * @param type 类型
	 * @param regex 正则表达式
	 * @description 构造方法
	 */
	public TableColumn(String key, String tableColumn, int type, String regex) {
		super();
		this.key = key;
		this.tableColumn = tableColumn;
		this.type = type;
		this.regex = regex;
	}
}
