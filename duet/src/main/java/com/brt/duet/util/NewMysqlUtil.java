package com.brt.duet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Calendar;

/**
 * @author 方杰
 * @date 2019年7月22日
 * @description 代码生成工具类
 */
public class NewMysqlUtil {
	static String newLine = System.getProperty("line.separator");
	/**
	 * @description 数据库连接的url
	 */
	static String url = "jdbc:mysql://localhost:3306?user=root&password=123456&useInformationSchema=true&serverTimezone=GMT%2B8";
	/**
	 * @description 生成的constant所在包路径
	 */
	static String constantPath = "com.brt.duet.constant.table.sys";
	/**
	 * @description 生成的dao所在包路径
	 */
	static String daoPath = "com.brt.duet.dao.sys";
	/**
	 * @description 生成的service所在包路径
	 */
	static String servicePath = "com.brt.duet.service.sys";
	/**
	 * @description 生成的controller所在包路径
	 */
	static String controllerPath = "com.brt.duet.controller.sys";
	/**
	 * @description 公共dao所在包路径
	 */
	static String commonDao = "com.brt.duet.dao.CommonDao";
	/**
	 * @description 公共tableColumn所在包路径
	 */
	static String tableColumn = "com.brt.duet.constant.TableColumn";
	/**
	 * @description 作者
	 */
	static String author = "方杰";
	/**
	 * @description 对应表名,使用%表示所有表
	 */
	static String table = "sys_log";
	/**
	 * @description 对应数据库
	 */
	static String database = "duet";
	/**
	 * @description 生成的文件所在的路径
	 */
	static String filePath = "D:/";

