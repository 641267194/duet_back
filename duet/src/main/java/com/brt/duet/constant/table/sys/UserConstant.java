package com.brt.duet.constant.table.sys;

import com.brt.duet.constant.RegexConstant;
import com.brt.duet.constant.TableColumn;

/**
 * @description 用户表
 */
public interface UserConstant {
    String TABLE_NAME = "sys_user";
    /**
     * @description 主键id
     */
    TableColumn ID =  new TableColumn("id", "sys_user.id", 1, RegexConstant.ID_NOT_NULL);
    /**
     * @description 用户名
     */
    TableColumn USERNAME =  new TableColumn("username", "sys_user.username", 12, "^[A-Za-z0-9_]{4,50}$");
    /**
     * @description 密码
     */
    TableColumn PASSWORD =  new TableColumn("password", "sys_user.password", 12, "^[\\s\\S]{6,255}$");
    /**
     * @description 真实姓名
     */
    TableColumn NAME =  new TableColumn("name", "sys_user.name", 12, "^[\\s\\S]{0,100}$");
    /**
     * @description 手机号码
     */
    TableColumn PHONE =  new TableColumn("phone", "sys_user.phone", 12, RegexConstant.PHONE);
    /**
     * @description 是否启用 1：已启用 0：禁止
     */
    TableColumn ENABLED =  new TableColumn("enabled", "sys_user.enabled", -7, RegexConstant.BOOLEAN);
    /**
     * @description 创建时间
     */
    TableColumn CREATE_AT =  new TableColumn("createAt", "sys_user.create_at", 93, RegexConstant.DATETIME);
    /**
     * @description 修改时间
     */
    TableColumn UPDATE_AT =  new TableColumn("updateAt", "sys_user.update_at", 93, RegexConstant.DATETIME);
    /**
     * @description 是否删除 1：已删除 0：正常
     */
    TableColumn DEL_FLAG =  new TableColumn("delFlag", "sys_user.del_flag", -7, RegexConstant.BOOLEAN);
    /**
     * @description 用户组信息
     */
    TableColumn ROLES =  new TableColumn("roles", "sys_user.id AS roles", -100, null);
}