	public static void main(String[] args) {
		//生成constant
		tableConstant();
		//生成dao
		daoUseMap();
		//生成dao.xml
		daoXmlUseMap();
		//生成service
		serviceUseMap();
		//生成serviceImpl
		serviceImplUseMap();
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @description 生成constant
	 */
	public static void tableConstant() {
		try {
			// 加载驱动
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// 连接数据库
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData metaData = conn.getMetaData();
			// sys_user对应表名,使用%表示所有表
			ResultSet tables = metaData.getTables(database, "%", table, new String[]{"TABLE"});
			while (tables.next()) {
				FileOutputStream fop = null;
				File file;
				String tableName = tables.getString("TABLE_NAME");
				String tableRemarks = tables.getString("REMARKS");

				file = new File(filePath + UnderlineToHump2(tableName.substring(tableName.indexOf("_") + 1)) + "Constant.java");
				fop = new FileOutputStream(file);
				if (!file.exists()) {
					file.createNewFile();
				}
				fop.write(("package " + constantPath + ";").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());
				
				fop.write(("import " + tableColumn + ";").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("/**").getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @description " + tableRemarks).getBytes());
				fop.write(newLine.getBytes());
				fop.write((" */").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("public interface " + UnderlineToHump2(tableName.substring(tableName.indexOf("_") + 1)) + "Constant {").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    String TABLE_NAME = \"" + tableName + "\";").getBytes());
				fop.write(newLine.getBytes());
				ResultSet columns = metaData.getColumns(database, "%", tableName, "%");
				while (columns.next()) {
					String columnRemarks = columns.getString("REMARKS");
					String columnName = columns.getString("COLUMN_NAME");
					String key = UnderlineToHump(columnName);
					String tableColumn = tableName + "." + columnName;
					fop.write(("    /**").getBytes());
					fop.write(newLine.getBytes());
					fop.write(("     * @description " + columnRemarks).getBytes());
					fop.write(newLine.getBytes());
					fop.write(("     */").getBytes());
					fop.write(newLine.getBytes());
					fop.write(("    TableColumn " + columnName.toUpperCase() + " = new TableColumn(\"" + key + "\", \"" + tableColumn + "\", " + columns.getInt("DATA_TYPE") + ", null);").getBytes());
					fop.write(newLine.getBytes());
				}

				fop.write(("}").getBytes());
				fop.write(newLine.getBytes());

				fop.flush();
				fop.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @description 生成dao接口
	 */
	public static void daoUseMap() {
		try {
			// 加载驱动
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// 连接数据库
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet tables = metaData.getTables(database, "%", table, new String[]{"TABLE"});
			while (tables.next()) {
				FileOutputStream fop = null;
				File file;
				// sys_user
				String tableName = tables.getString("TABLE_NAME");
				String tableRemarks = tables.getString("REMARKS");
				// User
				String name = UnderlineToHump2(tableName.substring(tableName.indexOf("_") + 1));

				file = new File(filePath + name + "Dao.java");
				fop = new FileOutputStream(file);
				if (!file.exists()) {
					file.createNewFile();
				}

				fop.write(("package " + daoPath + ";").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("import java.util.List;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import java.util.Map;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import java.util.Set;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("import org.apache.ibatis.annotations.Param;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("import com.github.pagehelper.Page;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("/**").getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @author " + author).getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @date " + getYmd()).getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @description " + tableRemarks + "Dao").getBytes());
				fop.write(newLine.getBytes());
				fop.write((" */").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("public interface " + name + "Dao {").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    int insert(@Param(\"mapInsert\") Map<String, Object> mapInsert);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    int delete(@Param(\"mapWhere\") Map<String, List<Map<String, Object>>> mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    int update(@Param(\"mapUpdate\") Map<String, Object> mapUpdate, @Param(\"mapWhere\") Map<String, List<Map<String, Object>>> mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    Page<Map<String, Object>> select(@Param(\"columns\") Set<String> columns, @Param(\"mapWhere\") Map<String, List<Map<String, Object>>> mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("}").getBytes());
				fop.write(newLine.getBytes());

				fop.flush();
				fop.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @description 生成dao.xml
	 */
	public static void daoXmlUseMap() {
		try {
			// 加载驱动
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// 连接数据库
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData metaData = conn.getMetaData();
			// sys_user对应表名,使用%表示所有表
			ResultSet tables = metaData.getTables(database, "%", table, new String[]{"TABLE"});
			while (tables.next()) {
				FileOutputStream fop = null;
				File file;
				// sys_user
				String tableName = tables.getString("TABLE_NAME");
				// User
				String name = UnderlineToHump2(tableName.substring(tableName.indexOf("_") + 1));

				file = new File(filePath + name + "Dao.xml");
				fop = new FileOutputStream(file);
				if (!file.exists()) {
					file.createNewFile();
				}

				fop.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("<mapper namespace=\"" + daoPath + "." + name + "Dao\">").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    <resultMap id=\"resultMap\" type=\"java.util.Map\">").getBytes());
				fop.write(newLine.getBytes());
				ResultSet columns = metaData.getColumns(database, "%", tableName, "%");
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					if (columnName.equals("id")) {
						fop.write(("        <id property=\"id\" column=\"id\" />").getBytes());
					} else {
						fop.write(("        <result property=\"" + UnderlineToHump(columnName) + "\" column=\"" + columnName + "\" />").getBytes());
					}
					fop.write(newLine.getBytes());
				}
				fop.write(("    </resultMap>").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    <sql id=\"defaultColumn\">").getBytes());
				columns = metaData.getColumns(database, "%", tableName, "%");
				boolean tag = false;
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					if (tag) {
						fop.write((",").getBytes());
						fop.write(newLine.getBytes());
					} else {
						fop.write(newLine.getBytes());
						tag = true;
					}
					fop.write(("        " + tableName + "." + columnName + "").getBytes());
				}
				fop.write(newLine.getBytes());
				fop.write(("    </sql>").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    <insert id=\"insert\">").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        INSERT INTO " + tableName + " <include refid=\"" + commonDao + ".mapInsert\"/>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    </insert>").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    <delete id=\"delete\">").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        DELETE FROM " + tableName).getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        <where>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("            <include refid=\"" + commonDao + ".mapWhere\"/>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        </where>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    </delete>").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    <update id=\"update\">").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        UPDATE " + tableName).getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        SET <include refid=\"" + commonDao + ".mapUpdate\"/>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        <where>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("            <include refid=\"" + commonDao + ".mapWhere\"/>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        </where>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    </update>").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    <select id=\"select\" resultMap=\"resultMap\">").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        SELECT <include refid=\"" + commonDao + ".columns\"/>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        FROM " + tableName).getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        <where>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("            <include refid=\"" + commonDao + ".mapWhere\"/>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        </where>").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    </select>").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("</mapper>").getBytes());
				fop.write(newLine.getBytes());

				fop.flush();
				fop.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @description 生成service接口
	 */
	public static void serviceUseMap() {
		try {
			// 加载驱动
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// 连接数据库
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData metaData = conn.getMetaData();
			// sys_user对应表名,使用%表示所有表
			ResultSet tables = metaData.getTables(database, "%", table, new String[]{"TABLE"});
			while (tables.next()) {
				FileOutputStream fop = null;
				File file;
				// sys_user
				String tableName = tables.getString("TABLE_NAME");
				String tableRemarks = tables.getString("REMARKS");
				// User
				String name = UnderlineToHump2(tableName.substring(tableName.indexOf("_") + 1));

				file = new File(filePath + name + "Service.java");
				fop = new FileOutputStream(file);
				if (!file.exists()) {
					file.createNewFile();
				}

				fop.write(("package " + servicePath + ";").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("import java.util.List;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import java.util.Map;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import java.util.Set;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("import com.github.pagehelper.Page;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("/**").getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @author " + author).getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @date " + getYmd()).getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @description " + tableRemarks + "Service接口").getBytes());
				fop.write(newLine.getBytes());
				fop.write((" */").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("public interface " + name + "Service {").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    int insert(Map<String, Object> mapInsert);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    int delete(Map<String, List<Map<String, Object>>> mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("    Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("}").getBytes());
				fop.write(newLine.getBytes());

				fop.flush();
				fop.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @description 生成service实现类
	 */
	public static void serviceImplUseMap() {
		try {
			// 加载驱动
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// 连接数据库
			Connection conn = DriverManager.getConnection(url);
			DatabaseMetaData metaData = conn.getMetaData();
			// sys_user对应表名,使用%表示所有表
			ResultSet tables = metaData.getTables(database, "%", table, new String[]{"TABLE"});
			while (tables.next()) {
				FileOutputStream fop = null;
				File file;
				// sys_user
				String tableName = tables.getString("TABLE_NAME");
				String tableRemarks = tables.getString("REMARKS");
				// User
				String name = UnderlineToHump2(tableName.substring(tableName.indexOf("_") + 1));

				file = new File(filePath + name + "ServiceImpl.java");
				fop = new FileOutputStream(file);
				if (!file.exists()) {
					file.createNewFile();
				}

				fop.write(("package " + servicePath + ".impl;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("import java.util.List;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import java.util.Map;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import java.util.Set;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("import org.springframework.beans.factory.annotation.Autowired;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import org.springframework.stereotype.Service;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("import com.github.pagehelper.Page;").getBytes());
				fop.write(newLine.getBytes());
				
				fop.write(newLine.getBytes());

				fop.write(("import " + daoPath + "." + name + "Dao;").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("import " + servicePath + "." + name + "Service;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("/**").getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @author " + author).getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @date " + getYmd()).getBytes());
				fop.write(newLine.getBytes());
				fop.write((" * @description " + tableRemarks + "Service接口实现类").getBytes());
				fop.write(newLine.getBytes());
				fop.write((" */").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("@Service").getBytes());
				fop.write(newLine.getBytes());

				fop.write(("public class " + name + "ServiceImpl implements " + name + "Service {").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    @Autowired").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    private " + name + "Dao " + toLowerCaseFirstOne(name) + "Dao;").getBytes());
				fop.write(newLine.getBytes());

				fop.write(newLine.getBytes());

				fop.write(("    @Override").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    public int insert(Map<String, Object> mapInsert) {").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        return " + toLowerCaseFirstOne(name) + "Dao.insert(mapInsert);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    }").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());
				
				fop.write(("    @Override").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    public int delete(Map<String, List<Map<String, Object>>> mapWhere) {").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        return " + toLowerCaseFirstOne(name) + "Dao.delete(mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    }").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());
				
				fop.write(("    @Override").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    public int update(Map<String, Object> mapUpdate, Map<String, List<Map<String, Object>>> mapWhere) {").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        return " + toLowerCaseFirstOne(name) + "Dao.update(mapUpdate, mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    }").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());
				
				fop.write(("    @Override").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    public Page<Map<String, Object>> select(Set<String> columns, Map<String, List<Map<String, Object>>> mapWhere) {").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("        return " + toLowerCaseFirstOne(name) + "Dao.select(columns, mapWhere);").getBytes());
				fop.write(newLine.getBytes());
				fop.write(("    }").getBytes());
				fop.write(newLine.getBytes());
				fop.write(newLine.getBytes());

				fop.write(("}").getBytes());
				fop.write(newLine.getBytes());

				fop.flush();
				fop.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param s
	 * @return 
	 * @description 将字符串的首字母转换成小写并返回
	 */
	public static String toLowerCaseFirstOne(String s){
		if (Character.isLowerCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param str 下划线命名的字符串
	 * @return 驼峰命名的字符串
	 * @description 下划线转驼峰，首字母小写
	 */
	public static String UnderlineToHump(String str){
		StringBuilder result = new StringBuilder();
		String a[] = str.split("_");
		for (String s : a) {
			if (result.length() == 0){
				result.append(s.toLowerCase());
			} else {
				result.append(s.substring(0, 1).toUpperCase());
				result.append(s.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @param str 下划线命名的字符串
	 * @return 驼峰命名的字符串
	 * @description 下划线转驼峰，首字母大写
	 */
	public static String UnderlineToHump2(String str){
		StringBuilder result=new StringBuilder();
		String a[] = str.split("_");
		for (String s : a) {
			result.append(s.substring(0, 1).toUpperCase());
			result.append(s.substring(1).toLowerCase());
		}
		return result.toString();
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月22日
	 * @return 当前时间 类似2019年7月22日
	 * @description 获取当前时间
	 */
	public static String getYmd() {
		System.setProperty("user.timezone","Asia/Shanghai");
		Calendar now = Calendar.getInstance();
		String ymd = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";
		return ymd;
	}
}
